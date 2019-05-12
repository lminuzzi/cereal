package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.*;
import org.springframework.stereotype.Service;

@Service
public class FreteService {
    public double calculaFreteCompra(Pedido pedido) {
        double valorFrete = 0;
        double impostoFrete = 0;
        Compra compra = pedido.getCompra();
        Fornecedor fornecedor = pedido.getFornecedor();
        if(compra == null || fornecedor == null) {
            //TODO criar Exception
        }

        return calculaFrete(compra.getCompraFrete(), compra.getCompraFreteTotal(),
                fornecedor.getTipoPessoa(), pedido.getQtSacos());
    }

    public double calculaFreteVenda(Pedido pedido) {
        Venda venda = pedido.getVenda();
        Cliente cliente = pedido.getCliente();
        if(venda == null || cliente == null) {
            //TODO criar Exception
        }

        return calculaFrete(venda.getVendaFrete(), venda.getVendaFreteTotal(),
                cliente.getTipoPessoa(), pedido.getQtSacos());
    }

    private double calculaFrete(String frete, double valorTotalFrete,
                                TipoPessoa tipoPessoa, double qtdSacos) {
        double valorFrete = 0;
        double impostoFrete = 0;
        // VALOR DO FRETE DE VENDA POR SACO COM TRANSPORTE PJ
        if (frete.equals("SIM") && valorTotalFrete > 0) {
            valorFrete = valorTotalFrete / 16.6666666666667;
            if(tipoPessoa.equals(TipoPessoa.PESSOA_FISICA)) {
                // VALOR DO FRETE DE VENDA POR SACO COM TRANSPORTE PF
                double y = valorFrete * qtdSacos;
                impostoFrete = calculaImpostosFrete(y) / qtdSacos;
            }
        }
        return valorFrete + impostoFrete;
    }

    public double calculaImpostosFrete(double valorTotal) {
        // SE O FRETE FOR PF ESSE MÉTODO DEVE CALCULAR OS IMPOSTOS SOBRE ESTE
        double inss = getInss(valorTotal);
        double baseIrpf = (valorTotal - inss) * 0.1;
        double irpf = getIrpf(baseIrpf);

        double sest = baseIrpf * 0.015;
        double senat = baseIrpf * 0.01;
        double soma = inss + irpf + sest + senat;
        return soma;
    }

    private double getIrpf(double baseIrpf) {
        double irpf;
        if (baseIrpf <= 1903.98) {
            irpf = 0;
        } else if (baseIrpf > 1903.98 && baseIrpf <= 2826.65) {
            irpf = (baseIrpf * 0.075) - 142.8;
        } else if (baseIrpf > 2826.65 && baseIrpf <= 3751.06) {
            irpf = (baseIrpf * 0.15) - 354.8;
        } else if (baseIrpf > 3751.06 && baseIrpf <= 4664.68) {
            irpf = (baseIrpf * 0.225) - 636.13;
        } else {
            irpf = (baseIrpf * 0.275) - 869.36;
        }
        return irpf;
    }

    private double getInss(double valorTotal) {
        double inss;
        if (valorTotal <= 1659.38) {
            inss = valorTotal * 0.08;
        } else if (valorTotal > 1659.38 && valorTotal <= 2765.66) {
            inss = valorTotal * 0.09;
        } else if (valorTotal > 2765.66 && valorTotal <= 5531.31) {
            inss = valorTotal * 0.11;
        } else {
            inss = 5531.31 * 0.11;
        }
        return inss;
    }
}