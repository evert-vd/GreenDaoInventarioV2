package com.evertvd.greendaoinventario.interfaces;

import com.evertvd.greendaoinventario.modelo.Historial;
import com.evertvd.greendaoinventario.modelo.Producto;

import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public interface IHistorial {
    public Historial obtenerHisotorial(long idHistorial);
    public boolean agregarHistorial(Historial historial);
    public List<Historial>listarHisotorial(long idConteo);
}
