package com.example.faten.testsql.model;

import java.util.Date;

public class ComptageAndroid {



   private String  CodeArticle  ;
    private String Designation ;
    private String CodeBarre  ;
    private int Quantite ;
    private Date DateCreation ;
    private String NomUtilisateur ;
    private String ValeurUniteVente;


    public ComptageAndroid(String codeArticle, String designation, String codeBarre, int quantite, Date dateCreation, String nomUtilisateur, String valeurUniteVente) {
        CodeArticle = codeArticle;
        Designation = designation;
        CodeBarre = codeBarre;
        Quantite = quantite;
        DateCreation = dateCreation;
        NomUtilisateur = nomUtilisateur;
        ValeurUniteVente = valeurUniteVente;
    }


    public String getCodeArticle() {
        return CodeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        CodeArticle = codeArticle;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getCodeBarre() {
        return CodeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        CodeBarre = codeBarre;
    }

    public int getQuantite() {
        return Quantite;
    }

    public void setQuantite(int quantite) {
        Quantite = quantite;
    }

    public Date getDateCreation() {
        return DateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        DateCreation = dateCreation;
    }

    public String getNomUtilisateur() {
        return NomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        NomUtilisateur = nomUtilisateur;
    }

    public String getValeurUniteVente() {
        return ValeurUniteVente;
    }

    public void setValeurUniteVente(String valeurUniteVente) {
        ValeurUniteVente = valeurUniteVente;
    }


    @Override
    public String toString() {
        return "ComptageAndroid{" +
                "CodeArticle='" + CodeArticle + '\'' +
                ", Designation='" + Designation + '\'' +
                ", CodeBarre='" + CodeBarre + '\'' +
                ", Quantite=" + Quantite +
                ", DateCreation=" + DateCreation +
                ", NomUtilisateur='" + NomUtilisateur + '\'' +
                ", ValeurUniteVente='" + ValeurUniteVente + '\'' +
                '}';
    }
}
