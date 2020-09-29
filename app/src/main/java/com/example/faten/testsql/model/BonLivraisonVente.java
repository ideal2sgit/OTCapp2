package com.example.faten.testsql.model;

import java.util.Date;

public class BonLivraisonVente {

    private String NumeroBonLivraisonVente;
    private Date DateBonLivraisonVente;
    private String NumeroBonCommandeVente;
    private double TotalHT;
    private double TotalRemise;
    private double TotalNetHT;
    private double TotalTVA;
    private double TotalTTC;
    private String NumeroEtat;
    private String Etat; //555
    private String NumeroFactureVente;
    private String CodeLivreur;
    private String Livreur; //555

    public BonLivraisonVente(String numeroBonLivraisonVente, Date dateBonLivraisonVente, String numeroBonCommandeVente, double totalHT, double totalRemise, double totalNetHT, double totalTVA, double totalTTC, String numeroEtat, String etat, String numeroFactureVente, String codeLivreur, String livreur) {
        NumeroBonLivraisonVente = numeroBonLivraisonVente;
        DateBonLivraisonVente = dateBonLivraisonVente;
        NumeroBonCommandeVente = numeroBonCommandeVente;
        TotalHT = totalHT;
        TotalRemise = totalRemise;
        TotalNetHT = totalNetHT;
        TotalTVA = totalTVA;
        TotalTTC = totalTTC;
        NumeroEtat = numeroEtat;
        Etat = etat;
        NumeroFactureVente = numeroFactureVente;
        CodeLivreur = codeLivreur;
        Livreur = livreur;
    }


    public String getNumeroBonLivraisonVente() {
        return NumeroBonLivraisonVente;
    }

    public void setNumeroBonLivraisonVente(String numeroBonLivraisonVente) {
        NumeroBonLivraisonVente = numeroBonLivraisonVente;
    }

    public Date getDateBonLivraisonVente() {
        return DateBonLivraisonVente;
    }

    public void setDateBonLivraisonVente(Date dateBonLivraisonVente) {
        DateBonLivraisonVente = dateBonLivraisonVente;
    }

    public String getNumeroBonCommandeVente() {
        return NumeroBonCommandeVente;
    }

    public void setNumeroBonCommandeVente(String numeroBonCommandeVente) {
        NumeroBonCommandeVente = numeroBonCommandeVente;
    }

    public double getTotalHT() {
        return TotalHT;
    }

    public void setTotalHT(double totalHT) {
        TotalHT = totalHT;
    }

    public double getTotalRemise() {
        return TotalRemise;
    }

    public void setTotalRemise(double totalRemise) {
        TotalRemise = totalRemise;
    }

    public double getTotalNetHT() {
        return TotalNetHT;
    }

    public void setTotalNetHT(double totalNetHT) {
        TotalNetHT = totalNetHT;
    }

    public double getTotalTVA() {
        return TotalTVA;
    }

    public void setTotalTVA(double totalTVA) {
        TotalTVA = totalTVA;
    }

    public double getTotalTTC() {
        return TotalTTC;
    }

    public void setTotalTTC(double totalTTC) {
        TotalTTC = totalTTC;
    }

    public String getNumeroEtat() {
        return NumeroEtat;
    }

    public void setNumeroEtat(String numeroEtat) {
        NumeroEtat = numeroEtat;
    }

    public String getEtat() {
        return Etat;
    }

    public void setEtat(String etat) {
        Etat = etat;
    }

    public String getNumeroFactureVente() {
        return NumeroFactureVente;
    }

    public void setNumeroFactureVente(String numeroFactureVente) {
        NumeroFactureVente = numeroFactureVente;
    }

    public String getCodeLivreur() {
        return CodeLivreur;
    }

    public void setCodeLivreur(String codeLivreur) {
        CodeLivreur = codeLivreur;
    }

    public String getLivreur() {
        return Livreur;
    }

    public void setLivreur(String livreur) {
        Livreur = livreur;
    }

    @Override
    public String toString() {
        return "BonLivraisonVente{" +
                "NumeroBonLivraisonVente='" + NumeroBonLivraisonVente + '\'' +
                ", DateBonLivraisonVente=" + DateBonLivraisonVente +
                ", NumeroBonCommandeVente='" + NumeroBonCommandeVente + '\'' +
                ", TotalHT=" + TotalHT +
                ", TotalRemise=" + TotalRemise +
                ", TotalNetHT=" + TotalNetHT +
                ", TotalTVA=" + TotalTVA +
                ", TotalTTC=" + TotalTTC +
                ", NumeroEtat='" + NumeroEtat + '\'' +
                ", Etat='" + Etat + '\'' +
                ", NumeroFactureVente='" + NumeroFactureVente + '\'' +
                ", CodeLivreur='" + CodeLivreur + '\'' +
                ", Livreur='" + Livreur + '\'' +
                '}';
    }
}
