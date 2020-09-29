package com.example.faten.testsql.model;

public class SuivieMensuel {

    private String marque;
    private String codeFamille;
    private int qt_vendu;
    private double total_ht;


    public SuivieMensuel(String marque, String codeFamille, int qt_vendu, double total_ht) {
        this.marque = marque;
        this.codeFamille = codeFamille;
        this.qt_vendu = qt_vendu;
        this.total_ht = total_ht;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getCodeFamille() {
        return codeFamille;
    }

    public void setCodeFamille(String codeFamille) {
        this.codeFamille = codeFamille;
    }

    public int getQt_vendu() {
        return qt_vendu;
    }

    public void setQt_vendu(int qt_vendu) {
        this.qt_vendu = qt_vendu;
    }

    public double getTotal_ht() {
        return total_ht;
    }

    public void setTotal_ht(double total_ht) {
        this.total_ht = total_ht;
    }

    @Override
    public String toString() {
        return "SuivieMensuel{" +
                "marque='" + marque + '\'' +
                ", codeFamille='" + codeFamille + '\'' +
                ", qt_vendu=" + qt_vendu +
                ", total_ht=" + total_ht +
                '}';
    }
}
