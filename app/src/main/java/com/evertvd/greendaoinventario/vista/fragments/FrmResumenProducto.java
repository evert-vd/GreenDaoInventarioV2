package com.evertvd.greendaoinventario.vista.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteZona;
import com.evertvd.greendaoinventario.vista.adapters.ResumenProductoAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrmResumenProducto extends Fragment {
    ResumenProductoAdapter adapter;
    List<Producto> productoList;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager lManager;
    View view;
    //private TextView txtResumenCodigo, txtSinCodigos;
    public FrmResumenProducto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_resumen_producto, container, false);


        /*SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putInt("tab", 1);
        myEditor.commit();*/

        IProducto iProducto=new SqliteProducto();
        productoList=iProducto.listarProductoDiferencia();


        recycler = (RecyclerView)view.findViewById(R.id.recyclerviewDiferenciaProducto);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new ResumenProductoAdapter(productoList, getActivity());
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());

        return view;

    }

}
