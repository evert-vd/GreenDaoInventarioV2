package com.evertvd.greendaoinventario.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Empresa;
import com.evertvd.greendaoinventario.modelo.Historial;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.EmpresaDao;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;

import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by evertvd on 17/08/2017.
 */

public class Reporte {

    public void Resumido(Context context, String nombreCarpeta){

        Inventario inventario= Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        QueryBuilder<Producto> queryBuilder=Controller.getDaoSession().getProductoDao().queryBuilder();
        queryBuilder.where(ProductoDao.Properties.Inventario_id.eq(inventario.getId()));
        List<Producto> productoList=queryBuilder.list();



        if (ComprobarSD.comprobarMemoriaSD()){
            try {
                SimpleDateFormat formato = new SimpleDateFormat("dd-MMMM-yyyy hh:mm aaa");
                Date hoy = new Date();
                String hoyFormato = formato.format(hoy);

                //File rutaSD = Environment.getExternalStorageDirectory(); //Ruta absoluta a la SD
                //String ruta=rutaSD.getAbsolutePath()+"/Download"; /escribir en directorio Download

                File carpeta = context.getExternalFilesDir(nombreCarpeta);//Ruta relativa a la app
                carpeta.mkdir();
                String ruta=carpeta.getAbsolutePath();//ruta de la carpeta
                File f = new File(ruta, "resumido-"+hoyFormato+".csv");



                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));
                //String cadena="";
                //fout.write("REPORTE RESUMIDO "+hoyFormato.toUpperCase());
                //fout.write("\n");
                //fout.write("ZONA, CODIGO, CANTIDAD, DESCRIPCION");
                //fout.write("\n");
                for (int i=0; i<productoList.size(); i++){
                    //cadena+=listProducto.get(i).getZona()+","+listProducto.get(i).getCodigo()+","+listProducto.get(i).getDescripcion()+","+listProducto.get(i).getCantidad()+"\n";
                    fout.write(productoList.get(i).getZona().toString());
                    Log.e("prueba",productoList.get(i).getZona().toString());
                    fout.write(",");
                    fout.write(String.valueOf(productoList.get(i).getCodigo()));
                    fout.write(",");
                    fout.write(productoList.get(i).getDescripcion());
                    int cantidad=Operaciones.totalConteoProducto1(productoList.get(i).getId());
                    fout.write(",");
                    fout.write(String.valueOf(cantidad));
                    fout.write("\n");
                }
                //fout.write(cadena);
                fout.close();
                //Toast.makeText(context, "Reporte Resumido exportado correctamente", Toast.LENGTH_SHORT).show();


            }
            catch (Exception ex)
            {
                Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
            }

        }else{
            Toast.makeText(context, "La memoria SD no está disponible", Toast.LENGTH_SHORT).show();
        }


    }


    public void Detallado(Context context, String nombreCarpeta){
        Inventario inventario= Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        Empresa empresa=Controller.getDaoSession().getEmpresaDao().queryBuilder().where(EmpresaDao.Properties.Id.eq(inventario.getId())).unique();



        /*
        QueryBuilder<Conteo> queryBuilder=Controller.getDaoSession().getConteoDao().queryBuilder();
        queryBuilder.join(ProductoDao.Properties.Id, Producto.class)
                .where(ProductoDao.Properties.Inventario_id.eq(inventario.getId()));
        List<Conteo> productoList=queryBuilder.list();

        //QueryBuilder<Conteo> conteoQueryBuilder=Controller.getDaoSession().getConteoDao().queryBuilder();
        //conteoQueryBuilder.join(Producto.class, ProductoDao.Properties.Id);
        //List<Conteo> outputPersonList = Controller.getDaoSession().getConteoDao().queryDeep("WHERE T0._ID=T.PRODUCTO_ID",null);
        List<Producto> productoList = Controller.getDaoSession().getProductoDao().queryDeep("LEFT JOIN CONTEO C ON T._id=C.PRODUCTO_ID WHERE T.INVENTARIO_ID="+inventario.getId(),null);

        for (int i=0; i<productoList.size();i++){
            Log.e("detallado1",String.valueOf(productoList.get(i).getCodigo()));
        }
        */
        List<Producto> productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();
        Log.e("tamaño",String.valueOf(productoList.size()));
        /*
        for(int i=0;i<productoList.size();i++){
            List<Conteo>conteoList=productoList.get(i).getConteoList();
            if(!productoList.get(i).getConteoList().isEmpty()){
            for(int j=0;j<conteoList.size();j++){
                Log.e("cod",String.valueOf(productoList.get(i).getCodigo())+" cont:"+ productoList.get(i).getConteoList().get(j).getCantidad());
                List<Historial> historialList=conteoList.get(j).getHistorialList();
                if(!conteoList.get(j).getHistorialList().isEmpty()){
                    for(int k=0;k<historialList.size();k++){
                        Log.e("hist",String.valueOf(historialList.get(i).getTipo())+" hit: 0");
                    }

                }

            }
            }else{
                Log.e("cod",String.valueOf(productoList.get(i).getCodigo())+" cont: 0");
            }


        }
*/
        /*
        QueryBuilder<Producto> queryBuilder=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId()));
        queryBuilder.join(Conteo.class, ConteoDao.Properties.Producto_id);
        List<Producto> productoList=queryBuilder.list();*/


        if (ComprobarSD.comprobarMemoriaSD()){
            try {
                SimpleDateFormat formato = new SimpleDateFormat("dd-MMMM-yyyy hh:mm aaa");
                Date hoy = new Date();
                String hoyFormato = formato.format(hoy);
                //File rutaSD = Environment.getExternalStorageDirectory(); //Ruta absoluta a la SD
                //String ruta=rutaSD.getAbsolutePath()+"/Download"; //escribir en directorio Dowload

                File carpeta = context.getExternalFilesDir(nombreCarpeta);//Ruta relativa a la app
                carpeta.mkdir();
                String ruta=carpeta.getAbsolutePath();


                String numeroEquipo="";
                if (inventario.getNumequipo()<10){
                    numeroEquipo="0"+String.valueOf(inventario.getNumequipo());
                }else{
                    numeroEquipo=String.valueOf(inventario.getNumequipo());
                }
                File f = new File(ruta, "detallado-"+hoyFormato+".csv");
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f),"iso-8859-1");
                fout.write("REPORTE DETALLADO "+hoyFormato.toUpperCase());
                fout.write("\n");
                //empresa.getCodempresa()
                fout.write("Archivo origen: INV-"+String.valueOf(inventario.getEmpresa().getCodempresa())+"-"+inventario.getNuminventario()+"-"+numeroEquipo);
                fout.write("\n");
                fout.write("Equipo Inventariador: "+numeroEquipo);
                fout.write("\n");
                fout.write("ZONA, CODIGO,DESCRIPCION,CANTIDAD,OBSERVACION,HISTORIAL,ESTADO");
                fout.write("\n");

                for (int i=0; i<productoList.size(); i++){
                    //Log.e("detallado",String.valueOf(productoList.get(i).getProducto().getCodigo())+" cantidad:"+String.valueOf(productoList.get(i).getCantidad()));
                    List<Conteo>conteoList=productoList.get(i).getConteoList();
                    if(!conteoList.isEmpty()){

                        for(int j=0;j<conteoList.size();j++){
                            fout.write(String.valueOf(productoList.get(i).getZona().toString()));
                            fout.write(",");
                            fout.write(String.valueOf(productoList.get(i).getCodigo()));
                            fout.write(",");
                            fout.write(String.valueOf(productoList.get(i).getDescripcion()));
                            fout.write(",");
                            if(conteoList.get(j).getEstado()==-1){
                                fout.write(String.valueOf(-conteoList.get(j).getCantidad()));
                            }else{
                                fout.write(String.valueOf(conteoList.get(j).getCantidad()));
                            }
                            fout.write(",");
                            fout.write(String.valueOf(conteoList.get(j).getObservacion()));
                            fout.write(",");
                            String historial="";

                            List<Historial> historialList=productoList.get(i).getConteoList().get(j).getHistorialList();
                            if(!productoList.get(i).getConteoList().get(j).getHistorialList().isEmpty()){
                                for(int k=0;k<historialList.size();k++){
                                    String tipo="";
                                    if(historialList.get(k).getTipo()==1){
                                        tipo="inicial:"+String.valueOf(historialList.get(k).getCantidad()+"/");
                                    }else if(historialList.get(k).getTipo()==2){
                                        tipo="modificación:"+String.valueOf(historialList.get(k).getCantidad()+"/");
                                    }else if(historialList.get(k).getTipo()==-1){
                                        tipo="eliminación:"+String.valueOf(historialList.get(k).getCantidad());
                                    }
                                    historial+=tipo;
                                }
                            }
                             //*/
                            if(conteoList.get(j).getEstado()==-1){
                                fout.write(String.valueOf(historial));
                            }else{
                                fout.write(String.valueOf(historial+"actual:"+conteoList.get(j).getCantidad()));
                            }
                            fout.write(",");
                            fout.write(String.valueOf(conteoList.get(j).getEstado()));
                            fout.write("\n");
                        }
                    }else{
                        fout.write(String.valueOf(productoList.get(i).getZona().toString()));
                        fout.write(",");
                        fout.write(String.valueOf(productoList.get(i).getCodigo()));
                        fout.write(",");
                        fout.write(String.valueOf(productoList.get(i).getDescripcion()));
                        fout.write(",");
                        fout.write(String.valueOf(0));//cantidad
                        fout.write(",");
                        fout.write(String.valueOf(""));
                        fout.write(",");
                        fout.write(String.valueOf("sin historial"));
                        fout.write(",");
                        fout.write(String.valueOf("estado del conteo"));
                        fout.write("\n");
                    }



                }
                fout.write("\n");
                fout.write("\n");

                /*
                List<BeanReporte> listProductoNuevo=iReporte.reporteDetalladoNuevoProducto(context);
                if (listProductoNuevo.isEmpty()==false){
                    fout.write("Productos que se encuentran fuera del stock del sistema".toUpperCase());
                    fout.write("\n");
                    fout.write("ZONA, CODIGO,DESCRIPCION,CANTIDAD,OBSERVACION, HISTORIAL");
                    fout.write("\n");

                    for (int i=0; i<listProductoNuevo.size(); i++){
                        fout.write(listProductoNuevo.get(i).getZona());
                        fout.write(",");
                        fout.write(listProductoNuevo.get(i).getCodigo());
                        fout.write(",");
                        fout.write(String.valueOf(listProductoNuevo.get(i).getDescripcion()));
                        fout.write(",");
                        fout.write(String.valueOf(listProductoNuevo.get(i).getCantidad()));
                        fout.write(",");
                        fout.write(String.valueOf(listProductoNuevo.get(i).getObservacion()));
                        fout.write(",");
                        fout.write(String.valueOf(iHistorial.obtenerHistorialCadena(context,listProductoNuevo.get(i).getIdConteo())));
                        fout.write("\n");
                    }
                }
            */

                fout.close();
                //Toast.makeText(context, "Reporte Detallado exportado correctamente", Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex)
            {
                Toast.makeText(context, "Error al generar reporte detallado", Toast.LENGTH_SHORT).show();
                Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
            }
        }else{
            Toast.makeText(context, "La memoria SD no estÃ¡ disponible", Toast.LENGTH_SHORT).show();
        }



    }


}


