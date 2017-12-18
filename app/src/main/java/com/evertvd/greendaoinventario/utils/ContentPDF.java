package com.evertvd.greendaoinventario.utils;

import android.content.Context;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IConteo;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.interfaces.IProducto;
import com.evertvd.greendaoinventario.interfaces.IZona;
import com.evertvd.greendaoinventario.modelo.Conteo;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.modelo.Producto;
import com.evertvd.greendaoinventario.modelo.Zona;
import com.evertvd.greendaoinventario.sqlitedao.SqliteConteo;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteProducto;
import com.evertvd.greendaoinventario.sqlitedao.SqliteZona;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by evertvd on 14/12/2017.
 */

public class ContentPDF {
    private static Font fontTittle = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.ITALIC);
    private static Font helvetica9= new Font(Font.FontFamily.HELVETICA, 9);
    private static Font timesNR10 = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.NORMAL);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
            Font.BOLD);


    public  static void addMetaData(Document document){
        document.addSubject("App android para inventario");
        document.addKeywords("Java, PDF, iText, Android, Inventario");
        document.addAuthor("Evertvd");
        document.addCreator("Evertvd");
    }

    public static void addContentBody(Document document) throws DocumentException {
        IZona iZona=new SqliteZona();
        List<Zona> zonaList=iZona.listarZona();
        IConteo iConteo=new SqliteConteo();

        for (int z=0;z<zonaList.size();z++){
            Paragraph preface = new Paragraph();
            preface.add(new Paragraph("ZONA: "+zonaList.get(z).getNombre()+"    Total:"+Utils.formatearNumero(iConteo.obtenerTotalConteoZona(zonaList.get(z).getId())), smallBold));
            createTable(preface, zonaList.get(z).getId());
            document.add(preface);
        }
    }



    public static void createTable(Paragraph parrafo, long idZona)
            throws BadElementException {

        float[] columnWidths = {2, 7, 1,3,6};//dimensiones de las columnas
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);

        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);//color de la linea de tabla
        //table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.setSpacingBefore(5);//espacio entre la zona y la tabla
        for (int i = 0; i < 1; i++) {
            table.addCell(new Paragraph("Código",smallBold));
            table.addCell(new Paragraph("Descripción",smallBold));
            table.addCell(new Paragraph("U.M",smallBold));
            table.addCell(new Paragraph("Stock Fisico", smallBold));
            table.addCell(new Paragraph("Detalle", smallBold));
        }
        table.setHeaderRows(1);
        //table.setFooterRows(1);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        //color de la linea de la tabla
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        IProducto iProducto=new SqliteProducto();
        List<Producto> productoList=iProducto.listarProductoZonaResumen(idZona);
        //PdfPCell cellCaveat = new PdfPCell();
        for (int p=0;p<productoList.size();p++){
            table.getDefaultCell().setMinimumHeight(20);//alto minimo de la celda
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            //table.addCell(String.valueOf(productoList.get(p).getCodigo()),font);
            table.addCell(new Paragraph(" "+String.valueOf(productoList.get(p).getCodigo()),helvetica9));
            //table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(new Paragraph(productoList.get(p).getDescripcion(),helvetica9));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Paragraph("UND", helvetica9));
            IConteo iConteo=new SqliteConteo();
            //table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Paragraph(Utils.formatearNumero(iConteo.obtenerTotalConteo(productoList.get(p).getId())),helvetica9));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(new Paragraph(" "+addDetalleConteo(productoList.get(p).getId()),helvetica9));
        }


        parrafo.setSpacingBefore(10);
        parrafo.add(table);

    }


    public static void addFirmaResponsables(Document document)throws DocumentException{
        float[] columnWidths = {5, 1, 5,1,5};//dimensiones de las columnas
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

        table.addCell("______________________");
        table.addCell("");
        table.addCell("______________________");
        table.addCell("");
        table.addCell("______________________");

        table.addCell(new Paragraph("Responsable de Inventario", subTitleFont));
        table.addCell("");
        table.addCell(new Paragraph("Almacenero", subTitleFont));
        table.addCell("");
        table.addCell(new Paragraph("Administrador de Almacén", subTitleFont));
        table.addCell("");
        document.add(table);
    }

    public static void addResum(Document document)throws DocumentException{

        float[] columnWidths = {4, 2, 4,2,4,4};//dimensiones de las columnas
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        //table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        //table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setMinimumHeight(20);
        table.getDefaultCell().setBorder(Rectangle.TOP);
        table.getDefaultCell().setBorder(Rectangle.BOTTOM);

        IZona iZona=new SqliteZona();
        List<Zona>zonaList=iZona.listarZona();
        IProducto iProducto=new SqliteProducto();
        List<Producto>productoList=iProducto.listarProducto();
        IConteo iConteo=new SqliteConteo();
        int totalConteo=iConteo.totalConteo();

        table.addCell(new Paragraph("Total Zonas",smallBold));
        table.addCell(new Paragraph(String.valueOf(zonaList.size()),smallBold));
        table.addCell(new Paragraph("Total Productos", smallBold));
        table.addCell(new Paragraph(String.valueOf(productoList.size()),smallBold));
        table.addCell(new Paragraph("Total Inventario", smallBold));
        table.addCell(new Paragraph(Utils.formatearNumero(totalConteo)+"  Unidades", smallBold));
        document.add(table);
    }

    public static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    private static String addDetalleConteo(long idProducto){
        IConteo iConteo=new SqliteConteo();
        List<Conteo>conteoList=iConteo.listarConteo(idProducto);
        String detalleConteo1="", detalleConteo2="";
        if (!conteoList.isEmpty()){
            for (int c=0;c<conteoList.size();c++){
                if(c==(conteoList.size()-1)){
                    detalleConteo2+=Utils.formatearNumero(conteoList.get(c).getCantidad());
                }else{
                    detalleConteo1+=Utils.formatearNumero(conteoList.get(c).getCantidad())+"/";
                }
            }
        }
        return detalleConteo1+detalleConteo2;
    }



    class RoundRectangle implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle rect,
                               PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle(
                    rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                    rect.getHeight() - 3, 4);
            cb.stroke();
        }
    }

}
