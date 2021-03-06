package br.com.cereal.cerealsul.controller;

import br.com.cereal.cerealsul.exception.ResourceNotFoundException;
import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.repository.PedidoRepository;
import br.com.cereal.cerealsul.service.GerarPDFService;
import br.com.cereal.cerealsul.service.PedidoService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
        getPDF(response, GerarPDFService.getPathOutput(pedidoId));
    }

    @GetMapping("pdfcontrato/{id}")
    public void getPdfContratos(@PathVariable(value = "id") Long pedidoId, HttpServletResponse response) {
        getPDF(response, GerarPDFService.getPathOutputContrato(pedidoId));
    }

    @PostMapping("pdfcontrato")
    public void getTest(@RequestBody String caminho, HttpServletResponse response) {
        try {
            getPDF(response, URLDecoder.decode(caminho.replace("caminho=", ""), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("listaarquivos")
    public String getLista(@RequestBody String caminho, HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();
        try {
            Files.list(Paths.get(URLDecoder.decode(caminho.replace("caminho=", ""),
                    "UTF-8"))).forEach(s -> sb.append(s.toString() + " - "));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @PostMapping("criapdf")
    public void createPDF(@RequestBody String caminhoParam, HttpServletResponse response) {
        try {
            String caminho = URLDecoder.decode(caminhoParam.replace(
                    "caminhoParam=", ""),"UTF-8");
            System.out.println("**** createPDF - caminho = " + caminho);
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(caminho));
            System.out.println("**** createPDF - PdfWriter criado");
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new FileInputStream("teste.pdf"));
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
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

    private void getPDF(HttpServletResponse response, String pathOutputContrato) {
        Path file = Paths.get(pathOutputContrato);
        if (Files.exists(file)) {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + pathOutputContrato);
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
