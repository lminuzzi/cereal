package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.Venda;
import br.com.cereal.cerealsul.service.FreteService;
import br.com.cereal.cerealsul.service.IcmsVendaService;
import br.com.cereal.cerealsul.service.PisCofinsService;
import br.com.cereal.cerealsul.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.cereal.cerealsul.service.TransformaReaisService.transformar;

@Service(value = "vendaService")
public class VendaServiceImpl implements VendaService {
    @Autowired
    FreteService freteService;

    @Autowired
    IcmsVendaService icmsVendaService;

    @Autowired
    PisCofinsService pisCofinsService;

    private Venda venda;

    @Override
    public Venda calcularAnaliseVenda(Pedido pedido) {
        venda = pedido.getVenda();
        venda.setVendaFreteTotal(freteService.calculaFreteVenda(venda, pedido.getQtSacos()));
        venda.setVendaValorPisECofins(pedido.getValorVenda() * pisCofinsService.calculaPisCofins(pedido));
        venda.setVendaValorIcms(icmsVendaService.calculaIcmsVenda(pedido));
        if (venda.getVendaPossuiCorretor()) {
            venda.setVendaCorretTotal(venda.getVendaCorret());
        } else {
            venda.setVendaCorretTotal((double) 0);
        }
        venda.setVendaCustoTotal(transformar(pedido.getCompra().getCompraCustoTotal() + venda.getVendaFreteTotal()
                + venda.getVendaCorretTotal() + venda.getVendaValorIcms() + venda.getVendaValorPisECofins()));
        //venda.setImpostoFreteVenda(venda.getImpostoFreteVenda());
        transformarValores();
        return venda;
    }

    private void transformarValores() {
        venda.setVendaFreteTotal(transformar(venda.getVendaFreteTotal()));
        venda.setVendaValorPisECofins(transformar(venda.getVendaValorPisECofins()));
        venda.setVendaValorIcms(transformar(venda.getVendaValorIcms()));
        venda.setVendaCorretTotal(transformar(venda.getVendaCorretTotal()));
        venda.setVendaCustoTotal(transformar(venda.getVendaCustoTotal()));
    }
}
