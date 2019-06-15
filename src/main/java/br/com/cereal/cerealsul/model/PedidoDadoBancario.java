package br.com.cereal.cerealsul.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pedidos_dados_bancarios")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Data
@JsonIgnoreProperties(value = { "pedido" }, allowSetters = true)
public class PedidoDadoBancario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nrSiscdb")
    @JsonProperty("pedido")
    protected Pedido pedido;

    private String titularBanco;
    private String cpfBanco;
    private String contaBanco;
    private String agenciaBanco;
    private String nomeBanco;
}
