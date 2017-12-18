package com.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;

public class Generator {
    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        /*version inicial del codigo
        Schema schema = new Schema(1, "com.apps.evertvd.inventariogreendao.database"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

        */
        crearSchema();

    }

    private static void crearSchema(){
        int schemaVersion = 4;   // incrementar en cada nueva actualización del esquema.
        String dataPackage = "com.evertvd.greendaoinventario.modelo";   // ruta donde almacenar las clases-entidades.


        Schema schema = new Schema(schemaVersion, dataPackage);
        schema.setDefaultJavaPackageDao(dataPackage + ".dao");
        schema.enableKeepSectionsByDefault();   // con esto no sobreescribe código personal añadido en las clases de entidades.

        crearTablas(schema);

        try {
            new DaoGenerator().generateAll(schema, PROJECT_DIR + "/app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    private static void addTables(final Schema schema) {
        addUserEntities(schema);
        // addPhonesEntities(schema);
    }

    // This is use to describe the colums of your table
    private static Entity addTables(final Schema schema) {
        Entity empresa = schema.addEntity("empresa");
        empresa.addIdProperty().primaryKey().autoincrement();
        empresa.addIntProperty("codempresa");
        empresa.addStringProperty("nombre");
        return empresa;
    }



    //    private static Entity addPhonesEntities(final Schema schema) {
    //        Entity phone = schema.addEntity("Phone");
    //        phone.addIdProperty().primaryKey().autoincrement();
    //        phone.addIntProperty("user_id").notNull();
    //        phone.addStringProperty("number");
    //        return phone;
    //    }

    */


    private static void crearTablas(Schema schema) {

        /* entidades */

        /* entidades */

        Entity empresa = schema.addEntity("Empresa");   // nombre de la tabla-entidad
        empresa.addIdProperty().primaryKey().autoincrement();   // columna id
        empresa.addIntProperty("codempresa").notNull().unique();
        empresa.addStringProperty("empresa").notNull().unique();
        empresa.addIntProperty("estado").notNull();   // puede ser NULL


        Entity inventario = schema.addEntity("Inventario");
        inventario.addIdProperty().primaryKey().autoincrement();
        inventario.addIntProperty("numinventario");
        inventario.addIntProperty("numequipo");
        inventario.addStringProperty("fechaCreacion");
        inventario.addStringProperty("fechaCierre");
        inventario.addIntProperty("estado");//0:abierto, 1:cerrado
        inventario.addIntProperty("contexto");//0 inventario, 1: diferencia, 2: resumen 0: terminado
        Property empresaId = inventario.addLongProperty("empresa_id").index().getProperty();// clave foránea


        Entity zona = schema.addEntity("Zona");
        zona.addIdProperty().primaryKey().autoincrement();
        zona.addStringProperty("nombre").unique();
        zona.addIntProperty("estado");//1:con diferencia, 0:sin diferencia
        Property inventarioId = zona.addLongProperty("inventario_id").index().getProperty();   // clave foránea
        //Property inventarioEmpresaId = inventario.addLongProperty("inventario_empresa_id").index().getProperty();



        Entity producto = schema.addEntity("Producto");
        producto.addIdProperty().primaryKey().autoincrement();
        producto.addIntProperty("codigo");
        producto.addStringProperty("descripcion");
        producto.addDoubleProperty("stock");
        producto.addStringProperty("tipo");//origen del dato:sistema, app
        producto.addIntProperty("estado");//-1:diferencia;abierto, 0:sin diferencia:cerrado
        //Property inventarioId = producto.addLongProperty("inventario_id").index().getProperty();   // clave foránea
        Property zonaId = producto.addLongProperty("zona_id").index().getProperty();   // clave foránea
        //Property zonaInventarioId = producto.addLongProperty("zona_inventario_id").index().getProperty();   // clave foránea
        //Property zonaInventarioEmpresaId = producto.addLongProperty("zona_inventario_empresa_id").index().getProperty();   // clave foránea


        Entity conteo = schema.addEntity("Conteo");
        conteo.addIdProperty().primaryKey().autoincrement();
        conteo.addIntProperty("cantidad");
        conteo.addStringProperty("observacion");
        conteo.addStringProperty("fecharegistro");
        conteo.addIntProperty("validado");//1:validado, 0:por validar
        conteo.addIntProperty("estado");//-1:Eliminado, 1:Modificado, 0:inicial
        Property productoId = conteo.addLongProperty("producto_id").index().getProperty();   // clave foránea
        //Property productoZonaId = conteo.addLongProperty("producto_zona_id").index().getProperty();   // clave foránea
        //Property productoZonaInventarioId = conteo.addLongProperty("producto_zona_inventario_id").index().getProperty();   // clave foránea
        //Property productoZonaInventarioEmpresaId = conteo.addLongProperty("producto_zona_inventario_empresa_id").index().getProperty();   // clave foránea

        Entity historial = schema.addEntity("Historial");
        historial.addIdProperty().primaryKey().autoincrement();
        historial.addIntProperty("cantidad");
        historial.addStringProperty("observacion");
        historial.addStringProperty("fecharegistro");
        historial.addStringProperty("fechamodificacion");
        historial.addIntProperty("tipo");//1:inicial, 2: modificacion, -1:eliminacion
        Property conteoId = historial.addLongProperty("conteo_id").index().getProperty();   // clave foránea
        //Property conteoProductoId = historial.addLongProperty("conteo_producto_id").index().getProperty();   // clave foránea
        //Property conteoProductoZonaId = historial.addLongProperty("conteo_producto_zona_id").index().getProperty();   // clave foránea
        //Property conteoProductoZonaInventarioId = historial.addLongProperty("conteo_producto_zona_inventario_id").index().getProperty();   // clave foránea
        //Property conteoProductoZonaInventarioEmpresaId = historial.addLongProperty("conteo_producto_zona_inventario_empresa_id").index().getProperty();   // clave foránea


        // Relaciones (tipo -> 1:N)
        // un viaje sólo puede pertenecer a un cliente, pero un cliente puede realizar varios viajes.
        //viaje.addToOne(cliente, clienteId);
        //cliente.addToMany(viaje, clienteId);

 /*
        inventarioZona.addToOne(inventario, inventarioId);
        inventarioZona.addToOne(zona, zonaId);
        inventario.addToMany(inventarioZona, inventarioId);
        zona.addToMany(inventarioZona, zonaId);
       */

        empresa.addToMany(inventario, empresaId);
        inventario.addToOne(empresa, empresaId);

        inventario.addToMany(zona, inventarioId);
        zona.addToOne(inventario, inventarioId);

        zona.addToMany(producto, zonaId);
        producto.addToOne(zona, zonaId);

        producto.addToMany(conteo, productoId);
        conteo.addToOne(producto, productoId);

        conteo.addToMany(historial, conteoId);
        historial.addToOne(conteo, conteoId);


    }
}
