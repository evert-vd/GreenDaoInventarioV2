package com.evertvd.greendaoinventario.vista.activitys;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.sqlitedao.SqliteEmpresa;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.interfaces.IEmpresa;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Empresa;
import com.evertvd.greendaoinventario.modelo.Historial;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.utils.MainDirectorios;
import com.evertvd.greendaoinventario.utils.Utils;
import com.evertvd.greendaoinventario.vista.adapters.ProductoAdapter;
import com.evertvd.greendaoinventario.vista.fragments.FragmentZonas;
import com.evertvd.greendaoinventario.vista.fragments.FrmContainerResumen;
import com.evertvd.greendaoinventario.vista.fragments.FrmNuevoProducto;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MenuItem menuDiferencia, menuInventario, menuResumen, menuNuevoProducto;
    private Menu menuNav;
    private Inventario inventario;
    private TextView txtNumEquipo, txtNumInventario;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Slide slideOut=new Slide(Gravity.LEFT);
        slideOut.setDuration(500);
        slideOut.setInterpolator(new DecelerateInterpolator());

        Slide slide=new Slide(Gravity.RIGHT);
        slide.setDuration(500);
        slide.setInterpolator(new DecelerateInterpolator());

        getWindow().setEnterTransition(slide);
        getWindow().setReturnTransition(slideOut);
        getWindow().setAllowEnterTransitionOverlap(false);*/

        setContentView(R.layout.activity_main);
        IEmpresa iEmpresa=new SqliteEmpresa();
        List<Empresa>empresaList=iEmpresa.listarEmpresa();
        if(empresaList.isEmpty()){
            cargarEmpresas();
        }
        MainDirectorios.crearDirectorioApp(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menuNav = navigationView.getMenu();
        menuInventario = menuNav.findItem(R.id.nav_inventario);
        menuDiferencia = menuNav.findItem(R.id.nav_diferencias);
        menuNuevoProducto=menuNav.findItem(R.id.nav_nuevo_Producto);
        menuResumen = menuNav.findItem(R.id.nav_resumen);



        //Forma de acceder al titulo del header
        View header = navigationView.getHeaderView(0);
        txtNumInventario = (TextView) header.findViewById(R.id.txtNumInventario);
        txtNumEquipo = (TextView) header.findViewById(R.id.txtNumEquipo);



        IInventario iInventario = new SqliteInventario();
        inventario = iInventario.obtenerInventario();
        if(inventario!=null){
            txtNumInventario.setText("Inventario Nro "+String.valueOf(inventario.getNuminventario()));
            if(inventario.getNumequipo()<10){
                txtNumEquipo.setText("0"+String.valueOf(inventario.getNumequipo()));
            }else{
                txtNumEquipo.setText(String.valueOf(inventario.getNumequipo()));
            }
            abrirContexto();
        }else{
            //Inventario inventario2=iInventario.obtenerInventarioCerrado();

            finishAfterTransition();
            startActivity(new Intent(this, Login.class));

            //Log.e("TAG", String.valueOf(inventario2.getContexto()));
            /*if(inventario2!=null){
                abrirContexto();
            }else{
                finish();
                startActivity(new Intent(this, Login2.class));
            }*/
        }
    }

    private void cargarEmpresas() {
        Empresa empresa=new Empresa();
        empresa.setEmpresa("Comercio");
        empresa.setCodempresa(2);
        empresa.setEstado(0);
        IEmpresa iEmpresa=new SqliteEmpresa();
        iEmpresa.agregarEmpresa(empresa);

    }


    //metodo sobreescrito que actualiza la cantidad al volver desde el activity detalle
    /*@Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
        //inicializarRecycler();
    }*/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           super.onBackPressed();
           //abrirContexto();
            //finish();
            //finishAfterTransition();
            //startActivity(new Intent(this,MainActivity.class));
        }
    }
    /*

     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (inventario==null || inventario.getContexto()==0){
            getMenuInflater().inflate(R.menu.toolbar_inventario, menu);
        }else if(inventario.getContexto()==1){
            getMenuInflater().inflate(R.menu.toolbar_diferencia, menu);
        }else{
            getMenuInflater().inflate(R.menu.toolbar_principal, menu);
        }
       getMenuInflater().inflate(R.menu.toolbar_principal, menu);
        return true;
    }*/


    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_inventario) {
            FragmentManager fragmentManager=getFragmentManager();
            DialogoCierreInventario dialogCierreInventario = new DialogoCierreInventario();
            dialogCierreInventario.setCancelable(false);
            dialogCierreInventario.show(fragmentManager, "dialogo cerrar inventario");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        //Fragment fragment=null;
        switch (id) {
            case R.id.nav_inventario:
                String titulo=item.getTitle().toString();
                abrirContextoInventario(fragmentTransaction);
                break;
            case R.id.nav_diferencias:
                abrirContextoInventario(fragmentTransaction);
                break;
            case R.id.nav_resumen:
                abrirContextoResumen(fragmentTransaction);
                break;
            case R.id.nav_nuevo_Producto:
                abrirContextoNuevoProducto(fragmentTransaction);
                break;

            case R.id.nav_manual:
                abrirPdf();
                break;
            case R.id.nav_cerrarSesion:
                mensajeConfirmacionCierre();
                break;
            default:
                finish();
                startActivity(new Intent(this, Login.class));
                break;
        }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void abrirPdf() {
        Utils.copyRawToSDCard(R.raw.manual_usuario, Environment.getExternalStorageDirectory() + "/manual_usuario.pdf", this);
        File pdfFile = new File(Environment.getExternalStorageDirectory(), "/manual_usuario.pdf");//File path
        if (pdfFile.exists()) { //Revisa si el archivo existe!
            Uri path = Uri.fromFile(pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //define el tipo de archivo
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //Inicia pdf viewer
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
            }
        } else {
            Toast.makeText(getApplicationContext(), "El archivo manual.pdf no existe o tiene otro nombre...! ", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirContexto() {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (inventario.getContexto() == 0|| inventario.getContexto()==1) {
                abrirContextoInventario(fragmentTransaction);

            } else if (inventario.getContexto() == 2) {
                navigationView.setCheckedItem(R.id.nav_resumen);
                menuNav.getItem(2).setEnabled(true);
                menuNav.getItem(0).setEnabled(false);
                menuNav.getItem(1).setEnabled(false);
                menuNav.getItem(3).setEnabled(false);
                abrirContextoResumen(fragmentTransaction);
            }
    }

    private void abrirContextoInventario(FragmentTransaction fragmentTransaction) {

        FragmentZonas fragmentZonas = new FragmentZonas();
        fragmentTransaction.replace(R.id.contenedor, fragmentZonas);
        fragmentTransaction.commit();
    }

    private void abrirContextoResumen(FragmentTransaction fragmentTransaction) {
        FrmContainerResumen frmContainerResumen = new FrmContainerResumen();
        fragmentTransaction.replace(R.id.contenedor, frmContainerResumen);
        setTitle("Resumen Invent. "+inventario.getNuminventario());
        fragmentTransaction.commit();
    }

    private void abrirContextoNuevoProducto(FragmentTransaction fragmentTransaction) {
        FrmNuevoProducto frmNuevoProducto = new FrmNuevoProducto();
        fragmentTransaction.replace(R.id.contenedor, frmNuevoProducto);
        setTitle("Nuevo producto");
        fragmentTransaction.commit();
    }

    private void agregarEmpresas() {
        Empresa empresa1 = new Empresa();
        empresa1.setCodempresa(1);
        empresa1.setEmpresa("Molinos");
        Controller.getDaoSession().insert(empresa1);

        Empresa empresa2 = new Empresa();
        empresa2.setCodempresa(2);
        empresa2.setEmpresa("Comercio");
        Controller.getDaoSession().insert(empresa2);

        Empresa empresa3 = new Empresa();
        empresa3.setCodempresa(3);
        empresa3.setEmpresa("Fertimax");
        Controller.getDaoSession().insert(empresa3);

        Empresa empresa4 = new Empresa();
        empresa4.setCodempresa(11);
        empresa4.setEmpresa("Miromina");
        Controller.getDaoSession().insert(empresa4);

    }


    private void mensajeConfirmacionCierre(){
        AlertDialog.Builder confirmar = new AlertDialog.Builder(this);
        confirmar.setMessage("¿Seguro que desea finalizar el inventario "+inventario.getNuminventario()+"?")
                .setTitle("Advertencia")
                .setCancelable(false)
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //dialog.cancel();
                                dialog.dismiss();
                            }
                        })
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
                                //Log.e("Inventario actual", String.valueOf(inventario.getId()));
                                //Controller.getDaoSession().getInventarioDao().delete(inventario);
                                Controller.getDaoSession().deleteAll(Inventario.class);
                                //Controller.getDaoSession().deleteAll(Empresa.class);
                                Controller.getDaoSession().deleteAll(Zona.class);
                                Controller.getDaoSession().deleteAll(Producto.class);
                                Controller.getDaoSession().deleteAll(Conteo.class);
                                Controller.getDaoSession().deleteAll(Historial.class);


                                startActivity(new Intent(getApplicationContext(), Login.class));
                                finish();//para que no se guarde en la pila
                            }
                        });
        AlertDialog alertDialog = confirmar.create();
        alertDialog.show();

        Button cancel = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if(cancel != null)
            //b.setBackgroundColor(Color.CYAN);
            cancel.setTextColor(getResources().getColor(R.color.colorGreyDarken_2));//color por código al boton cancelar del fialogo
    }


}

