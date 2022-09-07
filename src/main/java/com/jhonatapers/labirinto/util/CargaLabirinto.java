package com.jhonatapers.labirinto.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.jhonatapers.labirinto.model.LabirintoVo;

public class CargaLabirinto {

    private String _caminhoArquivo;

    public CargaLabirinto(String caminhoArquivo) {
        _caminhoArquivo = caminhoArquivo;
    }

    public LabirintoVo getLabirinto() throws FileNotFoundException, IOException {

        int n = 0;
        int comidas = 0;
        String[][] labirinto = new String[0][0];

        try (BufferedReader buffRead = new BufferedReader(new FileReader(_caminhoArquivo))) {
            n = Integer.parseInt(buffRead.readLine());
            comidas = n / 2;
            labirinto = new String[n][n];

            for (int i = 0; i < n; i++) {
                labirinto[i] = buffRead.readLine().split(" ");
            }

            buffRead.close();
        }

        return new LabirintoVo(n, comidas, labirinto);
    }

}
