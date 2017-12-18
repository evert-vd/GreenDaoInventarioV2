package com.evertvd.greendaoinventario.vista.adapters;

/**
 * Created by evertvd on 25/08/2017.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.evertvd.greendaoinventario.vista.fragments.FrmResumenProducto;
import com.evertvd.greendaoinventario.vista.fragments.FrmResumenZona;


public class ResumenTabsAdapter extends FragmentPagerAdapter{
    private static final String TAG = ResumenTabsAdapter.class.getSimpleName();
    private static final int FRAGMENT_COUNT = 2;
    public ResumenTabsAdapter(FragmentManager fm) {
        super(fm);

    }
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new FrmResumenZona();
            case 1:
                return new FrmResumenProducto();
        }
        return null;
    }
    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Res. por Zona";
            case 1:
                return "Dif. por Producto";
        }
        return null;
    }
}
