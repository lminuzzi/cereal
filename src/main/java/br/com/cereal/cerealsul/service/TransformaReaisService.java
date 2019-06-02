package br.com.cereal.cerealsul.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransformaReaisService {
    public static double transformar(double valor) {
        return new BigDecimal(valor)
                .setScale(2, RoundingMode.HALF_EVEN)
                .doubleValue();
    }
}
