package com.evertvd.greendaoinventario.sqlitedao;

import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.interfaces.IHistorial;
import com.evertvd.greendaoinventario.modelo.Historial;
import com.evertvd.greendaoinventario.modelo.dao.HistorialDao;

import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public class SqliteHistorial implements IHistorial {

    @Override
    public Historial obtenerHisotorial(long idHistorial) {
        Historial historial=Controller.getDaoSession().getHistorialDao().queryBuilder().where(HistorialDao.Properties.Id.eq(idHistorial)).unique();
        return historial;
    }

    @Override
    public boolean agregarHistorial(Historial historial) {
        boolean estado=false;
        try {
            Controller.getDaoSession().getHistorialDao().insert(historial);
            estado=true;
        }catch (Exception e){
            estado=false;
        }
        return estado;
    }

    @Override
    public List<Historial> listarHisotorial(long idConteo) {
        List<Historial>historialList=Controller.getDaoSession().getHistorialDao().queryBuilder().where(HistorialDao.Properties.Conteo_id.eq(idConteo)).list();
        return historialList;
    }
}
