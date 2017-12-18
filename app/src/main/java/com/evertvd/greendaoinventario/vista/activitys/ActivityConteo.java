package com.evertvd.greendaoinventario.vista.activitys;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteConteo;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.utils.Utils;
import com.evertvd.greendaoinventario.vista.adapters.ConteoAdapter;
import com.evertvd.greendaoinventario.vista.dialogs.RegistrarConteo;

import java.util.List;

public class ActivityConteo extends AppCompatActivity implements View.OnClickListener, RegistrarConteo.OnClickListener {

    private Producto producto;
    private List<Conteo> conteoList;
    private TextView txtSinRegistros;
    private RecyclerView mRecyclerView;
    private long idProducto;
    ConteoAdapter mAdapter;

    //datos del actionBar
    private TextView abTitulo, abCodigo, abZona;
    static TextView abCantidad;//tiene q ser statico para poder actualizarlo desde el dialogo de conteoAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Slide slideRight0=new Slide(Gravity.RIGHT);
        slideRight0.setDuration(300);
        slideRight0.setInterpolator(new DecelerateInterpolator());

        Slide slideRight=new Slide(Gravity.RIGHT);
        slideRight.setDuration(500);
        slideRight.setInterpolator(new DecelerateInterpolator());
        getWindow().setEnterTransition(slideRight0);
        //getWindow().setReenterTransition(slideRight0);
        getWindow().setReturnTransition(slideRight);
        getWindow().setAllowEnterTransitionOverlap(false);*/

        setContentView(R.layout.activity_conteos);
        setTitle("Registro de conteos");

        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        //setSupportActionBar(myToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);//elevacion de actbar

        idProducto=getIntent().getExtras().getLong("id");
        IProducto iProducto=new SqliteProducto();
        producto=iProducto.obtenerProducto(idProducto);

        IConteo iConteo=new SqliteConteo();
        conteoList=iConteo.listarConteo(idProducto);

        //Codigo del actionBar
        abCodigo = (TextView) findViewById(R.id.txtAbCodigo);
        if(producto.getTipo().equalsIgnoreCase("App")){
            abCodigo.setText(String.valueOf("NN"+producto.getCodigo()));
        }else{
            abCodigo.setText(String.valueOf(producto.getCodigo()));
        }
        abZona = (TextView) findViewById(R.id.txtAbZona);
        abZona.setText(producto.getZona().getNombre());
        //titulo del actionBar
        abTitulo = (TextView) findViewById(R.id.txtAbDescripcion);
        abTitulo.setText(producto.getDescripcion());

        abCantidad = (TextView) findViewById(R.id.txtAbCantidad);
        abCantidad.setText(Utils.formatearNumero(iConteo.obtenerTotalConteo(producto.getId())));

        txtSinRegistros = (TextView) findViewById(R.id.txtSinRegistros);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_detalle);
        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Item Decorator:
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        //mRecyclerView.setItemAnimator(new FadeInLeftAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
         mAdapter= new ConteoAdapter(this, conteoList);
        ((ConteoAdapter) mAdapter).setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);

        if(conteoList.isEmpty()){
            //mRecyclerView.setVisibility(View.GONE);
            txtSinRegistros.setVisibility(View.VISIBLE);
        }else{
            //mRecyclerView.setVisibility(View.VISIBLE);
            txtSinRegistros.setVisibility(View.GONE);
        }
        //crearAdaptadorLista(conteoList);
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
        DialogFragment dialogFragment = new RegistrarConteo();
        dialogFragment.show(getFragmentManager(), "dialogoRegistrar");
    }

    //6--
    //metodo que captura los valores del dialog
    public void registrarConteo(Conteo conteo) {
        conteo.setFecharegistro(Utils.fechaActual());
        conteo.setEstado(0);
        conteo.setValidado(0);//por validar
        conteo.setProducto_id(producto.getId());
        //Controller.getDaoSession().insert(conteo);
        IConteo iConteo=new SqliteConteo();
        mAdapter.addItem(conteo);
        txtSinRegistros.setVisibility(View.GONE);
        iConteo.agregarConteo(conteo);
        abCantidad.setText(String.valueOf(iConteo.obtenerTotalConteo(producto.getId())));
        Toast.makeText(this, "Conteo registrado", Toast.LENGTH_SHORT).show();

        //ocultar teclado
        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(yourEditText.getWindowToken(), 0);

        // Ocultar teclado virtual



        /*View view = this.getCurrentFocus();
        view.clearFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }*/
    }

    public void agregarFab() {
        IInventario iInventario=new SqliteInventario();
        Inventario inventario=iInventario.obtenerInventario();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAgregar);
        if(inventario.getContexto()==2){
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(this);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent = new Intent(this, ActivityConteo.class);
        //Intent intent=new Intent(view.getContext(), PruebaDetalle.class);
        //intent.putExtra("id",conteoList.get(0).getProducto().getZona().getId());
        //startActivity(intent);
    }



    public void actualizarConteoActionBar(int cantidad) {
        abCantidad.setText(Utils.formatearNumero(cantidad));
    }
}
