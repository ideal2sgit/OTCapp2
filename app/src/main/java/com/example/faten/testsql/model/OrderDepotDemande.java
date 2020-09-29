package com.example.faten.testsql.model;

public class OrderDepotDemande {

    private   String   CodeDepotDemandeur ;
    private   String CodeDepotDemandant ;
    private  int  ordre ;


    public OrderDepotDemande(String codeDepotDemandeur, String codeDepotDemandant, int ordre) {

        CodeDepotDemandeur = codeDepotDemandeur;
        CodeDepotDemandant = codeDepotDemandant;
        this.ordre = ordre;

    }

    public String getCodeDepotDemandeur() {
        return CodeDepotDemandeur;
    }

    public void setCodeDepotDemandeur(String codeDepotDemandeur) {
        CodeDepotDemandeur = codeDepotDemandeur;
    }

    public String getCodeDepotDemandant() {
        return CodeDepotDemandant;
    }

    public void setCodeDepotDemandant(String codeDepotDemandant) {
        CodeDepotDemandant = codeDepotDemandant;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    @Override
    public String toString() {

        return "OrderDepotDemande{" +
                "CodeDepotDemandeur='" + CodeDepotDemandeur + '\'' +
                ", CodeDepotDemandant='" + CodeDepotDemandant + '\'' +
                ", ordre=" + ordre +
                '}';
    }
}
