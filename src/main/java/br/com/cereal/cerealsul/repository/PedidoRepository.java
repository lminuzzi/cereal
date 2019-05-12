package br.com.cereal.cerealsul.repository;

import br.com.cereal.cerealsul.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    //Optional<List<Pedido>> findByCodTrading(Long codTrading);
}
