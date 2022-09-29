package com.jhonatapers.labirinto.serivce.impl;

import com.jhonatapers.labirinto.model.GeneVo;
import com.jhonatapers.labirinto.serivce.IAlgoritmo;

public class AlgorimoAEstrela implements IAlgoritmo {

    private boolean debug;

    @Override
    public GeneVo inicia() {
        return null;
    }

    @Override
    public IAlgoritmo debug(boolean ativo) {
        debug = ativo;
        return this;
    }
    
}
