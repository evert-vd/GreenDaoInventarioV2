package com.evertvd.greendaoinventario.vista.activitys;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.sqlitedao.SqliteEmpresa;
import com.evertvd.greendaoinventario.interfaces.IEmpresa;
import com.evertvd.greendaoinventario.modelo.Empresa;
import com.evertvd.greendaoinventario.threads.MainThreadsReadData;
import com.evertvd.greendaoinventario.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private Spinner spinnerEmpresa;
    private Button btnIniciarSesion;
    private EditText txtNumeroEquipo, txtNombreArchivo;
    private TextView txtManualUsuario;
    private String path, nombreArchivo;
    private View view;
    private static final int WRITE_STORAGE = 1 ;

    int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Iniciar Inventario");

        setContentView(R.layout.activity_login);
        spinnerEmpresa = (Spinner) findViewById(R.id.spinnerEmpresa);

        IEmpresa iEmpresa=new SqliteEmpresa();
        //iEmpresa.listarEmpresa();

        List<Empresa> empresaList = iEmpresa.listarEmpresa();

        ArrayAdapter<Empresa> adapter = new ArrayAdapter<Empresa>(this, R.layout.support_simple_spinner_dropdown_item, empresaList);
        spinnerEmpresa.setAdapter(adapter);
        //spinnerEmpresa.setOnClickListener(this);

        txtNumeroEquipo = (EditText) findViewById(R.id.txtNumEquipo);
        txtNombreArchivo = (EditText) findViewById(R.id.txtNombreArchivo);
        txtNombreArchivo.setOnClickListener(this);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(this);
        txtManualUsuario=(TextView)findViewById(R.id.txtManualUsuario);
        txtManualUsuario.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        try{
            view=v;
        if (v.getId() == R.id.txtNombreArchivo) {
                //si la API 23 a mas
              verificarPermisos(v);

        } else if (v.getId() == R.id.btnIniciarSesion) {

            String equipo = txtNumeroEquipo.getText().toString().trim();
            String nomArchivo = txtNombreArchivo.getText().toString().trim();

            if (TextUtils.isEmpty(equipo)) {
                Toast.makeText(getApplicationContext(), "Ingresar el número de equipo", Toast.LENGTH_SHORT).show();
                txtNumeroEquipo.requestFocus();
                txtNumeroEquipo.setFocusable(true);
                return;
            }

            if (TextUtils.isEmpty(nomArchivo)){

                txtNombreArchivo.requestFocus();
                Toast.makeText(getApplicationContext(), "Buscar y selecionar archivo", Toast.LENGTH_SHORT).show();
                return;
            }

            validarDatos(Integer.parseInt(txtNumeroEquipo.getText().toString()));
            //thread zona
            //thread data
        }else if(v.getId()==R.id.txtManualUsuario) {
            verificarPermisos(v);
        }
        }catch (Exception e){

            Toast.makeText(this,"El formato de archivo es incorrecto", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirPdf(){
        Utils.copyRawToSDCard(R.raw.manual_usuario, Environment.getExternalStorageDirectory() + "/manual_usuario.pdf", this );
        File pdfFile = new File(Environment.getExternalStorageDirectory(),"/manual_usuario.pdf" );//File path
        if (pdfFile.exists()){ //Revisa si el archivo existe!
            Uri path = Uri.fromFile(pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //define el tipo de archivo
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
            //Inicia pdf viewer
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
            }
        } else {
            Toast.makeText(getApplicationContext(), "El archivo manual.pdf no existe o tiene otro nombre...! ", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirIntentFileInterno(){
        Intent intent = new Intent(this,FileInterno.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void verificarPermisos(View v) {
        //si la API 23 a mas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Habilitar permisos para la version de API 23 a mas
            int verificarPermisoReadContacts = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //Verificamos si el permiso no existe
            if (verificarPermisoReadContacts != PackageManager.PERMISSION_GRANTED) {
                //verifico si el usuario a rechazado el permiso anteriormente
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Si a rechazado el permiso anteriormente muestro un mensaje
                    mostrarExplicacion();
                } else {
                    //De lo contrario carga la ventana para autorizar el permiso
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE);
                }

            } else {
                //Si el permiso ya fue concedido abrimos en intent de contactos
                if(v.getId()==R.id.txtManualUsuario){
                    abrirPdf();
                }else{
                    abrirIntentFileInterno();
                }

            }

        } else {//Si la API es menor a 23 - abrimos en intent de contactos
            if(v.getId()==R.id.txtManualUsuario){
                abrirPdf();
            }else{

                abrirIntentFileInterno();
            }

        }
    }


    private void validarDatos(int numEquipo){
        String file = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
        //Log.e("NAMEFILE", file);
        String[] valores = file.split("-");
        int codEmpresa = Integer.parseInt(valores[1]);
        int numInventario = Integer.parseInt(valores[2]);
        String equipo = valores[3].substring(0, valores[3].length());
        //String equipo = valores[3].substring(0, valores[3].length() - 4);
        //4:longitud de ".csv"
        int equipoFile = Integer.parseInt(equipo);
        if(numEquipo==equipoFile){
            MainThreadsReadData mainHilos=new MainThreadsReadData(this,btnIniciarSesion,txtNombreArchivo,path);
            mainHilos.execute();
        }else{
            Toast.makeText(this, "El número de equipo es incorrecto",Toast.LENGTH_SHORT).show();
            txtNumeroEquipo.requestFocus();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == REQUEST_CODE) {
                // cogemos el valor devuelto por la otra actividad
                path = data.getStringExtra("path");
                nombreArchivo = data.getStringExtra("nombreArchivo");

                // enseñamos al usuario el resultado
                txtNombreArchivo.setText(nombreArchivo);
                //Toast.makeText(this, "ParametrosActivity devolvió: " + nombreArchivo, Toast.LENGTH_LONG).show();
            }
        } else {
            if (txtNombreArchivo.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Ningún archivo seleccionado", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this,Login.class));
        //finish();
    }*/

    private void mostrarExplicacion() {
        String mensaje1 = "La aplicación";
        String mensaje2 = getResources().getString(R.string.app_name);
        String mensaje3 = getResources().getString(R.string.mensaje_permiso);

        SpannableString mensaje = new SpannableString(mensaje1 + " " + mensaje2 + " " + mensaje3);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorGreyDarken_2));// Puedes usar tambien .. new ForegroundColorSpan(Color.RED);
        mensaje.setSpan(colorSpan, mensaje1.length() + 1, (mensaje1.length() + mensaje2.length() + 1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.titulo_permiso));
        builder.setMessage(mensaje);
        builder.setCancelable(false);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE);
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Mensaje acción cancelada
                mensajeAccionCancelada();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button cancel = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (cancel != null) {
            //b.setBackgroundColor(Color.CYAN);
            cancel.setTextColor(getResources().getColor(R.color.colorGreyDarken_2));//color por código al boton cancelar del dialogo
        }
    }

    public void mensajeAccionCancelada(){
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.rechazo_permiso), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_STORAGE:
                //Si el permiso a sido concedido abrimos la agenda de contactos
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(view.getId()==R.id.txtManualUsuario){
                        verificarPermisos(view);
                    }else{
                        verificarPermisos(view);
                    }

                } else {
                    mensajeAccionCancelada();
                }
                break;
        }
    }

}
