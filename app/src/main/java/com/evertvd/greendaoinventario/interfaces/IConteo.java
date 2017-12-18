package com.evertvd.greendaoinventario.interfaces;

import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Producto;

import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public interface IConteo {
    public Conteo obtenerConteo(long idConteo);
    public boolean agregarConteo(Conteo conteo);
    public List<Conteo>listarConteo(long idProducto);
    public List<Conteo>listarAllConteo();
    public void actualizarConteo(Conteo conteo);
    public void eliminarConteo(Conteo conteo);
    public int obtenerTotalConteo(long idProducto);
    public int obtenerTotalConteoZona(long idZona);
    public int totalConteo();
    public List<Conteo>listarConteoPorValidar();
}
