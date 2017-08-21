package com.evertvd.greendaoinventario.vista.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;
import com.evertvd.greendaoinventario.vista.activitys.ProductoDif;
import com.evertvd.greendaoinventario.vista.activitys.ProductoInv;
import com.evertvd.greendaoinventario.vista.adapters.ZonasDifAdapter;
import com.evertvd.greendaoinventario.vista.dialogs.DialogCierreDiferencia;
import com.evertvd.greendaoinventario.vista.dialogs.DialogCierreInventario;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrmZonasDiferencia extends Fragment {
    private View view;
    private TextView txtTitulo;

    public FrmZonasDiferencia() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_zonas_diferencia, container, false);
        //Inflate the layout for this fragment
        setHasOptionsMenu(true);

        txtTitulo = (TextView) view.findViewById(R.id.txtTitulo);
        List<Inventario> inventarioList = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).list();
        for (int i = 0; i < inventarioList.size(); i++) {
            //Log.e("Inventarios", String.valueOf(inventarioList.get(i).getId()));
            if (inventarioList.get(i).getNumequipo() < 10) {
                txtTitulo.setText(("Diferencia del equipo 0" + inventarioList.get(i).getNumequipo()).toUpperCase());
            } else {
                txtTitulo.setText(("Diferencia del equipo " + inventarioList.get(i).getNumequipo()).toUpperCase());
            }
        }


        Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        Query<Zona> query = Controller.getDaoSession().getZonaDao().queryRawCreate(", PRODUCTO P WHERE P.ZONA_ID=T._id AND P.ESTADO<>0 AND P.INVENTARIO_ID="+inventario.getId() +" GROUP BY T._id");
        List<Zona> lista = query.list();

        /*
        List<ATableObj> listATableObj = ATableDao.queryRawCreate(", BTable BT
        WHERE BT.nameid = T.nameid").list();
         */

        for (int i = 0; i < lista.size(); i++) {
            Log.e("colists", String.valueOf(lista.get(i).getNombre()));
            Log.e("estado", String.valueOf(lista.get(i).getEstado()));
        }

        List<Producto> productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();

        for (int i = 0; i < productoList.size(); i++) {
            Log.e("stock", String.valueOf(productoList.get(i).getStock())+" estado:"+productoList.get(i).getEstado());

        }


        iniciarlizarAdapter(lista);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_diferencia, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_diferencia:
                //mostrarProceso()
                FragmentManager fragmentManager = getFragmentManager();
                //FragmentManager fragmentManager=getFragmentManager();
                DialogCierreDiferencia dialogCierreDiferencia = new DialogCierreDiferencia();
                dialogCierreDiferencia.setCancelable(false);
                dialogCierreDiferencia.show(fragmentManager, "dialogo cerrar diferencias");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void iniciarlizarAdapter(final List<Zona> zonaList) {

        ZonasDifAdapter adapter = new ZonasDifAdapter(getActivity(), zonaList);
        // C. Seleccionamos la lista de nuestro layout

        ListView listaZonas = (ListView) view.findViewById(R.id.listaZonas);
        listaZonas.setVisibility(View.VISIBLE);
        listaZonas.setAdapter(adapter);

        listaZonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),zonaList.get(position).getZona()+" "+zonaList.get(position).getZona(), Toast.LENGTH_LONG).show();

                //asignar estado cero a la zona seleccionada actual
                Zona zonaSeleccionadaActual = Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Estado.eq(1)).unique();
                if (zonaSeleccionadaActual != null) {
                    zonaSeleccionadaActual.setEstado(0);
                    zonaSeleccionadaActual.update();
                }

                //asignar estadao 1:seleccionado a la zona seleccionada
                Zona nuevaZonaSeleccionada = Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Id.eq(zonaList.get(position).getId())).unique();
                nuevaZonaSeleccionada.setEstado(1);
                nuevaZonaSeleccionada.update();

                Intent intent = new Intent(getActivity(), ProductoDif.class);
                startActivity(intent);
            }
        });
    }

}
