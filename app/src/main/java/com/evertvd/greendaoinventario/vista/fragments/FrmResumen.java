package com.evertvd.greendaoinventario.vista.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.utils.Reporte;
import com.evertvd.greendaoinventario.vista.adapters.TabsAdapterFragments;
import com.evertvd.greendaoinventario.vista.dialogs.DialogEnviarEmail;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrmResumen extends Fragment implements View.OnClickListener {
    private AppBarLayout appBar;
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppBarLayout appBarLayout;
    View view;

    public FrmResumen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frm_contenedor_resumen, container, false);
        agregarFab();

        context=getActivity();

        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(new TabsAdapterFragments(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabEmail) {
            generarArchivosCorreo();
        }
    }


    private void agregarFab() {
        FloatingActionButton fabEmail = (FloatingActionButton)view.findViewById(R.id.fabEmail);
        fabEmail.setOnClickListener(this);

    }


    private void generarArchivosCorreo(){

        Reporte repote=new Reporte();
        repote.Resumido(getActivity(),getResources().getString(R.string.carpetaReporte));
        repote.Detallado(getActivity(),getResources().getString(R.string.carpetaReporte));

        DialogEnviarEmail enviarEmail = new DialogEnviarEmail();
        enviarEmail.setCancelable(false);
        enviarEmail.show(getFragmentManager(), "tag");

    }


}