package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.TipoAtividadeVenda;
import org.springframework.stereotype.Service;

@Service
public class PisCofinsService {
    // SETA A TAXA DO PIS COFINS E CALCULA O VALOR
    public double calculaPisCofins(Pedido pedido) {
        double taxaPisCofins = 0;
        if (pedido.getProduto().equals("MILHO")) {
            if (pedido.getCliente().getRegiao().equals("EXP") ||
                    pedido.getVenda().getTipoAtividadeVenda().equals(TipoAtividadeVenda.HUMANO)) {
                taxaPisCofins = 0;
            } else if (pedido.getVenda().getTipoAtividadeVenda().equals(TipoAtividadeVenda.OUTROS)) {
                taxaPisCofins = 0.0925;
            } else {
                taxaPisCofins = 0;
            }
        } else if (pedido.getProduto().equals("SOJA")) {
            taxaPisCofins = 0;
        }
        return taxaPisCofins;
    }
}
