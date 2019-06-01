package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.service.CompraService;
import br.com.cereal.cerealsul.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "pedidoService")
public class PedidoServiceImpl implements PedidoService {
    @Autowired
    CompraService compraService;

    @Override
    public Pedido analisarPedido(Pedido pedido) {
        pedido.setCompra(compraService.calcularAnaliseCompra(pedido));
        pedido.setCompra(compraService.calcularAnaliseCompra(pedido));

        System.out.println("********* analisar pedido");
        return pedido;
    }
}
