package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.model.Compra;
import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.PedidoDetalhe;
import br.com.cereal.cerealsul.model.Venda;
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
        return pedidoDetalheRepository.save(getPedidoDetalhe(pedido));
    }

    private PedidoDetalhe getPedidoDetalhe(Pedido pedido) {
        PedidoDetalhe pedidoDetalhe = new PedidoDetalhe();
        pedidoDetalhe.setPedido(pedido);

        setPedidoDetalheCompra(pedidoDetalhe, pedido.getCompra());
        setPedidoDetalheVenda(pedidoDetalhe, pedido.getVenda());

        pedidoDetalhe.setValorVenda(pedido.getValorVenda());
        return pedidoDetalhe;
    }

    private void setPedidoDetalheVenda(PedidoDetalhe pedidoDetalhe, Venda venda) {
        pedidoDetalhe.setEstadoCliente(venda.getEstadoCliente());
        pedidoDetalhe.setTipoAtividadeVenda(venda.getTipoAtividadeVenda());
        pedidoDetalhe.setVendaPossuiCorretor(venda.getVendaPossuiCorretor());
        pedidoDetalhe.setLocalDestino(venda.getLocalDestino());
        pedidoDetalhe.setVendaPossuiFrete(venda.getVendaPossuiFrete());
        pedidoDetalhe.setVendaTipoFrete(venda.getVendaTipoFrete());
        pedidoDetalhe.setVendaValorIcms(venda.getVendaValorIcms());
        pedidoDetalhe.setVendaValorPisCofins(venda.getVendaValorPisECofins());
    }

    private void setPedidoDetalheCompra(PedidoDetalhe pedidoDetalhe, Compra compra) {
        pedidoDetalhe.setCompraPossuiCorretor(compra.getCompraPossuiCorretor());
        pedidoDetalhe.setCompraPossuiFrete(compra.getCompraPossuiFrete());
        pedidoDetalhe.setCompraTipoFrete(compra.getCompraTipoFrete());
        pedidoDetalhe.setFunrural(compra.getFunrural());
        pedidoDetalhe.setPossuiProRural(compra.getPossuiProRural());
        pedidoDetalhe.setTipoAtividadeCompra(compra.getTipoAtividadeCompra());
        pedidoDetalhe.setValorBrutoCompra(compra.getValorBrutoCompra());
        pedidoDetalhe.setValorFunRural(compra.getValorFunRural());
        pedidoDetalhe.setValorIcmsProdutor(compra.getValorIcmsProdutor());
        pedidoDetalhe.setValorPat(compra.getValorPat());
        pedidoDetalhe.setValorSenar(compra.getValorSenar());
    }
}
