package br.com.cereal.cerealsul.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pedidos_dados_bancarios")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class PedidoDadoBancario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "nrSiscdb")
    protected Pedido pedido;

    private String titularBanco;
    private String cpfBanco;
    private String contaBanco;
    private String agenciaBanco;
    private String nomeBanco;
}
