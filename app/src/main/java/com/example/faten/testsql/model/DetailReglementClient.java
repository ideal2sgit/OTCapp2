package com.example.faten.testsql.model;

public class DetailReglementClient {
    String Reference,CodeBaque,Echeance,CodeModeReglement,Montant,NumeroOrdre;

    public DetailReglementClient(String reference, String codeBaque, String echeance, String codeModeReglement, String montant, String numeroOrdre) {
        Reference = reference;
        CodeBaque = codeBaque;
        Echeance = echeance;
        CodeModeReglement = codeModeReglement;
        Montant = montant;
        NumeroOrdre = numeroOrdre;
    }

    public String getNumeroOrdre() {
        return NumeroOrdre;
    }

    public void setNumeroOrdre(String numeroOrdre) {
        NumeroOrdre = numeroOrdre;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public String getCodeBaque() {
        return CodeBaque;
    }

    public void setCodeBaque(String codeBaque) {
        CodeBaque = codeBaque;
    }

    public String getEcheance() {
        return Echeance;
    }

    public void setEcheance(String echeance) {
        Echeance = echeance;
    }

    public String getCodeModeReglement() {
        return CodeModeReglement;
    }

    public void setCodeModeReglement(String codeModeReglement) {
        CodeModeReglement = codeModeReglement;
    }

    public String getMontant() {
        return Montant;
    }

    public void setMontant(String montant) {
        Montant = montant;
    }
}
