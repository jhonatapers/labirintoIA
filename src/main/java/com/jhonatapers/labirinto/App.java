package com.jhonatapers.labirinto;

import java.io.File;
import java.io.IOException;

import com.jhonatapers.labirinto.model.GeneVo;
import com.jhonatapers.labirinto.model.LabirintoVo;
import com.jhonatapers.labirinto.serivce.IAlgoritmo;
import com.jhonatapers.labirinto.serivce.ICruzador;
import com.jhonatapers.labirinto.serivce.IGeradorPopulacao;
import com.jhonatapers.labirinto.serivce.impl.AlgorimoAEstrela;
import com.jhonatapers.labirinto.serivce.impl.AlgoritmoGenetico;
import com.jhonatapers.labirinto.serivce.impl.CruzadorUniponto;
import com.jhonatapers.labirinto.serivce.impl.GeradorPopulacaoRandom;
import com.jhonatapers.labirinto.util.CargaLabirinto;
import com.jhonatapers.labirinto.util.DateUtils;
import com.jhonatapers.labirinto.util.GravaLog;

public class App {
    public static void main(String[] args) {
        try {

            String labirintoFile = new File(".").getCanonicalPath() + "\\resources\\labirinto1.txt";
            LabirintoVo labirinto = new CargaLabirinto(labirintoFile).getLabirinto();

            // População
            IGeradorPopulacao geradorPopulacao = new GeradorPopulacaoRandom();
            GeneVo[] populacao = geradorPopulacao.gera(25, (int)Math.pow(labirinto.getN(), 2));
            System.out.print(populacao.toString());

            // Cruzamento
            ICruzador cruzador = new CruzadorUniponto();

            // Grava log
            GravaLog log = new GravaLog(DateUtils.getDateTime());

            // AlgoritmoGentico
            IAlgoritmo algoritmo = new AlgoritmoGenetico(populacao, labirinto, 50000, cruzador, 25, 10, log).debug(true);
            GeneVo melhor = algoritmo.inicia();

            System.out.println("ACABOU GENETICO!");
            System.out.println(melhor.toString());

            System.out.println("********************");

            // Solução A*
            IAlgoritmo aEstrela = new AlgorimoAEstrela(labirinto);
            GeneVo solucaoAEstrela = aEstrela.inicia();

            System.out.println("ACABOU A*!");
            System.out.print(solucaoAEstrela.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
