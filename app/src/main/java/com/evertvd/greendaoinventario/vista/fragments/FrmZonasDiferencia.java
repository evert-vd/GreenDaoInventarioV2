package com.evertvd.greendaoinventario.vista.fragments;


import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;
import com.evertvd.greendaoinventario.utils.Operaciones;
import com.evertvd.greendaoinventario.vista.activitys.ProductoDif;
import com.evertvd.greendaoinventario.vista.activitys.ProductoInv;
import com.evertvd.greendaoinventario.vista.adapters.ZonasDifAdapter;
import com.evertvd.greendaoinventario.vista.dialogs.DialogCierreDiferencia;
import com.evertvd.greendaoinventario.vista.dialogs.DialogCierreInventario;
import com.evertvd.greendaoinventario.vista.dialogs.DialogValidarConteo;

import org.greenrobot.greendao.query.Join;
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
        Query<Zona> query = Controller.getDaoSession().getZonaDao().queryRawCreate(", PRODUCTO P WHERE P.ZONA_ID=T._id AND P.ESTADO<>0 AND P.INVENTARIO_ID=" + inventario.getId() + " GROUP BY T._id");
        List<Zona> lista = query.list();

        /*
        List<ATableObj> listATableObj = ATableDao.queryRawCreate(", BTable BT
        WHERE BT.nameid = T.nameid").list();
         */

        for (int i = 0; i < lista.size(); i++) {
            Log.e("colists", String.valueOf(lista.get(i).getNombre()));
            Log.e("estado", String.valueOf(lista.get(i).getEstado()));
        }

        List<Producto> productoList = Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();

        for (int i = 0; i < productoList.size(); i++) {
            Log.e("stock", String.valueOf(productoList.get(i).getStock()) + " estado:" + productoList.get(i).getEstado());

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
                QueryBuilder<Conteo> conteoQueryBuilder = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Validado.eq(0)).where(ConteoDao.Properties.Estado.notEq(-1));
                Join producto = conteoQueryBuilder.join(ConteoDao.Properties.Producto_id, Producto.class).where(ProductoDao.Properties.Estado.eq(-1));//con diferencia
                Join invnetario = conteoQueryBuilder.join(producto, ProductoDao.Properties.Inventario_id, Inventario.class, InventarioDao.Properties.Id);
                invnetario.where(InventarioDao.Properties.Estado.eq(0));
                List<Conteo> conteoList = conteoQueryBuilder.list();

                Log.e("tamaño", String.valueOf(conteoList.size()));
                for (int i = 0; i < conteoList.size(); i++) {
                    Log.e("estado3221", String.valueOf(conteoList.get(i).getValidado()));
                    conteoList.get(i).getValidado();
                }


                if (!conteoList.isEmpty()) {
                    DialogValidarConteo validarDiferencias = new DialogValidarConteo();
                    validarDiferencias.setCancelable(false);
                    validarDiferencias.show(getFragmentManager(), "codigos por validar");
                } else {
                    //mostrarProceso()
                    //FragmentManager fragmentManager = getFragmentManager();
                    //FragmentManager fragmentManager=getFragmentManager();
                    DialogCierreDiferencia dialogCierreDiferencia = new DialogCierreDiferencia();
                    dialogCierreDiferencia.setCancelable(false);
                    dialogCierreDiferencia.show(getFragmentManager(), "dialogo cerrar diferencias");
                }
            break;

            case R.id.action_actualizar:
                QueryBuilder<Conteo> qb = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Validado.eq(0)).where(ConteoDao.Properties.Estado.notEq(-1));
                Join producto1 = qb.join(ConteoDao.Properties.Producto_id, Producto.class).where(ProductoDao.Properties.Estado.eq(-1));//con diferencia
                Join inventario1 = qb.join(producto1, ProductoDao.Properties.Inventario_id, Inventario.class, InventarioDao.Properties.Id);
                inventario1.where(InventarioDao.Properties.Estado.eq(0));
                List<Conteo> conteoList1 = qb.list();

                if (!conteoList1.isEmpty()) {
                    DialogValidarConteo validarDiferencias = new DialogValidarConteo();
                    validarDiferencias.setCancelable(false);
                    validarDiferencias.show(getFragmentManager(), "codigos por validar");
                } else {
                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.execute(String.valueOf(1));
                }
            break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0])*1000;
                Thread.sleep(time);
                resp = "Sleept for " + params[0] + " seconds";
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
            progressDialog = ProgressDialog.show(getActivity(),"","Actualizando diferencias...");

        }


        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);
            //habilitarMenus();
            //asignarContextoResumen();
            calcularDiferencias();
            //restaurarEstadoZona();
            abrirFragmentDiferenciaZonas();
            //MetodosAuxiliares diferencia=new MetodosAuxiliares();
            //diferencia.verificarDiferencia(getActivity());
            //abrirFragmentDiferenciaZonas();
        }
    }

    private void abrirFragmentDiferenciaZonas(){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        FrmZonasDiferencia diferencias = new FrmZonasDiferencia();
        transaction.replace(R.id.contenedor, diferencias);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //Regresa al fragment anterior con la tecla atras
        //transaction.addToBackStack(null);
        //getActivity().setTitle(menuItem.getTitle());
        //menuItem.setChecked(true);
        transaction.commit();
    }



    private List<Conteo> listarConteos(long idProducto){
        List<Conteo> conteoList=Controller.getDaoSession().getConteoDao().queryBuilder()
                .where(ConteoDao.Properties.Producto_id.eq(idProducto))
                .where(ConteoDao.Properties.Estado.notEq(-1)).list();//evitar los registros eliminados
        return conteoList;
    }

    private void calcularDiferencias() {
        //listar productos de inventario actual
        //listar conteos de productos listados
        Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        List<Producto> productoList = Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();


        for (int i = 0; i < productoList.size(); i++) {

            List<Conteo> conteoList = listarConteos(productoList.get(i).getId());


            int totalConteo = 0;
            //List<Conteo>conteoList=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(productoList.get(i).getId())).list();
            for (int j = 0; j < conteoList.size(); j++) {
                totalConteo += conteoList.get(j).getCantidad();
                //Log.e("totalConteo: ",String.valueOf(totalConteo));
            }
            if (totalConteo - productoList.get(i).getStock() != 0) {
                productoList.get(i).setEstado(-1);
                productoList.get(i).update();
            } else {
                productoList.get(i).setEstado(0);
                productoList.get(i).update();
            }

            Log.e("esstadoProd: ", String.valueOf(productoList.get(i).getEstado()));
        }


    }}
