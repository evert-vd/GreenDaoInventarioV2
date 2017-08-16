package com.evertvd.greendaoinventario.vista.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;

import java.util.List;

/**
 * Created by evertvd on 17/05/2017.
 */

public class ZonasDiferenciaAdapter extends BaseAdapter {


    protected Context context;
    protected List<Zona> zonaList;

    public ZonasDiferenciaAdapter(Context context, List<Zona> zonaList) {
        this.context = context;
        this.zonaList = zonaList;
    }

    @Override
    public int getCount() {
        return zonaList.size();
    }

    public void clear() {
        zonaList.clear();
    }


    @Override
    public Object getItem(int i) {
        return zonaList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.adapter_zona, null);
        }


        TextView title = (TextView) v.findViewById(R.id.lblZona);
        title.setText(String.valueOf(zonaList.get(position).getNombre()));
        Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        List<Producto> productoZonaList = Controller.getDaoSession().getProductoDao().queryBuilder()
                .where(ProductoDao.Properties.Zona_id.eq(zonaList.get(position).getId()))
                .where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();

        //IProducto iProducto=new Sqlite_Producto();
        //long cantidad=iProducto.cantidadRegistrosPorZona(context, beanZona.getZona());
        TextView productos = (TextView) v.findViewById(R.id.lblProductos);
        productos.setText(String.valueOf(productoZonaList.size()));

        int totalCont = 0;
        for (int i = 0; i < productoZonaList.size(); i++) {
            List<Conteo> conteoList = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(productoZonaList.get(i).getId())).where(ConteoDao.Properties.Estado.notEq(-1)).list();
            for (int j = 0; j < conteoList.size(); j++) {
                totalCont += conteoList.get(j).getCantidad();
            }
        }

        TextView totalConteo = (TextView) v.findViewById(R.id.lblTotalConteo);
        totalConteo.setText(String.valueOf(totalCont));

        return v;
    }

}
