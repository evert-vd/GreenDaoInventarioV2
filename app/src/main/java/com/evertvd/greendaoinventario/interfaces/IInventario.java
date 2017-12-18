package com.evertvd.greendaoinventario.interfaces;

import com.evertvd.greendaoinventario.modelo.Empresa;
import com.evertvd.greendaoinventario.modelo.Inventario;

import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public interface IInventario {
    public Inventario obtenerInventario();
    //public Inventario obtenerInventarioCerrado();
    public boolean agregarInventario(Inventario inventario);
    public boolean actualizarInventario(Inventario inventario);
    public List<Inventario>listarInventario();
}
