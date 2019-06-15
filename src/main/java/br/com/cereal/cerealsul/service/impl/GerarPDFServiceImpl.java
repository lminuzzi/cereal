package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.model.Pedido;
import br.com.cereal.cerealsul.service.GerarPDFService;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@Service("GerarPDFService")
public class GerarPDFServiceImpl implements GerarPDFService {
    private static final String PATH_INPUT = "src/main/resources/static/pdftemplatespedido.html";
    private static final String PATH_OUTPUT = "src/output/htmlPedido.pdf";

    private static final String COD_PRODUTOR = "COD_PRODUTOR";
    private static final String COMPRADOR = "COMPRADOR";
    private static final String COD_TRADING = "COD_TRADING";
    private static final String PEDIDO_NRO = "PEDIDO_NRO";
    private static final String RAZAO_NOME_PRODUTOR = "RAZAO_NOME_PRODUTOR";
    private static final String CIDADE_PRODUTOR = "CIDADE_PRODUTOR";
    private static final String ESTADO_PRODUTOR = "ESTADO_PRODUTOR";
    private static final String BAIRRO_PRODUTOR = "BAIRRO_PRODUTOR";
    private static final String CPF_CNPJ_PRODUTOR = "CPF_CNPJ_PRODUTOR";
    private static final String INSC_EST_PRODUTOR = "INSC_EST_PRODUTOR";
    private static final String ENDERECO_PRODUTOR = "ENDERECO_PRODUTOR";
    private static final String PRAZO_ENTREGA_PRODUTOR = "PRAZO_ENTREGA_PRODUTOR";
    private static final String LOCAL_EMBARQUE_PRODUTOR = "LOCAL_EMBARQUE_PRODUTOR";
    private static final String FILIAL = "FILIAL";
    private static final String DADOS_BANCARIOS_PRODUTOR = "DADOS_BANCARIOS_PRODUTOR";
    private static final String OBSERVACOES_PRODUTOR = "OBSERVACOES_PRODUTOR";
    private static final String RAZAO_TRADING = "RAZAO_TRADING";
    private static final String CIDADE_TRADING = "CIDADE_TRADING";
    private static final String ESTADO_TRADING = "ESTADO_TRADING";
    private static final String BAIRRO_TRADING = "BAIRRO_TRADING";
    private static final String CPF_CNPJ_TRADING = "CPF_CNPJ_TRADING";
    private static final String INSC_EST_TRADING = "INSC_EST_TRADING";
    private static final String DEST_GRAO_TRADING = "DEST_GRAO_TRADING";
    private static final String LOCAL_DESTINO_TRADING = "LOCAL_DESTINO_TRADING";
    private static final String QUANTIDADE_SACOS = "QUANTIDADE_SACOS";
    private static final String SAFRA = "SAFRA";
    private static final String PRODUTO = "PRODUTO";
    private static final String TIPO_VENDA = "TIPO_VENDA";
    private static final String PESO = "PESO";
    private static final String EMPRESA = "EMPRESA";
    private static final String COMPRA_VALOR_LIQUIDO = "COMPRA_VALOR_LIQUIDO";
    private static final String COMPRA_FUNRURAL = "COMPRA_FUNRURAL";
    private static final String COMPRA_IMPOSTOS = "COMPRA_IMPOSTOS";
    private static final String COMPRA_FRETE = "COMPRA_FRETE";
    private static final String COMPRA_CORRETAGEM = "COMPRA_CORRETAGEM";
    private static final String COMPRA_CUSTO_TOTAL = "COMPRA_CUSTO_TOTAL";
    private static final String VENDA_IMPOSTOS = "VENDA_IMPOSTOS";
    private static final String VENDA_FRETE = "VENDA_FRETE";
    private static final String VENDA_CORRETAGEM = "VENDA_CORRETAGEM";
    private static final String VENDA_CUSTO_FINAL = "VENDA_CUSTO_FINAL";
    private static final String VENDA_VALOR = "VENDA_VALOR";
    private static final String VENDA_MARGEM = "VENDA_MARGEM";
    private static final String TOTAL_IMPOSTOS_VENDA = "TOTAL_IMPOSTOS_VENDA";
    private static final String TOTAL_FRETE = "TOTAL_FRETE";
    private static final String TOTAL_CORRETAGEM = "TOTAL_CORRETAGEM";
    private static final String TOTAL_CUSTO_FINAL = "TOTAL_CUSTO_FINAL";
    private static final String TOTAL_VALOR_VENDA_FINAL = "TOTAL_VALOR_VENDA_FINAL";
    private static final String TOTAL_MARGEM_BRUTA_TOTAL = "TOTAL_MARGEM_BRUTA_TOTAL";
    private static final String PAG_FOR_VALOR_TOTAL_LIQ = "PAG_FOR_VALOR_TOTAL_LIQ";
    private static final String PAG_FOR_TODOS_IMPOSTOS = "PAG_FOR_TODOS_IMPOSTOS";
    private static final String PAG_FOR_IMPOSTOS_COMPRA = "PAG_FOR_IMPOSTOS_COMPRA";
    private static final String PAG_FOR_FRETE = "PAG_FOR_FRETE";
    private static final String PAG_FOR_CORRETAGEM = "PAG_FOR_CORRETAGEM";
    private static final String PAG_FOR_CUSTO_TOTAL_COMPRA = "PAG_FOR_CUSTO_TOTAL_COMPRA";
    private static final String PAG_FOR_CUSTO_POR_SACO = "PAG_FOR_CUSTO_POR_SACO";
    private static final String PAG_FOR_CUSTO_POR_KG = "PAG_FOR_CUSTO_POR_KG";
    private static final String SAP_JUROS_TOTAL_COM_JUROS = "SAP_JUROS_TOTAL_COM_JUROS";
    private static final String SAP_JUROS_VALOR_POR_SACO = "SAP_JUROS_VALOR_POR_SACO";
    private static final String SAP_JUROS_DIAS_JUROS = "SAP_JUROS_DIAS_JUROS";
    private static final String SAP_JUROS_JUROS_POR_SACO = "SAP_JUROS_JUROS_POR_SACO";
    private static final String SAP_JUROS_JUROS_TOTAL = "SAP_JUROS_JUROS_TOTAL";
    private static final String SAP_JUROS_DATA_PONDERADA_VENDA = "SAP_JUROS_DATA_PONDERADA_VENDA";
    private static final String SAP_JUROS_PAGA_JUROS_PRODUTOR = "SAP_JUROS_PAGA_JUROS_PRODUTOR";
    private static final String INF_FIN_A_PAGAR_DATA = "INF_FIN_A_PAGAR_DATA";
    private static final String INF_FIN_A_PAGAR_VALOR = "INF_FIN_A_PAGAR_VALOR";
    private static final String INF_FIN_A_PAGAR_VALOR_TOTAL = "INF_FIN_A_PAGAR_VALOR_TOTAL";
    private static final String INF_FIN_A_RECEBER_DATA = "INF_FIN_A_RECEBER_DATA";
    private static final String INF_FIN_A_RECEBER_VALOR = "INF_FIN_A_RECEBER_VALOR";
    private static final String INF_FIN_A_RECEBER_VALOR_TOTAL = "INF_FIN_A_RECEBER_VALOR_TOTAL";
    private static final String ASSINATURA_PEDIDO = "ASSINATURA_PEDIDO";
    private static final String DATA_PEDIDO = "DATA_PEDIDO";
    private static final String HORA_PEDIDO = "HORA_PEDIDO";

    @Override
    public String gerarPDF(Pedido pedido) {
        try {
            generatePDFFromHTML(PATH_INPUT, pedido);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return PATH_OUTPUT;
    }

    private static void generatePDFFromHTML(String filename, Pedido pedido)
            throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PATH_OUTPUT));
        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, getEntrada(filename, pedido));
        document.close();
    }

    private static FileInputStream getEntrada(String filename, Pedido pedido) throws FileNotFoundException {
        //TODO editar arquivo antes do fileInput
        FileInputStream fileInputStream = new FileInputStream(filename);
        return fileInputStream;
    }
}
