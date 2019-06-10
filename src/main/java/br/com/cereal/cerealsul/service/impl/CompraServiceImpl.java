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
        compra.setValorBrutoCompra(this.calcularValorBrutoDeCompra(
                compra.getCompraTaxa(), pedido.getValorLiq()));
        compra.setValorIcmsProdutor(this.calcularValorIcmsProdutor());
        compra.setValorFunRural(calcularValorFunrural());
        compra.setValorSenar(calcularValorSenar());
        compra.setValorPat(calcularValorPat());
        compra.setCompraFreteTotal(freteService.calculaFreteCompra(this.compra, pedido.getQtSacos()));
        if (compra.getCompraPossuiCorretor()) {
            compra.setCompraCorretTotal(compra.getCompraCorret() != null ? compra.getCompraCorret() : (double) 0);
        } else {
            compra.setCompraCorretTotal((double) 0);
        }
        compra.setCompraCustoTotal(transformar(compra.getValorBrutoCompra() +
                compra.getCompraFreteTotal() + compra.getCompraCorretTotal()));
        transformarValores();
        return this.compra;
    }

    private void transformarValores() {
        CompraTaxa compraTaxa = compra.getCompraTaxa();
        compraTaxa.setTaxaFunRural(transformar(compraTaxa.getTaxaFunRural()));
        compraTaxa.setTaxaPat(transformar(compraTaxa.getTaxaPat()));
        compraTaxa.setTaxaSenar(transformar(compraTaxa.getTaxaSenar()));
        compraTaxa.setTaxaIcmsProdutor(transformar(compraTaxa.getTaxaIcmsProdutor()));
        compra.setValorBrutoCompra(transformar(compra.getValorBrutoCompra()));
        compra.setValorIcmsProdutor(transformar(compra.getValorIcmsProdutor()));
        compra.setValorFunRural(transformar(compra.getValorFunRural()));
        compra.setValorSenar(transformar(compra.getValorSenar()));
        compra.setValorPat(transformar(compra.getValorPat()));
        compra.setCompraFreteTotal(transformar(compra.getCompraFreteTotal()));
        compra.setCompraCorretTotal(transformar(compra.getCompraCorretTotal()));
        compra.setCompraCustoTotal(transformar(compra.getCompraCustoTotal()));
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
