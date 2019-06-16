package br.com.cereal.cerealsul.controller;

import br.com.cereal.cerealsul.exception.ResourceNotFoundException;
import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.repository.PedidoRepository;
import br.com.cereal.cerealsul.service.GerarPDFService;
import br.com.cereal.cerealsul.service.PedidoService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoService pedidoService;

    @GetMapping()
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    @GetMapping("pdf/{id}")
    public void getPdfPedidos(@PathVariable(value = "id") Long pedidoId, HttpServletResponse response) {
        String fileName = GerarPDFService.getPathOutput(pedidoId);
        Path file = Paths.get(fileName);
        if (Files.exists(file)) {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @GetMapping("pdfcontrato/{id}")
    public void getPdfContratos(@PathVariable(value = "id") Long pedidoId, HttpServletResponse response) {
        String fileName = GerarPDFService.getPathOutputContrato(pedidoId);
        Path file = Paths.get(fileName);
        if (Files.exists(file)) {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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