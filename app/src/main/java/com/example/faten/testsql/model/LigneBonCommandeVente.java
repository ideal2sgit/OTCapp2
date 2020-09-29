package com.example.faten.testsql.model;

public class LigneBonCommandeVente {

    private String  NumeroBonCommandeVente;
    private String CodeArticle;
    private String DesignationArticle  ;
    private int NumeroOrdre;
    private double PrixVenteHT ;
    private int    Quantite ;
    private double MontantHT;
    private double TauxTVA;
    private double MontantTVA;
    private double MontantTTC;
    private String Observation;
    private double TauxRemise;
    private double MontantRemise;
    private double MontantFodec ;
    private double NetHT;
    private double PrixAchatNet;


    public LigneBonCommandeVente() {
    }


    public LigneBonCommandeVente(String numeroBonCommandeVente, String codeArticle, int quantite, double montantTTC) {
        NumeroBonCommandeVente = numeroBonCommandeVente;
        CodeArticle = codeArticle;
        Quantite = quantite;
        MontantTTC = montantTTC;
    }

    public LigneBonCommandeVente(String numeroBonCommandeVente, String codeArticle, String designationArticle, int numeroOrdre, double prixVenteHT, int quantite, double montantHT, double tauxTVA, double montantTVA, double montantTTC, String observation, double tauxRemise, double montantRemise, double montantFodec, double netHT, double prixAchatNet) {
        NumeroBonCommandeVente = numeroBonCommandeVente;
        CodeArticle = codeArticle;
        DesignationArticle = designationArticle;
        NumeroOrdre = numeroOrdre;
        PrixVenteHT = prixVenteHT;
        Quantite = quantite;
        MontantHT = montantHT;
        TauxTVA = tauxTVA;
        MontantTVA = montantTVA;
        MontantTTC = montantTTC;
        Observation = observation;
        TauxRemise = tauxRemise;
        MontantRemise = montantRemise;
        MontantFodec = montantFodec;
        NetHT = netHT;
        PrixAchatNet = prixAchatNet;
    }


    public String getNumeroBonCommandeVente() {
        return NumeroBonCommandeVente;
    }

    public void setNumeroBonCommandeVente(String numeroBonCommandeVente) {
        NumeroBonCommandeVente = numeroBonCommandeVente;
    }

    public String getCodeArticle() {
        return CodeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        CodeArticle = codeArticle;
    }

    public String getDesignationArticle() {
        return DesignationArticle;
    }

    public void setDesignationArticle(String designationArticle) {
        DesignationArticle = designationArticle;
    }

    public int getNumeroOrdre() {
        return NumeroOrdre;
    }

    public void setNumeroOrdre(int numeroOrdre) {
        NumeroOrdre = numeroOrdre;
    }

    public double getPrixVenteHT() {
        return PrixVenteHT;
    }

    public void setPrixVenteHT(double prixVenteHT) {
        PrixVenteHT = prixVenteHT;
    }

    public int getQuantite() {
        return Quantite;
    }

    public void setQuantite(int quantite) {
        Quantite = quantite;
    }

    public double getMontantHT() {
        return MontantHT;
    }

    public void setMontantHT(double montantHT) {
        MontantHT = montantHT;
    }

    public double getTauxTVA() {
        return TauxTVA;
    }

    public void setTauxTVA(double tauxTVA) {
        TauxTVA = tauxTVA;
    }

    public double getMontantTVA() {
        return MontantTVA;
    }

    public void setMontantTVA(double montantTVA) {
        MontantTVA = montantTVA;
    }

    public double getMontantTTC() {
        return MontantTTC;
    }

    public void setMontantTTC(double montantTTC) {
        MontantTTC = montantTTC;
    }

    public String getObservation() {
        return Observation;
    }

    public void setObservation(String observation) {
        Observation = observation;
    }

    public double getTauxRemise() {
        return TauxRemise;
    }

    public void setTauxRemise(double tauxRemise) {
        TauxRemise = tauxRemise;
    }

    public double getMontantRemise() {
        return MontantRemise;
    }

    public void setMontantRemise(double montantRemise) {
        MontantRemise = montantRemise;
    }

    public double getMontantFodec() {
        return MontantFodec;
    }

    public void setMontantFodec(double montantFodec) {
        MontantFodec = montantFodec;
    }

    public double getNetHT() {
        return NetHT;
    }

    public void setNetHT(double netHT) {
        NetHT = netHT;
    }

    public double getPrixAchatNet() {
        return PrixAchatNet;
    }

    public void setPrixAchatNet(double prixAchatNet) {
        PrixAchatNet = prixAchatNet;
    }

    @Override
    public String toString() {
        return "LigneBonCommandeVente{" +
                "NumeroBonCommandeVente='" + NumeroBonCommandeVente + '\'' +
                ", CodeArticle='" + CodeArticle + '\'' +
                ", DesignationArticle='" + DesignationArticle + '\'' +
                ", NumeroOrdre='" + NumeroOrdre + '\'' +
                ", PrixVenteHT=" + PrixVenteHT +
                ", Quantite=" + Quantite +
                ", MontantHT=" + MontantHT +
                ", TauxTVA=" + TauxTVA +
                ", MontantTVA=" + MontantTVA +
                ", MontantTTC=" + MontantTTC +
                ", Observation='" + Observation + '\'' +
                ", TauxRemise=" + TauxRemise +
                ", MontantRemise=" + MontantRemise +
                ", MontantFodec=" + MontantFodec +
                ", NetHT=" + NetHT +
                ", PrixAchatNet=" + PrixAchatNet +
                '}';
    }
}
