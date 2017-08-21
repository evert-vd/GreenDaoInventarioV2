package com.evertvd.greendaoinventario.vista.adapters;



import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Historial;
import com.evertvd.greendaoinventario.modelo.dao.ConteoDao;
import com.evertvd.greendaoinventario.modelo.dao.HistorialDao;
import com.evertvd.greendaoinventario.vista.activitys.ConteoDif;
import com.evertvd.greendaoinventario.vista.activitys.ConteoInv;
import com.evertvd.greendaoinventario.vista.dialogs.DialogHistorialConteo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ConteoDifAdapter extends RecyclerSwipeAdapter<ConteoDifAdapter.SimpleViewHolder> {
    TextView abCantidad;
    EditText txtCantidad;
    EditText txtObservacion;
    TextInputLayout tilCantidad;
    TextInputLayout tilObservacion;


    private long idProducto;
    private Context mContext;
    FragmentManager fragmentManager;
    private int validado;
    //Activity activity;
    View view;
    private List<Conteo> conteoList;

    public ConteoDifAdapter(Context context, List<Conteo> conteoList, long idProducto) {
        this.mContext = context;
        this.conteoList = conteoList;
        this.idProducto = idProducto;
    }

    public ConteoDifAdapter(Context context, List<Conteo> conteoList, long idProducto, FragmentManager fragmentManager) {
        this.mContext = context;
        this.conteoList = conteoList;
        this.idProducto = idProducto;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_conteo_dif, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        //final BeanDetalleProducto item = listaDetalle.get(position);

        viewHolder.lblCantidad.setText(String.valueOf(conteoList.get(position).getCantidad()));
        viewHolder.lblFechaRegistro.setText(String.valueOf(conteoList.get(position).getFecharegistro()));

        //viewHolder.lblFechaRegistro.setText("00:00:00");

        if(conteoList.get(position).getEstado()!=1){
            viewHolder.lblEstado.setText("Por Validar");
        }else{
            viewHolder.lblEstado.setText("Validado");
        }

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        // Drag From Left
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.layoutEliminar));

        // Drag From Right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.layoutEditar));


        // Handling different events when swiping
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        /*viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((((SwipeLayout) v).getOpenStatus() == SwipeLayout.Status.Close)) {
                    //Start your activity

                    Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
                }

            }
        });*/

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Historial> historialList = Controller.getDaoSession().getHistorialDao().queryBuilder().where(HistorialDao.Properties.Conteo_id.eq(conteoList.get(position).getId())).list();
                for (int i = 0; i < historialList.size(); i++) {
                    Log.e("Historial", String.valueOf(historialList.get(i).getCantidad()) + " estado:" + String.valueOf(historialList.get(i).getTipo()));
                }


                //IHistorial iHistorial=new Sqlite_Historial();
                //List<BeanHistorial> historialList=iHistorial.obtenerHistorial(mContext, listaDetalle.get(position).getIdconteo());
                if(fragmentManager!=null){
                    DialogHistorialConteo historial = new DialogHistorialConteo();
                    Bundle data = new Bundle();
                    data.putLong("idconteo", conteoList.get(position).getId());
                    historial.setArguments(data);
                    //historial.setCancelable(false);
                    historial.show(fragmentManager, "dialogo historial");

                } else{
                    Snackbar.make(v, "Sin historial", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }




            }
        });


        viewHolder.btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
        /*
         Conteo conteo=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Id.eq(conteoList.get(position).getId())).unique();
                                                   conteo.setValidado(0);
                                                   //iConteo.validarConteo(mContext,listaConteo.get(position).getIdconteo(),0);
                                                   Controller.getDaoSession().getConteoDao().update(conteo);
                                                   viewHolder.lblEstado.setText("Por validar");
         */

                if (conteoList.get(position).getValidado()==1){
                    Snackbar.make(v,"Este registro ya se encuentra validado...", Snackbar.LENGTH_LONG)
                            .setActionTextColor(v.getResources().getColor(R.color.colorPrimary))
                            .setAction("Volver a Validar", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Conteo conteo=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Id.eq(conteoList.get(position).getId())).unique();
                                    conteo.setValidado(0);
                                    //iConteo.validarConteo(mContext,listaConteo.get(position).getIdconteo(),0);
                                    Controller.getDaoSession().getConteoDao().update(conteo);
                                    viewHolder.lblEstado.setText("Por Validar");
                                }
                            })
                            .show();
                    mItemManger.closeAllItems();
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Validar registro");
                    builder.setCancelable(false);
                    builder.setMessage("¿Seguro que desea validar este registro?");

                    builder.setPositiveButton("Validar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //validar conteo
                                    Conteo conteo=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Id.eq(conteoList.get(position).getId())).unique();
                                    conteo.setValidado(1);
                                    viewHolder.lblEstado.setText("Validado");
                                    Controller.getDaoSession().getConteoDao().update(conteo);
                                    mItemManger.closeAllItems();
                                    Snackbar.make(v,"Registro validado", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                                }
                            })
                            .setNegativeButton("Cancelar",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //listener.onNegativeButtonClick();
                                            mItemManger.closeAllItems();
                                            //dialog.dismiss();
                                        }
                                    });
                    builder.show();
                }
            }

        });

        viewHolder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                AlertDialog.Builder dialogModificar = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View v = inflater.inflate(R.layout.dialogo_modificar_conteo, null);

                dialogModificar.setView(v).setTitle("Modificar");
                dialogModificar.setCancelable(false);
                dialogModificar.setMessage("Ingresar la nueva cantidad");

                abCantidad = (TextView) v.findViewById(R.id.txtAbCantidad);
                txtCantidad = (EditText) v.findViewById(R.id.etCantidad);
                txtObservacion = (EditText) v.findViewById(R.id.etObservacion);
                tilCantidad = (TextInputLayout) v.findViewById(R.id.tilCantidad);
                tilObservacion = (TextInputLayout) v.findViewById(R.id.tilObservacion);

                //final IConteo iConteo=new Sqlite_Conteo();
                //1: captura conteo original
                final Conteo conteo = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Id.eq(conteoList.get(position).getId())).unique();
                //final BeanConteo conteoOriginal=iConteo.obtenerConteo(mContext, listaDetalle.get(position).getIdconteo());
                txtCantidad.setText(String.valueOf(conteo.getCantidad()));
                txtCantidad.setSelection(txtCantidad.getText().length());//poner cursor al final
                txtObservacion.setText(conteo.getObservacion());

                dialogModificar.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //2:guardar dato inicial en tabla historial

                        //Log.e("cantidad modificada", String.valueOf(conteoOriginal.getConteo()+" "+conteoOriginal.getObservacion()));
                        Historial historial = new Historial();
                        historial.setCantidad(conteo.getCantidad());
                        historial.setConteo_id(conteo.getId());
                        historial.setFechamodificacion(fechaActual());
                        historial.setFecharegistro(conteo.getFecharegistro());
                        historial.setObservacion(conteo.getObservacion());

                        //verificacion si es la primera modificarion, se agrega cero (inicial) al tipo de modif
                        //por default la lista historial contiene 1 registro (el registro actual)
                        List<Historial> historialList = Controller.getDaoSession().getHistorialDao().loadAll();
                        if (historialList.isEmpty()) {
                            historial.setTipo(1);//una modificacion
                        } else {
                            historial.setTipo(2);//mas de una modificacion
                        }

                        Controller.getDaoSession().getHistorialDao().insert(historial);

                        //3:guardar objeto nuevo en tabla conteo
                        if (validarCantidad(tilCantidad.getEditText().getText().toString())) {
                            Conteo conteoAEditar = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Id.eq(conteoList.get(position).getId())).unique();
                            conteoAEditar.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                            conteoAEditar.setObservacion(txtObservacion.getText().toString());
                            conteoAEditar.setFecharegistro(fechaActual());
                            conteoAEditar.setEstado(1);//modificacion
                            Controller.getDaoSession().update(conteoAEditar);

                            //4:actualizacion de la vista

                            viewHolder.lblCantidad.setText(txtCantidad.getText().toString());
                            viewHolder.lblFechaRegistro.setText(fechaActual());

                            //Actualizacion totalConteo ActionBar
                            ConteoDif conteos = new ConteoDif();
                            List<Conteo> listarNuevoTotal = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(idProducto)).where(ConteoDao.Properties.Estado.notEq(-1)).list();
                            int nuevoTotal = 0;
                            for (int i = 0; i < listarNuevoTotal.size(); i++) {
                                nuevoTotal += listarNuevoTotal.get(i).getCantidad();
                            }
                            conteos.actualizarConteoActionBar(nuevoTotal);

                            dialog.dismiss();
                            Snackbar.make(view, "Conteo modificado", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        } else {
                            Snackbar.make(view, "No se ingresó un conteo válido. No se modificó", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }

                        mItemManger.closeAllItems();

                    }

                });

                dialogModificar.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(mContext,"Cantidad modificada", Toast.LENGTH_SHORT).show();
                                mItemManger.closeAllItems();
                                dialog.dismiss();
                            }

                        });
                dialogModificar.show();


            }


        });


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return conteoList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeItem;
    }
    //  ViewHolder Class

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView lblCantidad;
        TextView lblFechaRegistro;
        TextView lblEstado;
        //TextView tvDelete;
        ImageButton btnEditar;
        //TextView tvShare;
        ImageButton btnValidar;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipeItem);
            lblCantidad = (TextView) itemView.findViewById(R.id.lblCantidad);
            lblFechaRegistro = (TextView) itemView.findViewById(R.id.lblFechaRegistro);
            lblEstado = (TextView) itemView.findViewById(R.id.lblEstado);

            //tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            btnEditar = (ImageButton) itemView.findViewById(R.id.btnEditar);
            //tvShare = (TextView) itemView.findViewById(R.id.tvShare);
            btnValidar = (ImageButton) itemView.findViewById(R.id.btnValidar);
        }
    }


    /*
    public BeanConteo eliminarConteo(int idConteo){
        String horaEliminacion = horaActual();
        String observacion="";
        int conteo=0;
        int estado=-1;//-1: Eliminado
        int validado=1;//1: validado, 0: por validar

        BeanConteo beanConteo=new BeanConteo();
        beanConteo.setIdconteo(idConteo);
        beanConteo.setIdProducto(idProducto);
        beanConteo.setHoraRegistro(horaEliminacion);
        beanConteo.setConteo(conteo);
        beanConteo.setObservacion(observacion);
        beanConteo.setValidado(validado);
        beanConteo.setEstado(estado);
        return beanConteo;
    }
    */


    private String fechaActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date hoy = new Date();
        return formato.format(hoy);
    }

    private boolean validarCantidad(String cantidadIngresada) {
        if (cantidadIngresada.trim().length() == 0) {
            tilCantidad.setError("Ingresar una cantidad válida");
            return false;
        } else {
            tilCantidad.setError(null);
        }

        return true;
    }

}

