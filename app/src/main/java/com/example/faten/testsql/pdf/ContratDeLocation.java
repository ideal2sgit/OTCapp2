
package com.example.faten.testsql.pdf;

import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ContratDeLocation {


    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 24,
            Font.BOLD);

    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);

    private static Font blod14 = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);

    private static Font normal12 = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);

    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);

    public void executePDF(String FILE, String client, String marque, String modele, String matricule, String couleur, int nbr_jour, Date dateTransfert, Date dateRetour, double montant, Date dateContrat) throws Exception {

        try {


            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                    Font.BOLD);

            Log.e("pdf", "execute");
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            addDate(document, dateContrat);


            addTitlePage(document);
            addPeriode(document);
            addClient(document);

            addTable(document    ,normal12    )  ;




            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("exeption", "" + e.getMessage());
        }
    }

    private static void addDate(Document document, Date dateContrat) throws DocumentException {

        // format date
        SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyy");

        // Ecriture de date
        Paragraph preface = new Paragraph();
        preface.setAlignment(Element.ALIGN_RIGHT);
        addEmptyLine(preface, 3);
        preface.add(new Paragraph("Sfax le " + dt1.format(dateContrat),
                normal12));
        document.add(preface);

    }

    private static void addTitlePage(Document document)
            throws DocumentException {

        Paragraph periode = new Paragraph("Fiche Client", catFont);

        periode.setAlignment(Element.ALIGN_CENTER);

        addEmptyLine(periode, 1);

        document.add(periode);

    }


    private static void addPeriode(Document document)
            throws DocumentException {


        SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");


        Paragraph periode = new Paragraph();

        periode.add(new Phrase("Période : ", blod14));
        periode.add(new Phrase("Du " + dt1.format(new Date()) + " au " + dt1.format(new Date()), normal12));


        periode.setAlignment(Element.ALIGN_LEFT);

        addEmptyLine(periode, 1);

        document.add(periode);

    }


    private static void addClient(Document document)
            throws DocumentException {


        Paragraph periode = new Paragraph();

        periode.add(new Phrase("Client : ", blod14));
        periode.add(new Phrase("00001 " + "JASMIN OPTIC -TLILI SLIM ", normal12));


        periode.setAlignment(Element.ALIGN_LEFT);

        addEmptyLine(periode, 1);

        document.add(periode);

    }




    public static void addTable(Document document  , Font font  ) throws DocumentException {
        PdfPTable table = new PdfPTable(6);


        Paragraph TableFiche = new Paragraph();
        // Paragraph preface = new Paragraph();
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);


        PdfPCell c1 = new PdfPCell();

        c1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph paragraph1 = new Paragraph();
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph1.add(new Phrase("Date", font));
        c1.addElement(paragraph1);


        PdfPCell c2 = new PdfPCell();

        c2.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph paragraph2 = new Paragraph();
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        paragraph2.add(new Phrase("Opération", font));
        c2.addElement(paragraph2);


        PdfPCell c3 = new PdfPCell();

        c3.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.setAlignment(Element.ALIGN_CENTER);
        paragraph3.add(new Phrase("Référence", font));
        c3.addElement(paragraph3);

        PdfPCell c4 = new PdfPCell();

        c4.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.setAlignment(Element.ALIGN_CENTER);
        paragraph4.add(new Phrase("Débit", font));
        c4.addElement(paragraph4);


        PdfPCell c5 = new PdfPCell();

        c5.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph paragraph5 = new Paragraph();
        paragraph5.setAlignment(Element.ALIGN_CENTER);
        paragraph5.add(new Phrase("Crédit", font));
        c5.addElement(paragraph5);



        PdfPCell c6 = new PdfPCell();

        c6.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph paragraph6 = new Paragraph();
        paragraph6.setAlignment(Element.ALIGN_CENTER);
        paragraph6.add(new Phrase("Solde", font));
        c6.addElement(paragraph6);



        // linge 1
        table.addCell(c1);
        table.addCell(c2);
        table.addCell(c3);
        table.addCell(c4);
        table.addCell(c5);
        table.addCell(c6);



        PdfPCell c12 = new PdfPCell();

        c12.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        c12.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph paragraphc12 = new Paragraph();
        paragraphc12.setAlignment(Element.ALIGN_CENTER);
        paragraphc12.add(new Phrase("18/07/2019", font));
        c12.addElement(paragraphc12);



        // linge 2
        table.addCell(c12);
        table.addCell(c12);
        table.addCell(c12);
        table.addCell(c12);
        table.addCell(c12);
        table.addCell(c12);




        TableFiche.add(table);

        document.add(TableFiche);

    }






    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


}
