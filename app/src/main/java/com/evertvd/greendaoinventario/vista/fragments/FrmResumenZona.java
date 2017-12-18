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
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteConteo;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteZona;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.utils.Utils;
import com.evertvd.greendaoinventario.vista.adapters.ResumenZonasAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrmResumenZona extends Fragment {
    private View view;
    private ResumenZonasAdapter adapter;
    private List<Zona> zonaList;
    private RecyclerView recyclerResumenZona;
    private RecyclerView.LayoutManager lManager;
    private TextView txtTotalConteo;

    public FrmResumenZona() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_resumen_zona, container, false);

        //IInventario iInventario=new SqliteInventario();
        //Inventario inventario=iInventario.obtenerInventario();
        /*SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putInt("tab", 0);
        myEditor.commit();*/


        IZona iZona= new SqliteZona();
        zonaList=iZona.listarZona();

        IConteo iConteo=new SqliteConteo();
        //iConteo.totalConteo();
        txtTotalConteo=(TextView)view.findViewById(R.id.txtTotalConteo);
        txtTotalConteo.setText(Utils.formatearNumero(iConteo.totalConteo()) + " Und.");

        recyclerResumenZona = (RecyclerView)view. findViewById(R.id.recyclerResumenZona);
        recyclerResumenZona.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        recyclerResumenZona.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new ResumenZonasAdapter(zonaList, getActivity());
        recyclerResumenZona.setAdapter(adapter);
        recyclerResumenZona.setItemAnimator(new DefaultItemAnimator());
        recyclerResumenZona.setAdapter(adapter);

        return view;
    }
}