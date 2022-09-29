package com.jhonatapers.labirinto.serivce.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jhonatapers.labirinto.model.CoordenadaVo;
import com.jhonatapers.labirinto.model.GeneVo;
import com.jhonatapers.labirinto.model.LabirintoVo;
import com.jhonatapers.labirinto.serivce.IAlgoritmo;
import com.jhonatapers.labirinto.serivce.ICruzador;
import com.jhonatapers.labirinto.util.GravaLog;
import com.jhonatapers.labirinto.util.NumerosAleatorios;

public class AlgoritmoGenetico implements IAlgoritmo {

    GeneVo[] _populacao;
    LabirintoVo _labirinto;
    int _tentativasMaximas;
    ICruzador _cruzador;
    int _taxaMutacao;
    int _penalizacao;

    boolean debug = false;
    GravaLog _log;

    public AlgoritmoGenetico(GeneVo[] populacao, LabirintoVo labirinto, int tentativasMaximas, ICruzador cruzador,
            int taxaMutacao, int penalizacao, GravaLog log) {
        _populacao = populacao;
        _labirinto = labirinto;
        _tentativasMaximas = tentativasMaximas;
        _cruzador = cruzador;
        _taxaMutacao = taxaMutacao;
        _penalizacao = penalizacao;

        _log = log;
    }

    @Override
    public IAlgoritmo debug(boolean ativo) {
        debug = ativo;
        return this;
    }

    @Override
    public GeneVo inicia() {

        int melhorAptidao = 9999999;

        GeneVo escolhido = selecaoMelhor(_populacao);

        if (escolhido.getAptidao() == 0)
            return escolhido;

        int geracao = 0;
        for (geracao = 0; geracao < _tentativasMaximas; geracao++) {

            _populacao = proximaGeracao(escolhido);
            escolhido = selecaoMelhor(_populacao);

            if (escolhido.getAptidao() == 0)
                break;

            mutacao();
            escolhido = selecaoMelhor(_populacao);
            
            if (escolhido.getAptidao() == 0)
                break;

            if (escolhido.getAptidao() < melhorAptidao) {
                melhorAptidao = escolhido.getAptidao();
                System.out.println(escolhido.toString());
            }

            if (debug)
                _log.gravaGeracao(geracao, _populacao, escolhido);
        }

        _log.gravaGeracao(geracao, _populacao, escolhido);

        if (debug) {
            try {
                _log.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return escolhido;
    }

    private GeneVo selecaoMelhor(GeneVo[] populacao) {
        GeneVo melhorGene = populacao[0];
        heuristica(melhorGene);

        for (GeneVo gene : populacao) {
            heuristica(gene);

            if (gene.getAptidao() < melhorGene.getAptidao())
                melhorGene = gene;
        }

        return melhorGene;
    }

    private void heuristica(GeneVo gene) {

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

            final int x = coordenadaAtual.x;
            final int y = coordenadaAtual.y;
            long recorrencia = coordenadaVisitadas.stream().filter(c -> {
                return c.x == x && c.y == y;
            }).count();

            if (recorrencia > 1)
                aptidao += _penalizacao * recorrencia;

            if (temComida(coordenadaAtual) && recorrencia == 1)
                comidasComidas++;

            if (comidasComidas == _labirinto.getComidas()) {
                break;
            }

            coordenadaAtual = efetuaMovimento(movimento, coordenadaAtual);
            casasPercorridas++;

        }

        aptidao -= casasPercorridas;

        if (comidasComidas > 0) {
            aptidao = aptidao / comidasComidas;
        }


        aptidao += _labirinto.getComidas() - comidasComidas;

        if (comidasComidas == _labirinto.getComidas())
            aptidao = 0;

        gene.setComidasComidas(comidasComidas);
        gene.setAptidao(aptidao);
        gene.setCasasPercorridas(casasPercorridas);

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
                return new CoordenadaVo(coordenadaAtual.x - 1, coordenadaAtual.y);
            case 1:
                return new CoordenadaVo(coordenadaAtual.x + 1, coordenadaAtual.y);
            case 2:
                return new CoordenadaVo(coordenadaAtual.x, coordenadaAtual.y - 1);
            case 3:
                return new CoordenadaVo(coordenadaAtual.x, coordenadaAtual.y + 1);
            case 4:
                return new CoordenadaVo(coordenadaAtual.x - 1, coordenadaAtual.y - 1);
            case 5:
                return new CoordenadaVo(coordenadaAtual.x + 1, coordenadaAtual.y - 1);
            case 6:
                return new CoordenadaVo(coordenadaAtual.x + 1, coordenadaAtual.y + 1);
            case 7:
                return new CoordenadaVo(coordenadaAtual.x - 1, coordenadaAtual.y + 1);
            default:
                return coordenadaAtual;
        }
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

        double quantidadeMutacoesPorPopulacao = _populacao.length * (_taxaMutacao / 100.0);

        // parametrizavel por dps
        double quantidadeMutacoesAreaCerta = quantidadeMutacoesPorPopulacao / 10;

        for (int i = 0; i < quantidadeMutacoesPorPopulacao; i++) {

            int posicaoMutavel = NumerosAleatorios.random.nextInt(1, tamanhoPopulacao);
            int[] geneMutado = _populacao[posicaoMutavel].getGene();

            double quantidadeMutacoesPorGene = geneMutado.length * (_taxaMutacao / 100.0);

            CoordenadaVo coordenadaAtual = new CoordenadaVo();
            int movimentosEfetuados = 0;

            for (int movimento : geneMutado) {
                if (!podeMover(movimento, coordenadaAtual))
                    break;

                coordenadaAtual = efetuaMovimento(movimento, coordenadaAtual);
                movimentosEfetuados++;
            }

            for (int j = 0; j < (int) quantidadeMutacoesPorGene; j++) {

                if (movimentosEfetuados >= geneMutado.length)
                    break;

                int posicao = NumerosAleatorios.random.nextInt(movimentosEfetuados, geneMutado.length);
                int mutacao = NumerosAleatorios.novoNumero(8, geneMutado[posicao]);

                geneMutado[posicao] = mutacao;
            }

            for (int j = 0; j < (int) quantidadeMutacoesAreaCerta; j++) {

                int posicao = NumerosAleatorios.random.nextInt(movimentosEfetuados == 0 ? 1 : movimentosEfetuados);
                int mutacao = NumerosAleatorios.novoNumero(8, geneMutado[posicao]);

                geneMutado[posicao] = mutacao;
            }

            GeneVo mutado = new GeneVo(geneMutado);
            mutado.setMutacoes((int) quantidadeMutacoesPorGene + (int) quantidadeMutacoesAreaCerta);
            _populacao[posicaoMutavel] = mutado;

        }

    }

}
