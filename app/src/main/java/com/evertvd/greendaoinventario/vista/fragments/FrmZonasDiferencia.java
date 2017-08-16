package com.evertvd.greendaoinventario.vista.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.evertvd.greendaoinventario.vista.activitys.Productos;
import com.evertvd.greendaoinventario.vista.adapters.ZonasDiferenciaAdapter;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

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
        //List<Zona_has_Inventario> zona_has_inventario=AppController.getDaoSession().getZona_has_InventarioDao().loadAll();

        List<Inventario> zona_has_inventario = Controller.getDaoSession().getInventarioDao().loadAll();

        for (int i = 0; i < zona_has_inventario.size(); i++) {
            Log.e("idInventario", String.valueOf(zona_has_inventario.get(i).getId()) + " ,estado; " + String.valueOf(zona_has_inventario.get(i).getEstado()));
        }

        Log.e("Inv Actual", String.valueOf(inventario.getId()));

        QueryBuilder<Zona> queryBuilder = Controller.getDaoSession().getZonaDao().queryBuilder();
        queryBuilder.join(Producto.class, ProductoDao.Properties.Zona_id).where(ProductoDao.Properties.Estado.eq(-1));
        queryBuilder.join(Inventario.class, InventarioDao.Properties.Id);//where(new WhereCondition.StringCondition("J2.ESTADO=0 GROUP BY J1.ZONA_ID"));
        //queryBuilder.join(Inventario.class, InventarioDao.Properties.Empresa_id).where(InventarioDao.Properties.Estado.eq(0));
        //queryBuilder.join(Inventario.class,ProductoDao.Properties.Zona_id).where(new WhereCondition.StringCondition("J2._id="+inventario.getId()+ " AND J1.ESTADO=-1 GROUP BY J1.ZONA_ID"));
        List<Zona> zonaList = queryBuilder.list();


        //QueryBuilder<Producto> qb=AppController.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Estado.eq(-1));
        //qb.and(ProductoDao.Properties.Inventario_id.eq(inventario.getId()));

        Query<Zona> query = Controller.getDaoSession().getZonaDao().queryRawCreate(", PRODUCTO P WHERE P.ZONA_ID=T._id");
        List<Zona> lista = query.list();

        /*
        List<ATableObj> listATableObj = ATableDao.queryRawCreate(", BTable BT
        WHERE BT.nameid = T.nameid").list();
         */

        for (int i = 0; i < lista.size(); i++) {
            Log.e("colists", String.valueOf(lista.get(i).getNombre()));
            Log.e("dsa", String.valueOf(lista.get(i).getEstado()));
        }

            /*
        QueryBuilder<City> qb = cityDao.queryBuilder().where(Properties.Population.ge(1000000));
        Join country = qb.join(Properties.CountryId, Country.class);
        Join continent = qb.join(country, CountryDao.Properties.ContinentId,
         Continent.class, ContinentDao.Properties.Id);
        continent.where(ContinentDao.Properties.Name.eq("Europe"));
        List<City> bigEuropeanCities = qb.list();
         */


        //queryBuilder.join(Inventario.class, InventarioDao.Properties.Id).where(new WhereCondition.StringCondition("J1.INVENTARIO_ID="+inventario.getId()+ " GROUP BY ZONA_ID"));
        //queryBuilder.join(Zona_has_Inventario.class, Zona_has_InventarioDao.Properties.Zona_id2).where(Zona_has_InventarioDao.Properties.Inventario_id2.eq(inventario.getId()));
        //queryBuilder.join(Inventario.class, InventarioDao.Properties.Id).where(InventarioDao.Properties.Id.eq(inventario.getId()));
        //queryBuilder.join(Producto.class, ProductoDao.Properties.Zona_id).where(new WhereCondition.StringCondition("J1.ESTADO=-1 GROUP BY ZONA_ID"));

        //queryBuilder.join(Inventario.class, InventarioDao.Properties.Id).where(InventarioDao.Properties.Id.eq(inventario.getId()));


        //List<Zona>zonaList1=AppController.getDaoSession().getZonaDao().queryBuilder().where(new WhereCondition.StringCondition(ZonaDao.Properties.Id.eq(1)+" GROUP BY Zona")).build().list();

        //List<Conteo>conteos=AppController.getDaoSession().getConteoDao().queryBuilder().where(new WhereCondition.StringCondition(ConteoDao.Properties.Producto_id.eq(1)+" GROUP BY Producto_id")).list();
        //List<Conteo>conteos2=AppController.getDaoSession().getConteoDao().queryBuilder().where(new WhereCondition.StringCondition("_id=1 GROUP BY PRODUCTO_ID")).build().list();

        //List<Producto>productoList1=AppController.getDaoSession().getProductoDao().queryBuilder().where(new WhereCondition.StringCondition(ProductoDao.Properties.Zona_id.eq(1)+ " GROUP BY ZONA_ID")).build().list();


        for (int i = 0; i < zonaList.size(); i++) {
            Log.e("conteos", String.valueOf(zonaList.get(i).getNombre()));
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

            case R.id.action_inventario:
                //mostrarProceso()
                /*
                android.support.v4.app.FragmentManager fragmentManager=getFragmentManager();
                DialogCierreInventario dialogCierreInventario = new DialogCierreInventario();
                dialogCierreInventario.setCancelable(false);
                dialogCierreInventario.show(fragmentManager, "dialogo cerrar inventario");
                */
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void iniciarlizarAdapter(final List<Zona> zonaList) {

        ZonasDiferenciaAdapter adapter = new ZonasDiferenciaAdapter(getActivity(), zonaList);
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

                Intent intent = new Intent(getActivity(), Productos.class);
                startActivity(intent);
            }
        });
    }

}
