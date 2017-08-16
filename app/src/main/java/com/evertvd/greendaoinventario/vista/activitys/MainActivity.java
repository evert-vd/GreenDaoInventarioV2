package com.evertvd.greendaoinventario.vista.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Empresa;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.vista.fragments.FrmResumen;
import com.evertvd.greendaoinventario.vista.fragments.FrmZonasDiferencia;
import com.evertvd.greendaoinventario.vista.fragments.FrmZonasInventario;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MenuItem menuDiferencia, menuInventario, menuResumen;
    private Menu menuNav;
    private Inventario inventario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
        menuResumen = menuNav.findItem(R.id.nav_resumen);

        //verificar..esta devolviendo dos valores
        List<Inventario> inventarioList=Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).list();
        long idUltimoInventario=0;
        if(inventarioList.size()>1){
          for (int i=0; i<inventarioList.size();i++){
              Log.e("invab", String.valueOf(inventarioList.get(i).getId()));
              Inventario inventario=Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Id.eq(inventarioList.get(i).getId())).unique();
              inventario.setEstado(1);
              inventario.update();
              idUltimoInventario=inventarioList.get(i).getId();
          }
            Log.e("idUltimo", String.valueOf(idUltimoInventario));
         Inventario inventario=Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Id.eq(idUltimoInventario)).unique();
            inventario.setEstado(0);
            inventario.update();
        }

        inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        if (inventario != null) {
            if (inventario.getContexto() == 1) {
                abrirContextoInventario();
                menuInventario.setChecked(true);
                menuDiferencia.setEnabled(false);
                menuResumen.setEnabled(false);

            } else if (inventario.getContexto() == 2) {
                abrirContextoDiferencias();
                menuDiferencia.setChecked(true);
                menuDiferencia.setEnabled(true);
                menuInventario.setEnabled(false);
                menuResumen.setEnabled(false);
            } else if (inventario.getContexto() == 3) {
                abrirContextoResumen();
                menuResumen.setChecked(true);
                menuDiferencia.setEnabled(true);
                menuDiferencia.setEnabled(false);
                menuInventario.setEnabled(false);
            } else {
                startActivity(new Intent(this, Login.class));
            }

        } else {
            List<Empresa> empresaList = Controller.getDaoSession().getEmpresaDao().loadAll();
            if (empresaList.isEmpty()) {
                agregarEmpresas();
            }

            startActivity(new Intent(this, Login.class));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        if (id == R.id.nav_inventario) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FrmZonasInventario frmZonas = new FrmZonasInventario();
            fragmentTransaction.replace(R.id.contenedor, frmZonas);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_diferencias) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FrmZonasDiferencia frmZonasDiferencia = new FrmZonasDiferencia();
            fragmentTransaction.replace(R.id.contenedor, frmZonasDiferencia);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_resumen) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FrmResumen frmResumen = new FrmResumen();
            fragmentTransaction.replace(R.id.contenedor, frmResumen);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_pruebas) {
            /*
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            Pruebas fragment = new Pruebas();
            fragmentTransaction.replace(R.id.contenedor, fragment);
            fragmentTransaction.commit();
            */
        } else if (id == R.id.nav_iniciarSesion) {


        } else if (id == R.id.nav_cerrarSesion) {
            Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
            Log.e("Inventario actual", String.valueOf(inventario.getId()));
            Controller.getDaoSession().getInventarioDao().delete(inventario);
            startActivity(new Intent(this, Login.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void abrirContextoInventario() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        FrmZonasInventario fragment = new FrmZonasInventario();
        fragmentTransaction.replace(R.id.contenedor, fragment);
        fragmentTransaction.commit();

    }

    private void abrirContextoDiferencias() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrmZonasDiferencia frmZonasDiferencia = new FrmZonasDiferencia();
        fragmentTransaction.replace(R.id.contenedor, frmZonasDiferencia);
        fragmentTransaction.commit();
    }

    private void abrirContextoResumen() {

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


}
