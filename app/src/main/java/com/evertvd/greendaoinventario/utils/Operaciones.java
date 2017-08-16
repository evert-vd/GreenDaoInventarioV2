package com.evertvd.greendaoinventario.utils;

import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;

import java.util.List;

/**
 * Created by evertvd on 16/08/2017.
 */

public class Operaciones {


    public static int totalConteoProducto(long idProducto){
       List<Conteo> conteoList =Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(idProducto)).list();
       int totalConteo=0;
        for (int i=0; i<conteoList.size();i++){
            totalConteo+=conteoList.get(i).getCantidad();
        }

        return totalConteo;
    }



}
