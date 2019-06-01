package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.Venda;
import br.com.cereal.cerealsul.service.FreteService;
import br.com.cereal.cerealsul.service.IcmsVendaService;
import br.com.cereal.cerealsul.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "vendaService")
public class VendaServiceImpl implements VendaService {
    @Autowired
    FreteService freteService;

    @Autowired
    IcmsVendaService icmsVendaService;

    private Venda venda;

    @Override
    public Venda calcularAnaliseVenda(Pedido pedido) {
        Venda venda = pedido.getVenda();
        return venda;
    }
}
