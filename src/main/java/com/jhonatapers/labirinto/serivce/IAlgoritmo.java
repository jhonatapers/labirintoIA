package com.jhonatapers.labirinto.serivce;

import com.jhonatapers.labirinto.model.GeneVo;

public interface IAlgoritmo {
    
    public GeneVo inicia();

    IAlgoritmo debug(boolean ativo);
    
}
