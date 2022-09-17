package com.jhonatapers.labirinto.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.jhonatapers.labirinto.model.GeneVo;

public class GravaLog {

    private final static String OUTPUT_DIR = "\\resources\\";
    private String directoryName = "";
    private FileWriter arq;
    private PrintWriter gravarArq;

    public GravaLog(String nomeArquivo) throws IOException {
        Path path = FileSystems.getDefault().getPath("");
        directoryName = path.toAbsolutePath().toString() + OUTPUT_DIR + nomeArquivo + ".log";
        arq = new FileWriter(directoryName);
        gravarArq = new PrintWriter(arq);

    }

    public void gravaGeracao(int geracao, GeneVo[] populacao, GeneVo melhor) {
        grava("\n\nGERACAO Nº " + geracao);

        grava("\nMelhor: " + melhor.toString());

        String registro = "";

        registro += "Aptidao;ComidasComidas;CasasPercorridas;Mutacoes\n";
        for (GeneVo geneVo : populacao) {
            registro += geneVo.getAptidao() + ";" + geneVo.getComidasComidas() + ";" + geneVo.getCasasPercorridas()
                    + ";" + geneVo.getMutacoes()
                    + "\n";
        }

        grava(registro);

        grava("\n****************************************************");
    }

    public void gravaGene(int geracao, GeneVo gene) {
        grava("\n\nGERAÇÃO Nº " + geracao);

        // gesiel

        grava("\n****************************************************");
    }

    public void grava(String registro) {
        gravarArq.println(registro);
    }

    public void close() throws IOException {
        arq.close();
    }

}
