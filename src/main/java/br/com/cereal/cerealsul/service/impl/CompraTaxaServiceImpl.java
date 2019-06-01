package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.model.CompraTaxa;
import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.model.TipoPessoa;
import br.com.cereal.cerealsul.service.CompraTaxaService;
import br.com.cereal.cerealsul.service.IcmsCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "compraTaxaService")
public class CompraTaxaServiceImpl implements CompraTaxaService {
    @Autowired
    IcmsCompraService icmsCompraService;

    @Override
    public CompraTaxa calcularTaxas(Pedido pedido) {
        CompraTaxa compraTaxa = new CompraTaxa();
        compraTaxa.setTaxaIcmsProdutor(icmsCompraService.calculaIcmsCompra(pedido));
        TipoPessoa tipoPessoa = pedido.getFornecedor().getTipoPessoa();
        boolean possuiFunRural = pedido.getCompra().getFunrural();
        if (tipoPessoa.equals(TipoPessoa.PESSOA_FISICA)) {
            if(possuiFunRural) {
                compraTaxa.setTaxaFunRural(0.012);
            } else {
                compraTaxa.setTaxaFunRural(0.0);
            }
            compraTaxa.setTaxaSenar(0.002);
            compraTaxa.setTaxaPat(0.001);
        } else if (tipoPessoa.equals(TipoPessoa.PESSOA_JURIDICA)) {
            compraTaxa.setTaxaFunRural((double) 0);
            compraTaxa.setTaxaSenar((double) 0);
            compraTaxa.setTaxaPat((double) 0);
        }
        return compraTaxa;
    }
}
