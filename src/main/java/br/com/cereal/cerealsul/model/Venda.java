package br.com.cereal.cerealsul.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@NoArgsConstructor
@Data
@Embeddable
public class Venda {
    private String vendaCorret;
    private Double vendaCorretTotal;
    private String vendaCusto;
    private Double vendaCustoTotal;
    private String vendaFrete;
    private Double vendaFreteTotal;
    private String vendaImpostos;
    private Double vendaImpostosTotal;
    private Double vendaValorReal;
    private Double vendaValorRealTotal;

    private String tradingCidade;
    private String tradingEstado;
    private String tradingRazaoNome;

    @Transient
    private TipoAtividadeVenda tipoAtividadeVenda;
}