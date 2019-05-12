package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.TipoAtividadeCompra;
import org.springframework.stereotype.Service;

@Service
public class IcmsCompraService {
    // CALCULA O ICMS DE COMPRA (QUE O PROD DEVERIA PAGAR,
    // PORÉM A EMPRESA FAZ O CALCULO COM O VALOR LIQUIDO)
    public double calculaIcmsCompra(Pedido pedido) {
        double valorCalculo = 0;
        if (pedido.getProduto().equals("MILHO")) {
            valorCalculo = calculaTaxaIcmsMilhoCompra(pedido.getCompra().getProdutorEstado(),
                    pedido.getVenda().getTradingEstado(), pedido.getCompra().getTipoAtividadeCompra(),
                    pedido.getCompra().getPossuiProRural());
        } else if (pedido.getProduto().equals("SOJA")) {
            valorCalculo = calculaTaxaIcmsSojaCompra(pedido.getCompra().getProdutorEstado(),
                    pedido.getVenda().getTradingEstado(), pedido.getCompra().getTipoAtividadeCompra(),
                    pedido.getCompra().getPossuiProRural());
        }
        return valorCalculo;
    }


    // CALCULA O ICMS DE COMPRA NO CASO DO MATERIAL SER SOJA
    private double calculaTaxaIcmsSojaCompra(String estadoSaidaMaterial,
                                             String estadoFilial,
                                             TipoAtividadeCompra atividadeCompra,
                                             boolean proRural) {
        double valorCalculo = 0;
        // DEFINIÇÃO DA TAXA DE ICMS
        // GO - GO
        if (estadoSaidaMaterial.equals("GO")
                && estadoFilial.equals("GO")) {
            // GO - GO > PROD OU COOPERATIVA
            if (atividadeCompra.equals(TipoAtividadeCompra.PROD)
                || atividadeCompra.equals(TipoAtividadeCompra.COOPERATIVA)) {
                valorCalculo = 0;
            }
            // GO - GO > CEREALISTA
            if (atividadeCompra.equals(TipoAtividadeCompra.CEREALISTA)) {
                valorCalculo = 0.17;
            }
        }

        // DF - GO
        if (estadoSaidaMaterial.equals("DF")
                && estadoFilial.equals("GO")) {
            // DF - GO > PROD COM PRORURAL
            if(atividadeCompra.equals(TipoAtividadeCompra.PROD)
                    && proRural) {
                valorCalculo = 0.024;
            }
            // DF - GO > PROD SEM PRO RURAL, COOPERATIVA E CEREALISTAS
            if (!proRural) {
                valorCalculo = 0.12;
            }
            // DF - GO > PROD PRO RURAL, COOPERATIVA OU CEREALISTAS
            if (proRural
                    && (atividadeCompra.equals(TipoAtividadeCompra.COOPERATIVA)
                    || atividadeCompra.equals(TipoAtividadeCompra.CEREALISTA))) {
                valorCalculo = 0.12;
            }
        }

        // DF - MG
        if (estadoSaidaMaterial.equals("DF")
                && estadoFilial.equals("MG")) {
            // DF - MG > PROD COM PRO RURAL
            if(atividadeCompra.equals(TipoAtividadeCompra.PROD)
                    && proRural) {
                valorCalculo = 0.024;
            }
            // DF - MG > PROD SEM PRORURAL, COOPERATIVA E CEREALISTA
            if (!proRural) {
                valorCalculo = 0.12;
            }
        }

        // MG - MG
        if (estadoSaidaMaterial.equals("MG")
                && estadoFilial.equals("MG")) {
            // MG - MG > PROD OU COOPERATIVA
            if(atividadeCompra.equals(TipoAtividadeCompra.PROD)
                    || atividadeCompra.equals(TipoAtividadeCompra.COOPERATIVA)) {
                valorCalculo = 0;
            }
            // MG - MG > CEREALISTA
            if (atividadeCompra.equals(TipoAtividadeCompra.CEREALISTA)) {
                valorCalculo = 0.18;
            }
        }

        return valorCalculo;
    }


    // CALCULA O ICMS DE COMPRA NO CASO DO MATERIAL SER MILHO
    private double calculaTaxaIcmsMilhoCompra(String estadoSaidaMaterial,
                                              String estadoFilial,
                                              TipoAtividadeCompra atividadeCompra,
                                              boolean proRural) {
        double valorCalculo = 0;
        // DEFINIÇÃO DA TAXA DE ICMS
        // GO - GO > PROD OU COOPERATIVA
        if (estadoSaidaMaterial.equals("GO")
                && estadoFilial.equals("GO") && (atividadeCompra.equals(TipoAtividadeCompra.PROD)
                || atividadeCompra.equals(TipoAtividadeCompra.COOPERATIVA))) {
            valorCalculo = 0;
        }
        // GO - GO > CEREALISTA
        if (estadoSaidaMaterial.equals("GO")
                && estadoFilial.equals("GO") && atividadeCompra.equals(TipoAtividadeCompra.CEREALISTA)) {
            valorCalculo = 0.17;
        }
        // DF - GO > PROD COM PRORURAL
        if (estadoSaidaMaterial.equals("DF")
                && estadoFilial.equals("GO") && atividadeCompra.equals(TipoAtividadeCompra.PROD)
                && proRural) {
            valorCalculo = 0.024;
        }
        // DF - GO > PROD SEM PRO RURAL, COOPERATIVA E CEREALISTAS
        if (estadoSaidaMaterial.equals("DF")
                && estadoFilial.equals("GO") && !proRural) {
            valorCalculo = 0.12;

        }
        if (estadoSaidaMaterial.equals("DF")
                && estadoFilial.equals("GO") && proRural
                && (atividadeCompra.equals(TipoAtividadeCompra.COOPERATIVA)
                || atividadeCompra.equals(TipoAtividadeCompra.CEREALISTA))) {
            valorCalculo = 0.12;
        }

        // DF - MG > PROD COM PRO RURAL
        if (estadoSaidaMaterial.equals("DF")
                && estadoFilial.equals("MG") && atividadeCompra.equals(TipoAtividadeCompra.PROD)
                && proRural) {
            valorCalculo = 0.024;
        }
        // DF - MG > PROD SEM PRORURAL, COOPERATIVA E CEREALISTA
        if (estadoSaidaMaterial.equals("DF")
                && estadoFilial.equals("MG") && !proRural) {
            valorCalculo = 0.12;
        }
        // MG - MG > PROD OU COOPERATIVA
        if (estadoSaidaMaterial.equals("MG")
                && estadoFilial.equals("MG") && (atividadeCompra.equals(TipoAtividadeCompra.PROD)
                || atividadeCompra.equals(TipoAtividadeCompra.COOPERATIVA))) {
            valorCalculo = 0;
        }
        // MG - MG > CEREALISTA
        if (estadoSaidaMaterial.equals("MG")
                && estadoFilial.equals("MG") && atividadeCompra.equals(TipoAtividadeCompra.CEREALISTA)) {
            valorCalculo = 0.18;
        }
        return valorCalculo;
    }
}
