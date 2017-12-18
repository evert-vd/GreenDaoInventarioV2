package com.evertvd.greendaoinventario.interfaces;

import com.evertvd.greendaoinventario.modelo.Zona;

import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public interface  IZona {
    public List<Zona>listarZona();
    public Zona buscarZonaNombre(String nombre);
    public Zona buscarZonaId(long id);
    public Zona obtenerZona(long idZona);
    public boolean agregarZona(Zona zona);
    public boolean actualizarZona(Zona zona);
    public List<Zona>listarZonaDiferencia();
    public void calcularDiferenciaZona();
}
