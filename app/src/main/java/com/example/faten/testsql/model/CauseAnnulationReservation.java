package com.example.faten.testsql.model;

public class CauseAnnulationReservation {

    private  String  CodeCause ;
    private  String  Libelle ;


    public CauseAnnulationReservation(String codeCause, String libelle) {
        CodeCause = codeCause;
        Libelle = libelle;
    }

    public String getCodeCause() {
        return CodeCause;
    }

    public void setCodeCause(String codeCause) {
        CodeCause = codeCause;
    }

    public String getLibelle() {
        return Libelle;
    }


    public void setLibelle(String libelle) {
        Libelle = libelle;
    }


    @Override
    public String toString() {
        return "CauseAnnulationReservation{" +
                "CodeCause='" + CodeCause + '\'' +
                ", Libelle='" + Libelle + '\'' +
                '}';
    }
}
