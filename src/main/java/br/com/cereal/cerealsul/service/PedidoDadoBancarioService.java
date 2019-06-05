package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.PedidoDadoBancario;

public interface PedidoDadoBancarioService {
    PedidoDadoBancario salvarByPedido(Pedido pedido);
}
