package br.com.cereal.cerealsul.controller;

import br.com.cereal.cerealsul.exception.ResourceNotFoundException;
import br.com.cereal.cerealsul.model.Cliente;
import br.com.cereal.cerealsul.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    ClienteRepository clienteRepository;

    @GetMapping()
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @PostMapping()
    public Cliente createCliente(@Valid @RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable(value = "id") Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", clienteId));
    }

    @PutMapping("/{id}")
    public Cliente updateCliente(@PathVariable(value = "id") Long clienteId,
                                 @Valid @RequestBody Cliente clienteDetails) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", clienteId));

        cliente.setNomeCliente(clienteDetails.getNomeCliente());
        cliente.setRua(clienteDetails.getRua());

        Cliente updatedCliente = clienteRepository.save(cliente);
        return updatedCliente;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable(value = "id") Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", clienteId));

        clienteRepository.delete(cliente);

        return ResponseEntity.ok().build();
    }
}