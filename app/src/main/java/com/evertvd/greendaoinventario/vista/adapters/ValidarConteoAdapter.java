package com.evertvd.greendaoinventario.vista.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.modelo.Conteo;

import java.util.List;

/**
 * Created by evertvd on 16/08/2017.
 */

public class ValidarConteoAdapter extends BaseAdapter {

    protected Context  context;
    protected List<Conteo> conteoList;

    public ValidarConteoAdapter(Context context, List<Conteo> conteoList) {
        this.context = context;
        this.conteoList = conteoList;
    }



    @Override
    public int getCount() {
        return conteoList.size();
    }

    public void clear() {
        conteoList.clear();
    }

    public void addAll(List<Conteo> conteoList) {
        for (int i = 0; i < conteoList.size(); i++) {
            conteoList.add(conteoList.get(i));

        }
    }

    @Override
    public Object getItem(int arg0) {
        return conteoList.get(arg0);
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
            v = inf.inflate(R.layout.item_validar_conteo, null);
        }

        for (int i=0; i<conteoList.size(); i++){
            Log.e("historialAdp", String.valueOf( conteoList.get(position).getCantidad()));
        }

        Conteo conteo = conteoList.get(position);

        TextView codigo = (TextView) v.findViewById(R.id.txtCodigo);
        if (conteo.getProducto().getTipo().equalsIgnoreCase("App")){
           codigo.setText(String.valueOf("NN"+conteo.getProducto().getCodigo()));
        }else{
            codigo.setText(String.valueOf(conteo.getProducto().getCodigo()));
        }
        TextView description = (TextView) v.findViewById(R.id.txtZona);
        description.setText(conteo.getProducto().getZona().getNombre());

        TextView cantidad=(TextView)v.findViewById(R.id.txtCantidad);
        cantidad.setText(String.valueOf(conteo.getCantidad()));


        return v;
    }

}
