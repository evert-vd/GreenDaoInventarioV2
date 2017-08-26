package com.evertvd.greendaoinventario.utils;

import android.util.Log;

import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by evertvd on 16/08/2017.
 */

public class Operaciones {

    public Operaciones(){

    }

    public static int totalConteoProducto1(long idProducto) {
        //no incluye eliminados
        List<Conteo> conteoList = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(idProducto))
                .where(ConteoDao.Properties.Estado.notEq(-1)).list();
        int totalConteo = 0;
        for (int i = 0; i < conteoList.size(); i++) {
            totalConteo += conteoList.get(i).getCantidad();
        }

        return totalConteo;
    }

    public static int totalConteoProducto2(long idProducto) {
        //incluye eliminados
        List<Conteo> conteoList = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(idProducto)).list();
        int totalConteo = 0;
        for (int i = 0; i < conteoList.size(); i++) {
            totalConteo += conteoList.get(i).getCantidad();
        }
        return totalConteo;
    }

    public static int codigoUltimoProducto(long idInventario) {
        int idUltimoCodigo = 0;
        List<Producto> productoList = Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(idInventario)).list();
        idUltimoCodigo = productoList.size();
        for (int i = 0; i < productoList.size(); i++) {
            Log.e("idUltimo", String.valueOf(productoList.get(i).getId()) + " tamaño:" + String.valueOf(productoList.size()));
        }
        return idUltimoCodigo;
    }

    public void calcularDiferencias() {
        Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        List<Producto> productoList = Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();


        for (int i = 0; i < productoList.size(); i++) {

            List<Conteo> conteoList = listarConteos(productoList.get(i).getId());


            int totalConteo = 0;
            //List<Conteo>conteoList=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(productoList.get(i).getId())).list();
            for (int j = 0; j < conteoList.size(); j++) {
                totalConteo += conteoList.get(j).getCantidad();
                //Log.e("totalConteo: ",String.valueOf(totalConteo));
            }
            if (totalConteo - productoList.get(i).getStock() != 0) {
                productoList.get(i).setEstado(-1);
                productoList.get(i).update();
            } else {
                productoList.get(i).setEstado(0);
                productoList.get(i).update();
            }

            Log.e("esstadoProd: ", String.valueOf(productoList.get(i).getEstado()));
        }
    }

        public static List<Conteo> listarConteos(long idProducto){
            List<Conteo> conteoList = Controller.getDaoSession().getConteoDao().queryBuilder()
                    .where(ConteoDao.Properties.Producto_id.eq(idProducto))
                    .where(ConteoDao.Properties.Estado.notEq(-1)).list();//evitar los registros eliminados
            return conteoList;
        }


        public static int buscarConteo(long idConteo, long idProducto){
            //Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
            Conteo conteo=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Id.eq(idConteo))
                    .where(ConteoDao.Properties.Producto_id.eq(idProducto)).unique();

            return conteo.getCantidad();
        }


       public static String fechaActual(){
           SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
           Date hoy = new Date();
           return formato.format(hoy);
       }

}
