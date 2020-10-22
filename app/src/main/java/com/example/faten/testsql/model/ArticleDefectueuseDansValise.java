package com.example.faten.testsql.model;

public class ArticleDefectueuseDansValise {

    private String  NumeroBonCommande ;
    private String CodeArticle;
    private String CodeDepot;
    private String Designation;
    private int Quantite;
    private String CodeCause;
    private String LibelleCause;
    private int Valider;
    private int Cloturer;
    private int Annuler;


    public ArticleDefectueuseDansValise(String  NumeroBonCommande ,String codeArticle, String codeDepot, String designation, int quantite, String codeCause, String libelleCause, int valider, int cloturer, int annuler) {
        this.NumeroBonCommande  = NumeroBonCommande  ;
        CodeArticle = codeArticle;
        CodeDepot = codeDepot;
        Designation = designation;
        Quantite = quantite;
        CodeCause = codeCause;
        LibelleCause = libelleCause;
        Valider = valider;
        Cloturer = cloturer;
        Annuler = annuler;
    }


    public String getNumeroBonCommande() {
        return NumeroBonCommande;
    }

    public void setNumeroBonCommande(String numeroBonCommande) {
        NumeroBonCommande = numeroBonCommande;
    }

    public String getCodeArticle() {
        return CodeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        CodeArticle = codeArticle;
    }

    public String getCodeDepot() {
        return CodeDepot;
    }

    public void setCodeDepot(String codeDepot) {
        CodeDepot = codeDepot;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public int getQuantite() {
        return Quantite;
    }

    public void setQuantite(int quantite) {
        Quantite = quantite;
    }


    public String getCodeCause() {
        return CodeCause;
    }

    public void setCodeCause(String codeCause) {
        CodeCause = codeCause;
    }

    public String getLibelleCause() {
        return LibelleCause;
    }

    public void setLibelleCause(String libelleCause) {
        LibelleCause = libelleCause;
    }

    public int getValider() {
        return Valider;
    }

    public void setValider(int valider) {
        Valider = valider;
    }

    public int getCloturer() {
        return Cloturer;
    }

    public void setCloturer(int cloturer) {
        Cloturer = cloturer;
    }

    public int getAnnuler() {
        return Annuler;
    }

    public void setAnnuler(int annuler) {
        Annuler = annuler;
    }



    @Override
    public String toString() {
        return "ArticleDefectueuseDansValise{" +
                "CodeArticle='" + CodeArticle + '\'' +
                ", CodeDepot='" + CodeDepot + '\'' +
                ", Designation='" + Designation + '\'' +
                ", Quantite=" + Quantite +
                ", CodeCause='" + CodeCause + '\'' +
                ", LibelleCause='" + LibelleCause + '\'' +
                '}';
    }

}
