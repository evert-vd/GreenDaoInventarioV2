package com.evertvd.greendaoinventario.sqlitedao;

import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.interfaces.IEmpresa;
import com.evertvd.greendaoinventario.modelo.Empresa;
import com.evertvd.greendaoinventario.modelo.dao.EmpresaDao;


import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public class SqliteEmpresa implements IEmpresa {
    @Override
    public List<Empresa> listarEmpresa() {
        List<Empresa>empresaList=Controller.getDaoSession().getEmpresaDao().loadAll();
        return empresaList;
    }

    @Override
    public Empresa obtenerEmpresa(int codigo) {
        Empresa empresa= Controller.getDaoSession().getEmpresaDao().queryBuilder().where(EmpresaDao.Properties.Codempresa.eq(codigo)).unique();
        return empresa;
    }

    @Override
    public boolean agregarEmpresa(Empresa empresa) {
        boolean estado=false;
        try {
            Controller.getDaoSession().getEmpresaDao().insert(empresa);
            estado=true;
        }catch (Exception e){
            estado=false;
        }
        return estado;
    }
}
