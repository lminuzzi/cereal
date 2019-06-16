package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.exception.ResourceNotFoundException;
import br.com.cereal.cerealsul.model.*;
import br.com.cereal.cerealsul.repository.ClienteRepository;
import br.com.cereal.cerealsul.repository.FornecedorRepository;
import br.com.cereal.cerealsul.repository.PedidoRepository;
import br.com.cereal.cerealsul.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
    private PedidoDadoBancarioService pedidoDadoBancarioService;

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
        pedido.setPeso(transformar(pedido.getQtSacos()));
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
        Pedido pedidoTratado = tratarPedido(this.analisarPedido(pedido));

        PedidoDetalhe pedidoDetalhe = pedidoDetalheService.getByPedido(pedidoTratado);
        PedidoDadoBancario pedidoDadoBancario = pedidoDadoBancarioService.getByPedido(pedidoTratado);
        pedidoTratado.setPedidoDadoBancario(null);
        pedidoTratado.setPedidoDetalhe(null);
        Pedido pedidoSaved = pedidoRepository.save(pedidoTratado);
        pedidoDetalhe.setPedido(pedidoSaved);
        PedidoDetalhe pedidoDetalheSaved = pedidoDetalheService.salvar(pedidoDetalhe);
        pedidoSaved.setPedidoDetalhe(pedidoDetalheSaved);
        pedidoDadoBancario.setPedido(pedidoSaved);
        PedidoDadoBancario pedidoDadoBancarioSaved = pedidoDadoBancarioService.salvar(pedidoDadoBancario);
        pedidoSaved.setPedidoDadoBancario(pedidoDadoBancarioSaved);
        gerarPDFPedido(pedidoSaved);
        return pedidoSaved;
    }

    private void gerarPDFPedido(Pedido pedido) {
        Pedido pedidoSaved = pedidoRepository.findById(pedido.getNrSiscdb())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido", "id", pedido.getNrSiscdb()));
        GerarPDFService.gerarPDF(pedidoSaved);
    }

    private Pedido tratarPedido(Pedido pedido) {
        pedido.setPeso(transformar(pedido.getPeso()));
        pedido.setQtSacos(transformar(pedido.getQtSacos()));
        pedido.setCustosAdicionais((double) 0);
        pedido.setJuros((double) 0);
        pedido.setJurosTotal((double) 0);
        pedido.setNrPedido((long) 0);
        pedido.setDiasDeJuros("0");
        pedido.setStatus(VALOR_STATUS);
        pedido.setObsMod(VALOR_OBS_MOD);
        pedido.setDataPedido(LocalDate.now());

        pedido.setCompra(getCompra(pedido));
        pedido.setVenda(getVenda(pedido));

        pedido.setValorLiqTotal(transformar(pedido.getValorLiq() * pedido.getQtSacos()));
        pedido.setFunrural(transformar(pedido.getCompra().getValorFunRural() +
                pedido.getCompra().getValorPat() + pedido.getCompra().getValorSenar()));
        pedido.setFunruralTotal(transformar(pedido.getCompra().getValorFunRural() * pedido.getQtSacos()));

        return pedido;
    }

    private Compra getCompra(Pedido pedido) {
        Compra compra = pedido.getCompra();
        Fornecedor fornecedor = pedido.getFornecedor();
        compra.setProdutorRazaoNome(fornecedor.getNomeFornecedor());
        compra.setProdutorCidade(fornecedor.getLocal());
        compra.setProdutorEstado(fornecedor.getRegiao());
        compra.setCompraImpostos(compra.getValorIcmsProdutor());
        compra.setCompraImpostosTotal(transformar(compra.getValorIcmsProdutor() * pedido.getQtSacos()));
        compra.setCompraCusto(pedido.getValorLiq());
        compra.setCompraCustoTotal(transformar(compra.getCompraCusto() * pedido.getQtSacos()));
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
        venda.setVendaImpostos(venda.getVendaValorIcms());
        venda.setVendaImpostosTotal(transformar(venda.getVendaValorIcms() * pedido.getQtSacos()));
        venda.setVendaCusto(pedido.getValorVenda());
        venda.setVendaCustoTotal(transformar(venda.getVendaCusto() * pedido.getQtSacos()));
        return venda;
    }
}
