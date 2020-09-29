
package com.example.faten.testsql.pdf;

import android.util.Log;


import com.example.faten.testsql.model.FicheClient;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class FicheClientPdf {


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

    private static double totalDebit = 0;
    private static double totalCredit = 0;
    private static double totalSolde = 0;


    public void executePDF(String FILE, String codeClient, String RaisonSocialClient, Date dateDebut, Date dateFin, ArrayList<FicheClient> listFicheClient) throws Exception {

        try {
            totalDebit  = 0;
            totalCredit = 0;
            totalSolde  = 0;


            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                    Font.BOLD);

            Log.e("pdf", "execute");
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            addDate(document, new Date());

            addTitlePage(document);
            addPeriode(document, dateDebut, dateFin);
            addClient(document, codeClient, RaisonSocialClient);

            addTable(document, normal12, listFicheClient);


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
        preface.add(new Paragraph("Challenge    le " + dt1.format(dateContrat),
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


    private static void addPeriode(Document document, Date dateDebut, Date dateFin)
            throws DocumentException {


        SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");

        Paragraph periode = new Paragraph();

        periode.add(new Phrase("Période : ", blod14));
        periode.add(new Phrase("Du " + dt1.format(dateDebut) + " au " + dt1.format(dateFin), normal12));

        periode.setAlignment(Element.ALIGN_LEFT);

        addEmptyLine(periode, 1);

        document.add(periode);

    }


    private static void addClient(Document document, String codeClient, String RaisonSocialClient)
            throws DocumentException {


        Paragraph periode = new Paragraph();

        periode.add(new Phrase("Client : ", blod14));
        periode.add(new Phrase(codeClient + " " + RaisonSocialClient, normal12));


        periode.setAlignment(Element.ALIGN_LEFT);

        addEmptyLine(periode, 1);

        document.add(periode);

    }


    public static void addTable(Document document, Font font, ArrayList<FicheClient> listFicheClient) throws DocumentException {

        //   PdfPTable table = new PdfPTable(6);
        PdfPTable table = new PdfPTable(new float[]{14, 32, 14, 14, 14, 14});


        Paragraph TableFiche = new Paragraph();

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

        double  soldeIncremented = 0 ;


        for (FicheClient fc : listFicheClient)

        {
            soldeIncremented = soldeIncremented+ fc.getSolde() ;

            // linge i
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            PdfPCell ci1 = new PdfPCell();
            ci1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
            ci1.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph paragraphci1 = new Paragraph();
            paragraphci1.setAlignment(Element.ALIGN_CENTER);
            paragraphci1.add(new Phrase(sdf.format(fc.getDateOperation()), font));
            ci1.addElement(paragraphci1);


            PdfPCell ci2 = new PdfPCell();
            ci2.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
            ci2.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph paragraphci2 = new Paragraph();
            paragraphci2.setAlignment(Element.ALIGN_CENTER);
            paragraphci2.add(new Phrase(fc.getLibelle(), font));
            ci2.addElement(paragraphci2);


            PdfPCell ci3 = new PdfPCell();
            ci3.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
            ci3.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph paragraphci3 = new Paragraph();
            paragraphci3.setAlignment(Element.ALIGN_CENTER);
            paragraphci3.add(new Phrase(fc.getNumeroPiece(), font));
            ci3.addElement(paragraphci3);


            DecimalFormat df = new DecimalFormat("0.000");

            PdfPCell ci4 = new PdfPCell();
            ci4.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
            ci4.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph paragraphci4 = new Paragraph();
            paragraphci4.setAlignment(Element.ALIGN_CENTER);
            paragraphci4.add(new Phrase(df.format(fc.getDebit()), font));
            ci4.addElement(paragraphci4);


            PdfPCell ci5 = new PdfPCell();
            ci5.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
            ci5.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph paragraphci5 = new Paragraph();
            paragraphci5.setAlignment(Element.ALIGN_CENTER);
            paragraphci5.add(new Phrase(df.format(fc.getCredit()), font));
            ci5.addElement(paragraphci5);


            PdfPCell ci6 = new PdfPCell();
            ci6.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
            ci6.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph paragraphci6 = new Paragraph();
            paragraphci6.setAlignment(Element.ALIGN_CENTER);
            paragraphci6.add(new Phrase(df.format(soldeIncremented), font));
            ci6.addElement(paragraphci6);

            table.addCell(ci1);
            table.addCell(ci2);

            table.addCell(ci3);
            table.addCell(ci4);

            table.addCell(ci5);
            table.addCell(ci6);

            totalDebit = totalDebit + fc.getDebit() ;
            totalCredit = totalCredit + fc.getCredit() ;
            totalSolde = totalSolde +fc.getSolde() ;



        }


        PdfPTable tableSomme = new PdfPTable(new float[]{60, 14, 14, 14});
        tableSomme.setWidthPercentage(100);
        tableSomme.getDefaultCell().setBorder(1);
        PdfPCell cSomme1 = new PdfPCell();
        cSomme1.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        cSomme1.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph paragraphSomme1 = new Paragraph();
        paragraphSomme1.setAlignment(Element.ALIGN_CENTER);
        paragraphSomme1.add(new Phrase("Solde Client", blod14));
        cSomme1.addElement(paragraphSomme1);



        DecimalFormat df = new DecimalFormat("0.000");


        PdfPCell cSommeDebit = new PdfPCell();
        cSommeDebit.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        cSommeDebit.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph paragraphSommeDebit = new Paragraph();
        paragraphSommeDebit.setAlignment(Element.ALIGN_CENTER);
        paragraphSommeDebit.add(new Phrase(df.format(totalDebit), blod14));
        cSommeDebit.addElement(paragraphSommeDebit);



        PdfPCell cSommeCredit = new PdfPCell();
        cSommeCredit.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        cSommeCredit.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph paragraphSommeCredit = new Paragraph();
        paragraphSommeCredit.setAlignment(Element.ALIGN_CENTER);
        paragraphSommeCredit.add(new Phrase(df.format(totalCredit), blod14));
        cSommeCredit.addElement(paragraphSommeCredit);



        PdfPCell cSommeSolde = new PdfPCell();
        cSommeSolde.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        cSommeSolde.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph paragraphSommeSolde = new Paragraph();
        paragraphSommeSolde.setAlignment(Element.ALIGN_CENTER);
        paragraphSommeSolde.add(new Phrase(df.format(totalSolde), blod14));
        cSommeSolde.addElement(paragraphSommeSolde);


        tableSomme.addCell(cSomme1);
        tableSomme.addCell(cSommeDebit);
        tableSomme.addCell(cSommeCredit);
        tableSomme.addCell(cSommeSolde);


        Paragraph TableSomme = new Paragraph();
        TableSomme.add(tableSomme);

        TableFiche.add(table);
        addEmptyLine(TableFiche, 1);
        document.add(TableFiche);
        document.add(TableSomme);

    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0 ; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
