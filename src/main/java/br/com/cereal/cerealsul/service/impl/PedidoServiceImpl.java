package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.exception.ResourceNotFoundException;
import br.com.cereal.cerealsul.model.Cliente;
import br.com.cereal.cerealsul.model.Fornecedor;
import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.repository.ClienteRepository;
import br.com.cereal.cerealsul.repository.FornecedorRepository;
import br.com.cereal.cerealsul.service.CompraService;
import br.com.cereal.cerealsul.service.PedidoService;
import br.com.cereal.cerealsul.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.cereal.cerealsul.service.TransformaReaisService.transformar;

@Service(value = "pedidoService")
public class PedidoServiceImpl implements PedidoService {
    private static final int KG_POR_SACO = 60;

    @Autowired
    private CompraService compraService;

    @Autowired
    private VendaService vendaService;

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

        pedido.setMargemTotal(transformar(pedido.getValorVenda() - pedido.getVenda().getVendaCustoTotal()));
        pedido.setMargem(transformar(pedido.getMargemTotal() / pedido.getValorVenda()));

        System.out.println("********* analisar pedido finalizado");
        return pedido;
    }
}
