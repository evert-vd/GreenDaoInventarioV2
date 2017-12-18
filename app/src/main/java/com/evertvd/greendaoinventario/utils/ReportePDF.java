package com.evertvd.greendaoinventario.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.evertvd.greendaoinventario.R;
import com.evertvd.greendaoinventario.interfaces.IInventario;
import com.evertvd.greendaoinventario.modelo.Inventario;
import com.evertvd.greendaoinventario.sqlitedao.SqliteInventario;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by evertvd on 14/12/2017.
 */

public class ReportePDF {
    static Context context;
    private static Font helvetica12 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static Font helvetica8= new Font(Font.FontFamily.HELVETICA, 8);

    public void crearPDF(Context context) {
        this.context=context;
        //Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            String fpath = MainDirectorios.obtenerDirectorioApp(context) + "/" + context.getResources().getString(R.string.file_pdf) + context.getResources().getString(R.string.pdf);
            File file = new File(fpath);
            if (!file.exists()) {
                file.createNewFile();
            }
            Document document = new Document(PageSize.A4, 36, 36, 135, 60);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            // add header and footer
            HeaderFooter event = new HeaderFooter();
            writer.setPageEvent(event);

            Paragraph preface = new Paragraph();

            // write to document
            document.open();
            ContentPDF.addMetaData(document);

            /*ReportePDF.addEmptyLine(preface, 1);
            document.add(preface);*/
            ContentPDF.addContentBody(document);

            ContentPDF.addEmptyLine(preface, 1);
            document.add(preface);
            ContentPDF.addResum(document);


            ContentPDF.addEmptyLine(preface, 6);
            document.add(preface);

            ContentPDF.addFirmaResponsables(document);

            document.close();
        } catch (IOException e) {

        } catch (DocumentException e) {

        }
    }

    /**
     * Inner class to add a header and a footer.
     */
    static class HeaderFooter extends PdfPageEventHelper {

        private PdfTemplate t;
        private Image total;

        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            addHeader(writer);
            addFooter(writer);
        }

        private void addHeader(PdfWriter writer) {
            PdfPTable header = new PdfPTable(2);
            try {
                // set defaults
                header.setWidths(new int[]{3, 24});
                header.setTotalWidth(527);
                header.setLockedWidth(true);
                header.getDefaultCell().setFixedHeight(20);
                header.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                header.getDefaultCell().setBorder(Rectangle.BOTTOM);
                header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);


                IInventario iInventario=new SqliteInventario();
                Inventario inventario=iInventario.obtenerInventario();

                //acceso a la imagen drawable
                BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.grupomolicom);
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
                byte[] imageInByte = stream.toByteArray();
                //agregar imagen la Image de ItextPdf
                Image image = Image.getInstance(imageInByte);
                header.addCell(image);

                // add text
                PdfPCell text = new PdfPCell();
                text.setPaddingBottom(15);
                text.setPaddingLeft(10);
                text.setBorder(Rectangle.BOTTOM);
                text.setBorderColor(BaseColor.LIGHT_GRAY);
                Chunk glue = new Chunk(new VerticalPositionMark());
                Phrase p = new Phrase();
                p.add(new Paragraph("REPORTE DE INVENTARIO FISICO DE LLANTAS-AC2",helvetica12));
                p.add(glue);
                p.add(new Paragraph(Utils.fechaActual(), helvetica8));
                text.addElement(p);
                text.addElement(new Phrase("Inventario:"+inventario.getNuminventario(), helvetica8));
                if(inventario.getNumequipo()<10){
                    text.addElement(new Phrase("Equipo:"+"0"+inventario.getNumequipo(), helvetica8));
                }else{
                    text.addElement(new Phrase("Equipo:"+inventario.getNumequipo(), helvetica8));
                }

                text.addElement(new Phrase("Fecha de Apertura:"+inventario.getFechaCreacion(), helvetica8));
                text.addElement(new Phrase("Fecha de Cierre:"+inventario.getFechaCierre(), helvetica8));
                header.addCell(text);

                // write content
                header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            } catch (MalformedURLException e) {
                throw new ExceptionConverter(e);
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }
            }


        private void addFooter(PdfWriter writer) {
            PdfPTable footer = new PdfPTable(3);
            try {
                // set defaults
                footer.setWidths(new int[]{24, 4, 1});
                footer.setTotalWidth(527);
                footer.setLockedWidth(true);
                footer.getDefaultCell().setFixedHeight(40);
                footer.getDefaultCell().setBorder(Rectangle.TOP);
                footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

                // add copyright
                footer.addCell(new Phrase("Grupo Molicom", helvetica12));

                // add current page count
                footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                footer.addCell(new Phrase(String.format("Página %d de", writer.getPageNumber()), helvetica8));

                // add placeholder for total page count
                PdfPCell totalPageCount = new PdfPCell(total);
                totalPageCount.setBorder(Rectangle.TOP);
                totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
                footer.addCell(totalPageCount);

                // write page
                PdfContentByte canvas = writer.getDirectContent();
                canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
                footer.writeSelectedRows(0, -1, 34, 50, canvas);
                canvas.endMarkedContentSequence();
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        public void onCloseDocument(PdfWriter writer, Document document) {
            int totalLength = String.valueOf(writer.getPageNumber()).length();
            int totalWidth = totalLength * 5;
            ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
                    new Phrase(String.valueOf(writer.getPageNumber()),helvetica8),
                    totalWidth, 6, 0);
        }
    }
}

