package com.example.faten.testsql.model;

public class ArticleStock {


    private String   CodeDepot  ;
    private String   Depot ;
    private String   TelPhone ;

    private String   CodeArticle  ;
    private String   Designation ;
    private double   PrixVenteTTC ;
    private double   PrixVenteHT ;
    private double   PrixAchatHT ;
    private    int   CodeTVA ;
    private    double   TauxTVA ;
    private    int   Quantite  ;
    private    int   QteCMD ;
    private    int   nbrCLick  ;
    private    int   QtePanier  ;

    public ArticleStock() {
    }

    // panier
    public ArticleStock(String codeArticle, String designation, double prixVenteTTC, double prixVenteHT, double prixAchatHT, int codeTVA, double tauxTVA, int qtePanier) {
        CodeArticle = codeArticle;
        Designation = designation;
        PrixVenteTTC = prixVenteTTC;
        PrixVenteHT = prixVenteHT;
        PrixAchatHT = prixAchatHT;
        CodeTVA = codeTVA;
        TauxTVA = tauxTVA;
        QtePanier = qtePanier;
    }

    public ArticleStock (String codeDepot, String depot , String   TelPhone , String codeArticle, String designation, double prixVenteTTC, double prixVenteHT, double prixAchatHT, int codeTVA, double tauxTVA, int quantite, int QteCMD, int nbrCLick) {
        CodeDepot = codeDepot;
        Depot = depot;
        this.TelPhone = TelPhone ;
        CodeArticle = codeArticle;
        Designation = designation;
        PrixVenteTTC = prixVenteTTC;
        PrixVenteHT = prixVenteHT;
        PrixAchatHT = prixAchatHT;
        CodeTVA = codeTVA;
        TauxTVA = tauxTVA;
        Quantite = quantite;
        this.QteCMD = QteCMD;
        this.nbrCLick = nbrCLick;
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


    public int getQteCMD() {
        return QteCMD;
    }

    public void setQteCMD(int qteCMD) {
        QteCMD = qteCMD;
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


    public String getCodeDepot() {
        return CodeDepot;
    }

    public void setCodeDepot(String codeDepot) {
        CodeDepot = codeDepot;
    }

    public String getDepot() {
        return Depot;
    }

    public void setDepot(String depot) {
        Depot = depot;
    }


    public String getTelPhone() {
        return TelPhone;
    }

    public void setTelPhone(String telPhone) {
        TelPhone = telPhone;
    }


    public int getQtePanier() {
        return QtePanier;
    }

    public void setQtePanier(int qtePanier) {
        QtePanier = qtePanier;
    }


    @Override
    public String toString() {
        return "ArticleStock{" +
                "CodeDepot='" + CodeDepot + '\'' +
                ", Quantite=" + Quantite +
                '}';
    }

   /* @Override
    public String toString() {

        return "ArticleStock{" +
                "  CodeDepot='" + CodeDepot + '\'' +
                ", Depot='" + Depot + '\'' +
                ", TelPhone='" + TelPhone + '\'' +
                ", CodeArticle='" + CodeArticle + '\'' +
                ", Designation='" + Designation + '\'' +
                ", PrixVenteTTC=" + PrixVenteTTC +
                ", PrixVenteHT=" + PrixVenteHT +
                ", PrixAchatHT=" + PrixAchatHT +
                ", CodeTVA=" + CodeTVA +
                ", TauxTVA=" + TauxTVA +
                ", Quantite=" + Quantite +
                ", QteCMD=" + QteCMD +
                ", nbrCLick=" + nbrCLick +
                ", QtePanier=" + QtePanier +
                '}';
    }*/
}
