package com.evertvd.greendaoinventario.vista.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.modelo.Zona_has_Inventario;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.modelo.dao.ZonaDao;
import com.evertvd.greendaoinventario.modelo.dao.Zona_has_InventarioDao;
import com.evertvd.greendaoinventario.vista.activitys.ProductoInv;
import com.evertvd.greendaoinventario.vista.adapters.ZonasInvAdapter;
import com.evertvd.greendaoinventario.vista.dialogs.DialogCierreInventario;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrmZonasInventario extends Fragment {
    private View view;
    private TextView txtTitulo;

    public FrmZonasInventario() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_zonas_inventario, container, false);
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        txtTitulo = (TextView) view.findViewById(R.id.txtTitulo);
        List<Inventario> inventarioList = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).list();
        for (int i = 0; i < inventarioList.size(); i++) {
            //Log.e("Inventarios", String.valueOf(inventarioList.get(i).getId()));
            if (inventarioList.get(i).getNumequipo() < 10) {
                txtTitulo.setText(("zonas asignadas al equipo 0" + inventarioList.get(i).getNumequipo()).toUpperCase());
            } else {
                txtTitulo.setText(("zonas asignadas al equipo " + inventarioList.get(i).getNumequipo()).toUpperCase());
            }
        }

        Inventario inventario = Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(0)).unique();
        QueryBuilder<Zona> queryBuilder = Controller.getDaoSession().getZonaDao().queryBuilder();
        queryBuilder.join(Zona_has_Inventario.class, Zona_has_InventarioDao.Properties.Zona_id2).where(Zona_has_InventarioDao.Properties.Inventario_id2.eq(inventario.getId()));
        List<Zona> zonaList = queryBuilder.list();

        iniciarlizarAdapter(zonaList);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_inventario, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_inventario:
                FragmentManager fragmentManager = getFragmentManager();
                DialogCierreInventario dialogCierreInventario = new DialogCierreInventario();
                dialogCierreInventario.setCancelable(false);
                dialogCierreInventario.show(fragmentManager, "dialogo cerrar inventario");

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void iniciarlizarAdapter(final List<Zona> zonaList) {

        ZonasInvAdapter adapter = new ZonasInvAdapter(getActivity(), zonaList);
        // C. Seleccionamos la lista de nuestro layout

        ListView miLista = (ListView) view.findViewById(R.id.listaZonas);
        miLista.setVisibility(View.VISIBLE);
        miLista.setAdapter(adapter);


        miLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),zonaList.get(position).getZona()+" "+zonaList.get(position).getZona(), Toast.LENGTH_LONG).show();

                //asignar estado cero a la zona seleccionada actual
                Zona zonaSeleccionadaActual = Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Estado.eq(1)).unique();
                if (zonaSeleccionadaActual != null) {
                    zonaSeleccionadaActual.setEstado(0);
                    zonaSeleccionadaActual.update();
                }

                //asignar estadao 1:seleccionado a la zona seleccionada
                Zona nuevaZonaSeleccionada = Controller.getDaoSession().getZonaDao().queryBuilder().where(ZonaDao.Properties.Id.eq(zonaList.get(position).getId())).unique();
                nuevaZonaSeleccionada.setEstado(1);
                nuevaZonaSeleccionada.update();

                Intent intent = new Intent(getActivity(), ProductoInv.class);
                startActivity(intent);
            }
        });
    }

}
