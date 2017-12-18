package com.evertvd.greendaoinventario.vista.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.sqlitedao.SqliteConteo;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.interfaces.ItemClickListener;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.utils.Utils;
import com.evertvd.greendaoinventario.vista.activitys.ActivityProducto;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by evertvd on 30/01/2017.
 */

public class ZonasAdapter extends RecyclerView.Adapter<ZonasAdapter.ViewHolder> implements ItemClickListener {

    private Context contexto;
    private List<Zona> zonaList;
    private Activity activity;

    public ZonasAdapter(List<Zona> zonaList, Activity activity,Context contexto) {
        this.contexto = contexto;
        this.zonaList = zonaList;
        this.activity=activity;
    }

    @Override
    public void onItemClick(View view, int position) {


        Intent intent = new Intent(activity, ActivityProducto.class);
        intent.putExtra("id", zonaList.get(position).getId());
        //if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            //Explode explode=new Explode();
            //explode.setDuration(1200);
            //Slide slide=new Slide(Gravity.LEFT);
            //slide.setDuration(500);
            //slide.setInterpolator(new DecelerateInterpolator());
            //activity.getWindow().setExitTransition(slide);
            //activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
        //}else{
        contexto.startActivity(intent);
        //}
        //contexto.startActivity(intent);
        //view.getContext().startActivity(intent);
    }



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView txtZona, txtCantidad, txtConteo;

        public ItemClickListener listener;

        public ViewHolder(View v, ItemClickListener listener) {
            super(v);

            this.txtZona = (TextView) v.findViewById(R.id.txtZona);
            this.txtCantidad = (TextView) v.findViewById(R.id.txtCantidad);
            this.txtConteo=(TextView)v.findViewById(R.id.txtConteo);
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
                .inflate(R.layout.item_zona, viewGroup, false);
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
            IProducto iProducto=new SqliteProducto();
            int totalProductos=iProducto.totalProductosZona(zonaList.get(position).getId());
            holder.txtCantidad.setText(String.valueOf(totalProductos));
            //int totalConteo=iProducto.totalConteoProductoZona(zonaList.get(position).getId());
            IConteo iConteo=new SqliteConteo();
            int totalConteo=iConteo.obtenerTotalConteoZona(zonaList.get(position).getId());
            holder.txtConteo.setText(Utils.formatearNumero(totalConteo));

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
