package com.evertvd.greendaoinventario.vista.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.utils.Operaciones;
import com.evertvd.greendaoinventario.vista.fragments.FrmZonasDiferencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento con un diálogo personalizado
 */
public class DialogCierreInventario extends DialogFragment implements View.OnClickListener {
    private static final String TAG = DialogCierreInventario.class.getSimpleName();
    private Button btnAceptar, btnCancelar;
    private EditText etCodigo;
    private TextView tvCodigoAleatorio;
    private TextInputLayout tilCodigo;
    private MenuItem menuDiferencia;

    public DialogCierreInventario() {

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // this.idProducto=idProducto;
        //recuperacion del parametro que viene del punto de invocacion del dialog--viene como string
        // idProducto = getArguments().getInt("idProducto");

        return crearAgregarCantidad();
    }

    /**
     * Crea un diÃ¡logo con personalizado para comportarse
     * como formulario de entrada de cantidad
     *
     * @return DiÃ¡logo
     */
    public AlertDialog crearAgregarCantidad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_cerrar_inventario, null);
        //View v = inflater.inflate(R.layout.dialogo_registrar_conteo, null);

        tvCodigoAleatorio = (TextView) view.findViewById(R.id.tvCodAleatorio2);
        tvCodigoAleatorio.setText(String.valueOf(Operaciones.aleatorio()));

        etCodigo = (EditText) view.findViewById(R.id.etCodigo);

        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        btnAceptar = (Button) view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(this);

        tilCodigo = (TextInputLayout) view.findViewById(R.id.tilCodigo);

        builder.setView(view);
        builder.setTitle("Cerrar inventario");
        builder.setMessage("Ingresar el código generado para confirmar el cierre del inventario");
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAceptar) {
            if (validarCodigo(tilCodigo.getEditText().getText().toString())) {
                //NumeroAleatorio aleatorio=new NumeroAleatorio();
                //Log.e("Tiempo aleatotorio", String.valueOf(aleatorio.generarAleatorio(2,1)));

                //abrirFragmentDiferencia();


                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute(String.valueOf(1));
                this.dismiss();

            }
        } else if (v.getId() == R.id.btnCancelar) {
            this.dismiss();
        }
    }

    private boolean validarCodigo(String etCodigo) {
        if (etCodigo.trim().length() == 0 || etCodigo.equals(tvCodigoAleatorio.getText()) != true) {
            tilCodigo.setError("Los códigos no coinciden");
            return false;

        } else {
            tilCodigo.setError(null);
        }

        return true;
    }

    private void habilitarMenus() {
        NavigationView navView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        Menu menuNav = navView.getMenu();

        menuDiferencia = menuNav.findItem(R.id.nav_diferencias);
        menuDiferencia.setEnabled(true);
        menuDiferencia.setChecked(true);

        MenuItem menuInventario = menuNav.findItem(R.id.nav_inventario);
        menuInventario.setEnabled(false);

        MenuItem menuFinalizar = menuNav.findItem(R.id.nav_resumen);
        menuFinalizar.setEnabled(false);



    }

    private void abrirFragmentDiferencia() {
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FrmZonasDiferencia frmZonasDiferencia=new FrmZonasDiferencia();
        fragmentTransaction.replace(R.id.contenedor, frmZonasDiferencia);
        fragmentTransaction.commit();

    }

    private void asignarContextoDiferencias() {
        Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        inventario.setContexto(2);//diferencia
        inventario.update();

    }

    private List<Conteo> listarConteos(long idProducto){
        List<Conteo> conteoList=Controller.getDaoSession().getConteoDao().queryBuilder()
                .where(ConteoDao.Properties.Producto_id.eq(idProducto))
                .where(ConteoDao.Properties.Estado.notEq(-1)).list();//evitar los registros eliminados
        return conteoList;
    }

    private void calcularDiferencias(){
        //listar productos de inventario actual
        //listar conteos de productos listados
        Inventario inventario=Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        List<Producto> productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();


        for (int i=0; i<productoList.size();i++){
            List<Conteo>conteoList=listarConteos(productoList.get(i).getId());
            int totalConteo=0;
            //List<Conteo>conteoList=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(productoList.get(i).getId())).list();
                for (int j=0; j<conteoList.size();j++){
                    totalConteo+=conteoList.get(j).getCantidad();
                    //Log.e("totalConteo: ",String.valueOf(totalConteo));
                }
            if(totalConteo-productoList.get(i).getStock()==0 && productoList.get(i).getTipo().equalsIgnoreCase("Sistema")){
                productoList.get(i).setEstado(0);
                productoList.get(i).update();

            }else{
                productoList.get(i).setEstado(-1);
                productoList.get(i).update();
            }

            Log.e("esstadoProd: ",String.valueOf(productoList.get(i).getEstado()));
        }



    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0])*1000;
                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            //abrirFragmentDiferencia();
            progressDialog.dismiss();
            //abrirFragmentDiferencia(menuDiferencia);
            //startActivity(new Intent(getActivity(), MainActivity.class));
            //finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),"","Calculando diferencias...");

        }


        @Override
        protected void onProgressUpdate(String... text) {
           // finalResult.setText(text[0]);
            habilitarMenus();
            asignarContextoDiferencias();
            calcularDiferencias();
            //restaurarEstadoZona();
            abrirFragmentDiferencia();

        }
    }



}
