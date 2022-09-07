package com.jhonatapers.labirinto.util;

import java.util.Random;

public class NumerosAleatorios {

    public static final Random random = new Random();

    public static final int novoNumero(int bound, int exceto) {
        int escolhido = random.nextInt(bound);

        if (escolhido == exceto)
            return novoNumero(bound, exceto);

        return escolhido;
    }

}
