package com.evertvd.greendaoinventario.vista.fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.threads.ThreadReporte;
import com.evertvd.greendaoinventario.utils.MainDirectorios;
import com.evertvd.greendaoinventario.vista.adapters.ResumenTabsAdapter;
import com.evertvd.greendaoinventario.vista.dialogs.DialogEnviarEmail;
import com.evertvd.greendaoinventario.vista.dialogs.DialogWriteCsv;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrmContainerResumen extends Fragment implements DialogWriteCsv.OnClickEventoCsv{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Menu menu;

    View view;

    public FrmContainerResumen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_container_resumen, container, false);
        setHasOptionsMenu(true);

        NavigationView navView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        Menu menuNav = navView.getMenu();



        tabLayout=(TabLayout)getActivity().findViewById(R.id.tabs);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int tab = myPreferences.getInt("tab", 0);
        viewPager.setCurrentItem(tab);
        viewPager.setAdapter(new ResumenTabsAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        IInventario iInventario=new SqliteInventario();
        Inventario inventario=iInventario.obtenerInventario();
        if(inventario.getContexto()==2){
            navView.setCheckedItem(R.id.nav_resumen);
            getActivity().setTitle(menuNav.getItem(2).getTitle().toString()+" Invent. "+inventario.getNuminventario());
            menuNav.getItem(0).setEnabled(false);
            menuNav.getItem(1).setEnabled(false);
            menuNav.getItem(2).setEnabled(true);//habilitado
            menuNav.getItem(3).setEnabled(false);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            this.menu=menu;
            inflater.inflate(R.menu.menu_resumen, menu);
            //si no existe ningún archivo creado, el item estará deshabilitado
            if(!MainDirectorios.validarArchivo(getActivity())){
                menu.getItem(0).getSubMenu().getItem(1).setEnabled(false);
            }else{
                menu.getItem(0).getSubMenu().getItem(1).setEnabled(true);
            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exportar) {
            if(MainDirectorios.validarArchivo(getActivity())){
                DialogWriteCsv dialogWriteCsv = new DialogWriteCsv();
                dialogWriteCsv.setTargetFragment(this, 0);
                dialogWriteCsv.setCancelable(false);
                dialogWriteCsv.show(getFragmentManager(), "tag");
            }else{
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Generando Reportes...");
                ThreadReporte threadReporteCsv=new ThreadReporte(progressDialog,getActivity(),menu,0);
                threadReporteCsv.execute();
            }
            return true;
        }else if(id==R.id.action_email){
            DialogEnviarEmail enviarEmail = new DialogEnviarEmail();
            enviarEmail.setCancelable(false);
            enviarEmail.show(getFragmentManager(), "tag");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void botonDialogOnClick(String evento) {
        if (evento.equalsIgnoreCase("Nuevo")){
            //nuevo reporte (1)
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Generando Reportes...");
            ThreadReporte threadReporteCsv=new ThreadReporte(progressDialog,getActivity(),menu,1);
            threadReporteCsv.execute();
        }else {
            //reemplazar
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Generando Reportes...");
            ThreadReporte threadReporteCsv=new ThreadReporte(progressDialog,getActivity(),menu,0);
            threadReporteCsv.execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int tab = myPreferences.getInt("tab", 0);
        Log.e("TAB", String.valueOf(tab));
        viewPager.setCurrentItem(tab);
    }
}