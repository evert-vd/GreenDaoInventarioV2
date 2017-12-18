package com.evertvd.greendaoinventario.vista.adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.sqlitedao.SqliteConteo;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteZona;
import com.evertvd.greendaoinventario.threads.ThreadActualizarDiferencias;

import java.util.List;


public class NuevoProductoAdapter extends RecyclerSwipeAdapter<NuevoProductoAdapter.SimpleViewHolder> {

    FragmentManager fragmentManager;
    private Context context;

    private List<Producto> productoList;

    public NuevoProductoAdapter(Context context, List<Producto> productoList, FragmentManager manager) {
        this.context = context;
        this.productoList = productoList;
        this.fragmentManager = manager;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nuevo_producto, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        //para cerrar el slide desde el dialogo DCantidadNuevoProducto
        //obs: Genra error, no cierra todos los item
        //mItemManger=new SwipeItemRecyclerMangerImpl(this);

        //final BeanDetalleProducto item = listaDetalle.get(position);
        //IConteo iConteo = new Sqlite_Conteo();
        //obtencion del total de conto de productos nuevos por cada registro
        final long idProducto = productoList.get(position).getId();
        final IConteo iConteo=new SqliteConteo();
        int cantidad = iConteo.obtenerTotalConteo(idProducto);
        if(productoList.get(position).getTipo().equalsIgnoreCase("App")){
            viewHolder.lblCodigo2.setText(String.valueOf("NN"+productoList.get(position).getCodigo()));
        }else{
            viewHolder.lblCodigo2.setText(String.valueOf(productoList.get(position).getCodigo()));
        }
        viewHolder.lblDesripcion2.setText(productoList.get(position).getDescripcion());
        viewHolder.lblUnidades2.setText(String.valueOf(cantidad));
        viewHolder.lblZona2.setText(productoList.get(position).getZona().getNombre());
        viewHolder.lblFechaRegistro.setText("");
        //Log.e("Cantidad", String.valueOf(cantidad));

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Left
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.layoutEliminar));

        // Drag From Right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.layoutDerecha));


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


        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, " onClick : " + item.getCantidad() + " \n" + item.getHoraregistro(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final Producto productoTemp=productoList.get(position);

                builder.setTitle("Eliminar registro");
                builder.setMessage("¿Seguro que desea eliminar este registro");
                builder.setCancelable(false);
                builder.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Log.e("IDDETPROD", String.valueOf(listaProducto.get(position).getIdproducto()));

                                List<Conteo>conteoList=iConteo.listarConteo(productoTemp.getId());
                                //eliminacion de conteos realizados al producto a eliminar
                                if(!conteoList.isEmpty()){
                                    for (int i=0;i<conteoList.size();i++){
                                        iConteo.eliminarConteo(iConteo.obtenerConteo(conteoList.get(i).getId()));
                                    }
                                }
                                //eliminacion del producto
                                    IProducto iProducto=new SqliteProducto();
                                    iProducto.eliminarProducto(productoTemp);

                                //actualización del estado de la zona
                                IZona iZona=new SqliteZona();
                                iZona.calcularDiferenciaZona();
                                //Zona zona=iZona.obtenerZona(productoTemp.getZona_id());
                                //int total=iConteo.obtenerTotalConteoZona(zona.getId());
                                    mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                    productoList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, productoList.size());
                                    mItemManger.closeAllItems();
                                    Snackbar.make(v, "Registro eliminado", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                    //Toast.makeText(context, "Eliminado ", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                        });

                builder.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //listener.onNegativeButtonClick();

                                mItemManger.closeAllItems();

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


        viewHolder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mItemManger.closeAllItems();

                Snackbar.make(view, "Edición de registro denegado", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        });

        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position);
    }



    @Override
    public int getItemCount() {
        return productoList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_nuevo_producto;
    }

    //  ViewHolder Class

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView lblCodigo2, lblDesripcion2, lblZona2, lblUnidades2, lblFechaRegistro;
        ImageButton btnEditar;
        ImageButton btnEliminar;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe_nuevo_producto);
            lblCodigo2 = (TextView) itemView.findViewById(R.id.lblCodigo2);
            lblDesripcion2 = (TextView) itemView.findViewById(R.id.lblDesripcion2);
            lblUnidades2 = (TextView) itemView.findViewById(R.id.unidades2);
            lblZona2 = (TextView) itemView.findViewById(R.id.lblZona2);
            lblFechaRegistro = (TextView) itemView.findViewById(R.id.lblFechaRegistro);

            btnEditar = (ImageButton) itemView.findViewById(R.id.btnEditar);
            // btnAgregar = (ImageButton) itemView.findViewById(R.id.btnAgregar);
            btnEliminar = (ImageButton) itemView.findViewById(R.id.btnEliminar);
        }
    }

    public void addItem(Producto producto){
        productoList.add(producto);
        notifyItemInserted((productoList.size()+1));
    }


}