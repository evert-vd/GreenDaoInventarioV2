package com.evertvd.greendaoinventario.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.csvreader.CsvWriter;
import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.IHistorial;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Historial;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteConteo;
import com.evertvd.greendaoinventario.sqlitedao.SqliteHistorial;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by evertvd on 17/08/2017.
 */

public class ReporteCSV {


    private String nombreArchivo(Context context, int numReporte, String tipoReporte) {
        if (numReporte == 1 && tipoReporte.equalsIgnoreCase("resumido")) {
            return context.getString(R.string.file_resumido) + "(1)" + context.getString(R.string.extension_file);
        } else if (numReporte == 1 && tipoReporte.equalsIgnoreCase("detallado")) {
            return context.getString(R.string.file_detallado) + "(1)" + context.getString(R.string.extension_file);
        } else if (numReporte == 0 && tipoReporte.equalsIgnoreCase("resumido")) {
            return context.getString(R.string.file_resumido) + context.getString(R.string.extension_file);
        } else {
            return context.getString(R.string.file_detallado) + context.getString(R.string.extension_file);
        }
    }

    public void Resumido(Context context, int numReporte) {
        if (ComprobarSD.comprobarMemoriaSD()) {
            try {
                IProducto iProducto = new SqliteProducto();
                List<Producto> productoList = iProducto.listarProductoSistema();
                MainDirectorios.crearDirectorioApp(context);
                String nombreReporte = "";
                if (numReporte == 0) {
                    nombreReporte = nombreArchivo(context, 0, "resumido");
                    MainDirectorios.eliminarFichero(context, nombreReporte);
                } else {
                    nombreReporte = nombreArchivo(context, 1, "resumido");
                    MainDirectorios.eliminarFichero(context, nombreReporte);

                }

                CsvWriter csvOutput = new CsvWriter(new FileWriter(MainDirectorios.obtenerDirectorioApp(context) + "/" + nombreReporte, true), ',');
                for (int i = 0; i < productoList.size(); i++) {
                    csvOutput.write(productoList.get(i).getZona().getNombre());
                    csvOutput.write(String.valueOf(productoList.get(i).getCodigo()));
                    csvOutput.write(productoList.get(i).getDescripcion());
                    IConteo iConteo = new SqliteConteo();
                    csvOutput.write(String.valueOf(iConteo.obtenerTotalConteo(productoList.get(i).getId())));
                    csvOutput.endRecord();
                }
                csvOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
                //Log.e("ERROR", "error");
            }
        } else {
            Toast.makeText(context, "La memoria SD no está disponible", Toast.LENGTH_SHORT).show();
        }
    }

    public void Detallado(Context context, int numReporte) {
        if (ComprobarSD.comprobarMemoriaSD()) {
            try {
                IInventario iInventario = new SqliteInventario();
                Inventario inventario = iInventario.obtenerInventario();

                IConteo iConteo = new SqliteConteo();
                List<Conteo> conteoList = iConteo.listarAllConteo();
                Log.e("SIZE", String.valueOf(conteoList.size()));
                String nombreReporte;
                if (numReporte == 0) {
                    nombreReporte = nombreArchivo(context, 0, "detallado");
                    MainDirectorios.eliminarFichero(context, nombreReporte);
                } else {
                    nombreReporte = nombreArchivo(context, 1, "detallado");
                    MainDirectorios.eliminarFichero(context, nombreReporte);
                }

                CsvWriter csvOutput = new CsvWriter(new FileWriter(MainDirectorios.obtenerDirectorioApp(context) + "/" + nombreReporte, true), ',');

                String numeroEquipo = "";
                if (inventario.getNumequipo() < 10) {
                    numeroEquipo = "0" + String.valueOf(inventario.getNumequipo());
                } else {
                    numeroEquipo = String.valueOf(inventario.getNumequipo());
                }

                /*csvOutput.write("REPORTE DETALLADO "+Operaciones.fechaActual().toUpperCase());
                csvOutput.write("\n");
                //empresa.getCodempresa()
                csvOutput.write("Archivo origen: INV-"+String.valueOf(inventario.getEmpresa().getCodempresa())+"-"+inventario.getNuminventario()+"-"+numeroEquipo);
                csvOutput.write("\n");
                csvOutput.write("Equipo Inventariador: "+numeroEquipo);
                csvOutput.write("\n");*/
                //titulos
                csvOutput.write("ZONA");
                csvOutput.write("CODIGO");
                csvOutput.write("DESCRIPCION");
                csvOutput.write("CANTIDAD");
                csvOutput.write("OBSERVACION");
                csvOutput.write("HISTORIAL");
                csvOutput.write("ESTADO");
                csvOutput.endRecord();

                for (int i = 0; i < conteoList.size(); i++) {
                    csvOutput.write(conteoList.get(i).getProducto().getZona().getNombre());
                    csvOutput.write(String.valueOf(conteoList.get(i).getProducto().getCodigo()));
                    csvOutput.write(conteoList.get(i).getProducto().getDescripcion());
                    csvOutput.write(String.valueOf(conteoList.get(i).getCantidad()));
                    csvOutput.write(String.valueOf(conteoList.get(i).getObservacion()));

                    IHistorial iHistorial = new SqliteHistorial();
                    String historial = "";
                    List<Historial> historialList = iHistorial.listarHisotorial(conteoList.get(i).getId());
                    if (!historialList.isEmpty()) {
                        String tipo = "";
                        for (int j = 0; j < historialList.size(); j++) {
                            if (historialList.get(j).getTipo() == 1) {
                                tipo = "inicial:" + String.valueOf(historialList.get(j).getCantidad() + "/");
                            } else if (historialList.get(j).getTipo() == 2) {
                                tipo = "modificación:" + String.valueOf(historialList.get(j).getCantidad() + "/");
                            } else if (historialList.get(j).getTipo() == -1) {
                                tipo = "eliminación:" + String.valueOf(historialList.get(j).getCantidad());
                            }
                            historial += tipo;
                        }
                    }
                    if (conteoList.get(i).getEstado() == -1) {
                        csvOutput.write(String.valueOf(historial));
                    } else {
                        csvOutput.write(String.valueOf(historial + "actual:" + conteoList.get(i).getCantidad()));
                    }
                    if (conteoList.get(i).getEstado() == -1) {
                        csvOutput.write("Eliminado");
                    } else {
                        csvOutput.write("");
                    }
                    csvOutput.endRecord();
                }

                csvOutput.close();
                //Toast.makeText(context, "Reporte Detallado exportado correctamente", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(context, "Error al generar reporte detallado", Toast.LENGTH_SHORT).show();
                Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
            }
        } else {
            Toast.makeText(context, "La memoria SD no está disponible", Toast.LENGTH_SHORT).show();
        }
    }

   }





