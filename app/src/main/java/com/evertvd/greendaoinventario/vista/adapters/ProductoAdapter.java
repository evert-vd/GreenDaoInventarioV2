package com.evertvd.greendaoinventario.vista.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.ItemClickListener;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;
import com.evertvd.greendaoinventario.sqlitedao.SqliteConteo;
import com.evertvd.greendaoinventario.utils.Utils;
import com.evertvd.greendaoinventario.vista.activitys.ActivityConteo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by evertvd on 30/01/2017.
 */

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private Context contexto;
    private List<Producto> productoList;
    private Activity activity;
    private static OnItemClickListener onItemClickListener;

    public ProductoAdapter(List<Producto> productoList, Context contexto, Activity activity) {
        this.contexto = contexto;
        this.activity=activity;
        this.productoList = productoList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                         int viewType) {
        // create a new view

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_producto, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        //ViewHolder vh = new ViewHolder(v);
        //return vh;
        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //Tener cuidado con los tipos de datos que se almaccena, debemos parsear todo a Sring

        try {
            if(productoList.get(position).getTipo().equalsIgnoreCase("App")){
                holder.codigo.setText(String.valueOf("NN"+productoList.get(position).getCodigo()));
            }else{
                holder.codigo.setText(String.valueOf(productoList.get(position).getCodigo()));
            }
            holder.descripcion.setText(productoList.get(position).getDescripcion());
            //List<Zona> zonaList = Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Id.eq(productoList.get(position).getZona_id())).list();
            holder.zona.setText(productoList.get(position).getZona().getNombre());
            IConteo iConteo=new SqliteConteo();
            holder.cantidad.setText(Utils.formatearNumero(iConteo.obtenerTotalConteo(productoList.get(position).getId())));//cambiar por cantidad contada

            //METODOS DE PRUEBA
            //holder.stock.setText(String.valueOf(items.get(position).getStock()));
            //holder.estado.setText(String.valueOf(items.get(position).getEstado()));
            //TextDrawable drawable = TextDrawable.builder().buildRect("A", Color.RED);

        } catch (Exception e) {
            Log.e("Error", e.getMessage().toString());
            Toast.makeText(contexto, "Error al cargar los datos al adaptador", Toast.LENGTH_SHORT).show();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //Log.e(String.valueOf(items.size()),"errpr");
        return productoList.size();

    }

    public static interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }




    /*
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(contexto, ActivityConteo.class);
        intent.putExtra("id", productoList.get(position).getId());
        contexto.startActivity(intent);
    }*/


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        TextView codigo, stock, estado;
        TextView descripcion;
        TextView zona;
        TextView cantidad;//se está poniendo la cantidad

        //ImageView img;
        TextView idProducto;
        //public ItemClickListener listener;



        public ViewHolder(View view) {
            super(view);

            this.codigo = (TextView) view.findViewById(R.id.codigo);
            this.descripcion = (TextView) view.findViewById(R.id.descripcion);
            this.cantidad = (TextView) view.findViewById(R.id.cantidad);
            this.zona = (TextView) view.findViewById(R.id.nombreZona);
            //METODOS DE PRUEBA
            //this.stock=(TextView)v.findViewById(R.id.stock);
            //this.estado=(TextView)v.findViewById(R.id.estadoVista);
            //this.img=(ImageView)v.findViewById(R.id.imgLlanta);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position  = ViewHolder.super.getAdapterPosition();
                    onItemClickListener.onItemClick(v,position);
                }
            });

        }

    }


    //metodo que asigna la nueva lista filtrada al recycler
    public void setFilter(List<Producto> listaProducto) {
        this.productoList = new ArrayList<>();
        this.productoList.addAll(listaProducto);
        notifyDataSetChanged();

    }

}
