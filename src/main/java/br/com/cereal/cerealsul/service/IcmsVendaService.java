package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.TipoAtividadeCompra;
import br.com.cereal.cerealsul.model.TipoAtividadeVenda;
import org.springframework.stereotype.Service;

@Service
public class IcmsVendaService {
    // CALCULA DOS VALORES DE DÉBITO E CRÉDITO DE ICMS E ACHA O VALOR FINAL DO
    // ICMS
    // DE VENDA
    public double calculaIcmsVenda(Pedido pedido) {
        double debitoIcms = 0;
        double creditoIcms = this.creditoIcms(pedido.getCompra().getProdutorEstado(),
                pedido.getVenda().getEstadoCliente(), pedido.getCompra().getTipoAtividadeCompra());
        // SOJA - CALCULA TAXA DE DEBITO
        if (pedido.getProduto().equals("SOJA")) {
            debitoIcms = this.debitoIcmsSoja(pedido.getCompra().getProdutorEstado(),
                    pedido.getVenda().getEstadoCliente(), pedido.getVenda().getTipoAtividadeVenda());
        }
        // MILHO - CALCULA TAXA DE DEBITO
        if (pedido.getProduto().equals("MILHO")) {
            debitoIcms = this.debitoIcmsMilho(pedido.getCompra().getProdutorEstado(),
                    pedido.getVenda().getEstadoCliente(), pedido.getVenda().getEstadoCliente(),
                    pedido.getVenda().getTipoAtividadeVenda());
        }

        debitoIcms = pedido.getValorVenda() * debitoIcms;
        creditoIcms = pedido.getValorLiq() * creditoIcms;

        double diferenca = debitoIcms - creditoIcms;
        if (debitoIcms - creditoIcms <= 0) {
            return 0;
        }
        return diferenca;
    }

    // CALCULA O ICMS DE VENDA PARA POSTERIORMENTE CALCULAR SE A SALDO DE ICMS
    // NA OPERAÇÃO OU NÃO
    // QUANDO O MATERIAL É SOJA
    private double debitoIcmsSoja(String estadoFilial, String estadoCliente,
                                  TipoAtividadeVenda atividadeVenda) {
        double valorCalculo = 0;
        // GO - GO > OUTROS
        if (estadoFilial.equals("GO") && estadoCliente.equals("GO")
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = 0.17;
        }

        // GO - OUTROS ESTADOS > OUTROS
        if (estadoFilial.equals("GO")
                && (estadoCliente.equals("MG") || estadoCliente.equals("OUTROSESTADOS"))
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = 0.12;
        }

        // GO - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) > OUTROS
        if (estadoFilial.equals("GO") && isRegiaoSul(estadoCliente)
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = 0.12;
        }

        // GO - GO > IND.RAC/PROD/GRANJA
        if (estadoFilial.equals("GO") && estadoCliente.equals("GO")
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) || atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0;
        }

