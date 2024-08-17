package unibo.javafxmvc.model;

import unibo.javafxmvc.controller.OrdinaPassiController;

public class OrdinaPassiModel {
    private String im1,im2,im3;
    private String soluzione;

    public OrdinaPassiModel(String im1, String im2, String im3, String soluzione){
        this.im1 = im1;
        this.im2 = im2;
        this.im3 = im3;
        this.soluzione = soluzione;
    }
    public String getIm1(){
        return im1;
    }
    public String getIm2(){
        return im2;
    }
    public String getIm3(){
        return im3;
    }
    public String getSoluzione(){
        return soluzione;
    }

    public String toString(){
        return "Im1: " + im1 + " Im2: " + im2 + " Im3: " + im3 + " Soluzione: " + soluzione;
    }

}
