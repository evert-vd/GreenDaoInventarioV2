package com.evertvd.greendaoinventario.sqlitedao;

import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;

import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public class SqliteProducto implements IProducto {
    @Override
    public Producto obtenerProducto(long idProducto) {
        Producto producto=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Id.eq(idProducto)).unique();
        return producto;
    }

    @Override
    public boolean agregarProducto(Producto producto) {
        boolean estado=false;
        try {
            Controller.getDaoSession().getProductoDao().insert(producto);
            estado=true;
        }catch (Exception e){
            estado=false;
        }
        return estado;
    }

    @Override
    public void eliminarProducto(Producto producto) {
        Controller.getDaoSession().getProductoDao().delete(producto);
    }

    @Override
    public List<Producto> listarProducto() {
        List<Producto>productoList=Controller.getDaoSession().getProductoDao().queryBuilder().list();
        return productoList;
    }

    @Override
    public List<Producto> listarProductoApp() {
        List<Producto>productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Tipo.eq("App")).list();
        return productoList;
    }

    @Override
    public List<Producto> listarProductoSistema() {
        List<Producto>productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Tipo.notEq("App")).list();
        return productoList;
    }

    @Override
    public List<Producto> listarProductoDiferencia() {
        List<Producto>productoList=Controller.getDaoSession().getProductoDao().queryBuilder()
                .where(ProductoDao.Properties.Estado.eq(1)).list();//solo productos con diferencia
        return productoList;

    }

    @Override
    public List<Producto> listarProductoZona(long idZona) {
        List<Producto>productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Zona_id.eq(idZona))
                .where(ProductoDao.Properties.Estado.eq(1)).orderAsc(ProductoDao.Properties.Codigo).list();//solo productos con diferencia
        return productoList;

    }


    @Override
    public List<Producto> listarProductoZonaResumen(long idZona) {
        List<Producto>productoList=Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Zona_id.eq(idZona)).orderAsc(ProductoDao.Properties.Codigo).list();
        return productoList;

    }

    @Override
    public int totalProductosZona(long idZona) {
        int totalProductos=listarProductoZona(idZona).size();
        return totalProductos;
    }

    @Override
    public void calcularPoductoDiferencia() {
        List<Producto> productoList=listarProducto();
        IConteo iConteo=new SqliteConteo();
        for (int i=0;i<productoList.size();i++){
            Producto producto=obtenerProducto(productoList.get(i).getId());
            int totalConteo= iConteo.obtenerTotalConteo(producto.getId());
            //Log.e("CONTEO", String.valueOf(conteo));
            if((producto.getStock()-totalConteo)!=0){
               producto.setEstado(1);//con diferencia
            }else{
                producto.setEstado(0);//sin diferencia
                //actualizacion de validado conteo:
                List<Conteo>conteoList=iConteo.listarConteo(producto.getId());
                for (int j=0;j<conteoList.size();j++){
                    Conteo conteo=conteoList.get(j);
                    conteo.setValidado(1);
                    iConteo.actualizarConteo(conteo);
                }
            }
            Controller.getDaoSession().getProductoDao().update(producto);
        }
    }

    @Override
    public List<Producto> listarProductoDiferencia(long idZona) {
        List<Producto> productoList=Controller.getDaoSession().getProductoDao().queryBuilder()
                .where(ProductoDao.Properties.Estado.notEq(0)).where(ProductoDao.Properties.Zona_id.eq(idZona)).orderAsc(ProductoDao.Properties.Codigo).list();
        return productoList;
    }

    @Override
    public int ultimoProducto() {
        List<Producto> productoList=listarProducto();
        return (productoList.size());
    }
}
