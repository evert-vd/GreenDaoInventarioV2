package com.evertvd.greendaoinventario.vista.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.modelo.Conteo;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * Fragmento con un diálogo personalizado
 */
public class RegistrarConteo extends DialogFragment implements View.OnClickListener {
    private static final String TAG = RegistrarConteo.class.getSimpleName();
    private int cantidadIngresada;
    private Button btnAceptar, btnCancelar;
    private EditText txtCantidad, txtObservacion;
    private TextInputLayout tilCantidad, tilObservacion;

    //Definimos la interfaz
    public interface OnClickListener {
        void registrarConteo(Conteo conteo);
    }

    public RegistrarConteo() {
        //this.idProducto=idProducto;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
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
        View v = inflater.inflate(R.layout.dialogo_registrar_conteo, null);
        //View v = inflater.inflate(R.layout.dialogo_registrar_conteo, null);

        //txtCantidad=(EditText)v.findViewById(R.id.lblCantidadConteo);
        txtObservacion = (EditText) v.findViewById(R.id.txtObservacion);
        txtObservacion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        btnCancelar = (Button) v.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        btnAceptar = (Button) v.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(this);

        tilCantidad = (TextInputLayout) v.findViewById(R.id.tilCantidad);
        tilObservacion = (TextInputLayout) v.findViewById(R.id.tilObservacion);

        builder.setView(v);
        builder.setTitle("Registrar conteo");
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAceptar) {
            if (validarCantidad(tilCantidad.getEditText().getText().toString())) {
                RegistrarConteo.OnClickListener activity = (RegistrarConteo.OnClickListener) getActivity();
                Conteo conteo=new Conteo();
                conteo.setCantidad(Integer.parseInt(tilCantidad.getEditText().getText().toString()));
                conteo.setObservacion( tilObservacion.getEditText().getText().toString());
                conteo.setValidado(0);
                conteo.setEstado(0);//estado inicial
                activity.registrarConteo(conteo);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                this.dismiss();
            }
        } else if (v.getId() == R.id.btnCancelar) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            this.dismiss();
        }
        //ocultar teclado


    }

    private boolean validarCantidad(String cantidadIngresada) {
        if (cantidadIngresada.trim().length() == 0) {
            tilCantidad.setError("Ingresar una cantidad válida");
            return false;
        } else {
            tilCantidad.setError(null);
        }

        return true;
    }


}
