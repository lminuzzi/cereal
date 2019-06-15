package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.PedidoDadoBancario;
import br.com.cereal.cerealsul.repository.PedidoDadoBancarioRepository;
import br.com.cereal.cerealsul.service.PedidoDadoBancarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PedidoDadoBancarioService")
public class PedidoDadoBancarioServiceImpl implements PedidoDadoBancarioService {
    @Autowired
    private PedidoDadoBancarioRepository repository;

    @Override
    public PedidoDadoBancario salvarByPedido(Pedido pedido) {
        return repository.save(pedido.getPedidoDadoBancario());
    }

    @Override
    public PedidoDadoBancario getByPedido(Pedido pedido) {
        PedidoDadoBancario pedidoDadoBancario = pedido.getPedidoDadoBancario();
        pedidoDadoBancario.setPedido(pedido);
        return pedidoDadoBancario;
    }

    @Override
    public PedidoDadoBancario salvar(PedidoDadoBancario pedidoDadoBancario) {
        return repository.save(pedidoDadoBancario);
    }
}
