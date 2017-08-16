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
import com.evertvd.greendaoinventario.modelo.Zona_has_Inventario;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.modelo.dao.Zona_has_InventarioDao;

import java.util.List;

/**
 * Created by evertvd on 16/08/2017.
 */

public class ZonaResAdapter extends BaseAdapter {

    protected Context context;
    protected List<Zona_has_Inventario> zonaHasInventarioList;
    public ZonaResAdapter(Context context, List<Zona_has_Inventario> zonaHasInventarioList) {
        this.context = context;
        this.zonaHasInventarioList = zonaHasInventarioList;
    }



    @Override
    public int getCount() {
        return zonaHasInventarioList.size();
    }

    public void clear() {
        zonaHasInventarioList.clear();
    }

    public void addAll(List<Zona_has_Inventario> zonaList) {
        for (int i = 0; i < zonaList.size(); i++) {
            zonaHasInventarioList.add(zonaList.get(i));

        }
    }

    @Override
    public Object getItem(int arg0) {
        return zonaHasInventarioList.get(arg0);
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
            v = inf.inflate(R.layout.adapter_resumen_zonas, null);
        }

        Zona_has_Inventario zona_has_inventario = zonaHasInventarioList.get(position);



        Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        List<Producto> productoZonaList = Controller.getDaoSession().getProductoDao().queryBuilder()
                .where(ProductoDao.Properties.Zona_id.eq(zonaHasInventarioList.get(position).getZona_id2()))
                .where(ProductoDao.Properties.Inventario_id.eq(inventario.getId())).list();

        //IProducto iProducto=new Sqlite_Producto();
        //long cantidad=iProducto.cantidadRegistrosPorZona(context, beanZona.getZona());
        //TextView productos = (TextView) v.findViewById(R.id.lblProductos);
        //productos.setText(String.valueOf(12));

        int totalCont = 0;
        for (int i = 0; i < productoZonaList.size(); i++) {
            List<Conteo> conteoList = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(productoZonaList.get(i).getId())).where(ConteoDao.Properties.Estado.notEq(-1)).list();
            for (int j = 0; j < conteoList.size(); j++) {
                totalCont += conteoList.get(j).getCantidad();
            }
        }


        /*
        IConteo iConteo=new Sqlite_Conteo();
        for(int i=0; i<items.size(); i++){
            totalConteo=iConteo.totalConteoPorZona(context,beanZona.getZona());
        }
*/

        TextView zona = (TextView) v.findViewById(R.id.txtZona);
        zona.setText(zona_has_inventario.getNombreZona());
        //zona.setText("abs-cs");

        TextView cantidad = (TextView) v.findViewById(R.id.txtCantidad);
        cantidad.setText(String.valueOf(totalCont));
        //zona.setText("12");

        return v;
    }


}
