package com.example.faten.testsql.model;

public class ListReference {
    String Reference,CodeClient,Type;

    public ListReference(String reference, String codeClient, String type) {
        Reference = reference;
        CodeClient = codeClient;
        Type = type;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public String getCodeClient() {
        return CodeClient;
    }

    public void setCodeClient(String codeClient) {
        CodeClient = codeClient;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
