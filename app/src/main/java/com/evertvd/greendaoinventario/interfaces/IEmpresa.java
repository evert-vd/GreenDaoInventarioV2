package com.evertvd.greendaoinventario.interfaces;

import com.evertvd.greendaoinventario.modelo.Empresa;

import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public interface IEmpresa {
    public List<Empresa>listarEmpresa();
    public Empresa obtenerEmpresa(int codigo);
    public boolean agregarEmpresa(Empresa empresa);
}
