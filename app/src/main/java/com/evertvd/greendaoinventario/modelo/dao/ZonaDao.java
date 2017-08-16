package com.evertvd.greendaoinventario.modelo.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.evertvd.greendaoinventario.modelo.Zona;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ZONA".
*/
public class ZonaDao extends AbstractDao<Zona, Long> {

    public static final String TABLENAME = "ZONA";

    /**
     * Properties of entity Zona.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Nombre = new Property(1, String.class, "nombre", false, "NOMBRE");
        public final static Property Diferencia = new Property(2, Integer.class, "diferencia", false, "DIFERENCIA");
        public final static Property Estado = new Property(3, Integer.class, "estado", false, "ESTADO");
    }

    private DaoSession daoSession;


    public ZonaDao(DaoConfig config) {
        super(config);
    }
    
    public ZonaDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ZONA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NOMBRE\" TEXT UNIQUE ," + // 1: nombre
                "\"DIFERENCIA\" INTEGER," + // 2: diferencia
                "\"ESTADO\" INTEGER);"); // 3: estado
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ZONA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Zona entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nombre = entity.getNombre();
        if (nombre != null) {
            stmt.bindString(2, nombre);
        }
 
        Integer diferencia = entity.getDiferencia();
        if (diferencia != null) {
            stmt.bindLong(3, diferencia);
        }
 
        Integer estado = entity.getEstado();
        if (estado != null) {
            stmt.bindLong(4, estado);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Zona entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nombre = entity.getNombre();
        if (nombre != null) {
            stmt.bindString(2, nombre);
        }
 
        Integer diferencia = entity.getDiferencia();
        if (diferencia != null) {
            stmt.bindLong(3, diferencia);
        }
 
        Integer estado = entity.getEstado();
        if (estado != null) {
            stmt.bindLong(4, estado);
        }
    }

    @Override
    protected final void attachEntity(Zona entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Zona readEntity(Cursor cursor, int offset) {
        Zona entity = new Zona( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nombre
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // diferencia
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // estado
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Zona entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNombre(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDiferencia(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setEstado(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Zona entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Zona entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Zona entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}