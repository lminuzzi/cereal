package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.PedidoDetalhe;
import br.com.cereal.cerealsul.repository.PedidoDetalheRepository;
import br.com.cereal.cerealsul.service.PedidoDetalheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PedidoDetalheService")
public class PedidoDetalheServiceImpl implements PedidoDetalheService {
    @Autowired
    PedidoDetalheRepository pedidoDetalheRepository;

    @Override
    public PedidoDetalhe salvarByPedido(Pedido pedido) {
        return pedidoDetalheRepository.save(pedido.getPedidoDetalhe());
    }
}
