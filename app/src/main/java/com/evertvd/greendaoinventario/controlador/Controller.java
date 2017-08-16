package com.evertvd.greendaoinventario.controlador;

import android.app.Application;

import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.DaoMaster;
import com.evertvd.greendaoinventario.modelo.dao.DaoSession;
import com.evertvd.greendaoinventario.modelo.dao.EmpresaDao;
import com.evertvd.greendaoinventario.modelo.dao.HistorialDao;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by evertvd on 17/07/2017.
 */

public class Controller extends Application {
    public static final boolean ENCRYPTED = true;
    static DaoSession daoSession;
    static EmpresaDao empresaDao;
    static InventarioDao inventarioDao;
    static ZonaDao zonaDao;
    static ProductoDao productoDao;
    static ConteoDao conteoDao;
    static HistorialDao historialDao;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "inventariodb", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        empresaDao = daoSession.getEmpresaDao();
        inventarioDao = daoSession.getInventarioDao();
        zonaDao = daoSession.getZonaDao();
        productoDao = daoSession.getProductoDao();
        conteoDao = daoSession.getConteoDao();
        historialDao = daoSession.getHistorialDao();

        ///// Using the below lines of code we can toggle ENCRYPTED to true or false in other to use either an encrypted database or not.
//      DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "users-db-encrypted" : "users-db");
//      Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
//      daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
