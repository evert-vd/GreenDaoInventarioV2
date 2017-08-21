package com.evertvd.greendaoinventario.vista.adapters;


import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.controlador.Controller;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.dao.InventarioDao;
import com.evertvd.greendaoinventario.utils.Operaciones;

import java.util.List;


public class NuevoProductoAdapter extends RecyclerSwipeAdapter<NuevoProductoAdapter.SimpleViewHolder> {

    FragmentManager fragmentManager;
    private Context context;
    //Conteo listener;
    //Activity activity;
    private List<Producto> productoList;

    public NuevoProductoAdapter(Context context, List<Producto> productoList) {
        this.context = context;
        this.productoList = productoList;

    }

    public NuevoProductoAdapter(Context context, List<Producto> productoList, FragmentManager manager) {
        this.context = context;
        this.productoList = productoList;
        this.fragmentManager = manager;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_nuevo_producto, parent, false);
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
        long idProducto = productoList.get(position).getId();
        int cantidad = Operaciones.totalConteoProducto1(idProducto);

        viewHolder.lblCodigo2.setText(String.valueOf(productoList.get(position).getCodigo()));
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
                //Toast.makeText(mContext, " onClick : " + item.getCantidad() + " \n" + item.getHoraregistro(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Eliminar registro");
                builder.setMessage("¿Seguro que desea eliminar este registro");
                builder.setCancelable(false);
                builder.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Log.e("IDDETPROD", String.valueOf(listaProducto.get(position).getIdproducto()));
                                Inventario inventario= Controller.getDaoSession().getInventarioDao().queryBuilder().where(InventarioDao.Properties.Estado.eq(1)).unique();
                                Producto producto=Controller.getDaoSession().getProductoDao().queryBuilder().where(InventarioDao.Properties.Id.eq(productoList.get(position).getId())).unique();
                                Controller.getDaoSession().getProductoDao().delete(producto);

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
                builder.show();
            }


        });


        viewHolder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*


                try {
                    Bundle bundle = new Bundle();
                    bundle.putInt("idProducto", listaProducto.get(position).getId());
                    bundle.putString("codigo", listaProducto.get(position).getCodigo());
                    bundle.putString("descripcion", listaProducto.get(position).getDescripcion());
                    DCantidadNuevoProducto dialogFragment = new DCantidadNuevoProducto ();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.setCancelable(false);
                    dialogFragment.show(fragmentManager, "dialogo agregar cantidad");
                }catch (Exception e){

                }
                 */
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
        //return 1;
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


    private void eliminarConteoNuevoProducto(int idProducto) {
        //IConteo iConteo = new Sqlite_Conteo();
       // iConteo.eliminarConteoNuevoProducto(context, idProducto);
        Producto producto=new Producto();
        producto.delete();

    }

}