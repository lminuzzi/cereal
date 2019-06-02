package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.model.Compra;
import br.com.cereal.cerealsul.model.CompraTaxa;
import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.service.CompraService;
import br.com.cereal.cerealsul.service.CompraTaxaService;
import br.com.cereal.cerealsul.service.FreteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.cereal.cerealsul.service.TransformaReaisService.transformar;

@Service(value = "compraService")
public class CompraServiceImpl implements CompraService {
    @Autowired
    CompraTaxaService compraTaxaService;

    @Autowired
    FreteService freteService;

    private Compra compra;

    @Override
    public Compra calcularAnaliseCompra(Pedido pedido) {
        compra = pedido.getCompra();
        compra.setCompraTaxa(compraTaxaService.calcularTaxas(pedido));
        compra.setValorBrutoCompra(transformar(this.calcularValorBrutoDeCompra(
                compra.getCompraTaxa(), pedido.getValorLiq())));
        compra.setValorIcmsProdutor(transformar(this.calcularValorIcmsProdutor()));
        compra.setValorFunRural(transformar(calcularValorFunrural()));
        compra.setValorSenar(transformar(calcularValorSenar()));
        compra.setValorPat(transformar(calcularValorPat()));
        compra.setCompraFreteTotal(transformar(freteService.calculaFreteCompra(this.compra,
                pedido.getFornecedor(), pedido.getQtSacos())));
        compra.setCompraCorretTotal(transformar(compra.getCompraCorret()));
        compra.setCompraCustoTotal(transformar(compra.getValorBrutoCompra() +
                compra.getCompraFreteTotal() + compra.getCompraCorretTotal()));

        return this.compra;
    }

    private double calcularValorBrutoDeCompra(CompraTaxa compraTaxa, double valorLiq) {
        return valorLiq / (1 - compraTaxa.getTaxaFunRural() - compraTaxa.getTaxaSenar()
                - compraTaxa.getTaxaIcmsProdutor() - compraTaxa.getTaxaPat());
    }

    private double calcularValorIcmsProdutor() {
        return compra.getValorBrutoCompra() * compra.getCompraTaxa().getTaxaIcmsProdutor();
    }

    private double calcularValorFunrural() {
        return compra.getValorBrutoCompra() * compra.getCompraTaxa().getTaxaFunRural();
    }

    private double calcularValorSenar() {
        return compra.getValorBrutoCompra() * compra.getCompraTaxa().getTaxaSenar();
    }

    private double calcularValorPat() {
        return compra.getValorBrutoCompra() * compra.getCompraTaxa().getTaxaPat();
    }
}
