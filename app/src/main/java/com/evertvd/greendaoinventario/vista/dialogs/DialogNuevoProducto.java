package com.evertvd.greendaoinventario.vista.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.daimajia.swipe.util.Attributes;
import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona_has_Inventario;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;

import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.modelo.dao.Zona_has_InventarioDao;
import com.evertvd.greendaoinventario.utils.Operaciones;
import com.evertvd.greendaoinventario.vista.adapters.NuevoProductoAdapter;
import com.evertvd.greendaoinventario.vista.fragments.FrmNuevoProducto;

import java.util.List;


/**
 * Fragmento con un diálogo personalizado
 */
public class DialogNuevoProducto extends DialogFragment implements View.OnClickListener {
    private static final String TAG = DialogNuevoProducto.class.getSimpleName();


    private EditText txtDescripcion, txtCodigo, txtObservacion;
    private TextInputLayout tilDescripcion, tilZona, tilCodigo;
    private Button btnAceptar, btnCancelar;
    private Spinner spinner;
    private int ultimoId;
    Inventario inventario;
    public DialogNuevoProducto() {
        //this.idProducto=idProducto;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
       // this.idProducto=idProducto;
        //recuperacion del parametro que viene del punto de invocacion del dialog--viene como string
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
        View view=inflater.inflate(R.layout.dialog_nuevo_prod, null);
        //View v = inflater.inflate(R.layout.dialogo_registrar_conteo, null);
        // Referencias TILs
        tilDescripcion = (TextInputLayout) view.findViewById(R.id.tilDescripcion);
        tilCodigo = (TextInputLayout)view.findViewById(R.id.tilCodigo);
        //tilObservacion = (TextInputLayout)view.findViewById(R.id.tilObservacion);
        tilZona = (TextInputLayout)view.findViewById(R.id.tilZona);

        //Referencia btn

        btnAceptar=(Button)view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(this);
        btnCancelar=(Button)view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        //Referencia ETS
        txtDescripcion=(EditText) view.findViewById(R.id.txtDescripcion);
        txtDescripcion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        txtDescripcion.setActivated(true);
        txtCodigo=(EditText)view.findViewById(R.id.txtCodigo);
        //Codigo asignado automaticamente al registro NN+(UltimoRegistro+1)
        inventario=Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();

        //ultimoId=iProducto.ultimoIdProducto(getActivity());
        txtCodigo.setText("NN"+String.valueOf(Operaciones.codigoUltimoProducto(inventario.getId())).toString()+1);
        txtCodigo.setEnabled(false);
        //txtObservacion=(EditText)view.findViewById(R.id.campoObservacion);

        //Metodo funcional para listar Zonas
        spinner = (Spinner)view.findViewById(R.id.spnZonas);
        //IZona iZona=new Sqlite_Zona();
        //String[] spinnerLists = iZona.listarZonaSpinner(getActivity());
       List<Zona_has_Inventario>zona_has_inventarioList= Controller.getDaoSession().getZona_has_InventarioDao().queryBuilder()
               .where(Zona_has_InventarioDao.Properties.Inventario_id2.eq(inventario.getId())).list();

        ArrayAdapter<Zona_has_Inventario> spinnerAdapter = new ArrayAdapter<Zona_has_Inventario>(getActivity(),android.R.layout.simple_spinner_dropdown_item, zona_has_inventarioList);
        spinner.setAdapter(spinnerAdapter);
        //builder.setTitle(R.string.tituloZonas);
        builder.setView(view);
        return builder.create();

    }


    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btnAceptar){
            boolean descripcion=validaDescripcionLLanta(tilDescripcion.getEditText().getText().toString());
            boolean zona=validaZonaSeleccionada(spinner.getSelectedItemPosition());
            if (descripcion&&zona){
                guardarDatos();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                //Paso 3: Crear un nuevo fragmento y añadirlo
                FrmNuevoProducto frmNuevoProducto = new FrmNuevoProducto();
                transaction.replace(R.id.contenedor, frmNuevoProducto);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();

                //guardarDatos();
                this.dismiss();
                //
            }
            crearAdaptadorRecycler();

        }else if(v.getId()== R.id.btnCancelar){

            this.dismiss();
        }
    }

    private boolean validaZonaSeleccionada(int id){
        if(id==0){
            tilZona.setError("Seleccionar una zona válida");
            return false;
        }else{
            tilZona.setError(null);
        }
        return true;
    }

    private boolean validaDescripcionLLanta(String descripcion){
        if(descripcion.trim().length()==0){
            tilDescripcion.setError("Ingresar la descripción");
            return false;
        }else{
            tilDescripcion.setError(null);
        }
        return true;
    }

    public void guardarDatos(){
        String descripcion=tilDescripcion.getEditText().getText().toString().toUpperCase();
        //String zona=spinner.getSelectedItem().toString();
        Zona_has_Inventario zona_has_inventario=(Zona_has_Inventario)spinner.getSelectedItem();//obtener datos del objeto en el spinner

        Producto producto=new Producto();
        producto.setCodigo(Operaciones.codigoUltimoProducto(inventario.getId())+1);//ultimo+1
        producto.setDescripcion(descripcion);
        //producto.setCodigo(txtCodigo.getText().toString().toUpperCase());
        producto.setStock(0.0);
        producto.setTipo("aplicación");
        producto.setEstado(-1);
        producto.setInventario_id(inventario.getId());
        producto.setZona_id(zona_has_inventario.getZona_id2());
        Controller.getDaoSession().insert(producto);
    }



    public void crearAdaptadorRecycler(){

        FragmentManager  fragmentManager=getFragmentManager();
        Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        List<Producto>productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId()))
                .where(ProductoDao.Properties.Tipo.notEq("Sistema")).list();//lista los productos agregados desde la app
        RecyclerView mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerview_registrar_nuevo_producto);
        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        NuevoProductoAdapter mAdapter = new NuevoProductoAdapter(getActivity(), productoList, fragmentManager);
        ((NuevoProductoAdapter) mAdapter).setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);


    }

    /*
    public static DialogRegistrarNuevoProducto newInstance() {
        DialogRegistrarNuevoProducto f = new DialogRegistrarNuevoProducto();
        return f;
    }
    */
}
