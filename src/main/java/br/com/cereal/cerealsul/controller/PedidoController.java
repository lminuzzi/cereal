package br.com.cereal.cerealsul.controller;

import br.com.cereal.cerealsul.exception.ResourceNotFoundException;
import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    PedidoRepository pedidoRepository;

    @GetMapping()
    public List<Pedido> getAllPedidoes() {
        return pedidoRepository.findAll();
    }

    @PostMapping()
    public Pedido createCliente(@Valid @RequestBody Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @GetMapping("/{id}")
    public Pedido getPedidoById(@PathVariable(value = "id") Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));
    }

    /*
    @GetMapping("/{codTrading}")
    public List<Pedido> getPedidosByCodTrading(@PathVariable(value = "codTrading") Long codTrading) {
        return pedidoRepository.findByCodTrading(codTrading)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "codTrading", codTrading));
    }
    */

    @PutMapping("/{id}")
    public Pedido updatePedido(@PathVariable(value = "id") Long pedidoId,
                                       @Valid @RequestBody Pedido pedidoDetails) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));

        pedido.setObsMod(pedidoDetails.getObsMod());

        Pedido updatedPedido = pedidoRepository.save(pedido);
        return updatedPedido;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePedido(@PathVariable(value = "id") Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));

        pedidoRepository.delete(pedido);

        return ResponseEntity.ok().build();
    }
}