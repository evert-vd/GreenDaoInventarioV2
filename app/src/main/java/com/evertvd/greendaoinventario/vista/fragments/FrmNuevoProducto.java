package com.evertvd.greendaoinventario.vista.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteZona;
import com.evertvd.greendaoinventario.utils.DividerItemDecoration;
import com.evertvd.greendaoinventario.vista.adapters.NuevoProductoAdapter;
import com.evertvd.greendaoinventario.vista.dialogs.DialogNuevoProducto;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrmNuevoProducto extends Fragment implements View.OnClickListener, DialogNuevoProducto.InterfaceDialogNuevoProducto{
    private List<Producto> listaNuevoProducto;
    View view;
    private TextView txtSinRegistros;
    private RecyclerView mRecyclerView;
    FragmentManager fragmentManager;
    private NuevoProductoAdapter mAdapter;
    public FrmNuevoProducto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_nuevo_producto, container, false);

        IProducto iProducto=new SqliteProducto();
        listaNuevoProducto=iProducto.listarProductoApp();

        // Inflate the layout for this fragment
        txtSinRegistros = (TextView)view.findViewById(R.id.txtSinRegistros);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview_nuevo_producto);

        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Item Decorator:
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));

        // mRecyclerView.setItemAnimator(new FadeInLeftAnimator());
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        //fragment manager que se envia como parametro a los recyclerAdapter
        fragmentManager=getFragmentManager();

        //crearAdaptadorRecycler();
        agregarFab();

        // loadData();
        if (listaNuevoProducto.isEmpty()) {
            //mRecyclerView.setVisibility(View.GONE);
            txtSinRegistros.setVisibility(View.VISIBLE);

        } else {
            //mRecyclerView.setVisibility(View.VISIBLE);
            txtSinRegistros.setVisibility(View.GONE);
        }

        // Creating Adapter object
        mAdapter = new NuevoProductoAdapter(getActivity(), listaNuevoProducto,fragmentManager);
        ((NuevoProductoAdapter) mAdapter).setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btnNuevoProducto) {
            FragmentManager fm = getFragmentManager();
            try {
                DialogNuevoProducto registrarNuevoProducto=new DialogNuevoProducto();
                registrarNuevoProducto.setTargetFragment(this, 0);
                registrarNuevoProducto.setCancelable(false);
                registrarNuevoProducto.show(fm,"nuevo producto");
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
        }
        }

    public void  agregarFab(){
        FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.btnNuevoProducto);
        fab.setOnClickListener(this);
    }

    @Override
    public void addNuevoProducto(Producto producto) {
        if(producto!=null){
            IProducto iProducto=new SqliteProducto();
            iProducto.agregarProducto(producto);

            IZona iZona=new SqliteZona();
            iZona.calcularDiferenciaZona();

            mAdapter.addItem(producto);
            txtSinRegistros.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Producto Agregado", Toast.LENGTH_SHORT).show();

        }
    }
}
