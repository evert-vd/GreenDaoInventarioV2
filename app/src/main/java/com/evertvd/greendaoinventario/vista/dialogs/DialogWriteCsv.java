package com.evertvd.greendaoinventario.vista.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;

/**
 * Fragmento con un diálogo personalizado
 */
public class DialogWriteCsv extends DialogFragment implements View.OnClickListener {
    private static final String TAG = DialogWriteCsv.class.getSimpleName();
    private Button btnNuevo, btnCancelar, btnReemplazar;
    private TextView txtMensaje1, txtNuevo, txtReemplazar;

    //Definimos la interfaz
    public interface OnClickEventoCsv {
    void botonDialogOnClick(String evento);
    }

    public DialogWriteCsv() {
        //this.idProducto=idProducto;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme);
    }

    @Override
    public void onStart() {
        super.onStart();
        //dialog pantalla completa
        /*Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }*/
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        return crearDialogoViewCollage();
    }

    /**
     * Crea un diÃƒÂ¡logo con personalizado para comportarse
     * como formulario de entrada de cantidad
     *
     * @return DiÃƒÂ¡logo
     */
    public AlertDialog crearDialogoViewCollage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_validar_reportes, null);

        txtMensaje1=(TextView)view.findViewById(R.id.txtMensaje1);
        txtNuevo=(TextView)view.findViewById(R.id.txtNuevo);
        txtReemplazar=(TextView)view.findViewById(R.id.txtReemplazar);

        String mensaje1="Ya existen los archivos:";
        String resumido=getActivity().getString(R.string.file_resumido)+getActivity().getString(R.string.extension_file);
        String detallado=getActivity().getString(R.string.file_detallado)+getActivity().getString(R.string.extension_file);
        String controlInventario=getActivity().getString(R.string.file_pdf)+getActivity().getString(R.string.pdf);

        SpannableString mensaje = new SpannableString(mensaje1+" "+resumido+","+detallado+","+controlInventario);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.grey_darken_4));// Puedes usar tambien .. new ForegroundColorSpan(Color.RED);
        mensaje.setSpan(colorSpan, mensaje1.length()+1, (mensaje1.length()+resumido.length()+1+detallado.length()+1+controlInventario.length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtMensaje1.setText(mensaje);


        String mensaje3="Se generará reportes con los siguientes nombres:";
        String nuevoReporte=getActivity().getString(R.string.file_resumido)+getActivity().getString(R.string.extension_file)+"(1)"+","+getActivity().getString(R.string.file_detallado)+getActivity().getString(R.string.extension_file)+"(1)"+","+getActivity().getString(R.string.file_pdf)+getActivity().getString(R.string.pdf)+"(1)";
        SpannableString mensajeNuevo = new SpannableString(mensaje3+" "+nuevoReporte);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.grey_darken_4));// Puedes usar tambien .. new ForegroundColorSpan(Color.RED);
        mensaje.setSpan(colorSpan2, mensaje3.length()+1, (nuevoReporte.length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtNuevo.setText(mensajeNuevo);

        String mensaje4="Se reemplazará los reportes existentes:";
        String reemplazarReporte=getActivity().getString(R.string.file_resumido)+getActivity().getString(R.string.extension_file)+","+getActivity().getString(R.string.file_detallado)+getActivity().getString(R.string.extension_file)+","+getActivity().getString(R.string.file_pdf)+getActivity().getString(R.string.pdf);
        SpannableString mensajeReemplazar = new SpannableString(mensaje4+" "+reemplazarReporte);
        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(getResources().getColor(R.color.grey_darken_4));// Puedes usar tambien .. new ForegroundColorSpan(Color.RED);
        mensaje.setSpan(colorSpan3, mensaje4.length()+1, (reemplazarReporte.length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtReemplazar.setText(mensajeReemplazar);
        //txtMensaje1.setText("Ya existe un archivo ");
        //txtMensaje2.setText(nombreArchivo);
        //txtMensaje3.setText(". Seleccione una opción..!");
        //txtMensaje.setText("Existe un archivo con el nombre "+ Html.fromHtml("<b>"+nombreArchivo+"</b>")+ ".Seleccione una opción");

        btnNuevo = (Button) view.findViewById(R.id.btnNuevo);
        btnNuevo.setOnClickListener(this);

        btnReemplazar = (Button) view.findViewById(R.id.btnReemplazar);
        btnReemplazar.setOnClickListener(this);

        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        builder.setView(view);
        builder.setTitle("Advertencia");
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNuevo) {
            this.dismiss();
            OnClickEventoCsv filter = (OnClickEventoCsv) getTargetFragment();
            filter.botonDialogOnClick("nuevo");

        } else if (v.getId() == R.id.btnReemplazar) {
            this.dismiss();
            OnClickEventoCsv filter = (OnClickEventoCsv) getTargetFragment();
            filter.botonDialogOnClick("reemplazar");

        } else {
            this.dismiss();
        }
    }

}

