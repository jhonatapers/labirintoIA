package com.jhonatapers.labirinto.model;

public class LabirintoVo {

    private int n;
    private int comidas;
    private String[][] labirinto;

    public LabirintoVo(int n, int comidas, String[][] labirinto) {
        this.n = n;
        this.comidas = comidas;
        this.labirinto = labirinto;
    }

    public int getN() {
        return n;
    }

    public int getComidas() {
        return comidas;
    }

    public String[][] getLabirinto() {
        return labirinto;
    }

}
