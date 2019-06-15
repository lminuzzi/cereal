package br.com.cereal.cerealsul.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@NoArgsConstructor
@Data
@Embeddable
public class Venda {
    private Double vendaCorret;
    private Double vendaCorretTotal;
    private Double vendaCusto;
    private Double vendaCustoTotal;
    private Double vendaFrete;
    private Double vendaFreteTotal;
    private Double vendaImpostos;
    private Double vendaImpostosTotal;
    private Double vendaValorReal;
    private Double vendaValorRealTotal;

    private String tpPedido;
    private String destGrao;

    private String tradingCidade;
    private String tradingEstado;
    private String tradingRazaoNome;

    @Transient
    private TipoAtividadeVenda tipoAtividadeVenda;
    @Transient
    private Boolean vendaPossuiFrete;
    @Transient
    private TipoPessoa vendaTipoFrete;
    @Transient
    private Boolean vendaPossuiCorretor;
    @Transient
    private Double vendaValorPisECofins;
    @Transient
    private Double vendaValorIcms;
    @Transient
    private String estadoCliente;
    @Transient
    private String localDestino;
}