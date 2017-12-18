package com.evertvd.greendaoinventario.vista.fragments;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.sqlitedao.SqliteConteo;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteZona;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.threads.ThreadActualizarDiferencias;
import com.evertvd.greendaoinventario.vista.activitys.MainActivity;
import com.evertvd.greendaoinventario.vista.adapters.ProductoAdapter;
import com.evertvd.greendaoinventario.vista.adapters.ZonasAdapter;
import com.evertvd.greendaoinventario.vista.dialogs.DialogValidarConteo;
import com.evertvd.greendaoinventario.vista.dialogs.DialogoCierreInventario;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentZonas extends Fragment {
    private View view;
    private ZonasAdapter adapter;
    private List<Zona>zonaList;
    private RecyclerView recyclerViewZonas;
    private RecyclerView.LayoutManager lManager;
    private Inventario inventario;
    private Menu menuNav;
    private TextView txtMensaje;

    public FragmentZonas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*Slide slideOut=new Slide(Gravity.LEFT);
        slideOut.setDuration(500);
        slideOut.setInterpolator(new DecelerateInterpolator());

        Slide slide=new Slide(Gravity.RIGHT);
        slide.setDuration(500);
        slide.setInterpolator(new DecelerateInterpolator());

        getActivity().getWindow().setEnterTransition(slide);
        getActivity().getWindow().setReturnTransition(slideOut);
        getActivity().getWindow().setAllowEnterTransitionOverlap(false);*/

        view = inflater.inflate(R.layout.fragment_zona, container, false);
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        NavigationView navView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        Menu menuNav = navView.getMenu();
        txtMensaje=(TextView) view.findViewById(R.id.txtMensaje);

        /*MenuItem menuInventario = menuNav.findItem(R.id.nav_inventario);
        MenuItem menuDiferencia = menuNav.findItem(R.id.nav_diferencias);
        MenuItem menuNuevoProducto=menuNav.findItem(R.id.nav_nuevo_Producto);
        MenuItem menuResumen = menuNav.findItem(R.id.nav_resumen);*/
        IInventario iInventario=new SqliteInventario();
        inventario=iInventario.obtenerInventario();
        //Log.e("CONTEXTO", String.valueOf(inventario.getContexto()));
        IZona iZona=new SqliteZona();
        if(inventario.getContexto()==0){
            zonaList=iZona.listarZona();
            navView.setCheckedItem(R.id.nav_inventario);
            menuNav.getItem(1).setEnabled(false);
            menuNav.getItem(2).setEnabled(false);
            getActivity().setTitle(menuNav.getItem(0).getTitle().toString()+" "+inventario.getNuminventario());
            //setTitle(menuNav.getItem(R.id.nav_inventario).getTitle()+" "+inventario.getNuminventario());
        }else if(inventario.getContexto()==1){
            navView.setCheckedItem(R.id.nav_diferencias);
            menuNav.getItem(1).setEnabled(true);
            menuNav.getItem(0).setEnabled(false);
            menuNav.getItem(2).setEnabled(false);
            getActivity().setTitle(menuNav.getItem(1).getTitle().toString()+" "+inventario.getNuminventario());
            zonaList=iZona.listarZonaDiferencia();
            if(zonaList.isEmpty()){
                txtMensaje.setVisibility(View.VISIBLE);
            }
        }

        recyclerViewZonas = (RecyclerView)view. findViewById(R.id.recyclerViewZonas);
        recyclerViewZonas.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        recyclerViewZonas.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new ZonasAdapter(zonaList, getActivity(),getActivity());
        recyclerViewZonas.setItemAnimator(new DefaultItemAnimator());
        recyclerViewZonas.setAdapter(adapter);
        //recyclerViewZonas.notify();



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (inventario==null || inventario.getContexto()==0){
            inflater.inflate(R.menu.toolbar_inventario, menu);
        }else if(inventario.getContexto()==1){
            inflater.inflate(R.menu.toolbar_diferencia, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager=getFragmentManager();
        if (id == R.id.action_inventario) {
            DialogoCierreInventario dialogCierreInventario = new DialogoCierreInventario();
            Bundle bundle = new Bundle();
            bundle.putInt("contexto", inventario.getContexto());
            dialogCierreInventario.setArguments(bundle);
            dialogCierreInventario.setCancelable(false);
            dialogCierreInventario.show(fragmentManager, "dialogo cerrar inventario");
            return true;
        }else if(id==R.id.action_diferencia){
            IConteo iConteo=new SqliteConteo();
            List<Conteo>conteoList=iConteo.listarConteoPorValidar();
            if(!conteoList.isEmpty()) {
                DialogValidarConteo dialogValidarConteo = new DialogValidarConteo();
                dialogValidarConteo.setCancelable(false);
                dialogValidarConteo.show(fragmentManager, "dialogo validar inventario");
            }else {
                DialogoCierreInventario dialogCierreInventario = new DialogoCierreInventario();
                Bundle bundle = new Bundle();
                bundle.putInt("contexto", inventario.getContexto());
                dialogCierreInventario.setArguments(bundle);
                dialogCierreInventario.setCancelable(false);
                dialogCierreInventario.show(fragmentManager, "dialogo cerrar diferencias");
            }

            return true;

        }else if(id==R.id.action_actualizar){
            IConteo iConteo=new SqliteConteo();
            List<Conteo>conteoList=iConteo.listarConteoPorValidar();
            if(!conteoList.isEmpty()){
                DialogValidarConteo dialogValidarConteo = new DialogValidarConteo();
                dialogValidarConteo.setCancelable(false);
                dialogValidarConteo.show(fragmentManager, "dialogo validar inventario");

            }else{
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Actualizando diferencias...");
                ThreadActualizarDiferencias threadActualizarDiferencias=new ThreadActualizarDiferencias(progressDialog,getActivity(), fragmentManager);
                threadActualizarDiferencias.execute();
                Toast.makeText(getActivity(),"Diferencias Actualizadas", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        IZona iZona=new SqliteZona();
        if(inventario.getContexto()==0){
            zonaList=iZona.listarZona();
        }else if(inventario.getContexto()==1){
            zonaList=iZona.listarZonaDiferencia();
        }
        adapter = new ZonasAdapter(zonaList, getActivity(),getActivity());
        recyclerViewZonas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
