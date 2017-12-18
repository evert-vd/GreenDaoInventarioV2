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
import com.evertvd.greendaoinventario.sqlitedao.SqliteConteo;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.ItemClickListener;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.utils.Utils;
import com.evertvd.greendaoinventario.vista.activitys.ActivityProducto;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by evertvd on 30/01/2017.
 */

public class ResumenZonasAdapter extends RecyclerView.Adapter<ResumenZonasAdapter.ViewHolder> implements ItemClickListener {

    private Context contexto;
    private List<Zona> zonaList;




    public ResumenZonasAdapter(List<Zona> zonaList, Context contexto) {
        this.contexto = contexto;
        this.zonaList = zonaList;
    }

    @Override
    public void onItemClick(View view, int position) {

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(contexto);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putInt("tab", 0);
        myEditor.commit();

        Intent intent = new Intent(contexto, ActivityProducto.class);
        intent.putExtra("id", zonaList.get(position).getId());
        contexto.startActivity(intent);
        //view.getContext().startActivity(intent);
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView txtZona, txtCantidad;

        public ItemClickListener listener;

        public ViewHolder(View v, ItemClickListener listener) {
            super(v);
            this.txtZona = (TextView) v.findViewById(R.id.txtZona);
            this.txtCantidad = (TextView) v.findViewById(R.id.txtCantidad);
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
                .inflate(R.layout.item_resumen_zonas, viewGroup, false);
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
            holder.txtZona.setText(zonaList.get(position).getNombre());
            //IProducto iProducto=new SqliteProducto();
            IConteo iConteo=new SqliteConteo();
            int conteo=iConteo.obtenerTotalConteoZona(zonaList.get(position).getId());
            //Log.e("conteo", String.valueOf(conteo));

            /*List<Conteo> conteo=iConteo.obtenerTotalConteoZona(zonaList.get(position).getId());
            Log.e("SIZE", String.valueOf(conteo.size()));
            for (int i=0;i<conteo.size();i++){
                Log.e("conteo", String.valueOf(conteo.get(i).getCantidad()));
            }*/
            //Log.e("CONTEO", String.valueOf(conteo));

            holder.txtCantidad.setText(Utils.formatearNumero(conteo));

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
        return zonaList.size();

    }

    //metodo que asigna la nueva lista filtrada al recycler
    public void setFilter(List<Zona> listaProducto) {
        this.zonaList = new ArrayList<>();
        this.zonaList.addAll(listaProducto);
        notifyDataSetChanged();

    }

}
