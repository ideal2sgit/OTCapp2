package com.example.faten.testsql.model;

import java.util.Date;

public class BonCommandeVente {

    private String NumeroBonCommandeVente;
    private Date   DateBonCommandeVente;
    private String NumeroDevisVente;
    private String CodeClient;
    private String ReferenceClient;
    private Date   DateReferenceClient;
    private  int   DelaiLivraison;
    private double TotalHT;
    private double TotalTVA;
    private double TotalTTC;
    private String NumeroEtat;
    private String NomUtilisateur ;
    private Date   DateCreation;
    private Date   HeureCreation;
    private String Observation ;
    private double TotalRemise;
    private double TotalFodec;
    private double TotalNetHT;
    private double TauxRemiseExceptionnel;
    private double MontantRemiseExceptionnel;
    private String CodeLivreur;


    public BonCommandeVente() {
    }


    public BonCommandeVente(String numeroBonCommandeVente, Date dateBonCommandeVente, String referenceClient, double totalTTC,String NumeroEtat) {
        NumeroBonCommandeVente = numeroBonCommandeVente;
        DateBonCommandeVente = dateBonCommandeVente;
        ReferenceClient = referenceClient;
        TotalTTC = totalTTC;
        this.NumeroEtat = NumeroEtat  ;
    }

    public BonCommandeVente(String numeroBonCommandeVente, Date dateBonCommandeVente, String numeroDevisVente, String codeClient, String referenceClient, Date dateReferenceClient, int delaiLivraison, double totalHT, double totalTVA, double totalTTC, String numeroEtat, String nomUtilisateur, Date dateCreation, Date heureCreation, String observation, double totalRemise, double totalFodec, double totalNetHT, double tauxRemiseExceptionnel, double montantRemiseExceptionnel, String codeLivreur) {

        NumeroBonCommandeVente = numeroBonCommandeVente;
        DateBonCommandeVente = dateBonCommandeVente;
        NumeroDevisVente = numeroDevisVente;
        CodeClient = codeClient;
        ReferenceClient = referenceClient;
        DateReferenceClient = dateReferenceClient;
        DelaiLivraison = delaiLivraison;
        TotalHT = totalHT;
        TotalTVA = totalTVA;
        TotalTTC = totalTTC;
        NumeroEtat = numeroEtat;
        NomUtilisateur = nomUtilisateur;
        DateCreation = dateCreation;
        HeureCreation = heureCreation;
        Observation = observation;
        TotalRemise = totalRemise;
        TotalFodec = totalFodec;
        TotalNetHT = totalNetHT;
        TauxRemiseExceptionnel = tauxRemiseExceptionnel;
        MontantRemiseExceptionnel = montantRemiseExceptionnel;
        CodeLivreur = codeLivreur;
    }

    public String getNumeroBonCommandeVente() {
        return NumeroBonCommandeVente;
    }

    public void setNumeroBonCommandeVente(String numeroBonCommandeVente) {
        NumeroBonCommandeVente = numeroBonCommandeVente;
    }

    public Date getDateBonCommandeVente() {
        return DateBonCommandeVente;
    }

    public void setDateBonCommandeVente(Date dateBonCommandeVente) {
        DateBonCommandeVente = dateBonCommandeVente;
    }

    public String getNumeroDevisVente() {
        return NumeroDevisVente;
    }

    public void setNumeroDevisVente(String numeroDevisVente) {
        NumeroDevisVente = numeroDevisVente;
    }

    public String getCodeClient() {
        return CodeClient;
    }

    public void setCodeClient(String codeClient) {
        CodeClient = codeClient;
    }

    public String getReferenceClient() {
        return ReferenceClient;
    }

    public void setReferenceClient(String referenceClient) {
        ReferenceClient = referenceClient;
    }

    public Date getDateReferenceClient() {
        return DateReferenceClient;
    }

    public void setDateReferenceClient(Date dateReferenceClient) {
        DateReferenceClient = dateReferenceClient;
    }

    public int getDelaiLivraison() {
        return DelaiLivraison;
    }

    public void setDelaiLivraison(int delaiLivraison) {
        DelaiLivraison = delaiLivraison;
    }

    public double getTotalHT() {
        return TotalHT;
    }

    public void setTotalHT(double totalHT) {
        TotalHT = totalHT;
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

    public String getNomUtilisateur() {
        return NomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        NomUtilisateur = nomUtilisateur;
    }

    public Date getDateCreation() {
        return DateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        DateCreation = dateCreation;
    }

    public Date getHeureCreation() {
        return HeureCreation;
    }

    public void setHeureCreation(Date heureCreation) {
        HeureCreation = heureCreation;
    }

    public String getObservation() {
        return Observation;
    }

    public void setObservation(String observation) {
        Observation = observation;
    }

    public double getTotalRemise() {
        return TotalRemise;
    }

    public void setTotalRemise(double totalRemise) {
        TotalRemise = totalRemise;
    }

    public double getTotalFodec() {
        return TotalFodec;
    }

    public void setTotalFodec(double totalFodec) {
        TotalFodec = totalFodec;
    }

    public double getTotalNetHT() {
        return TotalNetHT;
    }

    public void setTotalNetHT(double totalNetHT) {
        TotalNetHT = totalNetHT;
    }

    public double getTauxRemiseExceptionnel() {
        return TauxRemiseExceptionnel;
    }

    public void setTauxRemiseExceptionnel(double tauxRemiseExceptionnel) {
        TauxRemiseExceptionnel = tauxRemiseExceptionnel;
    }

    public double getMontantRemiseExceptionnel() {
        return MontantRemiseExceptionnel;
    }

    public void setMontantRemiseExceptionnel(double montantRemiseExceptionnel) {
        MontantRemiseExceptionnel = montantRemiseExceptionnel;
    }

    public String getCodeLivreur() {
        return CodeLivreur;
    }

    public void setCodeLivreur(String codeLivreur) {
        CodeLivreur = codeLivreur;
    }

    @Override
    public String toString() {
        return "BonCommandeVente{" +
                "NumeroBonCommandeVente='" + NumeroBonCommandeVente + '\'' +
                ", DateBonCommandeVente=" + DateBonCommandeVente +
                ", NumeroDevisVente='" + NumeroDevisVente + '\'' +
                ", CodeClient='" + CodeClient + '\'' +
                ", ReferenceClient='" + ReferenceClient + '\'' +
                ", DateReferenceClient=" + DateReferenceClient +
                ", DelaiLivraison=" + DelaiLivraison +
                ", TotalHT=" + TotalHT +
                ", TotalTVA=" + TotalTVA +
                ", TotalTTC=" + TotalTTC +
                ", NumeroEtat='" + NumeroEtat + '\'' +
                ", NomUtilisateur='" + NomUtilisateur + '\'' +
                ", DateCreation=" + DateCreation +
                ", HeureCreation=" + HeureCreation +
                ", Observation='" + Observation + '\'' +
                ", TotalRemise=" + TotalRemise +
                ", TotalFodec=" + TotalFodec +
                ", TotalNetHT=" + TotalNetHT +
                ", TauxRemiseExceptionnel=" + TauxRemiseExceptionnel +
                ", MontantRemiseExceptionnel=" + MontantRemiseExceptionnel +
                ", CodeLivreur='" + CodeLivreur + '\'' +
                '}';
    }
}
