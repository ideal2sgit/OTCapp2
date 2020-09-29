package com.example.faten.testsql.model;

public class Client {

    private String  CodeClient  ;
    private String  RaisonSociale ;
    private   int nbrClick  ;
    private String  Responsable ;
    private String  Adresse1 ;
    private String  Tel1 ;
    private String  MatriculeFiscale ;
    private String  Tel2  ;
    private String  Ville1 ;
    private String  Mail  ;

    private String  PayerTVA ;


    public Client(String codeClient, String raisonSociale) {
        CodeClient = codeClient;
        RaisonSociale = raisonSociale;
    }

    public Client(String codeClient, String raisonSociale, int nbrClick) {
        CodeClient = codeClient;
        RaisonSociale = raisonSociale;
        this.nbrClick = nbrClick;
    }



    public Client(String codeClient, String raisonSociale, String responsable, String payerTVA) {
        CodeClient = codeClient;
        RaisonSociale = raisonSociale;
        Responsable = responsable;
        PayerTVA = payerTVA;
    }

    public Client(String codeClient, String raisonSociale, String responsable, String adresse1, String tel1, String matriculeFiscale, String tel2, String ville1, String mail) {
        CodeClient = codeClient;
        RaisonSociale = raisonSociale;
        Responsable = responsable;
        Adresse1 = adresse1;
        Tel1 = tel1;
        MatriculeFiscale = matriculeFiscale;
        Tel2 = tel2;
        Ville1 = ville1;
        Mail = mail;
    }

    public String getCodeClient() {
        return CodeClient;
    }

    public void setCodeClient(String codeClient) {
        CodeClient = codeClient;
    }

    public String getRaisonSociale() {
        return RaisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        RaisonSociale = raisonSociale;
    }

    public int getNbrClick() {
        return nbrClick;
    }

    public void setNbrClick(int nbrClick) {
        this.nbrClick = nbrClick;
    }

    public String getResponsable() {
        return Responsable;
    }

    public void setResponsable(String responsable) {
        Responsable = responsable;
    }

    public String getAdresse1() {
        return Adresse1;
    }

    public void setAdresse1(String adresse1) {
        Adresse1 = adresse1;
    }

    public String getTel1() {
        return Tel1;
    }

    public void setTel1(String tel1) {
        Tel1 = tel1;
    }

    public String getMatriculeFiscale() {
        return MatriculeFiscale;
    }

    public void setMatriculeFiscale(String matriculeFiscale) {
        MatriculeFiscale = matriculeFiscale;
    }

    public String getTel2() {
        return Tel2;
    }

    public void setTel2(String tel2) {
        Tel2 = tel2;
    }

    public String getVille1() {
        return Ville1;
    }

    public void setVille1(String ville1) {
        Ville1 = ville1;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }


    public String getPayerTVA() {
        return PayerTVA;
    }

    public void setPayerTVA(String payerTVA) {
        PayerTVA = payerTVA;
    }

    @Override
    public String toString() {

        return "Client{" +
                "CodeClient='" + CodeClient + '\'' +
                ", RaisonSociale='" + RaisonSociale + '\'' +
                ", Responsable='" + Responsable + '\'' +
                ", Adresse1='" + Adresse1 + '\'' +
                ", Tel1='" + Tel1 + '\'' +
                ", MatriculeFiscale='" + MatriculeFiscale + '\'' +
                ", Tel2='" + Tel2 + '\'' +
                ", Ville1='" + Ville1 + '\'' +
                ", Mail='" + Mail + '\'' +
                '}';
    }
}
