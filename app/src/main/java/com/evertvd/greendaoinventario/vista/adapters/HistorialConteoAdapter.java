package com.evertvd.greendaoinventario.vista.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.modelo.Historial;

import java.util.List;

/**
 * Created by evertvd on 16/08/2017.
 */

public class HistorialConteoAdapter extends BaseAdapter {

    protected Context  context;
    protected List<Historial> historialList;

    public HistorialConteoAdapter(Context context, List<Historial> historialList) {
        this.context = context;
        this.historialList = historialList;
    }



    @Override
    public int getCount() {
        return historialList.size();
    }

    public void clear() {
        historialList.clear();
    }

    public void addAll(List<Historial> historialList) {
        for (int i = 0; i < historialList.size(); i++) {
            historialList.add(historialList.get(i));

        }
    }

    @Override
    public Object getItem(int arg0) {
        return historialList.get(arg0);
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
            v = inf.inflate(R.layout.item_historial, null);
        }

        for (int i=0; i<historialList.size(); i++){
            Log.e("historialAdp", String.valueOf( historialList.get(position).getCantidad()));
        }

        Historial dir = historialList.get(position);

        TextView title = (TextView) v.findViewById(R.id.txtConteo);
        title.setText(String.valueOf(dir.getCantidad()));

        TextView description = (TextView) v.findViewById(R.id.txtHoraRegistro);
        description.setText(dir.getFechamodificacion());

        TextView estado = (TextView) v.findViewById(R.id.txtTipo);


        if(dir.getTipo()==-1){
            estado.setText("Eliminado");
        }else if(dir.getTipo()==1){
            estado.setText("Inicial");//una modificacion
        }else if(dir.getTipo()==0){
            estado.setText("Conteo actual");
        }else{
            estado.setText("Modificado");
        }
        return v;
    }

}
