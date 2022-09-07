package com.jhonatapers.labirinto.serivce.impl;

import com.jhonatapers.labirinto.model.GeneVo;
import com.jhonatapers.labirinto.serivce.ICruzador;

public class CruzadorUniponto implements ICruzador {

    @Override
    public GeneVo cruza(GeneVo pai, GeneVo mae) {

        int[] genePai = pai.getGene();

        int[] geneMae = mae.getGene();

        int[] geneFilho = new int[genePai.length];

        for (int i = 0; i < genePai.length / 2; i++)
            geneFilho[i] = genePai[i];

        for (int i = geneMae.length / 2; i < geneMae.length; i++)
            geneFilho[i] = geneMae[i];

        return new GeneVo(geneFilho);
    }

}
