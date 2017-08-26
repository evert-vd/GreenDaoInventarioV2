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

import com.daimajia.swipe.util.Attributes;
import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.utils.DividerItemDecoration;
import com.evertvd.greendaoinventario.vista.adapters.NuevoProductoAdapter;
import com.evertvd.greendaoinventario.vista.dialogs.DialogNuevoProducto;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrmNuevoProducto extends Fragment implements View.OnClickListener{
    private List<Producto> listaNuevoProducto;
    View view;
    private TextView tvSinRegistros;
    private RecyclerView mRecyclerView;
    FragmentManager fragmentManager;
    public FrmNuevoProducto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_nuevo_producto, container, false);
        // Inflate the layout for this fragment
        tvSinRegistros = (TextView)view.findViewById(R.id.tvSinRegistros);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview_registrar_nuevo_producto);

        Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        listaNuevoProducto=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId()))
                .where(ProductoDao.Properties.Tipo.notEq("Sistema")).list();

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
            mRecyclerView.setVisibility(View.GONE);
            tvSinRegistros.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvSinRegistros.setVisibility(View.GONE);
        }


        // Creating Adapter object
        NuevoProductoAdapter mAdapter = new NuevoProductoAdapter(getActivity(), listaNuevoProducto,fragmentManager);

        // Setting Mode to Single to reveal bottom View for one item in List
        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
        ((NuevoProductoAdapter) mAdapter).setMode(Attributes.Mode.Single);

        mRecyclerView.setAdapter(mAdapter);

        /* Scroll Listeners */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("Error RecyclerView", "onScrollStateChanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
        //return inflater.inflate(R.layout.fragment_listar_nuevo, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //
        // Nos aseguramos que es la petición que hemos realizado
        //
        //IProducto iProducto =new Sqlite_Producto();
        //iProducto.listarNuevosProductos(getActivity());
    }


    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btnAddNuevo) {
            FragmentManager fm = getFragmentManager();
            try {
                //fragmentManager=getFragmentManager();
                DialogNuevoProducto registrarNuevoProducto=new DialogNuevoProducto();
                registrarNuevoProducto.setCancelable(false);
                registrarNuevoProducto.show(fm,"nuevo producto");

            } catch (Exception e) {
                Log.e("error", e.toString());
            }

        }

        }


    private void crearAdaptadorRecycler(){
        Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(1)).unique();
        List<Producto>productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId()))
                .where(ProductoDao.Properties.Tipo.notEq("Sistema")).list();

        //IProducto iProducto=new Sqlite_Producto();
        //listaNuevoProducto=iProducto.listarNuevosProductos(getActivity());


    }

    public void  agregarFab(){
        FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.btnAddNuevo);
        fab.setOnClickListener(this);

        //FloatingActionButton fab2=(FloatingActionButton)view.findViewById(R.id.btnAddPruebas);
        //fab2.setOnClickListener(this);

    }


}
