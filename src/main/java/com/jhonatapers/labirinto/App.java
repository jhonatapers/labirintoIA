package com.jhonatapers.labirinto;

import java.io.IOException;

import com.jhonatapers.labirinto.model.GeneVo;
import com.jhonatapers.labirinto.model.LabirintoVo;
import com.jhonatapers.labirinto.serivce.IAlgoritmo;
import com.jhonatapers.labirinto.serivce.ICruzador;
import com.jhonatapers.labirinto.serivce.IGeradorPopulacao;
import com.jhonatapers.labirinto.serivce.impl.AlgoritmoGenetico;
import com.jhonatapers.labirinto.serivce.impl.CruzadorUniponto;
import com.jhonatapers.labirinto.serivce.impl.GeradorPopulacaoRandom;
import com.jhonatapers.labirinto.util.CargaLabirinto;
import com.jhonatapers.labirinto.util.DateUtils;
import com.jhonatapers.labirinto.util.GravaLog;

public class App {
    public static void main(String[] args) {
        try {

            // carga labirinto
            CargaLabirinto aham = new CargaLabirinto(
                    "D:\\Workspace\\PUCRS\\Inteligencia Artificial\\T1\\labirintoIA\\resources\\labirinto1.txt");
            LabirintoVo labirinto = aham.getLabirinto();
            System.out.print(labirinto.toString());

            // populacao
            IGeradorPopulacao geradorPopulacao = new GeradorPopulacaoRandom();
            // GeneVo[] populacao = geradorPopulacao.gera(100,
            // (int)Math.pow(labirinto.getN(), 2));
            GeneVo[] populacao = geradorPopulacao.gera(100, labirinto.getN() * 10);
            System.out.print(populacao.toString());

            // Cruzamento
            ICruzador cruzador = new CruzadorUniponto();

            // gravaLog
            GravaLog log = new GravaLog(DateUtils.getDateTime());

            // AlgoritmoGentico
            IAlgoritmo algoritmo = new AlgoritmoGenetico(populacao, labirinto, 5000000, cruzador, 25, 10, log)
                    .debug(true);
            GeneVo melhor = algoritmo.inicia();
            System.out.print(melhor.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
