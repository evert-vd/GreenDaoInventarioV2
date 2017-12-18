package com.evertvd.greendaoinventario.vista.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteZona;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * Fragmento con un diálogo personalizado
 */
public class DialogNuevoProducto extends DialogFragment implements View.OnClickListener {
    private static final String TAG = DialogNuevoProducto.class.getSimpleName();


    private EditText txtDescripcion, txtCodigo, txtObservacion;
    private TextInputLayout tilDescripcion, tilZona, tilCodigo;
    private Button btnAceptar, btnCancelar;
    private Spinner spinner;
    private int codigo;
    Inventario inventario;
    public DialogNuevoProducto() {
        //this.idProducto=idProducto;
    }

    public interface InterfaceDialogNuevoProducto {
        public void addNuevoProducto(Producto producto);
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


        IProducto iProducto=new SqliteProducto();
        codigo=iProducto.ultimoProducto()+1;
        txtCodigo.setText("NN"+String.valueOf(codigo));
        txtCodigo.setEnabled(false);
        //txtObservacion=(EditText)view.findViewById(R.id.campoObservacion);

        //Metodo funcional para listar Zonas
        spinner = (Spinner)view.findViewById(R.id.spnZonas);
        //IZona iZona=new Sqlite_Zona();
        //String[] spinnerLists = iZona.listarZonaSpinner(getActivity());

        /*
        ArrayList<Integer> lista = new ArrayList<Integer>();
        lista.add(5);
        ArrayList<Integer> listaCopiada = new ArrayList<Integer>(lista);
        */
        IInventario iInventario=new SqliteInventario();
        Inventario inventario=iInventario.obtenerInventario();

        Zona zona=new Zona();
        zona.setNombre("Seleccionar Zona");

        List<Zona>zonaList1=new ArrayList<>();
        zonaList1.add(zona);

        IZona iZona=new SqliteZona();
        List<Zona>zonaList2=iZona.listarZona();

        List<Zona>zonaList3=new ArrayList<>();
        zonaList3.addAll(zonaList1);
        zonaList3.addAll(zonaList2);

        ArrayAdapter<Zona> adapter = new ArrayAdapter<Zona>(getActivity(), android.R.layout.simple_spinner_dropdown_item, zonaList3);
        spinner.setAdapter(adapter);
        builder.setView(view);
        return builder.create();

    }


    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btnAceptar){
            boolean descripcion=validaDescripcionLLanta(tilDescripcion.getEditText().getText().toString());
            Zona zona=(Zona) spinner.getSelectedItem();
            boolean zonaSeleccionada=validaZonaSeleccionada(spinner.getSelectedItemPosition());//debe guardar a partir de la psicion 1, 0=Seleccionar Zona
            if (descripcion&&zonaSeleccionada){
                Producto producto=new Producto();
                producto.setDescripcion(tilDescripcion.getEditText().getText().toString());
                producto.setZona_id(zona.getId());
                producto.setTipo("App");
                producto.setEstado(1);
                producto.setStock(0.00);
                producto.setCodigo(codigo);
                InterfaceDialogNuevoProducto filter = (InterfaceDialogNuevoProducto) getTargetFragment();
                filter.addNuevoProducto(producto);
                //guardarDatos();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                this.dismiss();
                //
            }
           // crearAdaptadorRecycler();

        }else if(v.getId()== R.id.btnCancelar){
            this.dismiss();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        //ocultar teclado

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

}
