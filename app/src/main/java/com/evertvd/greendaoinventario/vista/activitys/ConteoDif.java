package com.evertvd.greendaoinventario.vista.activitys;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;
import com.evertvd.greendaoinventario.vista.adapters.ConteoDifAdapter;
import com.evertvd.greendaoinventario.vista.dialogs.RegistrarConteoDif;
import com.evertvd.greendaoinventario.vista.dialogs.RegistrarConteoInv;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConteoDif extends AppCompatActivity implements View.OnClickListener, RegistrarConteoDif.OnClickListener {

    private Producto producto;
    private List<Conteo> conteoList;
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;

    //datos del actionBar
    private TextView abTitulo, abCodigo, abZona;
    static TextView abCantidad;//tiene q ser statico para poder actualizarlo desde el dialogo de conteoAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteos);

        setTitle("Registro y Validación de Conteos");


        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        //setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        producto = Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Seleccionado.eq(1)).unique();//producto seleccionado
        Zona zona = Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Id.eq(producto.getZona_id())).unique();
        conteoList = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(producto.getId())).where(ConteoDao.Properties.Estado.notEq(-1)).list();

        //Codigo del actionBar
        abCodigo = (TextView) findViewById(R.id.txtAbCodigo);
        abCodigo.setText(String.valueOf(producto.getCodigo()));

        abZona = (TextView) findViewById(R.id.txtAbZona);
        abZona.setText(zona.getNombre());
        //titulo del actionBar
        abTitulo = (TextView) findViewById(R.id.txtAbDescripcion);
        abTitulo.setText(producto.getDescripcion());

        int cantidad = 0;
        for (int i = 0; i < conteoList.size(); i++) {
            cantidad += conteoList.get(i).getCantidad();
        }
        abCantidad = (TextView) findViewById(R.id.txtAbCantidad);
        abCantidad.setText(String.valueOf(cantidad));

        tvEmptyView = (TextView) findViewById(R.id.txtSinRegistros);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_detalle);

        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Item Decorator:
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        //mRecyclerView.setItemAnimator(new FadeInLeftAnimator());

        crearAdaptadorLista(conteoList);
        agregarFab();

        /* Scroll Listeners */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.e("RecyclerView", "onScrollStateChanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //
        // Nos aseguramos que es la petición que hemos realizado
        //
        //IConteo iConteo =new Sqlite_Conteo();
        //iConteo.obtenerConteoProducto(this,idProducto);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAgregar) {
            try {
                abrirDialogo(v);
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
        }
    }

    public void abrirDialogo(View view) {
        DialogFragment dialogFragment = new RegistrarConteoDif();
        dialogFragment.show(getFragmentManager(), "dialogoRegistrar");
    }

    //6--
    //metodo que captura los valores del dialog
    public void onInputDialog(int conteoCapturado, String observacion) {
        guardarDatos(conteoCapturado, observacion);
    }


    public void agregarFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAgregar);
        fab.setOnClickListener(this);
    }


    private void guardarDatos(int conteoCapturado, String observacion) {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date hoy = new Date();
        String horaRegistro = formato.format(hoy);

        Producto producto = Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Seleccionado.eq(1)).unique();//producto seleccionado
        Conteo conteo = new Conteo();
        conteo.setCantidad(conteoCapturado);
        conteo.setFecharegistro(horaRegistro);
        conteo.setEstado(0);//-1: eliminado, 1: modificado
        conteo.setValidado(0);
        conteo.setObservacion(observacion);
        conteo.setProducto_id(producto.getId());
        Controller.getDaoSession().insert(conteo);

        List<Conteo> listaDetalle = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(producto.getId())).where(ConteoDao.Properties.Estado.notEq(-1)).list();
        int cantidad = 0;
        for (int i = 0; i < listaDetalle.size(); i++) {
            cantidad += listaDetalle.get(i).getCantidad();
        }
        abCantidad.setText(String.valueOf(cantidad));
        crearAdaptadorLista(listaDetalle);
        Toast.makeText(this, "Conteo registrado", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent=new Intent(this, DetalleZona.class);

        //Intent intent=new Intent(view.getContext(), PruebaDetalle.class);
        //intent.putExtra("zona",zona );

        // startActivity(intent);
    }

/*

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        // Esto es lo que hace mi botón al pulsar ir a atrás
            startActivity(new Intent(this, ListaProducto.class));
            Toast.makeText(getApplicationContext(), "Voy hacia atrás!!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
    */


    private void crearAdaptadorLista(List<Conteo> conteoList) {

        if (conteoList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }


        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ConteoDifAdapter mAdapter = new ConteoDifAdapter(this, conteoList, producto.getId(), getFragmentManager());
        ((ConteoDifAdapter) mAdapter).setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);

    }


    public void actualizarConteoActionBar(int cantidad) {
        abCantidad.setText(String.valueOf(cantidad));
    }
}
