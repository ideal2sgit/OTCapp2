package com.example.faten.testsql.model;

public class ReservationArticleDansDepot {


    private String NumeroBonCommandeVente;
    private String CodeArticle;
    private String Designation;
    private String CodeDepotDemendeur;
    private String  DepotDemendeur;
    private String  TelPhoneDemendeur;

    private String CodeDepotDemandant;
    private String DepotDemandant;
    private String  TelPhoneDemandant;


    private int Quantite;
    private int Valider;
    private  int Annuler   ;
    private  int Cloturer  ;

    int QTStock  ;
    int QtCMD    ;


    public ReservationArticleDansDepot(String numeroBonCommandeVente, String codeArticle, String designation, String codeDepotDemendeur, String depotDemendeur, String telPhoneDemendeur, String codeDepotDemandant, String depotDemandant, String telPhoneDemandant, int quantite, int valider , int  Annuler  , int Cloturer) {
        NumeroBonCommandeVente = numeroBonCommandeVente;
        CodeArticle = codeArticle;
        Designation = designation;
        CodeDepotDemendeur = codeDepotDemendeur;
        DepotDemendeur = depotDemendeur;
        TelPhoneDemendeur = telPhoneDemendeur;
        CodeDepotDemandant = codeDepotDemandant;
        DepotDemandant = depotDemandant;
        TelPhoneDemandant = telPhoneDemandant;
        Quantite = quantite;
        Valider = valider;
        this.Annuler  = Annuler ;
        this.Cloturer = Cloturer ;
    }

    public ReservationArticleDansDepot(String numeroBonCommandeVente, String codeArticle, String designation, String codeDepotDemendeur, String DepotDemandeur ,  String codeDepotDemandant, String DepotDemandant, int quantite, int valider  ,int QTStock  , int QtCMD ) {

        NumeroBonCommandeVente = numeroBonCommandeVente;
        CodeArticle = codeArticle;
        Designation = designation;


        CodeDepotDemendeur = codeDepotDemendeur;
        this.DepotDemendeur = DepotDemandeur ;
        CodeDepotDemandant = codeDepotDemandant;
        this.DepotDemandant =  DepotDemandant ;

        Quantite = quantite;
        Valider = valider;

        this.QTStock=QTStock ;
        this.QtCMD=QtCMD ;

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

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getCodeDepotDemendeur() {
        return CodeDepotDemendeur;
    }

    public void setCodeDepotDemendeur(String codeDepotDemendeur) {
        CodeDepotDemendeur = codeDepotDemendeur;
    }

    public String getCodeDepotDemandant() {
        return CodeDepotDemandant;
    }

    public void setCodeDepotDemandant(String codeDepotDemandant) {
        CodeDepotDemandant = codeDepotDemandant;
    }

    public int getQuantite() {
        return Quantite;
    }

    public void setQuantite(int quantite) {
        Quantite = quantite;
    }

    public int getValider() {
        return Valider;
    }

    public void setValider(int valider) {
        Valider = valider;
    }


    public String getDepotDemendeur() {
        return DepotDemendeur;
    }

    public void setDepotDemendeur(String depotDemendeur) {
        DepotDemendeur = depotDemendeur;
    }

    public String getDepotDemandant() {
        return DepotDemandant;
    }

    public void setDepotDemandant(String depotDemandant) {
        DepotDemandant = depotDemandant;
    }


    public String getTelPhoneDemendeur() {
        return TelPhoneDemendeur;
    }

    public void setTelPhoneDemendeur(String telPhoneDemendeur) {
        TelPhoneDemendeur = telPhoneDemendeur;
    }

    public String getTelPhoneDemandant() {
        return TelPhoneDemandant;
    }

    public void setTelPhoneDemandant(String telPhoneDemandant) {
        TelPhoneDemandant = telPhoneDemandant;
    }

    public int getAnnuler() {
        return Annuler;
    }

    public void setAnnuler(int annuler) {
        Annuler = annuler;
    }

    public int getCloturer() {
        return Cloturer;
    }

    public void setCloturer(int cloturer) {
        Cloturer = cloturer;
    }


    public int getQTStock() {
        return QTStock;
    }

    public void setQTStock(int QTStock) {
        this.QTStock = QTStock;
    }

    public int getQtCMD() {
        return QtCMD;
    }

    public void setQtCMD(int qtCMD) {
        QtCMD = qtCMD;
    }

    @Override
    public String toString() {

        return "ReservationArticleDansDepot{" +

                ", CodeArticle='" + CodeArticle + '\'' +
                ", CodeDepotDemandant='" + CodeDepotDemandant + '\'' +
                ", Quantite=" + Quantite +

                '}';
    }
}
