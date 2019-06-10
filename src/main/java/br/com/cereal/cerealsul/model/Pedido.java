package br.com.cereal.cerealsul.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "pedidos")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Pedido implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nrSiscdb;
    private Long nrPedido;

    private Long codComprador;

    @ManyToOne
    @JoinColumn(name="COD_PRODUTOR")
    protected Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name="COD_TRADING")
    protected Cliente cliente;

    @OneToOne
    @JoinColumn(name="nrSiscdb")
    protected PedidoDadoBancario pedidoDadoBancario;

    @OneToOne
    @JoinColumn(name="nrSiscdb")
    protected PedidoDetalhe pedidoDetalhe;

    @Embedded
    private Compra compra;

    @Embedded
    private Venda venda;

    private LocalDate dataPedido;
    private String tpPedido;

    private Double custosAdicionais;

    private String destGrao;
    private String diasDeJuros;
    private Double funrural;
    private Double funruralTotal;
    private Double juros;
    private Double jurosTotal;
    private Double margem;
    private Double margemTotal;

    private String obsMod;
    private Double peso;
    private String produto;

    private Double qtSacos;
    private String status;

    private Double valorLiq;
    private Double valorLiqTotal;

    @Transient
    private Double valorVenda;
}