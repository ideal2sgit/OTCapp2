package com.example.faten.testsql.model;

import java.util.Date;

public class FicheClient {

    private String CodeClient;
    private Date dateOperation;
    private String Libelle;
    private String NumeroPiece;
    private double debit;
    private double credit;
    private double solde;
    private double soldeIncrement;
    private Date dateCreation;


    public FicheClient(String codeClient, Date dateOperation, String libelle, String numeroPiece, double debit, double credit, double solde,double soldeIncrement , Date dateCreation) {
        CodeClient = codeClient;
        this.dateOperation = dateOperation;
        Libelle = libelle;
        NumeroPiece = numeroPiece;
        this.debit = debit;
        this.credit = credit;
        this.solde = solde;
        this.soldeIncrement = soldeIncrement  ;
        this.dateCreation = dateCreation;
    }

    public String getCodeClient() {
        return CodeClient;
    }

    public void setCodeClient(String codeClient) {
        CodeClient = codeClient;
    }

    public Date getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(Date dateOperation) {
        this.dateOperation = dateOperation;
    }

    public String getLibelle() {
        return Libelle;
    }

    public void setLibelle(String libelle) {
        Libelle = libelle;
    }

    public String getNumeroPiece() {
        return NumeroPiece;
    }

    public void setNumeroPiece(String numeroPiece) {
        NumeroPiece = numeroPiece;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }


    public double getSoldeIncrement() {
        return soldeIncrement;
    }

    public void setSoldeIncrement(double soldeIncrement) {
        this.soldeIncrement = soldeIncrement;
    }

    @Override
    public String toString() {

        return  " FicheClient {" +
                " CodeClient='" + CodeClient + '\'' +
                ", dateOperation=" + dateOperation +
                ", Libelle='" + Libelle + '\'' +
                ", NumeroPiece='" + NumeroPiece + '\'' +
                ", debit=" + debit +
                ", credit=" + credit +
                ", solde=" + solde +
                ", dateCreation=" + dateCreation +
                '}';
    }
}
