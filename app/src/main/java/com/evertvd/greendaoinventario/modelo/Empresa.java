package com.evertvd.greendaoinventario.modelo;

import org.greenrobot.greendao.annotation.*;

import java.util.List;
import com.evertvd.greendaoinventario.modelo.dao.DaoSession;
import org.greenrobot.greendao.DaoException;

import com.evertvd.greendaoinventario.modelo.dao.EmpresaDao;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "EMPRESA".
 */
@Entity(active = true)
public class Empresa {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private int codempresa;

    @NotNull
    @Unique
    private String empresa;
    private int estado;

    /** Used to resolve relations */
    @Generated
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated
    private transient EmpresaDao myDao;

    @ToMany(joinProperties = {
        @JoinProperty(name = "id", referencedName = "empresa_id")
    })
    private List<Inventario> inventarioList;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Empresa() {
    }

    public Empresa(Long id) {
        this.id = id;
    }

    @Generated
    public Empresa(Long id, int codempresa, String empresa, int estado) {
        this.id = id;
        this.codempresa = codempresa;
        this.empresa = empresa;
        this.estado = estado;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEmpresaDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCodempresa() {
        return codempresa;
    }

    public void setCodempresa(int codempresa) {
        this.codempresa = codempresa;
    }

    @NotNull
    public String getEmpresa() {
        return empresa;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setEmpresa(@NotNull String empresa) {
        this.empresa = empresa;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    @Generated
    public List<Inventario> getInventarioList() {
        if (inventarioList == null) {
            __throwIfDetached();
            InventarioDao targetDao = daoSession.getInventarioDao();
            List<Inventario> inventarioListNew = targetDao._queryEmpresa_InventarioList(id);
            synchronized (this) {
                if(inventarioList == null) {
                    inventarioList = inventarioListNew;
                }
            }
        }
        return inventarioList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated
    public synchronized void resetInventarioList() {
        inventarioList = null;
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void delete() {
        __throwIfDetached();
        myDao.delete(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void update() {
        __throwIfDetached();
        myDao.update(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void refresh() {
        __throwIfDetached();
        myDao.refresh(this);
    }

    @Generated
    private void __throwIfDetached() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
    }

    // KEEP METHODS - put your custom methods here

    @Override
    public String toString() {
        return empresa.toString();
    }
    // KEEP METHODS END

}
