package com.jhonatapers.labirinto.serivce.impl;

import java.util.ArrayList;
import java.util.List;

import com.jhonatapers.labirinto.model.CoordenadaVo;
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
    int _penalizacao;
    boolean debug = false;

    public AlgoritmoGenetico(GeneVo[] populacao, LabirintoVo labirinto, int tentativasMaximas, ICruzador cruzador,
            int taxaMutacao, int penalizacao) {
        _populacao = populacao;
        _labirinto = labirinto;
        _tentativasMaximas = tentativasMaximas;
        _cruzador = cruzador;
        _taxaMutacao = taxaMutacao;
        _penalizacao = penalizacao;
    }

    @Override
    public IAlgoritmo debug(boolean ativo) {
        debug = ativo;
        return this;
    }

    @Override
    public GeneVo inicia() {

        GeneVo escolhido = selecaoMelhor(_populacao);
        int aptidao = euristica(escolhido);

        if (aptidao == -1)
            return escolhido;

        for (int i = 0; i < _tentativasMaximas; i++) {

            _populacao = proximaGeracao(escolhido);
            escolhido = selecaoMelhor(_populacao);
            aptidao = euristica(escolhido);

            // printaInformacoes(String.format("GERAÇÃO %s", i + 1), escolhido, score);
            if (aptidao == -1)
                break;

            mutacao();
            escolhido = selecaoMelhor(_populacao);
            aptidao = euristica(escolhido);

            // printaInformacoes(String.format("GERAÇÃO %s , COM MUTAÇÃO", i + 1),
            // escolhido, score);
            if (aptidao == -1)
                break;

            System.out.println(aptidao);
        }

        // printaFinal(score);
        return escolhido;
    }

    private GeneVo selecaoMelhor(GeneVo[] populacao) {

        int melhorResultado = euristica(populacao[0]);
        int melhorGene = 0;

        for (int i = 0; i < populacao.length; i++) {
            int aptidao = euristica(populacao[i]);

            if (aptidao < melhorResultado) {
                melhorGene = i;
                melhorResultado = aptidao;
            }
        }

        return populacao[melhorGene];
    }

    private int euristica(GeneVo gene) {

        int aptidao = 0;

        int casasPercorridas = 0;

        int comidasComidas = 0;

        CoordenadaVo coordenadaAtual = new CoordenadaVo();

        List<CoordenadaVo> coordenadaVisitadas = new ArrayList<CoordenadaVo>();

        for (int movimento : gene.getGene()) {

            coordenadaVisitadas.add(coordenadaAtual);

            if (!podeMover(movimento, coordenadaAtual)) {
                aptidao += _penalizacao * (gene.getGene().length - casasPercorridas);
                break;
            }

            coordenadaAtual = efetuaMovimento(movimento, coordenadaAtual);            
            casasPercorridas++;

            if(temComida(coordenadaAtual))
                comidasComidas++;

            if(comidasComidas == _labirinto.getComidas())     
                return -1;           

            final int x = coordenadaAtual.x;
            final int y = coordenadaAtual.y;
            long recorrencia = coordenadaVisitadas.stream().filter(c -> { return c.x == x && c.y == y; }).count();

            if(recorrencia > 1)
                aptidao += _penalizacao * recorrencia;

            _labirinto.getLabirinto();
        }

        if(comidasComidas > 0)
            return aptidao / comidasComidas;
        
        return aptidao;        
    }

    private boolean temComida(CoordenadaVo coordenadaAtual) {
        return _labirinto.getLabirinto()[coordenadaAtual.y][coordenadaAtual.x].equals("C");
    }

    private boolean podeMover(int movimento, CoordenadaVo coordenadaAtual) {

        switch (movimento) {
            case 0:
                if (coordenadaAtual.x - 1 < 0)
                    return false;

                if (_labirinto.getLabirinto()[coordenadaAtual.y][coordenadaAtual.x - 1].equals("1"))
                    return false;

                break;
            case 1:
                if (coordenadaAtual.x + 1 >= _labirinto.getN())
                    return false;

                if (_labirinto.getLabirinto()[coordenadaAtual.y][coordenadaAtual.x + 1].equals("1"))
                    return false;

                break;
            case 2:
                if (coordenadaAtual.y - 1 < 0)
                    return false;

                if (_labirinto.getLabirinto()[coordenadaAtual.y - 1][coordenadaAtual.x].equals("1"))
                    return false;

                break;
            case 3:
                if (coordenadaAtual.y + 1 >= _labirinto.getN())
                    return false;

                if (_labirinto.getLabirinto()[coordenadaAtual.y + 1][coordenadaAtual.x].equals("1"))
                    return false;

                break;
            case 4:
                if (coordenadaAtual.x - 1 < 0 || coordenadaAtual.y - 1 < 0)
                    return false;

                if (_labirinto.getLabirinto()[coordenadaAtual.y - 1][coordenadaAtual.x - 1].equals("1"))
                    return false;

                break;
            case 5:
                if (coordenadaAtual.x + 1 >= _labirinto.getN() || coordenadaAtual.y - 1 < 0)
                    return false;

                if (_labirinto.getLabirinto()[coordenadaAtual.y - 1][coordenadaAtual.x + 1].equals("1"))
                    return false;

                break;
            case 6:
                if (coordenadaAtual.x + 1 >= _labirinto.getN() || coordenadaAtual.y + 1 >= _labirinto.getN())
                    return false;

                if (_labirinto.getLabirinto()[coordenadaAtual.y + 1][coordenadaAtual.x + 1].equals("1"))
                    return false;

                break;
            case 7:
                if (coordenadaAtual.x - 1 < 0 || coordenadaAtual.y + 1 >= _labirinto.getN())
                    return false;

                if (_labirinto.getLabirinto()[coordenadaAtual.y + 1][coordenadaAtual.x - 1].equals("1"))
                    return false;

                break;
        }

        return true;
    }

    public CoordenadaVo efetuaMovimento(int movimento, CoordenadaVo coordenadaAtual) {

        switch (movimento) {
            case 0:
                coordenadaAtual.x--;
                break;
            case 1:
                coordenadaAtual.x++;
                break;
            case 2:
                coordenadaAtual.y--;
                break;
            case 3:
                coordenadaAtual.y++;
                break;
            case 4:
                coordenadaAtual.x--;
                coordenadaAtual.y--;
                break;
            case 5:
                coordenadaAtual.x++;
                coordenadaAtual.y--;
                break;
            case 6:
                coordenadaAtual.x++;
                coordenadaAtual.y++;
                break;
            case 7:
                coordenadaAtual.x--;
                coordenadaAtual.y++;
                break;
        }

        return coordenadaAtual;
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

        for (int i = 0; i < (int) quantidadeMutacoes; i++) {
            int posicao = NumerosAleatorios.random.nextInt(geneMutado.length);
            int mutacao = NumerosAleatorios.novoNumero(8, geneMutado[posicao]);

            geneMutado[posicao] = mutacao;
        }

        _populacao[posicaoMutavel] = new GeneVo(geneMutado);
    }

}
