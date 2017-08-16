package com.evertvd.greendaoinventario.vista.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.dao.ProductoDao;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;
import com.evertvd.greendaoinventario.modelo.interfaces.ItemClickListener;
import com.evertvd.greendaoinventario.vista.activitys.ConteoInv;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by evertvd on 30/01/2017.
 */

public class ProductosInvAdapter extends RecyclerView.Adapter<ProductosInvAdapter.ViewHolder> implements ItemClickListener {

    private Context contexto;
    private List<Producto> productoList;

    public ProductosInvAdapter(List<Producto> productoList, Context contexto) {
        this.contexto = contexto;
        this.productoList = productoList;
    }

    @Override
    public void onItemClick(View view, int position) {
        //
        //Intent intent=new Intent(view.getContext(), PruebaDetalle.class);
        //intent.putExtra("idproducto", items.get(position).getIdproducto());
        //intent.putExtra("codigo", items.get(position).getCodigo());
        //intent.putExtra("descripcion", items.get(position).getDescripcion());
        //intent.putExtra("zona", items.get(position).getZona());


        //asignar estado cero a producto anterior selecionado
        List<Producto> productoSeleccionadoAnterior = Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Seleccionado.eq(1)).list();
        if (!productoSeleccionadoAnterior.isEmpty()) {
            for (int i = 0; i < productoSeleccionadoAnterior.size(); i++) {
                productoSeleccionadoAnterior.get(i).setSeleccionado(0);
                productoSeleccionadoAnterior.get(i).update();
            }
        }

        //asignar estado uno al producto actual
        Producto productoSeleccionadoActual = Controller.getDaoSession().getProductoDao().queryBuilder().where(ProductoDao.Properties.Id.eq(productoList.get(position).getId())).unique();
        productoSeleccionadoActual.setSeleccionado(1);//producto seleccionado actual
        productoSeleccionadoActual.update();

        Intent intent = new Intent(view.getContext(), ConteoInv.class);
        view.getContext().startActivity(intent);
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView codigo, stock, estado;
        TextView descripcion;
        TextView zona;
        TextView cantidad;//se está poniendo la cantidad
        //ImageView img;
        TextView idProducto;
        public ItemClickListener listener;

        public ViewHolder(View v, ItemClickListener listener) {
            super(v);

            this.codigo = (TextView) v.findViewById(R.id.codigo);
            this.descripcion = (TextView) v.findViewById(R.id.descripcion);
            this.cantidad = (TextView) v.findViewById(R.id.cantidad);
            this.zona = (TextView) v.findViewById(R.id.nombreZona);
            //METODOS DE PRUEBA
            //this.stock=(TextView)v.findViewById(R.id.stock);
            //this.estado=(TextView)v.findViewById(R.id.estadoVista);
            //this.img=(ImageView)v.findViewById(R.id.imgLlanta);
            this.listener = listener;
            v.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());

        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_producto_inv, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        //ViewHolder vh = new ViewHolder(v);
        //return vh;
        return new ViewHolder(v, this);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //Tener cuidado con los tipos de datos que se almaccena, debemos parsear todo a Sring

        try {
            holder.codigo.setText(String.valueOf(productoList.get(position).getCodigo()));
            holder.descripcion.setText(productoList.get(position).getDescripcion());
            List<Zona> zonaList = Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Id.eq(productoList.get(position).getZona_id())).list();
            holder.zona.setText(zonaList.get(0).getNombre());
            holder.cantidad.setText(String.valueOf(productoList.get(position).getStock()));

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

    //metodo que asigna la nueva lista filtrada al recycler
    public void setFilter(List<Producto> listaProducto) {
        this.productoList = new ArrayList<>();
        this.productoList.addAll(listaProducto);
        notifyDataSetChanged();

    }


}
