package com.example.faten.testsql.model;

import java.util.Date;

public class SuivieRecouvrement {

    private String CodeClient;
    private String RaisonSocial;
    private Date DateReglement;
    private String CodeModeReglement;
    private String Refrence;
    private Date DateEcheance;
    private double montantRecu;


    public SuivieRecouvrement(String codeClient, String raisonSocial, Date dateReglement, String codeModeReglement, String refrence, Date dateEcheance, double montantRecu) {
        CodeClient = codeClient;
        RaisonSocial = raisonSocial;
        DateReglement = dateReglement;
        CodeModeReglement = codeModeReglement;
        Refrence = refrence;
        DateEcheance = dateEcheance;
        this.montantRecu = montantRecu;
    }


    public String getCodeClient() {
        return CodeClient;
    }

    public void setCodeClient(String codeClient) {
        CodeClient = codeClient;
    }

    public String getRaisonSocial() {
        return RaisonSocial;
    }

    public void setRaisonSocial(String raisonSocial) {
        RaisonSocial = raisonSocial;
    }

    public Date getDateReglement() {
        return DateReglement;
    }

    public void setDateReglement(Date dateReglement) {
        DateReglement = dateReglement;
    }

    public String getCodeModeReglement() {
        return CodeModeReglement;
    }

    public void setCodeModeReglement(String codeModeReglement) {
        CodeModeReglement = codeModeReglement;
    }

    public String getRefrence() {
        return Refrence;
    }

    public void setRefrence(String refrence) {
        Refrence = refrence;
    }

    public Date getDateEcheance() {
        return DateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        DateEcheance = dateEcheance;
    }

    public double getMontantRecu() {
        return montantRecu;
    }

    public void setMontantRecu(double montantRecu) {
        this.montantRecu = montantRecu;
    }


    @Override
    public String toString() {
        return "SuivieRecouvrement{" +
                "CodeClient='" + CodeClient + '\'' +
                ", RaisonSocial='" + RaisonSocial + '\'' +
                ", DateReglement=" + DateReglement +
                ", CodeModeReglement='" + CodeModeReglement + '\'' +
                ", Refrence='" + Refrence + '\'' +
                ", DateEcheance=" + DateEcheance +
                ", montantRecu=" + montantRecu +
                '}';
    }


}
