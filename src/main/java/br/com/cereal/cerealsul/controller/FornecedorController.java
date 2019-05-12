package br.com.cereal.cerealsul.controller;

import br.com.cereal.cerealsul.exception.ResourceNotFoundException;
import br.com.cereal.cerealsul.model.Fornecedor;
import br.com.cereal.cerealsul.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {
    @Autowired
    FornecedorRepository fornecedorRepository;

    @GetMapping()
    public List<Fornecedor> getAllFornecedores() {
        return fornecedorRepository.findAll();
    }

    @PostMapping()
    public Fornecedor createCliente(@Valid @RequestBody Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    @GetMapping("/{id}")
    public Fornecedor getFornecedorById(@PathVariable(value = "id") Long fornecedorId) {
        return fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", "id", fornecedorId));
    }

    @PutMapping("/{id}")
    public Fornecedor updateFornecedor(@PathVariable(value = "id") Long fornecedorId,
                                       @Valid @RequestBody Fornecedor fornecedorDetails) {

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", "id", fornecedorId));

        fornecedor.setNomeFornecedor(fornecedorDetails.getNomeFornecedor());
        fornecedor.setRua(fornecedorDetails.getRua());

        Fornecedor updatedFornecedor = fornecedorRepository.save(fornecedor);
        return updatedFornecedor;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFornecedor(@PathVariable(value = "id") Long fornecedorId) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", "id", fornecedorId));

        fornecedorRepository.delete(fornecedor);

        return ResponseEntity.ok().build();
    }
}