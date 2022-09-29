package com.jhonatapers.labirinto.model;

import java.util.ArrayList;
import java.util.List;

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

    public char[][] toChar(){
        char[][] labirintoChar = new char[labirinto.length][labirinto.length];

        for (int i = 0; i < labirintoChar.length; i++) {
            for (int j = 0; j < labirintoChar.length; j++) {
                labirintoChar[i][j] = labirinto[i][j].charAt(0);
            }
        }

        return labirintoChar;

    }

}
