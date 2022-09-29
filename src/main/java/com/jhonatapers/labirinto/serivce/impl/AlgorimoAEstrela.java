package com.jhonatapers.labirinto.serivce.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jhonatapers.labirinto.model.GeneVo;
import com.jhonatapers.labirinto.model.LabirintoVo;
import com.jhonatapers.labirinto.serivce.IAlgoritmo;
import com.jhonatapers.labirinto.util.grafo.Grafo;
import com.jhonatapers.labirinto.util.grafo.Nodo;

public class AlgorimoAEstrela implements IAlgoritmo {

    LabirintoVo _labirinto;
    List<Integer[]> objetivos;

    boolean debug;

    @Override
    public GeneVo inicia() {

        char[][] labirinto = _labirinto.toChar();

        Grafo grafo = new Grafo(labirinto);

        Object[] objetivos = grafo.objetivos.toArray();

        Set<Nodo> melhorCaminho = new HashSet<Nodo>();


        for (int i = 0; i < objetivos.length; i++) {

            if(i!=0) grafo.raiz = (Nodo) objetivos[i-1];

            grafo.objetivos.clear();
            grafo.objetivos.add((Nodo) objetivos[i]);
    
            grafo.AEstrela();
            melhorCaminho.addAll(grafo.solucoesComuns);
        }

        grafo.objetivos.clear();

        for (Object nodo : objetivos) {
            grafo.objetivos.add((Nodo) nodo);
        }

        grafo.solucoesComuns.addAll(melhorCaminho);

        grafo.printAEstrela();

        return null;

    }

    @Override
    public IAlgoritmo debug(boolean ativo) {
        debug = ativo;
        return this;
    }

    public AlgorimoAEstrela(LabirintoVo labirinto) {
        _labirinto = labirinto;
    }

    public void troca(Object[] objetivos, int count) {
        int i = count % objetivos.length;

        Object temp = objetivos[i];

        if (i == objetivos.length - 1) {
            objetivos[i] = objetivos[0];
            objetivos[0] = temp;
        } else {

            objetivos[i] = objetivos[i + 1];
            objetivos[i + 1] = temp;
        }

    }

}
