package br.com.cereal.cerealsul.repository;

import br.com.cereal.cerealsul.model.PedidoDadoBancario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoDadoBancarioRepository extends JpaRepository<PedidoDadoBancario, Long> {
}
