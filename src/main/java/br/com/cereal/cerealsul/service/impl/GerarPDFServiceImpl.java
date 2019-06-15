package br.com.cereal.cerealsul.service.impl;

import br.com.cereal.cerealsul.model.*;
import br.com.cereal.cerealsul.service.GerarPDFService;
import br.com.cereal.cerealsul.service.TransformaReaisService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service("GerarPDFService")
public class GerarPDFServiceImpl implements GerarPDFService {
    private static final String PATH_INPUT = "src/main/resources/static/pdftemplates/pedido.html";
    private static final String PATH_OUTPUT = "src/output/htmlPedido_";

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
        final String pathOutput = getPathOutput(pedido.getNrSiscdb());
        File file = new File(pathOutput);
        if(!file.exists()) {
            criarArquivoPedido(pedido);
        }
        try {
            generatePDFFromHTML(pedido, pathOutput);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return pathOutput;
    }

    private String getPathOutput(long nrSiscdb) {
        return getPrefixPath(nrSiscdb) + ".pdf";
    }

    private String getPathInput(long nrSiscdb) {
        return getPrefixPath(nrSiscdb) + ".html";
    }

    private String getPrefixPath(long nrSiscdb) {
        return PATH_OUTPUT + nrSiscdb;
    }

    private void generatePDFFromHTML(Pedido pedido, String pathOutput)
            throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pathOutput));
        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new FileInputStream(getPathInput(pedido.getNrSiscdb())));
        document.close();
    }

    private void criarArquivoPedido(Pedido pedido) {
        final Pattern SUBST = Pattern.compile("\\*{5}\\w+\\*{5}");
        final Map<String, String> mapaValores = initMapaValores(pedido);
        final String pathInput = getPathInput(pedido.getNrSiscdb());
        try (Stream<String> stream = Files.lines(Paths.get(PATH_INPUT))) {
            Stream<String> newLines = stream.map(linha -> getLinhaFormatada(linha, mapaValores, SUBST));
            Files.write(Paths.get(pathInput), (Iterable<String>)newLines::iterator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLinhaFormatada(String linha, Map<String, String> mapaValores, Pattern SUBST) {
        Matcher matcher = SUBST.matcher(linha);
        if (matcher.find()) {
            String indexRegex = matcher.group(0);
            String index = indexRegex.replace("*", "");
            if (mapaValores.containsKey(index)) {
                return linha.replace(indexRegex, mapaValores.get(index));
            }
        }
        return linha;
    }

    private static Map<String, String> initMapaValores(Pedido pedido) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        Fornecedor fornecedor = pedido.getFornecedor();
        Compra compra = pedido.getCompra();
        PedidoDetalhe pedidoDetalhe = pedido.getPedidoDetalhe();
        Venda venda = pedido.getVenda();
        Cliente cliente = pedido.getCliente();
        Map<String, String> mapa = new HashMap<>();
        mapa.put(COD_PRODUTOR, pedido.getFornecedor().getIdFornecedor().toString());
        mapa.put(COMPRADOR, pedido.getNomeComprador());
        mapa.put(COD_TRADING, pedido.getCliente().getIdCliente().toString());
        mapa.put(PEDIDO_NRO, pedido.getNrSiscdb().toString());
        mapa.put(RAZAO_NOME_PRODUTOR, fornecedor.getNomeFornecedor());
        mapa.put(CIDADE_PRODUTOR, fornecedor.getLocal());
        mapa.put(ESTADO_PRODUTOR, compra.getProdutorEstado());
        mapa.put(BAIRRO_PRODUTOR, fornecedor.getBairro());
        mapa.put(CPF_CNPJ_PRODUTOR, fornecedor.getCpf() != null && !fornecedor.getCpf().equals("") ?
                fornecedor.getCpf() : fornecedor.getCnpj());
        mapa.put(INSC_EST_PRODUTOR, fornecedor.getInscEst());
        mapa.put(ENDERECO_PRODUTOR, fornecedor.getRua());
        mapa.put(PRAZO_ENTREGA_PRODUTOR, pedidoDetalhe.getPeriodoEntrega());
        mapa.put(LOCAL_EMBARQUE_PRODUTOR, pedidoDetalhe.getLocalEmbarque());
        mapa.put(FILIAL, pedidoDetalhe.getFilialCompra());
        mapa.put(DADOS_BANCARIOS_PRODUTOR, getDescricaoDadosBancarios(pedido.getPedidoDadoBancario()));
        mapa.put(OBSERVACOES_PRODUTOR, pedido.getObsMod());
        mapa.put(RAZAO_TRADING, venda.getTradingRazaoNome());
        mapa.put(CIDADE_TRADING, cliente.getLocal());
        mapa.put(ESTADO_TRADING, venda.getTradingEstado());
        mapa.put(BAIRRO_TRADING, cliente.getBairro());
        mapa.put(CPF_CNPJ_TRADING, cliente.getCpf() != null && !cliente.getCpf().equals("") ?
                cliente.getCpf() : cliente.getCnpj());
        mapa.put(INSC_EST_TRADING, cliente.getInscEst());
        mapa.put(DEST_GRAO_TRADING, venda.getDestGrao());
        mapa.put(LOCAL_DESTINO_TRADING, venda.getLocalDestino());
        mapa.put(QUANTIDADE_SACOS, pedido.getQtSacos().toString());
        mapa.put(SAFRA, compra.getSafra());
        mapa.put(PRODUTO, pedido.getProduto());
        mapa.put(TIPO_VENDA, venda.getTpPedido());
        mapa.put(PESO, pedido.getPeso().toString());
        mapa.put(EMPRESA, compra.getEmpresa());
        mapa.put(COMPRA_VALOR_LIQUIDO, "R$ " + pedido.getValorLiq().toString());
        mapa.put(COMPRA_FUNRURAL, "R$ " + compra.getValorFunRural().toString());
        mapa.put(COMPRA_IMPOSTOS, "R$ " + compra.getValorIcmsProdutor().toString());
        mapa.put(COMPRA_FRETE, "R$ " + compra.getCompraFrete().toString());
        mapa.put(COMPRA_CORRETAGEM, "R$ " + compra.getCompraCorret().toString());
        mapa.put(COMPRA_CUSTO_TOTAL, "R$ " + compra.getCompraCustoTotal().toString());
        mapa.put(VENDA_IMPOSTOS, "R$ " + venda.getVendaValorIcms().toString());
        mapa.put(VENDA_FRETE, "R$ " + venda.getVendaFrete().toString());
        mapa.put(VENDA_CORRETAGEM, "R$ " + venda.getVendaCorret().toString());
        mapa.put(VENDA_CUSTO_FINAL, "R$ " + venda.getVendaCustoTotal().toString());
        mapa.put(VENDA_VALOR, "R$ " + pedido.getValorVenda().toString());
        mapa.put(VENDA_MARGEM, pedido.getMargem().toString());
        mapa.put(TOTAL_IMPOSTOS_VENDA, "R$ " + venda.getVendaImpostosTotal().toString());
        mapa.put(TOTAL_FRETE, "R$ " + venda.getVendaFreteTotal().toString());
        mapa.put(TOTAL_CORRETAGEM, "R$ " + venda.getVendaCorretTotal().toString());
        mapa.put(TOTAL_CUSTO_FINAL, "R$ " + venda.getVendaCusto().toString());
        mapa.put(TOTAL_VALOR_VENDA_FINAL, "R$ " + venda.getVendaValorRealTotal().toString());
        mapa.put(TOTAL_MARGEM_BRUTA_TOTAL, pedido.getMargemTotal().toString());
        mapa.put(PAG_FOR_VALOR_TOTAL_LIQ, "R$ " + pedido.getValorLiq().toString());
        mapa.put(PAG_FOR_TODOS_IMPOSTOS, "R$ " + pedido.getFunruralTotal().toString());
        mapa.put(PAG_FOR_IMPOSTOS_COMPRA, "R$ " + compra.getValorIcmsProdutor().toString());
        mapa.put(PAG_FOR_FRETE, "R$ " + compra.getCompraFrete().toString());
        mapa.put(PAG_FOR_CORRETAGEM, "R$ " + compra.getCompraCorret().toString());
        mapa.put(PAG_FOR_CUSTO_TOTAL_COMPRA, "R$ " + compra.getCompraCustoTotal().toString());
        mapa.put(PAG_FOR_CUSTO_POR_SACO, "R$ " + TransformaReaisService.transformar(
                compra.getCompraCusto() / pedido.getQtSacos()));
        mapa.put(PAG_FOR_CUSTO_POR_KG, "R$ " + TransformaReaisService.transformar(
                compra.getCompraCusto() / pedido.getPeso()));
        mapa.put(SAP_JUROS_TOTAL_COM_JUROS, "R$ 0,00");
        mapa.put(SAP_JUROS_VALOR_POR_SACO, "R$ 0,00");
        mapa.put(SAP_JUROS_DIAS_JUROS, pedido.getDiasDeJuros());
        mapa.put(SAP_JUROS_JUROS_POR_SACO, "R$ 0,00");
        mapa.put(SAP_JUROS_JUROS_TOTAL, "R$ 0,00");
        mapa.put(SAP_JUROS_DATA_PONDERADA_VENDA, venda.getVendaDataPagamento().format(dateTimeFormatter));
        mapa.put(SAP_JUROS_PAGA_JUROS_PRODUTOR, "NÃO");
        mapa.put(INF_FIN_A_PAGAR_DATA, compra.getCompraDataPagamento().format(dateTimeFormatter));
        mapa.put(INF_FIN_A_PAGAR_VALOR, compra.getCompraCustoTotal().toString());
        mapa.put(INF_FIN_A_PAGAR_VALOR_TOTAL, compra.getCompraCustoTotal().toString());
        mapa.put(INF_FIN_A_RECEBER_DATA, venda.getVendaDataPagamento().format(dateTimeFormatter));
        mapa.put(INF_FIN_A_RECEBER_VALOR, venda.getVendaCustoTotal().toString());
        mapa.put(INF_FIN_A_RECEBER_VALOR_TOTAL, venda.getVendaCustoTotal().toString());
        mapa.put(ASSINATURA_PEDIDO, pedido.getNomeComprador());
        mapa.put(DATA_PEDIDO, pedido.getDataPedido().format(dateTimeFormatter));
        mapa.put(HORA_PEDIDO, "");
        return mapa;
    }

    private static String getDescricaoDadosBancarios(PedidoDadoBancario pedidoDadoBancario) {
        return  " CPF: " + pedidoDadoBancario.getCpfBanco() +
                " BCO: " + pedidoDadoBancario.getNomeBanco() +
                " AG: " + pedidoDadoBancario.getAgenciaBanco() +
                " CC: " + pedidoDadoBancario.getContaBanco() +
                " TITULAR: " + pedidoDadoBancario.getTitularBanco();
    }
}
