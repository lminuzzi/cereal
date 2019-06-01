package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.Compra;
import br.com.cereal.cerealsul.model.Pedido;

public interface CompraService {
    Compra calcularAnaliseCompra(Pedido pedido);
}
