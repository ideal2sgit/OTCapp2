package com.example.faten.testsql.model;

public class Article {

    private String   CodeArticle  ;
    private   String  CodeABarre ;
    private String   Designation ;
    private double   PrixVenteTTC ;
    private double   PrixVenteHT ;
    private double   PrixAchatHT ;
    private    int   CodeTVA ;
    private    double   TauxTVA ;
    private    int   Quantite  ;
    private    int   QteRestante ;
    private    int   nbrCLick  ;

    public Article(String codeArticle,String  CodeABarre , String designation) {
        CodeArticle = codeArticle;
        this.CodeABarre  =CodeABarre  ;
        Designation = designation;
    }

    public Article(String codeArticle, String designation, double prixVenteTTC, double prixVenteHT, double prixAchatHT) {
        CodeArticle = codeArticle;
        Designation = designation;
        PrixVenteTTC = prixVenteTTC;
        PrixVenteHT = prixVenteHT;
        PrixAchatHT = prixAchatHT;
    }

    //  constructor for list article
    public Article(String codeArticle, String designation,  int   CodeTVA ,double   TauxTVA ,double prixVenteTTC, double prixVenteHT, int quantite, int qteRestante) {
        CodeArticle = codeArticle;
        Designation = designation;
        this.CodeTVA =CodeTVA  ;
        this.TauxTVA=TauxTVA ;
        PrixVenteTTC = prixVenteTTC;
        PrixVenteHT = prixVenteHT;
        Quantite = quantite;
        QteRestante = qteRestante;
    }


    //  constructor for list article
    public Article(String codeArticle, String designation, double prixVenteTTC, double prixVenteHT, int quantite, int qteRestante) {
        CodeArticle = codeArticle;
        Designation = designation;
        PrixVenteTTC = prixVenteTTC;
        PrixVenteHT = prixVenteHT;
        Quantite = quantite;
        QteRestante = qteRestante;
    }

    //  constructeur pour commande
    public Article(String codeArticle, String designation,double  PrixVenteTTC  ,  double PrixVenteHT , double prixAchatHT,int CodeTVA , int quantite, int qteRestante, int nbrCLick) {
        this.CodeArticle = codeArticle;
        this.Designation = designation;

        this.PrixVenteTTC = PrixVenteTTC;
        this.PrixVenteHT = PrixVenteHT;
        this.PrixAchatHT = prixAchatHT;
        this.CodeTVA     = CodeTVA  ;

        this.Quantite    = quantite;
        this.QteRestante = qteRestante;
        this.nbrCLick    = nbrCLick;

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


    public double getPrixVenteTTC() {
        return PrixVenteTTC;
    }

    public void setPrixVenteTTC(double prixVenteTTC) {
        PrixVenteTTC = prixVenteTTC;
    }

    public double getPrixVenteHT() {
        return PrixVenteHT;
    }

    public void setPrixVenteHT(double PrixVenteHT) {
        PrixVenteHT = PrixVenteHT;
    }


    public double getPrixAchatHT() {
        return PrixAchatHT;
    }

    public void setPrixAchatHT(double prixAchatHT) {
        PrixAchatHT = prixAchatHT;
    }

    public int getQuantite() {
        return Quantite;
    }

    public void setQuantite(int quantite) {
        Quantite = quantite;
    }

    public int getQteRestante() {
        return QteRestante;
    }

    public void setQteRestante(int qteRestante) {
        QteRestante = qteRestante;
    }

    public int getNbrCLick() {
        return nbrCLick;
    }

    public void setNbrCLick(int nbrCLick) {
        this.nbrCLick = nbrCLick;
    }


    public int getCodeTVA() {
        return CodeTVA;
    }

    public void setCodeTVA(int codeTVA) {
        CodeTVA = codeTVA;
    }


    public double getTauxTVA() {
        return TauxTVA;
    }

    public void setTauxTVA(double tauxTVA) {
        TauxTVA = tauxTVA;
    }


    public String getCodeABarre() {
        return CodeABarre;
    }

    public void setCodeABarre(String codeABarre) {
        CodeABarre = codeABarre;
    }

    @Override
    public String toString() {
        return "Article{" +
                "CodeArticle='" + CodeArticle + '\'' +
                ", Designation='" + Designation + '\'' +
                ", PrixVenteTTC=" + PrixVenteTTC +
                ", PrixVenteHT=" + PrixVenteHT +
                ", PrixAchatHT=" + PrixAchatHT +
                ", CodeTVA=" + CodeTVA +
                ", Quantite=" + Quantite +
                ", QteRestante=" + QteRestante +
                ", nbrCLick=" + nbrCLick +
                '}';
    }
    }
