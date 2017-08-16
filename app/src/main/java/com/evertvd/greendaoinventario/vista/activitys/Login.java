package com.evertvd.greendaoinventario.vista.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Empresa;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.Zona_has_Inventario;
import com.evertvd.greendaoinventario.modelo.dao.EmpresaDao;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;
import com.evertvd.greendaoinventario.utils.Desencriptar;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private View view;
    private Spinner spinnerEmpresa;
    private Button btnIniciarSesion;
    private EditText txtNumeroEquipo, txtNombreArchivo;
    private String path, nombreArchivo;
    private static final int SOLICITUD_PERMISO_RW_MEMORY1 = 1;
    int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spinnerEmpresa = (Spinner) findViewById(R.id.spinnerEmpresa);

        List<Empresa> empresaList = Controller.getDaoSession().getEmpresaDao().loadAll();

        ArrayAdapter<Empresa> adapter = new ArrayAdapter<Empresa>(this, R.layout.support_simple_spinner_dropdown_item, empresaList);
        spinnerEmpresa.setAdapter(adapter);
        //spinnerEmpresa.setOnClickListener(this);

        txtNumeroEquipo = (EditText) findViewById(R.id.txtNumEquipo);
        txtNombreArchivo = (EditText) findViewById(R.id.txtNombreArchivo);
        txtNombreArchivo.setOnClickListener(this);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        try{

        if (v.getId() == R.id.txtNombreArchivo) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, FileInterno.class);
                //intent.putExtra("param1", "valor del parámetro 1 (viene de mainActivity)");
                startActivityForResult(intent, REQUEST_CODE);

            } else {
                solicitarPermisoMemoria1();
            }
        } else if (v.getId() == R.id.btnIniciarSesion) {

            //1.-generar nuevo inventario en bd
            //-obtener nombre del archivo
            //-validar con datos de entrada
            //2.-obtener zonas
            //3.-guardar zonas en bd
            //4.-obtener datos del producto del archivo
            //5.-guardar produtos del archivo en bd
            //6.-lanzar activity (con finish)
            //startActivity(new Intent(this, MainActivity.class));
            //finish();
            //obtener objeto seleccionado del spinner
            Empresa empresa = (Empresa) spinnerEmpresa.getSelectedItem();
            if (crearNuevoInventario(nombreArchivo, empresa.getEmpresa())) {
                List<Inventario> inventarioList = Controller.getDaoSession().getInventarioDao().loadAll();
                long idInventario = 0;
                for (int i = 0; i < inventarioList.size(); i++) {
                    if (i == inventarioList.size() - 1) {
                        idInventario = inventarioList.get(i).getId();
                        Log.e("idinvnetaiooo", String.valueOf(idInventario));
                        break;
                    }

                }
                crearZonas(idInventario);
                crearProductos(idInventario);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Ocurrió un error e el proceso", Toast.LENGTH_SHORT).show();
            }
        }
        }catch (Exception e){
            Log.e("ErrorCatch",e.toString());
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


    private String[] leerArchivoSD(String nombre) {
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            File file;
            FileReader lectorArchivo;

            file = new File(nombre);
            lectorArchivo = new FileReader(file);

            byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedReader bufferedReader = new BufferedReader(lectorArchivo);
            int i = bufferedReader.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);

                i = bufferedReader.read();
            }

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Error al leer el archivo .txt", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error al leer el archivo .txt", Toast.LENGTH_LONG).show();
            //btnCargarDatos.setVisibility(View.VISIBLE);
        }
        return byteArrayOutputStream.toString().split("\n");
    }


    private boolean crearNuevoInventario(String nombreArchivo, String nombreEmpresa) {
        Log.e("crearNuevoInv archivo", nombreArchivo);

        try {
            //INV-2-1028-1
            String[] valores = nombreArchivo.split("-");
        /*
        [0]=INV
        [1]=2: codigo empresa
        [2]=1028: nro inventario
        [3]=1.csv : nro equipo
        */

            int codEmpresa = Integer.parseInt(valores[1]);
            int numInventario = Integer.parseInt(valores[2]);
            String equipo = valores[3].substring(0, valores[3].length() - 4);
            //4:longitud de ".csv"
            int numEquipo = Integer.parseInt(equipo);

            //fecInventario=auxiliares.horaActual();

            Empresa empresa = Controller.getDaoSession().getEmpresaDao().queryBuilder().where(EmpresaDao.Properties.Empresa.eq(nombreEmpresa)).unique();
            if (numEquipo == Integer.parseInt(txtNumeroEquipo.getText().toString()) && empresa.getCodempresa() == codEmpresa) {
                Toast.makeText(this, "Bienvenido: " + empresa.getCodempresa(), Toast.LENGTH_SHORT).show();
                Inventario inventario = new Inventario();
                inventario.setNumequipo(numEquipo);
                inventario.setNuminventario(numInventario);
                inventario.setEstado(0);//0:abierto, 1:cerrado
                inventario.setContexto(1);//1:inventario, 2:diferencia, 3:resumen, 0: terminado
                inventario.setEmpresa_id(empresa.getId());
                Controller.getDaoSession().getInventarioDao().insert(inventario);
                return true;
            } else if (empresa.getCodempresa() != codEmpresa) {
                Toast.makeText(this, "Empresa incorrecta", Toast.LENGTH_SHORT).show();
                return false;
            } else if (numEquipo != Integer.parseInt(txtNumeroEquipo.getText().toString())) {
                Toast.makeText(this, "Número de equipo incorrecto", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Toast.makeText(this, "Formato de archivo incorrecto ", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error. Verificar datos o formato de arhivo incorrecto", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    private void crearZonas(long idInventario) {
        String[] productos = leerArchivoSD(path);
        //convertir arraystring en liststring
        List<String> lista = new ArrayList<String>();
        lista = Arrays.asList(productos);

        //lista para guardar zonas
        List<String> zonasList = new ArrayList<String>();

        //agregar zonas a lista evitando duplicados
        for (int i = 0; i < lista.size(); i++) {
            int primeraComa = lista.get(i).indexOf(",");
            String zona = lista.get(i).substring(0, primeraComa);
            //verificar que las zonas no se repitan
            if (!zonasList.contains(zona)) {
                zonasList.add(zona);
            }
        }

        //agregar zonas a BD
        for (int i = 0; i < zonasList.size(); i++) {
            //si no existe la zona, se crea el objeto y se guarda
            if (buscarZonas(zonasList.get(i)) == null) {
                Zona zona = new Zona();
                zona.setNombre(zonasList.get(i));
                zona.setEstado(0);
                Controller.getDaoSession().insert(zona);
            }
        }

        crearZonasInventario(zonasList, idInventario);
    }

    private void crearZonasInventario(List<String> zonaList, long idInventario) {
        for (int i = 0; i < zonaList.size(); i++) {
            Zona zona = buscarZonas(zonaList.get(i));
            Zona_has_Inventario zona_has_inventario = new Zona_has_Inventario();
            zona_has_inventario.setZona_id2(zona.getId());
            zona_has_inventario.setInventario_id2(idInventario);
            zona_has_inventario.setNombreZona(zona.getNombre());
            Controller.getDaoSession().getZona_has_InventarioDao().insert(zona_has_inventario);
        }
    }

    private Zona buscarZonas(String nombreZona) {
        Zona zona = Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Nombre.eq(nombreZona)).unique();
        return zona;
    }


    private void crearProductos(long idInventario) {
        String[] productos = leerArchivoSD(path);
        Desencriptar desencripta = new Desencriptar(nombreArchivo);
        for (int i = 0; i < productos.length; i++) {
            String[] linea = productos[i].split(",");
            Producto producto = new Producto();
            Zona zona = buscarZonas(linea[0]);
            producto.setCodigo(Integer.parseInt(linea[1]));
            producto.setDescripcion(linea[2]);
            //desencripta.stockDesencriptado(Double.parseDouble(linea[3]),Integer.parseInt(linea[1]));
            producto.setStock(desencripta.stockDesencriptado(Double.parseDouble(linea[3]), Integer.parseInt(linea[1])));
            producto.setTipo("Sistema");
            producto.setEstado(-1);//con diferencia;
            producto.setSeleccionado(0);//deseleccionado;
            producto.setInventario_id(idInventario);
            producto.setZona_id(zona.getId());
            Controller.getDaoSession().getProductoDao().insert(producto);
        }

    }


    private void solicitarPermisoMemoria1() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //explicacionPermisoMemoria1();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE}, SOLICITUD_PERMISO_RW_MEMORY1);
        }
    }





    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case SOLICITUD_PERMISO_RW_MEMORY1:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Realizamos la accion
                    Log.e("id archivo", String.valueOf(R.id.txtNombreArchivo));
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    //intent.putExtra("param1", "valor del parámetro 1 (viene de mainActivity)");
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    //finish();
                    //Toast.makeText(this, "Aceptar los permisos para poder acceder al archivo", Toast.LENGTH_SHORT).show();
                    //openSettings();
                    //showSnackBar();
                }
                break;


        }

    }
*/
}
