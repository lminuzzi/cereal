package br.com.cereal.cerealsul.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "pedidos_detalhes")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class PedidoDetalhe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="nrSiscdb")
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

    //Venda
    private TipoAtividadeVenda tipoAtividadeVenda;
    private Boolean vendaPossuiFrete;
    private TipoPessoa vendaTipoFrete;
    private Boolean vendaPossuiCorretor;
    private Double vendaValorPisCofins;
    private Double vendaValorIcms;
    private String estadoCliente;
    private String localDestino;
}
