package com.evertvd.greendaoinventario.sqlitedao;

import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;

import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public class SqliteConteo implements IConteo {

    @Override
    public Conteo obtenerConteo(long idConteo) {
        Conteo conteo=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Id.eq(idConteo)).unique();
        return conteo;
    }


    @Override
    public boolean agregarConteo(Conteo conteo) {
        boolean estado=false;
        try {
            Controller.getDaoSession().getConteoDao().insert(conteo);
            estado=true;
        }catch (Exception e){
            estado=false;
        }
        return estado;
    }

    @Override
    public List<Conteo> listarConteo(long idProducto) {
        List<Conteo>conteoList=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(idProducto)).where(ConteoDao.Properties.Estado.notEq(-1)).list();
        return conteoList;
    }

    @Override
    public List<Conteo> listarAllConteo() {
        List<Conteo>conteoList=Controller.getDaoSession().getConteoDao().loadAll();
        return conteoList;
    }

    @Override
    public void actualizarConteo(Conteo conteo) {
        Controller.getDaoSession().getConteoDao().update(conteo);
    }

    @Override
    public void eliminarConteo(Conteo conteo) {
        Controller.getDaoSession().getConteoDao().delete(conteo);
    }

    @Override
    public int obtenerTotalConteo(long idProducto) {
        int cantidad=0;
        List<Conteo>conteoList=listarConteo(idProducto);
        for (int i=0;i<conteoList.size();i++){
            cantidad+=conteoList.get(i).getCantidad();
        }
        return cantidad;
    }

    @Override
    public int obtenerTotalConteoZona(long idZona) {
        QueryBuilder<Conteo> qb=Controller.getDaoSession().getConteoDao().queryBuilder();
        Join producto=qb.join(ConteoDao.Properties.Producto_id, Producto.class);
        Join zona=qb.join(producto,ProductoDao.Properties.Zona_id, Zona.class, ZonaDao.Properties.Id);
        zona.where(ZonaDao.Properties.Id.eq(idZona));
        List<Conteo> conteoList=qb.list();

        int conteo=0;
        for (int i=0;i<conteoList.size();i++){
            conteo+=conteoList.get(i).getCantidad();
        }
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        return conteo;
    }

    @Override
    public int totalConteo() {
        List<Conteo>conteoList=Controller.getDaoSession().getConteoDao().loadAll();
        int totalConteo=0;
        for (int i=0;i<conteoList.size();i++){
            totalConteo+=conteoList.get(i).getCantidad();
        }
        return totalConteo;
    }

    @Override
    public List<Conteo>listarConteoPorValidar(){
        List<Conteo>conteoList=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Validado.eq(0)).list();

        return conteoList;

    }

}
