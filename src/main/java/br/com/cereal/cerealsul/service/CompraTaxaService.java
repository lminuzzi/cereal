package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.CompraTaxa;
import br.com.cereal.cerealsul.model.Pedido;

public interface CompraTaxaService {
    CompraTaxa calcularTaxas(Pedido pedido);
}
