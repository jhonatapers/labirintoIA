package com.jhonatapers.labirinto.model;

public class GeneVo {

    private int[] gene;

    private int aptidao;

    private int comidasComidas;

    private int casasPercorridas;

    public GeneVo(int[] gene) {
        this.gene = gene;
    }

    public int[] getGene() {
        return gene;
    }

    public int getAptidao() {
        return aptidao;
    }

    public void setAptidao(int aptidao) {
        this.aptidao = aptidao;
    }

    public int getComidasComidas() {
        return comidasComidas;
    }

    public void setComidasComidas(int comidasComidas) {
        this.comidasComidas = comidasComidas;
    }

    public int getCasasPercorridas() {
        return casasPercorridas;
    }

    public void setCasasPercorridas(int casasPercorridas) {
        this.casasPercorridas = casasPercorridas;
    }

    @Override
    public String toString() {
        return "A: " + aptidao + " Com.: " + comidasComidas + " Cas.: " + casasPercorridas + "; " ; 
    }    

}
