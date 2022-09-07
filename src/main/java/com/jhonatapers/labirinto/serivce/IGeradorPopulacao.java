package com.jhonatapers.labirinto.serivce;

import com.jhonatapers.labirinto.model.GeneVo;

public interface IGeradorPopulacao {
    
    public GeneVo[] gera(int tamanhoPopulacao, int tamanhoLabirinto);

}
