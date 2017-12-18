package com.evertvd.greendaoinventario.vista.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.ItemClickListener;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteConteo;
import com.evertvd.greendaoinventario.utils.Utils;
import com.evertvd.greendaoinventario.vista.activitys.ActivityConteo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by evertvd on 30/01/2017.
 */

public class ResumenProductoAdapter extends RecyclerView.Adapter<ResumenProductoAdapter.ViewHolder> implements ItemClickListener {

    private Context contexto;
    private List<Producto> productoList;

    public ResumenProductoAdapter(List<Producto> productoList, Context contexto) {
        this.contexto = contexto;
        this.productoList = productoList;
    }

    @Override
    public void onItemClick(View view, int position) {

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(contexto);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putInt("tab", 1);
        myEditor.commit();


        Intent intent = new Intent(contexto, ActivityConteo.class);
        intent.putExtra("id", productoList.get(position).getId());
        contexto.startActivity(intent);
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView txtCodigo;
        TextView txtId;
        TextView txtZona;
        TextView txtTotal;//se está poniendo la cantidad
        //ImageView img;
        TextView idProducto;
        public ItemClickListener listener;

        public ViewHolder(View v, ItemClickListener listener) {
            super(v);
            this.txtId = (TextView) v.findViewById(R.id.txtId);
            this.txtCodigo = (TextView) v.findViewById(R.id.txtCodigo);
            this.txtTotal = (TextView) v.findViewById(R.id.txtTotal);
            this.txtZona = (TextView) v.findViewById(R.id.txtZona);
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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_resumen_producto, viewGroup, false);
        return new ViewHolder(v, this);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //Tener cuidado con los tipos de datos que se almaccena, debemos parsear todo a Sring

        try {
            holder.txtId.setText(String.valueOf(position+1));
            holder.txtZona.setText(productoList.get(position).getZona().getNombre());
            if(productoList.get(position).getTipo().equalsIgnoreCase("App")){
                holder.txtCodigo.setText(String.valueOf("NN"+productoList.get(position).getCodigo()));
            }else{
                holder.txtCodigo.setText(String.valueOf(productoList.get(position).getCodigo()));
            }
           //List<Zona> zonaList = Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Id.eq(productoList.get(position).getZona_id())).list();
            IConteo iConteo=new SqliteConteo();
            int total=iConteo.obtenerTotalConteo(productoList.get(position).getId());
            holder.txtTotal.setText(Utils.formatearNumero(total));//cambiar por cantidad contada

        } catch (Exception e) {
            Log.e("Error", e.getMessage().toString());
            Toast.makeText(contexto, "Error al cargar los datos al adaptador", Toast.LENGTH_SHORT).show();
        }


    }

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
