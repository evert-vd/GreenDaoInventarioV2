package com.evertvd.greendaoinventario.utils;

import android.util.Log;

/**
 * Created by evertvd on 27/07/2017.
 */

public class Desencriptar {
    private int codigoEmpresa;
    private int nroEquipo;
    private int nroInventario;
    String nombreArchivo;

    public Desencriptar(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;

    }


    public double stockDesencriptado(Double stockCifrado, int codProducto) {
        Log.e("codProd", String.valueOf(codProducto));
        Log.e("stocCifrado", String.valueOf(stockCifrado));
        Log.e("Auxiliar", String.valueOf(numeroAuxiliar()));
        return (stockCifrado - codProducto) / numeroAuxiliar();
    }

    private int numeroAuxiliar() {
        String[] valores = nombreArchivo.split("-");
        codigoEmpresa = Integer.parseInt(valores[1]);
        nroInventario = Integer.parseInt(valores[2]);
        //String equipo=valores[3];
        //4:longitud de .csv
        String equipo = valores[3].substring(0, valores[3].length() - 4);
        nroEquipo = Integer.parseInt(equipo);
        return (codigoEmpresa * nroEquipo) + nroInventario;
    }

}
