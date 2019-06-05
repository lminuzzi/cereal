package br.com.cereal.cerealsul.repository;

import br.com.cereal.cerealsul.model.PedidoDetalhe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoDetalheRepository extends JpaRepository<PedidoDetalhe, Long> {
}
