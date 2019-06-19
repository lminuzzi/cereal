package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.*;
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
public class GerarPDFService {

    private static final String BASE_INPUT = "src/main/resources/static/pdftemplates/";
    private static final String BASE_OUTPUT = "src/main/resources/static/output/";
    private static final String PATH_INPUT = BASE_INPUT + "pedido.html";
    private static final String PATH_OUTPUT = BASE_OUTPUT + "htmlPedido_";
    private static final String PATH_INPUT_CONTRATO = BASE_INPUT + "Contrato_pedido.html";
    private static final String PATH_OUTPUT_CONTRATO = BASE_OUTPUT + "Contratohtml_";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/YYYY");

    public static void gerarPDF(Pedido pedido) {
        final String pathOutput = getPathOutput(pedido.getNrSiscdb());
        File file = new File(pathOutput);
        if (!file.exists()) {
            criarArquivoPedido(pedido);
        }
        try {
            generatePDFFromHTML(pathOutput, getPathInput(pedido.getNrSiscdb()));
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public static String getPathOutput(long nrSiscdb) {
        return getPrefixPath(nrSiscdb) + ".pdf";
    }

    private static String getPathInput(long nrSiscdb) {
        return getPrefixPath(nrSiscdb) + ".html";
    }

    private static String getPrefixPath(long nrSiscdb) {
        return PATH_OUTPUT + nrSiscdb;
    }

    private static void generatePDFFromHTML(String pathOutput, String pathInput)
            throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pathOutput));
        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new FileInputStream(pathInput));
        document.close();
    }

    private static void criarArquivoPedido(Pedido pedido) {
        final Pattern SUBST = Pattern.compile("\\*{5}\\w+\\*{5}");
        final Map<String, String> mapaValores = initMapaValores(pedido);
        final String pathInput = getPathInput(pedido.getNrSiscdb());
        try (Stream<String> stream = Files.lines(Paths.get(PATH_INPUT))) {
            Stream<String> newLines = stream.map(linha -> getLinhaFormatada(linha, mapaValores, SUBST));
            Files.write(Paths.get(pathInput), (Iterable<String>) newLines::iterator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLinhaFormatada(String linha, Map<String, String> mapaValores, Pattern SUBST) {
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
        Fornecedor fornecedor = pedido.getFornecedor();
        Compra compra = pedido.getCompra();
        PedidoDetalhe pedidoDetalhe = pedido.getPedidoDetalhe();
        Venda venda = pedido.getVenda();
        Cliente cliente = pedido.getCliente();
        Map<String, String> mapa = new HashMap<>();
        mapa.put("COD_PRODUTOR", pedido.getFornecedor().getIdFornecedor().toString());
        mapa.put("COMPRADOR", pedido.getNomeComprador());
        mapa.put("COD_TRADING", cliente.getIdCliente().toString());
        mapa.put("PEDIDO_NRO", pedido.getNrSiscdb().toString());
        mapa.put("RAZAO_NOME_PRODUTOR", fornecedor.getNomeFornecedor());
        mapa.put("CIDADE_PRODUTOR", fornecedor.getLocal());
        mapa.put("ESTADO_PRODUTOR", compra.getProdutorEstado());
        mapa.put("BAIRRO_PRODUTOR", fornecedor.getBairro());
        mapa.put("CPF_CNPJ_PRODUTOR", fornecedor.getCpf() != null && !fornecedor.getCpf().equals("") ?
                fornecedor.getCpf() : fornecedor.getCnpj());
        mapa.put("INSC_EST_PRODUTOR", fornecedor.getInscEst());
        mapa.put("ENDERECO_PRODUTOR", fornecedor.getRua());
        mapa.put("PRAZO_ENTREGA_PRODUTOR", pedidoDetalhe.getPeriodoEntrega());
        mapa.put("LOCAL_EMBARQUE_PRODUTOR", pedidoDetalhe.getLocalEmbarque());
        mapa.put("FILIAL", pedidoDetalhe.getFilialCompra());
        mapa.put("DADOS_BANCARIOS_PRODUTOR", getDescricaoDadosBancarios(pedido.getPedidoDadoBancario()));
        mapa.put("OBSERVACOES_PRODUTOR", pedido.getObsMod());
        mapa.put("RAZAO_TRADING", venda.getTradingRazaoNome());
        mapa.put("CIDADE_TRADING", cliente.getLocal());
        mapa.put("ESTADO_TRADING", venda.getTradingEstado());
        mapa.put("BAIRRO_TRADING", cliente.getBairro());
        mapa.put("CPF_CNPJ_TRADING", cliente.getCpf() != null && !cliente.getCpf().equals("") ?
                cliente.getCpf() : cliente.getCnpj());
        mapa.put("INSC_EST_TRADING", cliente.getInscEst());
        mapa.put("DEST_GRAO_TRADING", venda.getDestGrao());
        mapa.put("LOCAL_DESTINO_TRADING", pedidoDetalhe.getLocalDestino());
        mapa.put("QUANTIDADE_SACOS", pedido.getQtSacos().toString());
        mapa.put("SAFRA", compra.getSafra());
        mapa.put("PRODUTO", pedido.getProduto());
        mapa.put("TIPO_VENDA", venda.getTpPedido());
        mapa.put("PESO", pedido.getPeso().toString());
        mapa.put("EMPRESA", compra.getEmpresa());
        mapa.put("COMPRA_VALOR_LIQUIDO", "R$ " + pedido.getValorLiq().toString());
        mapa.put("COMPRA_FUNRURAL", "R$ " + compra.getValorFunRural().toString());
        mapa.put("COMPRA_IMPOSTOS", "R$ " + pedidoDetalhe.getValorIcmsProdutor().toString());
        mapa.put("COMPRA_FRETE", "R$ " + (pedidoDetalhe.getCompraPossuiFrete() ? compra.getCompraFrete().toString() : 0));
        mapa.put("COMPRA_CORRETAGEM", "R$ " + (pedidoDetalhe.getCompraPossuiCorretor() ?
                compra.getCompraCorret().toString() : 0));
        mapa.put("COMPRA_CUSTO_TOTAL", "R$ " + compra.getCompraCustoTotal().toString());
        mapa.put("VENDA_IMPOSTOS", "R$ " + pedidoDetalhe.getVendaValorIcms().toString());
        mapa.put("VENDA_FRETE", "R$ " + (pedidoDetalhe.getVendaPossuiFrete() ? venda.getVendaFrete().toString() : 0));
        mapa.put("VENDA_CORRETAGEM", "R$ " + (pedidoDetalhe.getVendaPossuiCorretor() ?
                venda.getVendaCorret().toString() : 0));
        mapa.put("VENDA_CUSTO_FINAL", "R$ " + venda.getVendaCustoTotal().toString());
        mapa.put("VENDA_VALOR", "R$ " + pedidoDetalhe.getValorVenda().toString());
        mapa.put("VENDA_MARGEM", pedido.getMargem().toString());
        mapa.put("TOTAL_IMPOSTOS_VENDA", "R$ " + venda.getVendaImpostosTotal().toString());
        mapa.put("TOTAL_FRETE", "R$ " + venda.getVendaFreteTotal().toString());
        mapa.put("TOTAL_CORRETAGEM", "R$ " + venda.getVendaCorretTotal().toString());
        mapa.put("TOTAL_CUSTO_FINAL", "R$ " + venda.getVendaCusto().toString());
        mapa.put("TOTAL_VALOR_VENDA_FINAL", "R$ " + venda.getVendaValorRealTotal().toString());
        mapa.put("TOTAL_MARGEM_BRUTA_TOTAL", pedido.getMargemTotal().toString());
        mapa.put("PAG_FOR_VALOR_TOTAL_LIQ", "R$ " + pedido.getValorLiq().toString());
        mapa.put("PAG_FOR_TODOS_IMPOSTOS", "R$ " + pedido.getFunruralTotal().toString());
        mapa.put("PAG_FOR_IMPOSTOS_COMPRA", "R$ " + compra.getValorIcmsProdutor().toString());
        mapa.put("PAG_FOR_FRETE", "R$ " + (pedidoDetalhe.getCompraPossuiFrete() ? compra.getCompraFrete().toString() : 0));
        mapa.put("PAG_FOR_CORRETAGEM", "R$ " + (pedidoDetalhe.getCompraPossuiCorretor() ?
                compra.getCompraCorret().toString() : 0));
        mapa.put("PAG_FOR_CUSTO_TOTAL_COMPRA", "R$ " + compra.getCompraCustoTotal().toString());
        mapa.put("PAG_FOR_CUSTO_POR_SACO", "R$ " + TransformaReaisService.transformar(
                compra.getCompraCusto() / pedido.getQtSacos()));
        mapa.put("PAG_FOR_CUSTO_POR_KG", "R$ " + TransformaReaisService.transformar(
                compra.getCompraCusto() / pedido.getPeso()));
        mapa.put("SAP_JUROS_TOTAL_COM_JUROS", "R$ 0,00");
        mapa.put("SAP_JUROS_VALOR_POR_SACO", "R$ 0,00");
        mapa.put("SAP_JUROS_DIAS_JUROS", pedido.getDiasDeJuros());
        mapa.put("SAP_JUROS_JUROS_POR_SACO", "R$ 0,00");
        mapa.put("SAP_JUROS_JUROS_TOTAL", "R$ 0,00");
        mapa.put("SAP_JUROS_DATA_PONDERADA_VENDA", venda.getVendaDataPagamento().format(DATE_TIME_FORMATTER));
        mapa.put("SAP_JUROS_PAGA_JUROS_PRODUTOR", "NÃO");
        mapa.put("INF_FIN_A_PAGAR_DATA", pedidoDetalhe.getCompraDataPagamento().format(DATE_TIME_FORMATTER));
        mapa.put("INF_FIN_A_PAGAR_VALOR", compra.getCompraCustoTotal().toString());
        mapa.put("INF_FIN_A_PAGAR_VALOR_TOTAL", compra.getCompraCustoTotal().toString());
        mapa.put("INF_FIN_A_RECEBER_DATA", pedidoDetalhe.getVendaDataPagamento().format(DATE_TIME_FORMATTER));
        mapa.put("INF_FIN_A_RECEBER_VALOR", venda.getVendaCustoTotal().toString());
        mapa.put("INF_FIN_A_RECEBER_VALOR_TOTAL", venda.getVendaCustoTotal().toString());
        mapa.put("ASSINATURA_PEDIDO", pedido.getNomeComprador());
        mapa.put("DATA_PEDIDO", pedido.getDataPedido().format(DATE_TIME_FORMATTER));
        mapa.put("HORA_PEDIDO", "");
        return mapa;
    }

    private static String getDescricaoDadosBancarios(PedidoDadoBancario pedidoDadoBancario) {
        return " CPF: " + pedidoDadoBancario.getCpfBanco() +
                " BCO: " + pedidoDadoBancario.getNomeBanco() +
                " AG: " + pedidoDadoBancario.getAgenciaBanco() +
                " CC: " + pedidoDadoBancario.getContaBanco() +
                " TITULAR: " + pedidoDadoBancario.getTitularBanco();
    }

    public static void gerarPDFContrato(Pedido pedido) {
        final String pathOutput = getPathOutputContrato(pedido.getNrSiscdb());
        File file = new File(pathOutput);
        if (!file.exists()) {
            criarArquivoContrato(pedido);
        }
        try {
            generatePDFFromHTML(pathOutput, getPathInputContrato(pedido.getNrSiscdb()));
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private static void criarArquivoContrato(Pedido pedido) {
        final Pattern SUBST = Pattern.compile("\\*{5}\\w+\\*{5}");
        final Map<String, String> mapaValores = initMapaValoresContrato(pedido);
        final String pathInput = getPathInputContrato(pedido.getNrSiscdb());
        try (Stream<String> stream = Files.lines(Paths.get(PATH_INPUT_CONTRATO))) {
            Stream<String> newLines = stream.map(linha -> getLinhaFormatada(linha, mapaValores, SUBST));
            Files.write(Paths.get(pathInput), (Iterable<String>) newLines::iterator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> initMapaValoresContrato(Pedido pedido) {
        Map<String, String> mapa = new HashMap<>();

        Compra compra = pedido.getCompra();
        Venda venda = pedido.getVenda();
        Cliente cliente = pedido.getCliente();
        PedidoDadoBancario pedidoDadoBancario = pedido.getPedidoDadoBancario();
        Fornecedor fornecedor = pedido.getFornecedor();

        mapa.put("COMPRADOR_DETALHES", getCompradorDetalhes(fornecedor));
        mapa.put("VENDEDOR_DETALHES", getVendedorDetalhes(cliente));
        mapa.put("MERCADORIA", pedido.getProduto());
        mapa.put("CODIGO", pedido.getNrSiscdb().toString());
        mapa.put("SAFRA", compra.getSafra());
        mapa.put("QUILOS_LIQ_FINAIS", pedido.getPeso().toString());
        mapa.put("QUANTIDADE_QUILOS_POR_EXTENSAO", pedido.getPeso().toString());
        mapa.put("PRAZO_ENTREGA_ATE", compra.getPeriodoEntrega());
        mapa.put("PRAZO_RETIRADA", compra.getPeriodoEntrega());
        mapa.put("QUANTIDADE", pedido.getQtSacos().toString());
        mapa.put("CLAUSULA_QUARTA_SAFRA", compra.getSafra());
        mapa.put("ORIGEM_MP", compra.getProdutorEstado());
        mapa.put("LOCAL_ENTREGA", venda.getLocalDestino());
        mapa.put("PRACA_PRODUCAO", fornecedor.getLocal());
        mapa.put("PRACA_PRODUCAO_UF", fornecedor.getRegiao());
        mapa.put("PRACA_EMBARQUE", cliente.getLocal());
        mapa.put("PRACA_EMBARQUE_UF", cliente.getRegiao());
        mapa.put("MES_PRODUCAO", pedido.getDataPedido().format(DateTimeFormatter.ofPattern("MM")));
        mapa.put("QUANTIDADE_KG", pedido.getPeso().toString());
        mapa.put("CLAUSULA_SEXTA_MOEDA", "REAL");
        mapa.put("CLAUSULA_SEXTA_VALOR", venda.getVendaValorRealTotal().toString());
        mapa.put("CLAUSULA_SEXTA_DT_PAGAMENTO", compra.getCompraDataPagamento().format(DATE_TIME_FORMATTER));
        mapa.put("CLAUSULA_SEXTA_SAFRA", compra.getSafra());
        mapa.put("CLAUSULA_SEXTA_BANCO", pedidoDadoBancario.getNomeBanco());
        mapa.put("CLAUSULA_SEXTA_AGENCIA", pedidoDadoBancario.getAgenciaBanco());
        mapa.put("CLAUSULA_SEXTA_CONTA", pedidoDadoBancario.getContaBanco());
        mapa.put("CLAUSULA_SEXTA_CNPJ_CPF", pedidoDadoBancario.getCpfBanco());
        mapa.put("CLAUSULA_SEXTA_FAVORECIDO", pedidoDadoBancario.getTitularBanco());
        mapa.put("LOCAL_ASSINATURA", pedido.getVenda().getEstadoCliente());

        return mapa;
    }

    private static String getVendedorDetalhes(Cliente cliente) {
        String cpfCnpj = cliente.getCpf() != null && !cliente.getCpf().equals("") ?
                cliente.getCpf() : cliente.getCnpj();
        return cliente.getNomeCliente() + ", situada no município de " + cliente.getLocal() + " estado de " +
                cliente.getRegiao() + ", na " + cliente.getRua() + ", Bairro " + cliente.getBairro() +
                ", inscrito no CPF/CNPJ Nº. " + cpfCnpj + " e Inscrição Estadual Nº. " + cliente.getInscEst();
    }

    private static String getCompradorDetalhes(Fornecedor fornecedor) {
        String cpfCnpj = fornecedor.getCpf() != null && !fornecedor.getCpf().equals("") ?
                fornecedor.getCpf() : fornecedor.getCnpj();
        return fornecedor.getNomeFornecedor() + ", com sede no Município de " + fornecedor.getLocal() + ", estado de " +
                fornecedor.getRegiao() + ", na " + fornecedor.getRua() + ", Bairro " + fornecedor.getBairro() +
                "inscrito no CPF/CNPJ Nº.: " + cpfCnpj + " e Inscrição Estadual N°.: " + fornecedor.getInscEst();
    }

    public static String getPathOutputContrato(long nrSiscdb) {
        return getPrefixPathContrato(nrSiscdb) + ".pdf";
    }

    private static String getPathInputContrato(long nrSiscdb) {
        return getPrefixPathContrato(nrSiscdb) + ".html";
    }

    private static String getPrefixPathContrato(long nrSiscdb) {
        return PATH_OUTPUT_CONTRATO + nrSiscdb;
    }
}
