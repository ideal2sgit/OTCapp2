package com.example.faten.testsql.model;

public class LigneBonLivraisonVente {

    private String NumeroBonLivraisonVente;
    private String CodeArticle;
    private String DesignationArticle;
    private int NumeroOrdre;
    private double PrixVenteHT;
    private int Quantite;
    private double MontantHT;
    private double TauxRemise;
    private double MontantRemise;
    private double NetHT;
    private double TauxTVA;
    private double MontantTVA;
    private double MontantTTC;
    private double PrixAchatNet;
    private String NumeroBonCommandeVente;


    public LigneBonLivraisonVente  (String numeroBonLivraisonVente, String codeArticle, String designationArticle, int numeroOrdre, double prixVenteHT, int quantite, double montantHT, double tauxRemise, double montantRemise, double netHT, double tauxTVA, double montantTVA, double montantTTC, double prixAchatNet, String numeroBonCommandeVente)  {
        NumeroBonLivraisonVente = numeroBonLivraisonVente;
        CodeArticle = codeArticle;
        DesignationArticle = designationArticle;
        NumeroOrdre = numeroOrdre;
        PrixVenteHT = prixVenteHT;
        Quantite = quantite;
        MontantHT = montantHT;
        TauxRemise = tauxRemise;
        MontantRemise = montantRemise;
        NetHT = netHT;
        TauxTVA = tauxTVA;
        MontantTVA = montantTVA;
        MontantTTC = montantTTC;
        PrixAchatNet = prixAchatNet;
        NumeroBonCommandeVente = numeroBonCommandeVente;
    }

    public String getNumeroBonLivraisonVente() {
        return NumeroBonLivraisonVente;
    }

    public void setNumeroBonLivraisonVente(String numeroBonLivraisonVente) {
        NumeroBonLivraisonVente = numeroBonLivraisonVente;
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

    public double getNetHT() {
        return NetHT;
    }

    public void setNetHT(double netHT) {
        NetHT = netHT;
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

    public double getPrixAchatNet() {
        return PrixAchatNet;
    }

    public void setPrixAchatNet(double prixAchatNet) {
        PrixAchatNet = prixAchatNet;
    }

    public String getNumeroBonCommandeVente() {
        return NumeroBonCommandeVente;
    }

    public void setNumeroBonCommandeVente(String numeroBonCommandeVente) {
        NumeroBonCommandeVente = numeroBonCommandeVente;
    }


    @Override
    public String toString() {

        return "LigneBonLivraisonVente { " +
                "NumeroBonLivraisonVente='" + NumeroBonLivraisonVente + '\'' +
                ", CodeArticle='" + CodeArticle + '\'' +
                ", DesignationArticle='" + DesignationArticle + '\'' +
                ", NumeroOrdre=" + NumeroOrdre +
                ", PrixVenteHT=" + PrixVenteHT +
                ", Quantite=" + Quantite +
                ", MontantHT=" + MontantHT +
                ", TauxRemise=" + TauxRemise +
                ", MontantRemise=" + MontantRemise +
                ", NetHT=" + NetHT +
                ", TauxTVA=" + TauxTVA +
                ", MontantTVA=" + MontantTVA +
                ", MontantTTC=" + MontantTTC +
                ", PrixAchatNet=" + PrixAchatNet +
                ", NumeroBonCommandeVente='" + NumeroBonCommandeVente + '\'' +
                '}';
    }
}
