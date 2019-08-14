package br.com.cereal.cerealsul.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pedidos_detalhes")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"pedido"}, allowSetters = true)
public class PedidoDetalhe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "nrSiscdb")
    @JsonProperty("pedido")
    protected Pedido pedido;

    private Double valorVenda;

    //Compra
    private TipoAtividadeCompra tipoAtividadeCompra;
    private Boolean possuiProRural;
    private Boolean compraPossuiFrete;
    private TipoPessoa compraTipoFrete;
    private Boolean compraPossuiCorretor;
    private Boolean funrural;
    private Double valorIcmsProdutor;
    private Double valorBrutoCompra;
    private Double valorFunRural;
    private Double valorSenar;
    private Double valorPat;
    private String periodoEntrega;
    private String localEmbarque;
    private String filialCompra;
    private LocalDate compraDataPagamento;
    private String compraObs;


    //Venda
    private TipoAtividadeVenda tipoAtividadeVenda;
    private Boolean vendaPossuiFrete;
    private TipoPessoa vendaTipoFrete;
    private Boolean vendaPossuiCorretor;
    private Double vendaValorPisCofins;
    private Double vendaValorIcms;
    private String estadoCliente;
    private String localDestino;
    private LocalDate vendaDataPagamento;
}
