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
import com.evertvd.greendaoinventario.modelo.Zona_has_Inventario;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.Zona_has_InventarioDao;
import com.evertvd.greendaoinventario.utils.Operaciones;

import java.util.List;

/**
 * Created by evertvd on 16/08/2017.
 */

public class ProductoResAdapater extends BaseAdapter {


    protected Context context;
    protected List<Producto> productoList;

    public ProductoResAdapater(Context context, List<Producto> productoList) {
        this.context = context;
        this.productoList = productoList;
    }



    @Override
    public int getCount() {
        return productoList.size();
    }

    public void clear() {
        productoList.clear();
    }

    public void addAll(List<Producto> productoList) {
        for (int i = 0; i < productoList.size(); i++) {
            productoList.add(productoList.get(i));

        }
    }

    @Override
    public Object getItem(int arg0) {
        return productoList.get(arg0);
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
            v = inf.inflate(R.layout.adapter_resumen_codigos, null);
        }

        /*
        BeanProducto producto = items.get(position);
        IConteo iConteo=new Sqlite_Conteo();
        int conteo=iConteo.totalConteo(context,producto.getIdproducto());
        */
        Inventario inventario=Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        Zona_has_Inventario zona_has_inventario=Controller.getDaoSession().getZona_has_InventarioDao().queryBuilder()
                .where(Zona_has_InventarioDao.Properties.Zona_id2.eq(productoList.get(position).getZona_id()))
                .where(Zona_has_InventarioDao.Properties.Inventario_id2.eq(inventario.getId())).unique();


        TextView id = (TextView) v.findViewById(R.id.txtId);
        TextView codigo = (TextView) v.findViewById(R.id.txtCodigo);
        TextView zona = (TextView) v.findViewById(R.id.txtZona);
        TextView cantidad=(TextView)v.findViewById(R.id.txtTotal);
        //zona.setText(producto.getZona());
        //cantidad.setText(String.valueOf(producto.getCodigo()));
        id.setText(String.valueOf(position+1));
        zona.setText(zona_has_inventario.getNombreZona());
        codigo.setText(String.valueOf(productoList.get(position).getCodigo()));
        cantidad.setText(String.valueOf(Operaciones.totalConteoProducto1(productoList.get(position).getId())));

        return v;
    }



}
