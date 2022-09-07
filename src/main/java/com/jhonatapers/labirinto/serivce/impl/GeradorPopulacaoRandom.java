package com.jhonatapers.labirinto.serivce.impl;

import com.jhonatapers.labirinto.model.GeneVo;
import com.jhonatapers.labirinto.serivce.IGeradorPopulacao;
import com.jhonatapers.labirinto.util.NumerosAleatorios;

public class GeradorPopulacaoRandom implements IGeradorPopulacao {

    @Override
    public GeneVo[] gera(int tamanhoPopulacao, int tamanhoLabirinto) {

        GeneVo[] populacao = new GeneVo[tamanhoPopulacao];

        for (int i = 0; i < tamanhoPopulacao; i++)
            populacao[i] = criaGene(tamanhoLabirinto);

        return populacao;
    }

    private GeneVo criaGene(int tamanhoLabirinto) {

        int[] movimentos = new int[tamanhoLabirinto];

        for (int i = 0; i < tamanhoLabirinto; i++)
            movimentos[i] = NumerosAleatorios.random.nextInt(8);

        return new GeneVo(movimentos);
    }

}
