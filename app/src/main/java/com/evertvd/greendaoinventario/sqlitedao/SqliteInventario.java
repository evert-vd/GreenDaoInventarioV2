package com.evertvd.greendaoinventario.sqlitedao;

import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;

import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public class SqliteInventario implements IInventario {
    @Override
    public Inventario obtenerInventario() {
        Inventario inventario=Controller.getDaoSession().getInventarioDao().queryBuilder().unique();
        return inventario;
    }

    /*@Override
    public Inventario obtenerInventarioCerrado() {
        Inventario inventario=Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(1)).unique();
        return inventario;
    }*/

    @Override
    public boolean agregarInventario(Inventario inventario) {
            Controller.getDaoSession().deleteAll(Inventario.class);
            Controller.getDaoSession().getInventarioDao().insert(inventario);
        return true;
    }

    @Override
    public boolean actualizarInventario(Inventario inventario) {
        Controller.getDaoSession().getInventarioDao().update(inventario);
        return true;
    }

    @Override
    public List<Inventario> listarInventario() {
        List<Inventario>inventarioList=Controller.getDaoSession().getInventarioDao().loadAll();
        return inventarioList;
    }
}
