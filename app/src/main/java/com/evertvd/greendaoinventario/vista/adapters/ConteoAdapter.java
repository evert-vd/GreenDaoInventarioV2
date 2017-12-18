package com.evertvd.greendaoinventario.vista.adapters;


import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.sqlitedao.SqliteConteo;
import com.evertvd.greendaoinventario.sqlitedao.SqliteHistorial;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.IHistorial;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Historial;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.dao.HistorialDao;
import com.evertvd.greendaoinventario.utils.Utils;
import com.evertvd.greendaoinventario.vista.activitys.ActivityConteo;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.evertvd.greendaoinventario.vista.dialogs.DialogHistorialConteo;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class ConteoAdapter extends RecyclerSwipeAdapter<ConteoAdapter.SimpleViewHolder> {
    TextView abCantidad;
    EditText txtCantidad;
    EditText txtObservacion;
    TextInputLayout tilCantidad;
    TextInputLayout tilObservacion;
    Button btnAceptar, btnCancelar;

    private String historialInicial;
    //private long idProducto;
    private long idConteo;
    private Context mContext;
    private Inventario inventario;
    //FragmentManager fragmentManager;
    //ConteoInv listener;
    //Activity activity;
    View view;
    private List<Conteo> conteoList;

    public ConteoAdapter(Context context, List<Conteo> conteoList) {
        this.mContext = context;
        this.conteoList = conteoList;
        //this.idProducto = idProducto;
        IInventario iInventario=new SqliteInventario();
       inventario=iInventario.obtenerInventario();

    }



    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conteo, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        //final BeanDetalleProducto item = listaDetalle.get(position);
        idConteo=conteoList.get(position).getId();
        viewHolder.txtCantidad.setText(Utils.formatearNumero(conteoList.get(position).getCantidad()));
        viewHolder.txtFechaRegistro.setText(String.valueOf(conteoList.get(position).getFecharegistro()));
        Conteo conteo=conteoList.get(position);
        if(conteo.getValidado()==0){
            viewHolder.txtEstado.setText("Por validar");
        }else{
            viewHolder.txtEstado.setText("Validado");
        }
        //viewHolder.lblFechaRegistro.setText("00:00:00");

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        if(inventario.getContexto()==2){
            // Drag From Left
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, null);
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, null);

        }else{
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.layoutEliminar));
            // Drag From Right
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.layoutEditar));
        }

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
                //Toast.makeText(mContext,"onClose",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
        viewHolder.swipeLayout.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                closeAllItems();
                FragmentManager fragmentManager = ((ActivityConteo)mContext).getFragmentManager();
                DialogHistorialConteo historial = new DialogHistorialConteo();
                Bundle data = new Bundle();
                data.putLong("idconteo", conteoList.get(position).getId());
                historial.setArguments(data);
                historial.show(fragmentManager, "dialogo historial");
                return true;
            }
        });

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        viewHolder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Eliminar Registro");
                builder.setCancelable(false);
                builder.setMessage("¿Seguro que desea eliminar este registro?");
                builder.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Conteo conteoTemp=conteoList.get(position);
                                Historial historial=new Historial();
                                historial.setCantidad(conteoTemp.getCantidad());
                                historial.setConteo_id(conteoTemp.getId());
                                historial.setFecharegistro(conteoTemp.getFecharegistro());
                                historial.setFechamodificacion(Utils.fechaActual());
                                historial.setObservacion(conteoTemp.getObservacion());
                                historial.setTipo(-1);//-1:eliminacion
                                IHistorial iHistorial=new SqliteHistorial();
                                iHistorial.agregarHistorial(historial);

                                //actualizar estado conteo
                                conteoTemp.setEstado(-1);//eliminado
                                conteoTemp.setValidado(-1);//eliminado
                                conteoTemp.setCantidad(0);
                                IConteo iConteo=new SqliteConteo();
                                iConteo.actualizarConteo(conteoTemp);
                                //1:Obtencion del conteo a eliminar
                                //Conteo conteoAEliminar = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Id.eq(conteoList.get(position).getId())).unique();

                                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                conteoList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, conteoList.size());

                                ActivityConteo conteos = new ActivityConteo();
                                //List<Conteo> listarNuevoTotal = Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Producto_id.eq(idProducto)).where(ConteoDao.Properties.Estado.notEq(-1)).list();
                                conteos.actualizarConteoActionBar(iConteo.obtenerTotalConteo(conteoTemp.getProducto_id()));

                                mItemManger.closeAllItems();
                                //Toast.makeText(mContext, "Eliminado " + viewHolder.lblCantidad.getText().toString(), Toast.LENGTH_SHORT).show();
                                Snackbar.make(v, "Conteo Eliminado", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }
                        });
                builder.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //listener.onNegativeButtonClick();
                                mItemManger.closeAllItems();
                                //dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button cancel = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                if(cancel != null){
                    //b.setBackgroundColor(Color.CYAN);
                    cancel.setTextColor(v.getResources().getColor(R.color.colorGreyDarken_2));//color por código al boton cancelar del fialogo
            }

            }

        });

        viewHolder.btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                /*Conteo conteo=Controller.getDaoSession().getConteoDao().queryBuilder().where(ConteoDao.Properties.Id.eq(conteoList.get(position).getId())).unique();
                conteo.setValidado(0);
                //iConteo.validarConteo(mContext,listaConteo.get(position).getIdconteo(),0);
                Controller.getDaoSession().getConteoDao().update(conteo);*/
                final IConteo iConteo=new SqliteConteo();
                final Conteo conteo=iConteo.obtenerConteo(conteoList.get(position).getId());
                //viewHolder.txtEstado.setText("Por validar");
                if (conteo.getValidado()==1){
                    Snackbar.make(v,"Este registro ya se encuentra validado...", Snackbar.LENGTH_LONG)
                            .setActionTextColor(v.getResources().getColor(R.color.colorPrimary))
                            .setAction("Volver a Validar", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    conteo.setValidado(0);
                                    iConteo.actualizarConteo(conteo);
                                    viewHolder.txtEstado.setText("Por Validar");
                                }
                            })
                            .show();
                    mItemManger.closeAllItems();
                }else{
                    final AlertDialog.Builder dialogValidar = new AlertDialog.Builder(mContext);
                    dialogValidar.setTitle("Validar registro");
                    dialogValidar.setCancelable(false);
                    dialogValidar.setMessage("Seguro que desea validar este registro?");

                    dialogValidar.setPositiveButton("Validar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //validar conteo
                                    conteo.setValidado(1);
                                    iConteo.actualizarConteo(conteo);
                                    viewHolder.txtEstado.setText("Validado");
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

                    AlertDialog alertDialog = dialogValidar.create();
                    alertDialog.show();

                    Button cancel = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    if(cancel != null){
                        //b.setBackgroundColor(Color.CYAN);
                        cancel.setTextColor(v.getResources().getColor(R.color.colorGreyDarken_2));//color por cÃ³digo al boton cancelar del fialogo
                    }
                }
            }

        });


        viewHolder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Conteo conteTemp = conteoList.get(position);
                if (conteoList.get(position).getValidado() != 1) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = LayoutInflater.from(mContext);

                    View v = inflater.inflate(R.layout.dialogo_editar_conteo, null);
                    builder.setCancelable(false);
                    builder.setTitle("Modificar");
                    builder.setMessage("Ingresar la nueva cantidad");
                    builder.setView(v);

                    abCantidad = (TextView) v.findViewById(R.id.txtAbCantidad);
                    txtCantidad = (EditText) v.findViewById(R.id.txtCantidad);
                    txtObservacion = (EditText) v.findViewById(R.id.txtObservacion);
                    tilCantidad = (TextInputLayout) v.findViewById(R.id.tilCantidad);
                    tilObservacion = (TextInputLayout) v.findViewById(R.id.tilObservacion);
                    btnAceptar=(Button)v.findViewById(R.id.btnAceptar);
                    btnCancelar=(Button)v.findViewById(R.id.btnCancelar);

                    txtCantidad.setText(String.valueOf(conteTemp.getCantidad()));
                    txtCantidad.setSelection(txtCantidad.getText().length());//poner cursor al final
                    txtObservacion.setText(conteTemp.getObservacion());


                    final AlertDialog dialog = builder.create();

                    btnAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //dialog.cancel();
                            //update conteo
                            //Log.e("EDIT", String.valueOf(validarCantidadVacia(tilCantidad.getEditText().getText().toString())));
                            if (validarCantidadVacia(tilCantidad.getEditText().getText().toString(), conteTemp.getCantidad())|| validarCantidadDiferente(conteTemp.getCantidad(),tilCantidad.getEditText().getText().toString())){
                                Historial historial = new Historial();
                                historial.setCantidad(conteTemp.getCantidad());
                                historial.setConteo_id(conteTemp.getId());
                                historial.setFechamodificacion(Utils.fechaActual());
                                historial.setFecharegistro(conteTemp.getFecharegistro());
                                historial.setObservacion(conteTemp.getObservacion());

                                //verificacion si es la primera modificarion, se agrega cero (inicial) al tipo de modif
                                //por default la lista historial contiene 1 registro (el registro actual)
                                List<Historial> historialList = Controller.getDaoSession().getHistorialDao().queryBuilder().where(HistorialDao.Properties.Conteo_id.eq(conteoList.get(position).getId())).list();
                                if (historialList.isEmpty()) {
                                    historial.setTipo(1);//una modificacion
                                } else {
                                    historial.setTipo(2);//mas de una modificacion
                                }
                                IHistorial iHistorial = new SqliteHistorial();
                                iHistorial.agregarHistorial(historial);


                                conteTemp.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                                conteTemp.setObservacion(txtObservacion.getText().toString());
                                conteTemp.setFecharegistro(Utils.fechaActual());
                                conteTemp.setEstado(1);//modificacion
                                IConteo iConteo = new SqliteConteo();
                                iConteo.actualizarConteo(conteTemp);
                                //4:actualizacion de la vista
                                viewHolder.txtCantidad.setText(txtCantidad.getText().toString());
                                viewHolder.txtFechaRegistro.setText(Utils.fechaActual());

                                ActivityConteo conteos = new ActivityConteo();
                                conteos.actualizarConteoActionBar(iConteo.obtenerTotalConteo(conteoList.get(position).getProducto_id()));
                                //dialog.dismiss();

                                Snackbar.make(view, "Conteo modificado", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();



                            } else {

                            Snackbar.make(view, "No se modificó porque la cantidad no es válida o diferente", Snackbar.LENGTH_SHORT)
                                      .setAction("Action", null).show();
                            }
                            //ocultar teclado
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                            dialog.dismiss();
                            mItemManger.closeAllItems();
                        }
                    });

                    btnCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //ocultar teclado
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                            dialog.dismiss();
                            mItemManger.closeAllItems();
                        }
                    });
                    dialog.show();

                } else {
                    Snackbar.make(view, "Este registro ya se encuentra validado...", Snackbar.LENGTH_LONG)
                            .setActionTextColor(view.getResources().getColor(R.color.colorOrange))
                            .setAction("Volver a validar", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    conteTemp.setValidado(0);
                                    IConteo iConteo = new SqliteConteo();
                                    iConteo.actualizarConteo(conteTemp);
                                    viewHolder.txtEstado.setText("Por validar");
                                }
                            })
                            .show();
                    mItemManger.closeAllItems();
                }

            }
        });
        mItemManger.bindView(viewHolder.itemView, position);
    }

    private boolean validarCantidadVacia(String cantidadIngresada,int cantidadAnterior) {
        //obtener la cantidad anterior
        if (cantidadIngresada.trim().length()== 0 || Integer.parseInt(cantidadIngresada)==cantidadAnterior) {
            //Log.e("cantidad anterior", String.valueOf(Operaciones.buscarConteo(idConteo, idProducto)));
            tilCantidad.setError("Ingresar una cantidad válida");
            return false;
        } else {
            tilCantidad.setError(null);
        }
        return true;
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
        TextView txtCantidad;
        TextView txtFechaRegistro;
        TextView txtEstado;
        LinearLayout layoutEstado, layoutEditar, layoutEliminar;
        ImageButton btnEditar;
        //TextView tvShare;
        ImageButton btnEliminar;
        ImageButton btnValidar;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipeItem);
            layoutEstado=(LinearLayout)itemView.findViewById(R.id.layoutEstado);
            layoutEditar=(LinearLayout)itemView.findViewById(R.id.layoutEditar);
            layoutEliminar=(LinearLayout)itemView.findViewById(R.id.layoutEliminar);
            txtCantidad = (TextView) itemView.findViewById(R.id.txtCantidad);
            txtEstado = (TextView) itemView.findViewById(R.id.txtEstado);
            txtFechaRegistro = (TextView) itemView.findViewById(R.id.txtFechaRegistro);

            btnEditar = (ImageButton) itemView.findViewById(R.id.btnEditar);
            btnValidar = (ImageButton) itemView.findViewById(R.id.btnValidar);
            btnEliminar = (ImageButton) itemView.findViewById(R.id.btnEliminar);
            IInventario iInventario=new SqliteInventario();
            Inventario inventario=iInventario.obtenerInventario();

            if(inventario.getContexto()==0){
                btnEliminar.setVisibility(View.VISIBLE);
                btnValidar.setVisibility(View.GONE);
                layoutEstado.setVisibility(View.GONE);
            }else if(inventario.getContexto()==1){
                btnEliminar.setVisibility(View.GONE);
                btnValidar.setVisibility(View.VISIBLE);
                layoutEstado.setVisibility(View.VISIBLE);
            }else if(inventario.getContexto()==2){
                btnEliminar.setVisibility(View.GONE);
                btnValidar.setVisibility(View.GONE);
                btnEditar.setVisibility(View.GONE);
                layoutEditar.setVisibility(View.GONE);
                layoutEliminar.setVisibility(View.GONE);
                //layoutEstado.setVisibility(View.GONE);
            }
        }
    }


    private boolean validarCantidadDiferente(int cantidadAnterior, String cantidadIngresada) {
        //obtener la cantidad anterior
        if (cantidadAnterior==Integer.parseInt(cantidadIngresada)) {
            //Log.e("cantidad anterior:", String.valueOf(Operaciones.buscarConteo(idConteo, idProducto))+" nuevaCantidad:"+cantidadIngresada);
            tilCantidad.setError("Ingresar una cantidad diferente");
            return false;
        } else {
            //tdilCantidad.setError(null);
        }

        return true;
    }

    public void addItem(Conteo conteo){
        conteoList.add(conteo);
        notifyItemInserted((conteoList.size()+1));
    }

}

