package com.container.listaamiga.Classes;

public class Produto {

    private int idProduto;
    private String nomeProduto;
    private float precoItemProduto;
    private double quantItemProduto;
    private boolean checkItemProduto;

    public Produto() {

    }


    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public float getPrecoItemProduto() {
        return precoItemProduto;
    }

    public void setPrecoItemProduto(float precoItemProduto) {
        this.precoItemProduto = precoItemProduto;
    }

    public double getQuantItemProduto() {
        return quantItemProduto;
    }

    public void setQuantItemProduto(double quantItemProduto) {
        this.quantItemProduto = quantItemProduto;
    }

    public boolean isCheckItemProduto() {
        return checkItemProduto;
    }

    public void setCheckItemProduto(boolean checkItemProduto) {
        this.checkItemProduto = checkItemProduto;
    }
}
