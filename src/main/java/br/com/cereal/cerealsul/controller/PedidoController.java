package br.com.cereal.cerealsul.controller;

import br.com.cereal.cerealsul.exception.ResourceNotFoundException;
import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.repository.PedidoRepository;
import br.com.cereal.cerealsul.service.PedidoService;
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

    @Autowired
    PedidoService pedidoService;

    @GetMapping()
    public List<Pedido> getAllPedidoes() {
        return pedidoRepository.findAll();
    }

    @PostMapping()
    public Pedido createPedido(@Valid @RequestBody Pedido pedido) {
        return pedidoService.salvarPedido(pedido);
    }

    @PostMapping("analisar")
    public Pedido analisarPedido(@Valid @RequestBody Pedido pedido) {
        return pedidoService.analisarPedido(pedido);
    }

    @GetMapping("/{id}")
    public Pedido getPedidoById(@PathVariable(value = "id") Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePedido(@PathVariable(value = "id") Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));

        pedidoRepository.delete(pedido);

        return ResponseEntity.ok().build();
    }
}