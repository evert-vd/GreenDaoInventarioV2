package com.evertvd.greendaoinventario.vista.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.Zona_has_Inventario;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;
import com.evertvd.greendaoinventario.modelo.dao.Zona_has_InventarioDao;
import com.evertvd.greendaoinventario.utils.Operaciones;
import com.evertvd.greendaoinventario.utils.Reporte;
import com.evertvd.greendaoinventario.vista.adapters.ProductoResAdapater;
import com.evertvd.greendaoinventario.vista.adapters.ZonaResAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrmResumen extends Fragment implements View.OnClickListener{

    private static View view;
    private static Context context;
    private TextView txttotal, txtResumenZona, txtResumenCodigo, txtSinCodigos;


    public FrmResumen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_resumen, container, false);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_resumen, container, false);
        context = getActivity();

        txttotal=(TextView)view.findViewById(R.id.txtTotal);

        agregarFab();


        String tituloResumenZona="resumen por zona";
        String tituloResumenCodigo="codigos con diferencia";


        txtResumenZona=(TextView)view.findViewById(R.id.txtResumenZona);
        txtResumenCodigo=(TextView)view.findViewById(R.id.txtResumenCodigo);
        txtResumenZona.setText(tituloResumenZona.toUpperCase());
        txtResumenCodigo.setText(tituloResumenCodigo.toUpperCase());
        txtSinCodigos=(TextView)view.findViewById(R.id.txtSinCodigos);
        //setHasOptionsMenu(true);



        // A. Creamos el arreglo de Strings para llenar la lista
        Inventario inventario= Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        List<Zona_has_Inventario> zona_has_inventarioList=Controller.getDaoSession().getZona_has_InventarioDao().queryBuilder().
                where(Zona_has_InventarioDao.Properties.Inventario_id2.eq(inventario.getId())).list();
        //IConteo iConteo=new Sqlite_Conteo();
        int totalConteo=0;
       /*
        for (int i=0; i<zonaList.size(); i++){
            totalConteo+=iConteo.totalConteoPorZona(context, zonaList.get(i).getZona());
        }
        */
        //Lista todos los productos del inventario actual
       List<Producto> productoListZona=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();



        for(int i=0; i<productoListZona.size();i++){
            totalConteo+=Operaciones.totalConteoProducto1(productoListZona.get(i).getId());
        }


        txttotal.setText(String.valueOf(totalConteo));

        // B. Creamos un nuevo ArrayAdapter con nuestra lista
        ZonaResAdapter adapter = new ZonaResAdapter(getActivity(), zona_has_inventarioList);

        // C. Seleccionamos la lista de nuestro layout
        ListView miLista = (ListView)view.findViewById(R.id.listaResumenZona);
        miLista.setAdapter(adapter);


        //Diferencia por codigo
        // A. Creamos el arreglo de Strings para llenar la lista

        //List<Producto> productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Estado.notEq(0)).where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();

        List<Producto> productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Estado.notEq(0)).where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();

        // B. Creamos un nuevo ArrayAdapter con nuestra lista
        ProductoResAdapater adapterCodigo = new ProductoResAdapater(getActivity(), productoList);

        // C. Seleccionamos la lista de nuestro layout
        ListView listCodigo = (ListView)view.findViewById(R.id.listaResumenCodigo);
        listCodigo.setAdapter(adapterCodigo);

        if(productoListZona.isEmpty()){
            listCodigo.setVisibility(view.GONE);
            txtSinCodigos.setVisibility(view.VISIBLE);
        }else{
            listCodigo.setVisibility(view.VISIBLE);
            txtSinCodigos.setVisibility(view.GONE);
        }


        return view;
    }

    private void generarArchivosCorreo(){

        Reporte repote=new Reporte();
        repote.Resumido(getActivity(),getResources().getString(R.string.carpetaReporte));
        repote.Detallado(getActivity(),getResources().getString(R.string.carpetaReporte));

        /*
        DialogEnviarEmail enviarEmail = new DialogEnviarEmail();
        enviarEmail.setCancelable(false);
        enviarEmail.show(getFragmentManager(), "tag");
        */
    }



    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.fabEmail){
            generarArchivosCorreo();
        }
    }



    private void  agregarFab(){
        FloatingActionButton fabEmail=(FloatingActionButton)view.findViewById(R.id.fabEmail);
        fabEmail.setOnClickListener(this);

    }


}
