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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;


/**
 * Fragmento con un diálogo personalizado
 */
public class RegistrarConteoInv extends DialogFragment implements View.OnClickListener {
    private static final String TAG = RegistrarConteoInv.class.getSimpleName();
    private int cantidadIngresada;
    private Button btnAceptar, btnCancelar;
    private EditText txtCantidad, txtObservacion;
    private TextInputLayout tilCantidad, tilObservacion;

    TextView lblConteo;
    //private String idProducto;

    //Definimos la interfaz
    public interface OnClickListener {
        void onInputDialog(int conteo, String observacion);
    }

    public RegistrarConteoInv() {
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
                RegistrarConteoInv.OnClickListener activity = (RegistrarConteoInv.OnClickListener) getActivity();
                activity.onInputDialog(Integer.parseInt(tilCantidad.getEditText().getText().toString()), tilObservacion.getEditText().getText().toString());
                this.dismiss();
            }
        } else if (v.getId() == R.id.btnCancelar) {
            this.dismiss();
        }
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
