package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.exception.ResourceNotFoundException;
import br.com.cereal.cerealsul.model.*;
import br.com.cereal.cerealsul.repository.ClienteRepository;
import br.com.cereal.cerealsul.repository.FornecedorRepository;
import br.com.cereal.cerealsul.repository.PedidoRepository;
import br.com.cereal.cerealsul.service.CompraService;
import br.com.cereal.cerealsul.service.PedidoDetalheService;
import br.com.cereal.cerealsul.service.PedidoService;
import br.com.cereal.cerealsul.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

import static br.com.cereal.cerealsul.service.TransformaReaisService.transformar;

@Service(value = "pedidoService")
public class PedidoServiceImpl implements PedidoService {
    private static final int KG_POR_SACO = 60;
    private static final String VALOR_OBS_MOD = "NAO MODIFICADO";
    private static final String VALOR_STATUS = "LANCADO E AGUARDANDO DESBLOQUEIO";

    @Autowired
    private CompraService compraService;

    @Autowired
    private VendaService vendaService;

    @Autowired
    private PedidoDetalheService pedidoDetalheService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Override
    public Pedido analisarPedido(Pedido pedido) {
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente", "id", pedido.getCliente().getIdCliente()));
        Fornecedor fornecedor = fornecedorRepository.findById(pedido.getFornecedor().getIdFornecedor())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fornecedor", "id", pedido.getFornecedor().getIdFornecedor()));
        pedido.setCliente(cliente);
        pedido.setFornecedor(fornecedor);
        pedido.setQtSacos(transformar(pedido.getQtSacos() / KG_POR_SACO));
        pedido.setCompra(compraService.calcularAnaliseCompra(pedido));
        pedido.setVenda(vendaService.calcularAnaliseVenda(pedido));

        pedido.setValorLiq(transformar(pedido.getValorLiq()));
        pedido.setValorVenda(transformar(pedido.getValorVenda()));

        pedido.setMargemTotal(transformar(pedido.getValorVenda() - pedido.getVenda().getVendaCustoTotal()));
        pedido.setMargem(transformar((pedido.getMargemTotal() / pedido.getValorVenda()) * 100));

        System.out.println("********* analisar pedido finalizado");
        return pedido;
    }

    public Pedido salvarPedido(Pedido pedido) {
        Pedido pedidoSaved = pedidoRepository.save(tratarPedido(pedido));
        PedidoDetalhe pedidoDetalhe = pedidoDetalheService.salvarByPedido(pedido);
        pedidoSaved.setPedidoDetalhe(pedidoDetalhe);
        return pedidoSaved;
    }

    private Pedido tratarPedido(Pedido pedido) {
        pedido.setPeso(transformar(pedido.getQtSacos()/1000));
        pedido.setQtSacos(transformar(pedido.getQtSacos() / KG_POR_SACO));

        pedido.setCustosAdicionais((double) 0);
        pedido.setStatus(VALOR_STATUS);
        pedido.setObsMod(VALOR_OBS_MOD);
        pedido.setDataPedido(LocalDate.now());

        pedido.setCompra(getCompra(pedido));

        pedido.setVenda(getVenda(pedido));

        return pedido;
    }

    private Compra getCompra(Pedido pedido) {
        Compra compra = pedido.getCompra();
        Fornecedor fornecedor = pedido.getFornecedor();
        compra.setProdutorRazaoNome(fornecedor.getNomeFornecedor());
        compra.setProdutorCidade(fornecedor.getLocal());
        compra.setProdutorEstado(fornecedor.getRegiao());
        pedido.setValorLiqTotal(transformar(pedido.getValorLiq() * pedido.getQtSacos()));
        pedido.setFunrural(transformar(compra.getValorFunRural() + compra.getValorPat() + compra.getValorSenar()));
        pedido.setFunruralTotal(transformar(compra.getValorFunRural() * pedido.getQtSacos()));
        return compra;
    }

    private Venda getVenda(Pedido pedido) {
        Cliente cliente = pedido.getCliente();
        Venda venda = pedido.getVenda();
        venda.setTradingRazaoNome(cliente.getNomeCliente());
        venda.setTradingCidade(cliente.getLocal());
        venda.setTradingEstado(cliente.getRegiao());
        venda.setVendaValorReal(transformar(pedido.getValorVenda()));
        venda.setVendaValorRealTotal(transformar(pedido.getValorVenda() * pedido.getQtSacos()));
        return venda;
    }
}
