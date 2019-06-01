package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.Venda;

public interface VendaService {
    Venda calcularAnaliseVenda(Pedido pedido);
}
