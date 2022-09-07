package com.jhonatapers.labirinto.serivce.impl;

import com.jhonatapers.labirinto.model.GeneVo;
import com.jhonatapers.labirinto.model.LabirintoVo;
import com.jhonatapers.labirinto.serivce.IAlgoritmo;
import com.jhonatapers.labirinto.serivce.ICruzador;
import com.jhonatapers.labirinto.util.NumerosAleatorios;

public class AlgoritmoGenetico implements IAlgoritmo {

    GeneVo[] _populacao;
    LabirintoVo _labirinto;
    int _tentativasMaximas;
    ICruzador _cruzador;
    int _taxaMutacao;
    boolean debug = false;

    public AlgoritmoGenetico(GeneVo[] populacao, LabirintoVo labirinto, int tentativasMaximas, ICruzador cruzador,
            int taxaMutacao) {
        _populacao = populacao;
        _labirinto = labirinto;
        _tentativasMaximas = tentativasMaximas;
        _cruzador = cruzador;
        _taxaMutacao = taxaMutacao;
    }
    
    @Override
    public IAlgoritmo debug(boolean ativo) {
        debug = ativo;
        return this;
    }

    @Override
    public GeneVo inicia() {

        GeneVo escolhido = selecaoMelhor(_populacao);
        int pontuacaoEuristica = euristica(escolhido);

        if (pontuacaoEuristica == 0)
            return escolhido;

        for (int i = 0; i < _tentativasMaximas; i++) {

            _populacao = proximaGeracao(escolhido);
            escolhido = selecaoMelhor(_populacao);
            pontuacaoEuristica = euristica(escolhido);

            //printaInformacoes(String.format("GERAÇÃO %s", i + 1), escolhido, score);
            if (pontuacaoEuristica == 0)
                break;

            mutacao();
            escolhido = selecaoMelhor(_populacao);
            pontuacaoEuristica = euristica(escolhido);

            //printaInformacoes(String.format("GERAÇÃO %s , COM MUTAÇÃO", i + 1), escolhido, score);
            if (pontuacaoEuristica == 0)
                break;

        }

        //printaFinal(score);
        return escolhido;
    }

    private GeneVo selecaoMelhor(GeneVo[] populacao) {

        int melhorResultado = euristica(populacao[0]);
        int melhorGene = 0;

        for (int i = 0; i < populacao.length; i++) {
            int scoreAtual = euristica(populacao[i]);

            if (scoreAtual < melhorResultado) {
                melhorGene = i;
                melhorResultado = scoreAtual;
            }
        }

        return populacao[melhorGene];
    }

    private int euristica(GeneVo gene) {     
           
        return 100;
        
    }

    private GeneVo[] proximaGeracao(GeneVo melhorPopulacaoAnterior) {

        int tamanhoPopulacao = _populacao.length;

        GeneVo[] novaPopulacao = new GeneVo[tamanhoPopulacao];
        novaPopulacao[0] = melhorPopulacaoAnterior;

        for (int i = 1; i < tamanhoPopulacao; i++) {

            int aleatorio1 = NumerosAleatorios.novoNumero(tamanhoPopulacao, -1);
            int aleatorio2 = NumerosAleatorios.novoNumero(tamanhoPopulacao, aleatorio1);

            GeneVo pai = selecaoMelhor(new GeneVo[] { _populacao[aleatorio1], _populacao[aleatorio2] });

            int aleatorio3 = NumerosAleatorios.novoNumero(tamanhoPopulacao, -1);
            int aleatorio4 = NumerosAleatorios.novoNumero(tamanhoPopulacao, aleatorio3);

            GeneVo mae = selecaoMelhor(new GeneVo[] { _populacao[aleatorio3], _populacao[aleatorio4] });

            GeneVo filho = _cruzador.cruza(pai, mae);

            novaPopulacao[i] = filho;
        }

        return novaPopulacao;
    }

    private void mutacao() {

        int tamanhoPopulacao = _populacao.length;

        int posicaoMutavel = NumerosAleatorios.random.nextInt(tamanhoPopulacao);

        int[] geneMutado = _populacao[posicaoMutavel].getGene();
        double quantidadeMutacoes = geneMutado.length * (_taxaMutacao / 100.0);

        for (int i = 0; i < (int)quantidadeMutacoes; i++) {
            int posicao = NumerosAleatorios.random.nextInt(geneMutado.length);
            int mutacao = NumerosAleatorios.novoNumero(9, geneMutado[posicao]);

            geneMutado[posicao] = mutacao;
        }

        _populacao[posicaoMutavel] = new GeneVo(geneMutado);
    }

}
