package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.PedidoDetalhe;

public interface PedidoDetalheService {
    PedidoDetalhe salvarByPedido(Pedido pedido);
    PedidoDetalhe getByPedido(Pedido pedido);
    PedidoDetalhe salvar(PedidoDetalhe pedidoDetalhe);
}
