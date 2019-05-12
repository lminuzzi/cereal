package br.com.cereal.cerealsul.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Embeddable
public class Compra implements Serializable {
    private String compraCorret;
    private Double compraCorretTotal;
    private Double compraCusto;
    private Double compraCustoTotal;
    private String compraFrete;
    private Double compraFreteTotal;
    private String compraImpostos;
    private Double compraImpostosTotal;
    private String nomeComprador;

    private String produtorCidade;
    private String produtorEstado;
    private String produtorRazaoNome;

    private String empresa;
    private String safra;

    @Transient
    private TipoAtividadeCompra tipoAtividadeCompra;
    @Transient
    private Boolean possuiProRural;
}