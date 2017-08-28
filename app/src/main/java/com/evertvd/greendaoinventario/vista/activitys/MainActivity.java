package com.evertvd.greendaoinventario.vista.activitys;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Empresa;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.vista.fragments.FrmNuevoProducto;
import com.evertvd.greendaoinventario.vista.fragments.FrmResumen;
import com.evertvd.greendaoinventario.vista.fragments.FrmZonasDiferencia;
import com.evertvd.greendaoinventario.vista.fragments.FrmZonasInventario;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    //private FragmentManager fragmentManager;
    //private Fragment fragment = null;


    private MenuItem menuDiferencia, menuInventario, menuResumen, menuNuevoProducto;
    private Menu menuNav;
    private Inventario inventario;
    private TextView txtNumEquipo, txtNumInventario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Inventario> inventarioList = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).list();
        //long idUltimoInventario=0;
        if (inventarioList.size() > 1) {
            //cierra todos los inventarios menos el ultimo
            for (int i = 0; i < inventarioList.size() - 1; i++) {
                Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Id.eq(inventarioList.get(i).getId())).unique();
                inventario.setEstado(1);
                inventario.update();

            }
        }

        inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        //fragmentManager = getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragment = new LibraryFragment();
        //fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
        //fragmentTransaction.commit();


        //verificar..esta devolviendo dos valores



        if (inventario != null) {
            txtNumInventario.setText("INV-" + inventario.getEmpresa().getCodempresa() + "-" + inventario.getNuminventario() + "-" + inventario.getNumequipo());
            if (inventario.getNumequipo() < 10) {
                txtNumEquipo.setText("0" + String.valueOf(inventario.getNumequipo()));
            } else {
                txtNumEquipo.setText(String.valueOf(inventario.getNumequipo()));
            }

        }

        if (inventario != null) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            if (inventario.getContexto() == 1) {
                abrirContextoInventario(fragmentTransaction);
                menuInventario.setChecked(true);
                menuDiferencia.setEnabled(false);
                menuResumen.setEnabled(false);

            } else if (inventario.getContexto() == 2) {
                abrirContextoDiferencias(fragmentTransaction);
                menuDiferencia.setChecked(true);
                menuDiferencia.setEnabled(true);
                menuInventario.setEnabled(false);
                menuResumen.setEnabled(false);
            } else if (inventario.getContexto() == 3) {
                abrirContextoResumen(fragmentTransaction);
                menuResumen.setChecked(true);
                menuDiferencia.setEnabled(true);
                menuDiferencia.setEnabled(false);
                menuInventario.setEnabled(false);
                menuNuevoProducto.setEnabled(false);
            } else {
                startActivity(new Intent(this, Login.class));
                finish();
            }

        } else {
            List<Empresa> empresaList = Controller.getDaoSession().getEmpresaDao().loadAll();
            if (empresaList.isEmpty()) {
                agregarEmpresas();
            }


            startActivity(new Intent(this, Login.class));
        }
    }
    //metodo sobreescrito que actualiza la cantidad al volver desde el activity detalle
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
        //inicializarRecycler();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (inventario==null || inventario.getContexto()==1){
            getMenuInflater().inflate(R.menu.toolbar_inventario, menu);
        }else if(inventario.getContexto()==2){
            getMenuInflater().inflate(R.menu.toolbar_diferencia, menu);
        }else{
            getMenuInflater().inflate(R.menu.toolbar_principal, menu);
        }

       getMenuInflater().inflate(R.menu.toolbar_principal, menu);
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_inventario) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            DialogCierreInventario dialogCierreInventario = new DialogCierreInventario();
            dialogCierreInventario.setCancelable(false);
            dialogCierreInventario.show(fragmentManager, "dialogo cerrar inventario");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

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
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FrmZonasInventario frmZonasInventario = new FrmZonasInventario();
                fragmentTransaction.replace(R.id.contenedor, frmZonasInventario);
                fragmentTransaction.setTransition(fragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
                //fragment = new FrmZonasInventario();
                break;

            // } else if (id == R.id.nav_diferencias) {
            case R.id.nav_diferencias:
                //FragmentManager fragmentManager = getFragmentManager();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FrmZonasDiferencia frmZonasDiferencia = new FrmZonasDiferencia();
                fragmentTransaction.replace(R.id.contenedor, frmZonasDiferencia);
                fragmentTransaction.setTransition(fragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
                //fragment = new FrmZonasDiferencia();
                break;

            //} else if (id == R.id.nav_resumen) {
            case R.id.nav_resumen:

                FrmResumen frmResumen = new FrmResumen();
                fragmentTransaction.replace(R.id.contenedor, frmResumen);
                fragmentTransaction.setTransition(fragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
                //fragment = new FrmResumen();
                break;

            case R.id.nav_nuevo_Producto:

                FrmNuevoProducto listarNuevo = new FrmNuevoProducto();
                //Reemplazar el fragment actual por el nuevo fragment
                fragmentTransaction.replace(R.id.contenedor, listarNuevo);
                //Animacion al abrir el nuevo fragment
                 fragmentTransaction.setTransition(fragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                //Regresa al fragment anterior con la tecla atras
                //transaction.addToBackStack(null);
                //getSupportActionBar().setTitle(menuItem.getTitle());
                fragmentTransaction.commit();
                //fragment = new FrmNuevoProducto();
                break;


            case R.id.nav_iniciarSesion:
                break;
            case R.id.nav_cerrarSesion:
                mensajeConfirmacionCierre();
                break;
            default:

                startActivity(new Intent(this, Login.class));
                finish();
                break;


        /*
        if(id==R.id.nav_inventario) {
            fragment = new FrmZonasInventario();

        }else if(id==R.id.nav_diferencias) {
            fragment=new FrmZonasDiferencia();
        }else if(id==R.id.nav_resumen) {
            fragment=new FrmResumen();
        }else if(id==R.id.nav_nuevo_Producto){
            fragment=new FrmNuevoProducto();
        }else if(id==R.id.nav_cerrarSesion){
            Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
            //Log.e("Inventario actual", String.valueOf(inventario.getId()));
            Controller.getDaoSession().getInventarioDao().delete(inventario);
            startActivity(new Intent(this, Login.class));
        }
                // } else if (id == R.id.nav_cerrarSesion) {
*/

            //FragmentTransaction transaction = fragmentManager.beginTransaction();
            //transaction.replace(R.id.contenedor, fragment);
            //transaction.commit();
        }


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    private void abrirContextoInventario(FragmentTransaction fragmentTransaction ) {
        //FragmentManager fragmentManager = getFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        FrmZonasInventario fragment = new FrmZonasInventario();
        fragmentTransaction.replace(R.id.contenedor, fragment);
        fragmentTransaction.commit();

    }

    private void abrirContextoDiferencias(FragmentTransaction fragmentTransaction) {
        //FragmentManager fragmentManager = getFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            FrmZonasDiferencia frmZonasDiferencia = new FrmZonasDiferencia();
            fragmentTransaction.replace(R.id.contenedor, frmZonasDiferencia);
            fragmentTransaction.commit();


    }

    private void abrirContextoResumen(FragmentTransaction fragmentTransaction) {
                  FrmResumen frmResumen = new FrmResumen();
            fragmentTransaction.replace(R.id.contenedor, frmResumen);
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
        confirmar.setMessage("¿Seguro que desea finalizar el inventario"+inventario.getNuminventario()+"?")
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

                                Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
                                //Log.e("Inventario actual", String.valueOf(inventario.getId()));
                                Controller.getDaoSession().getInventarioDao().delete(inventario);
                                startActivity(new Intent(getApplicationContext(), Login.class));
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

