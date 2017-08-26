package com.evertvd.greendaoinventario.vista.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.vista.adapters.ProductoResAdapater;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrmResumenDifProd extends Fragment {
    View view;
    private TextView txtResumenCodigo, txtSinCodigos;
    public FrmResumenDifProd() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_res_dif_prod, container, false);
        String tituloResumenCodigo = "codigos con diferencia";

        txtResumenCodigo=(TextView)view.findViewById(R.id.txtResumenCodigo);
        txtResumenCodigo.setText(tituloResumenCodigo.toUpperCase());
        txtSinCodigos=(TextView)view.findViewById(R.id.txtSinCodigos);

        Inventario inventario= Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        List<Producto> productoListZona=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();
        List<Producto> productoList= Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Estado.notEq(0)).where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();

        // B. Creamos un nuevo ArrayAdapter con nuestra lista
        ProductoResAdapater adapterCodigo = new ProductoResAdapater(getActivity(), productoList);

        // C. Seleccionamos la lista de nuestro layout
        ListView listCodigo = (ListView)view.findViewById(R.id.listaResumenCodigo);
        listCodigo.setAdapter(adapterCodigo);

        if(productoList.isEmpty()){
            listCodigo.setVisibility(view.GONE);
            txtSinCodigos.setVisibility(view.VISIBLE);
        }else{
            listCodigo.setVisibility(view.VISIBLE);
            txtSinCodigos.setVisibility(view.GONE);
        }

        return view;

    }

}
