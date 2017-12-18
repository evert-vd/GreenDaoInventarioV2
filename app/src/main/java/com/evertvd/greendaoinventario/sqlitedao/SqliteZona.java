package com.evertvd.greendaoinventario.sqlitedao;

import android.util.Log;

import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;


import java.util.List;

/**
 * Created by evertvd on 06/12/2017.
 */

public class SqliteZona implements IZona {
    @Override
    public List<Zona> listarZona() {
        List<Zona>zonaList=Controller.getDaoSession().getZonaDao().queryBuilder().orderAsc(ZonaDao.Properties.Nombre).list();
        return zonaList;
    }

    @Override
    public Zona buscarZonaNombre(String nombre) {
        Zona zona=new Zona();
        try {

            zona=Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Nombre.eq(nombre)).unique();
        }catch (Exception e){
            zona=null;
        }
        return zona;
    }

    @Override
    public Zona buscarZonaId(long id) {
        Zona zona=new Zona();
        try {
            zona=Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Id.eq(id)).unique();
        }catch (Exception e){
            zona=null;
        }
        return zona;
    }

    @Override
    public Zona obtenerZona(long idZona) {
        Zona zona=Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Id.eq(idZona)).unique();
        return zona;
    }

    @Override
    public boolean agregarZona(Zona zona) {
        boolean estado=false;
        try {
            Controller.getDaoSession().getZonaDao().insert(zona);
            estado=true;
        }catch (Exception e){
            estado=false;
        }
        return estado;
    }

    @Override
    public List<Zona> listarZonaDiferencia() {
       List<Zona>zonaList=Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Estado.notEq(0)).orderAsc(ZonaDao.Properties.Nombre).list();
       for (int i=0;i<zonaList.size();i++){
           Log.e("ZONA-ESTADO", zonaList.get(i).getNombre()+":"+String.valueOf(zonaList.get(i).getEstado()));
       }
       return  zonaList;
    }
    @Override
    public void calcularDiferenciaZona(){
        List<Zona>zonaList=listarZona();
        for (int i=0;i<zonaList.size();i++){
            Zona zona=obtenerZona(zonaList.get(i).getId());
            IProducto iProducto=new SqliteProducto();
            List<Producto>productoList=iProducto.listarProductoDiferencia(zona.getId());
              if(productoList.isEmpty()){
                zona.setEstado(0);//zona sin diferencia
            }else{
                zona.setEstado(1);
            }
            actualizarZona(zona);
        }
    }

    @Override
    public boolean actualizarZona(Zona zona) {
        Controller.getDaoSession().getZonaDao().update(zona);
        return true;
    }
}
