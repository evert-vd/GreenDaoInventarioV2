package com.evertvd.greendaoinventario.vista.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.modelo.BeanFile;

import java.util.List;

/**
 * Created by evertvd on 25/02/2017.
 */

public class FileAdapter extends ArrayAdapter<BeanFile> {

    private Context c;
    private int id;
    private List<BeanFile> items;

    public FileAdapter(Context context, int textViewResourceId,
                       List<BeanFile> objects) {
        super(context, textViewResourceId, objects);
        c = context;
        id = textViewResourceId;
        items = objects;
    }

    public BeanFile getItem(int i) {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            //LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }
        final BeanFile beanOpcionFile = items.get(position);
        if (beanOpcionFile != null) {
            TextView folder = (TextView) v.findViewById(R.id.txtFolder);
            TextView file = (TextView) v.findViewById(R.id.txtFile);

            if (folder != null) {
                folder.setText(beanOpcionFile.getName());

            }
            if (file != null) {
                file.setText(beanOpcionFile.getData());
            }
        }
        return v;
    }
}
