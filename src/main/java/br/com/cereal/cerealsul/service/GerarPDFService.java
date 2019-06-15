package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.Pedido;

public interface GerarPDFService {
    String gerarPDF(Pedido pedido);
}
