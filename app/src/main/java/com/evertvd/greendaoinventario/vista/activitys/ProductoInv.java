package com.evertvd.greendaoinventario.vista.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;

import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;

import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;
import com.evertvd.greendaoinventario.vista.adapters.ProductosInvAdapter;

import java.util.ArrayList;
import java.util.List;


public class ProductoInv extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ProductosInvAdapter adapter;
    List<Producto> productoList;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        Zona zonaSeleccionada = Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Estado.eq(1)).unique();
        Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        //Log.e("zonaSeleccionada", String.valueOf(zonaSeleccionada.getZona()));
        setTitle("Prod. en la zona " + zonaSeleccionada.getNombre());
        //setSupportActionBar(myToolbar);
        //flecha en el actionbar para regresar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productoList = Controller.getDaoSession().getProductoDao().queryBuilder()
                .where(ProductoDao.Properties.Zona_id.eq(zonaSeleccionada.getId()))
                .where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();

        for(int i=0; i<productoList.size();i++){
            Log.e("Cod",String.valueOf(productoList.get(i).getStock())+" Estado:"+String.valueOf(productoList.get(i).getEstado()));
        }

        recycler = (RecyclerView) findViewById(R.id.listadoProductos);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new ProductosInvAdapter(productoList, this);
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());

        if (savedInstanceState != null) {
            Log.e("saved instance", savedInstanceState.toString());
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_productos, menu);
        MenuItem item = menu.findItem(R.id.action_buscar);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adapter.setFilter(productoList);
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            List<Producto> listaFiltrada = filter(productoList, newText);
            //aca se envía la nueva lista al recycler
            adapter.setFilter(listaFiltrada);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<Producto> filter(List<Producto> lista, String query) {
        List<Producto> listaFiltrada = new ArrayList<Producto>();
        try {

            //aca se recibe el parametro de busqueda
            query = query.toLowerCase();
            for (Producto producto : lista) {
                //busqueda por descripcion
                String queryDescripcion = producto.getDescripcion().toLowerCase();
                //busqueda por codigo
                String queryCodigo = String.valueOf(producto.getCodigo()).toLowerCase();
                if (queryDescripcion.contains(query)) {
                    listaFiltrada.add(producto);
                } else if (queryCodigo.contains(query)) {
                    listaFiltrada.add(producto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaFiltrada;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        //Intent intent=new Intent(view.getContext(), PruebaDetalle.class);
        //intent.putExtra("zona",zona );
        startActivity(intent);
    }

}
