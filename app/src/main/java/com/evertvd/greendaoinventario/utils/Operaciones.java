package com.evertvd.greendaoinventario.utils;

import android.util.Log;

import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;

import java.util.List;

/**
 * Created by evertvd on 16/08/2017.
 */

public class Operaciones {


    public static int totalConteoProducto1(long idProducto){
        //no incluye eliminados
       List<Conteo> conteoList =Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(idProducto))
               .where(ConteoDao.Properties.Estado.notEq(-1)).list();
       int totalConteo=0;
        for (int i=0; i<conteoList.size();i++){
            totalConteo+=conteoList.get(i).getCantidad();
        }

        return totalConteo;
    }

    public static int totalConteoProducto2(long idProducto){
        //incluye eliminados
        List<Conteo> conteoList =Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(idProducto)).list();
        int totalConteo=0;
        for (int i=0; i<conteoList.size();i++){
            totalConteo+=conteoList.get(i).getCantidad();
        }
        return totalConteo;
    }

    public static int codigoUltimoProducto(long idInventario){
        int idUltimoCodigo=0;
        List<Producto> productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(idInventario)).list();
        idUltimoCodigo=productoList.size();
        for(int i=0; i<productoList.size();i++){
            Log.e("idUltimo",String.valueOf(productoList.get(i).getId())+" tamaño:" +String.valueOf(productoList.size()));
        }
        return idUltimoCodigo;
    }


    public static List<Conteo> listarConteos(long idProducto){
        //incluye eliminados
        List<Conteo> conteoList =Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(idProducto)).list();

        return conteoList;
    }

}
