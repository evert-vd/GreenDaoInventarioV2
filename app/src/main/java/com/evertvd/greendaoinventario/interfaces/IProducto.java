package com.evertvd.greendaoinventario.interfaces;

import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;

import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public interface IProducto {
    public Producto obtenerProducto(long idProducto);
    public void eliminarProducto(Producto producto);
    public boolean agregarProducto(Producto producto);
    public List<Producto>listarProducto();
    public List<Producto>listarProductoSistema();//el reporte resumido solo incluye los productos del sistema
    public List<Producto> listarProductoApp();
    public List<Producto>listarProductoZona(long idZona);
    public List<Producto> listarProductoZonaResumen(long idZona);
    public int totalProductosZona(long idZona);
    public List<Producto> listarProductoDiferencia();
    public void calcularPoductoDiferencia();
    public List<Producto> listarProductoDiferencia(long idZona);
    public int ultimoProducto();
}
