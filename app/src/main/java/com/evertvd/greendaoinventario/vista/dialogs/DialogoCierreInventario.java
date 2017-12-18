package com.evertvd.greendaoinventario.vista.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.threads.ThreadCierreInventario;
import com.evertvd.greendaoinventario.utils.Utils;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Fragmento con un diálogo personalizado
 */
public class DialogoCierreInventario extends DialogFragment implements View.OnClickListener {
    private static final String TAG = DialogoCierreInventario.class.getSimpleName();
    private Button btnAceptar, btnCancelar;
    private EditText etCodigo;
    private TextView tvCodigoAleatorio;
    private TextInputLayout tilCodigo;
    private int contexto;
    public DialogoCierreInventario() {

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
        contexto=getArguments().getInt("contexto");
        Log.e("CONTEXTO", String.valueOf(contexto));

        tvCodigoAleatorio = (TextView) view.findViewById(R.id.tvCodAleatorio2);
        tvCodigoAleatorio.setText(String.valueOf(Utils.aleatorio()));

        etCodigo = (EditText) view.findViewById(R.id.etCodigo);

        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        btnAceptar = (Button) view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(this);
        tilCodigo = (TextInputLayout) view.findViewById(R.id.tilCodigo);
        builder.setView(view);
        if(contexto==0){
            builder.setTitle("Cerrar inventario");
            builder.setMessage("Ingresar el código generado para confirmar el cierre del inventario");
        }else if(contexto==1){
            builder.setTitle("Cerrar diferencias");
            builder.setMessage("Ingresar el código generado para confirmar el cierre de las diferencias");
        }

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAceptar) {
            if (validarCodigo(tilCodigo.getEditText().getText().toString())) {
            //ABRIR THREAD
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                //progressDialog.setTitle("Foto");
                progressDialog.setTitle("Realizando cálculo...");
                FragmentManager fragmentManager = getFragmentManager();
                ThreadCierreInventario threadCierreInventario=new ThreadCierreInventario(progressDialog,getActivity(),fragmentManager, contexto);
                threadCierreInventario.execute();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                this.dismiss();
            }
        } else if (v.getId() == R.id.btnCancelar) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            this.dismiss();
        }
    }

    private boolean validarCodigo(String etCodigo) {
        if (etCodigo.trim().length() == 0 || etCodigo.equals(tvCodigoAleatorio.getText()) != true) {
            tilCodigo.setError("Los códigos no coinciden");
            tilCodigo.setFocusable(true);
            return false;

        } else {
            tilCodigo.setError(null);
        }

        return true;
    }
}
