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
    private Double compraCorret;
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
    @Transient
    private Boolean compraPossuiFrete;
    @Transient
    private Boolean compraPossuiCorretor;
    @Transient
    private Boolean funrural;
    @Transient
    private Double valorIcmsProdutor;
    @Transient
    private CompraTaxa compraTaxa;
    @Transient
    private Double valorBrutoCompra;
    @Transient
    private Double valorFunRural;
    @Transient
    private Double valorSenar;
    @Transient
    private Double valorPat;

}