        // GO - OUTROS ESTADOS > IND.RAC/PROD/GRANJA
        if (estadoFilial.equals("GO")
                && (estadoCliente.equals("MG") || estadoCliente.equals("OUTROSESTADOS"))
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) || atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0.084;
        }

        // GO - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) > IND.RAC/PROD/GRANJA
        if (estadoFilial.equals("GO") && isRegiaoSul(estadoCliente)
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) || atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0.084;
        }

        // GO - GO > CONSUMO HUMANO
        if (estadoFilial.equals("GO") && estadoCliente.equals("GO")
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.17;
        }

        // GO - OUTROS ESTADOS > CONSUMO HUMANO
        if (estadoFilial.equals("GO")
                && (estadoCliente.equals("MG") || estadoCliente.equals("OUTROSESTADOS"))
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.12;
        }

        // GO - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) > CONSUMO HUMANO
        if (estadoFilial.equals("GO") && isRegiaoSul(estadoCliente)
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.12;
        }

        // GO/MG - EXPORTAÇÃO > ISENTO
        if ((estadoFilial.equals("GO") || estadoCliente.equals("MG"))
                && estadoCliente.equals("MG") && atividadeVenda.equals(TipoAtividadeVenda.EXP)) {
            valorCalculo = 0;
        }

        // MG - MG > IND.RAC/PROD/GRANJA
        if (estadoFilial.equals("MG") && estadoCliente.equals("MG")
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) || atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0;
        }

        // MG - OUTROS ESTADOS > IND.RAC/PROD/GRANJA
        if (estadoFilial.equals("MG")
                && (estadoCliente.equals("GO") || estadoCliente.equals("OUTROSESTADOS"))
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) || atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0.049;
        }

        // MG - MG > OUTROS
        if (estadoFilial.equals("MG") && estadoCliente.equals("MG")
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = 0.18;
        }

        // MG - OUTROS ESTADOS > OUTROS
        if (estadoFilial.equals("MG")
                && (estadoCliente.equals("GO") || estadoCliente.equals("OUTROSESTADOS"))
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = 0.07;
        }

        // MG - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) > OUTROS
        if (estadoFilial.equals("MG") && isRegiaoSul(estadoCliente)
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = 0.12;
        }

        // MG - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) > IND.RAC/PROD/GRANJA
        if (estadoFilial.equals("MG") && isRegiaoSul(estadoCliente)
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) || atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0.084;
        }

        // MG - MG > CONSUMO HUMANO
        if (estadoFilial.equals("MG") && estadoCliente.equals("MG")
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.18;
        }

        // MG - OUTROS ESTADOS > CONSUMO HUMANO
        if (estadoFilial.equals("MG")
                && (estadoCliente.equals("GO") || estadoCliente.equals("OUTROSESTADOS"))
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.07;
        }

        // MG - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) > CONSUMO HUMANO
        if (estadoFilial.equals("MG") && isRegiaoSul(estadoCliente)
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.12;
        }
        return valorCalculo;
    }

    // CALCULA TAXA DE DEBITO DE ICMS NO CASO DO MATERIAL SER MILHO PARA
    // POSTERIOR CALCULO FINAL DE VENDA DE ICMS

    private double debitoIcmsMilho(String estadoFilial, String estadoCliente,
                                   String estadoSaidaMaterial, TipoAtividadeVenda atividadeVenda) {
        double valorCalculo = 0;
        // GO - GO OUTROS
        if (estadoFilial.equals("GO") && estadoCliente.equals("GO")
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = 0.17;
        }

        // GO - OUTROS ESTADOS OUTROS (OUTORGADO 6% E PROTEGE 15%)
        if (estadoFilial.equals("GO") && estadoCliente.equals("OUTROSESTADOS")
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = ((0.12 - 0.06) + ((0.12 - 0.06) * 0.15));
        }

        // GO - GO IND.RAC/PROD/GRANJA
        if (estadoFilial.equals("GO") && estadoCliente.equals("GO")
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) ||
                atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0;
        }

        // GO - OUTROS ESTADOS IND.RAC/PROD/GRANJA (COMPRA DO DF)
        if (estadoSaidaMaterial.equals("DF") && estadoFilial.equals("GO")
                && estadoCliente.equals("OUTROSESTADOS") &&
                (atividadeVenda.equals(TipoAtividadeVenda.PROD) ||
                        atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                        || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0.084;
        }

        // GO - OUTROS ESTADOS IND.RAC/PROD/GRANJA (COMPRA DE GO COM CR�DITO
        // OUTORGADO)
        if (estadoSaidaMaterial.equals("GO") && estadoFilial.equals("GO")
                && estadoCliente.equals("OUTROSESTADOS")
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) || atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = ((0.084 - 0.0343) + ((0.084 - 0.0343) * 0.15));
        }

        // GO - GO CONSUMO HUMANO
        if (estadoFilial.equals("GO") && estadoCliente.equals("GO")
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.12;
        }

        // GO - OUTROS ESTADOS CONSUMO HUMANO
        if (estadoFilial.equals("GO") && estadoCliente.equals("OUTROSESTADOS")
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.12;
        }

        // GO - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) CONSUMO HUMANO
        if (estadoFilial.equals("GO") && isRegiaoSul(estadoCliente)
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.12;
        }

        // GO - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) IND.RAC/PROD/GRANJA
        // (COMPRA DO
        // DF)
        if (estadoSaidaMaterial.equals("DF") && estadoFilial.equals("GO")
                && isRegiaoSul(estadoCliente)
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) || atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0.084;
        }

        // GO - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) IND.RAC/PROD/GRANJA
        // (COMPRA DO
        // GO COM CR�DITO OUTORGADO)
        if (estadoSaidaMaterial.equals("GO") && estadoFilial.equals("GO")
                && isRegiaoSul(estadoCliente)
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) || atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = ((0.084 - 0.0343) + ((0.084 - 0.0343) * 0.15));
        }

        // GO - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) OUTROS
        if (estadoFilial.equals("GO") && isRegiaoSul(estadoCliente)
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = 0.12;
        }

        // GO/MG - EXPORTA��O ISENTO
        if ((estadoFilial.equals("GO") || estadoFilial.equals("MG"))
                && estadoCliente.equals("EXP")) {
            valorCalculo = 0;
        }

        // MG - MG IND.RAC/PROD/GRANJA
        if (estadoFilial.equals("MG") && estadoCliente.equals("MG")
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) ||
                atividadeVenda.equals(TipoAtividadeVenda.RACOES) ||
                atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0;
        }

        // MG - OUTROS ESTADOS IND.RAC/PROD/GRANJA
        if (estadoFilial.equals("MG") && estadoCliente.equals("OUTROSESTADOS")
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) ||
                atividadeVenda.equals(TipoAtividadeVenda.RACOES) ||
                atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0.049;
        }

        // MG - MG OUTROS
        if (estadoFilial.equals("MG") && estadoCliente.equals("MG")
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = 0.18;
        }

        // MG - OUTROS ESTADOS OUTROS
        if (estadoFilial.equals("MG") && estadoCliente.equals("OUTROSESTADOS")
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = 0.07;
        }

        // MG - MG CONSUMO HUMANO
        if (estadoFilial.equals("MG") && estadoCliente.equals("MG")
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.18;
        }

        // MG - OUTROS ESTADOS CONSUMO HUMANO
        if (estadoFilial.equals("MG") && estadoCliente.equals("OUTROSESTADOS")
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.07;
        }

        // MG - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) CONSUMO HUMANO
        if (estadoFilial.equals("MG") && isRegiaoSul(estadoCliente)
                && atividadeVenda.equals(TipoAtividadeVenda.HUMANO)) {
            valorCalculo = 0.12;
        }

        // MG - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) OUTROS
        if (estadoFilial.equals("MG") && isRegiaoSul(estadoCliente)
                && atividadeVenda.equals(TipoAtividadeVenda.OUTROS)) {
            valorCalculo = 0.12;
        }

        // MG - SUL/SUDESTE ( PR, RS, RJ, SC OU SP) IND.RAC/PROD/GRANJA
        if (estadoFilial.equals("MG") && isRegiaoSul(estadoCliente)
                && (atividadeVenda.equals(TipoAtividadeVenda.PROD) || atividadeVenda.equals(TipoAtividadeVenda.RACOES)
                || atividadeVenda.equals(TipoAtividadeVenda.GRANJA))) {
            valorCalculo = 0.084;
        }
        return valorCalculo;
    }

    // CALCULA O CRÉDITO DE ICMS

    private double creditoIcms(String estadoFilial, String estadoSaidaMaterial,
                               TipoAtividadeCompra atividadeCompra) {
        double valorCalculo = 0;
        // GO - GO > PROD/COOPERATIVA
        if (estadoSaidaMaterial.equals("GO") && estadoFilial.equals("GO")
                && (atividadeCompra.equals(TipoAtividadeCompra.PROD) ||
                atividadeCompra.equals(TipoAtividadeCompra.COOPERATIVA))) {
            valorCalculo = 0;
        }
        // GO - GO > CEREALISTA
        if (estadoSaidaMaterial.equals("GO") && estadoFilial.equals("GO")
                && atividadeCompra.equals(TipoAtividadeCompra.CEREALISTA)) {
            valorCalculo = 0.17;
        }
        // DF - GO/MG > QUALQUER UM
        if (estadoSaidaMaterial.equals("DF")
                && (estadoFilial.equals("MG") || estadoFilial.equals("GO"))) {
            valorCalculo = 0.12;
        }
        // MG - MG > PROD/COOPERATIVA
        if (estadoSaidaMaterial.equals("MG") && estadoFilial.equals("MG")
                && (atividadeCompra.equals(TipoAtividadeCompra.PROD) ||
                atividadeCompra.equals(TipoAtividadeCompra.COOPERATIVA))) {
            valorCalculo = 0;
        }
        // MG - MG > CEREALISTA
        if (estadoSaidaMaterial.equals("MG") && estadoFilial.equals("MG")
                && atividadeCompra.equals(TipoAtividadeCompra.CEREALISTA)) {
            valorCalculo = 0.18;
        }
        return valorCalculo;
    }

    private boolean isRegiaoSul(String estado) {
        return estado.equals("PR") || estado.equals("RS") || estado.equals("RJ") ||
                estado.equals("SC") || estado.equals("SP");
    }
}