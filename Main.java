import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main {

    public static void main(String[] args) {
        while (true) {
            AlgoritmoTSP algoritmo = selecionarAlgoritmo();
            if (algoritmo == null) {
                return;
            }

            executarFluxoTSP(algoritmo);
        }
    }

    private static void executarFluxoTSP(AlgoritmoTSP algoritmo) {
        EntradaTSP entrada = lerEntrada();
        if (entrada == null) {
            return;
        }

        ResultadoTSP resultado;
        try {
            long inicio = System.nanoTime();
            resultado = algoritmo.resolver(entrada.n, entrada.dist);
            long fim = System.nanoTime();
            resultado.tempoExecucaoMs = (fim - inicio) / 1_000_000L;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Erro na execução",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (resultado == null || resultado.melhorRota == null || resultado.melhorRota.length == 0
                || resultado.custoMinimo == Integer.MAX_VALUE) {
            JOptionPane.showMessageDialog(
                    null,
                    "Não foi encontrada uma rota válida para a instância informada.",
                    "Resultado TSP",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rotaFormatada = formatarRota(resultado.melhorRota);

        System.out.println(resultado.custoMinimo);
        System.out.println(rotaFormatada);

        JOptionPane.showMessageDialog(
                null,
                resultado.custoMinimo + "\n" + rotaFormatada,
                "Resultado TSP",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static AlgoritmoTSP selecionarAlgoritmo() {
        String[] opcoes = {
                "Backtracking",
                "Branch and Bound",
                "Programação Dinâmica",
                "Gulosa"
        };

        int escolha = JOptionPane.showOptionDialog(
                null,
                "Escolha o método de solução para o TSP:",
                "Seleção de Algoritmo",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]);

        switch (escolha) {
            case 0:
                return new TSPBacktracking();
            case 1:
                return new TSPBranchAndBound();
            case 2:
                return new TSPDynamicProgramming();
            case 3:
                return new TSPGreedy();
            default:
                return null;
        }
    }

    private static EntradaTSP lerEntrada() {
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);

        int opcao = JOptionPane.showConfirmDialog(
                null,
                scrollPane,
                "Informe n e a matriz de distâncias (tudo de uma vez)",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcao != JOptionPane.OK_OPTION) {
            return null;
        }

        String textoEntrada = textArea.getText();
        if (textoEntrada == null || textoEntrada.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Nenhuma entrada fornecida.",
                    "Erro na entrada",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String[] linhasBrutas = textoEntrada.split("\\r?\\n");
        java.util.List<String> linhas = new java.util.ArrayList<>();
        for (String linha : linhasBrutas) {
            if (!linha.trim().isEmpty()) {
                linhas.add(linha.trim());
            }
        }

        if (linhas.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Entrada inválida.",
                    "Erro na entrada",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int n;
        try {
            n = Integer.parseInt(linhas.get(0));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "A primeira linha deve conter um inteiro representando o número de cidades.",
                    "Erro na entrada",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (n <= 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "O número de cidades deve ser maior que zero.",
                    "Erro na entrada",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (linhas.size() - 1 < n) {
            JOptionPane.showMessageDialog(
                    null,
                    "Foram fornecidas apenas " + (linhas.size() - 1)
                            + " linhas para a matriz, mas são necessárias " + n + ".",
                    "Erro na entrada",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            String[] partes = linhas.get(i + 1).split("\\s+");
            if (partes.length != n) {
                JOptionPane.showMessageDialog(
                        null,
                        "A linha " + (i + 2) + " deve conter exatamente " + n + " valores.",
                        "Erro na entrada",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            try {
                for (int j = 0; j < n; j++) {
                    dist[i][j] = Integer.parseInt(partes[j]);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "A linha " + (i + 2) + " contém valores não inteiros.",
                        "Erro na entrada",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        return new EntradaTSP(n, dist);
    }

    private static String formatarRota(int[] rota) {
        StringBuilder rotaBuilder = new StringBuilder();
        for (int cidade : rota) {
            if (rotaBuilder.length() > 0) {
                rotaBuilder.append(' ');
            }
            rotaBuilder.append(cidade);
        }
        rotaBuilder.append(' ').append(rota[0]);
        return rotaBuilder.toString();
    }

    private static class EntradaTSP {
        private final int n;
        private final int[][] dist;

        private EntradaTSP(int n, int[][] dist) {
            this.n = n;
            this.dist = dist;
        }
    }
}