# Solucionador do Problema do Caixeiro Viajante (TSP)

Este projeto é uma aplicação em Java desenvolvida para resolver o **Problema do Caixeiro Viajante** (TSP — *Traveling Salesperson Problem*). O sistema conta com uma interface gráfica simples baseada em `JOptionPane` que permite ao usuário escolher entre diferentes abordagens algorítmicas para calcular a rota de menor custo que visita todas as cidades e retorna à origem.

## Funcionalidades

O projeto implementa quatro algoritmos distintos, permitindo a comparação entre métodos exatos (que garantem a melhor solução) e heurísticos (que buscam uma boa solução em menos tempo):

- **Backtracking:** Método de busca exaustiva que testa as permutações de rotas válidas, interrompendo caminhos (podas simples) que já excedem o melhor custo encontrado.

- **Branch and Bound:** Algoritmo exato mais eficiente que o Backtracking puro. Utiliza o cálculo de limites inferiores (baseado nos custos mínimos de saída de cada cidade) para podar grandes áreas do espaço de busca.

- **Programação Dinâmica (Held-Karp):** Algoritmo exato de alta eficiência em comparação à força bruta. Utiliza memoização e bitmask para armazenar estados já calculados. **Nota:** Devido ao limite de inteiros de 32 bits no Java, esta implementação suporta no máximo **30 cidades**.

- **Gulosa (Heurística do Vizinho Mais Próximo):** Abordagem aproximada e extremamente rápida. Constrói a rota escolhendo sempre a próxima cidade não visitada com o menor custo imediato. Não garante a rota ótima, mas é escalável para grafos muito grandes.

## Estrutura do Projeto

| Arquivo | Descrição |
|--------|-----------|
| `AlgoritmoTSP.java` | Interface padronizada que todos os algoritmos de resolução devem implementar. |
| `ResultadoTSP.java` | Classe de modelo que encapsula o resultado da execução (custo mínimo, melhor rota e tempo de execução). |
| `EntradaTSP` (dentro de `Main`) | Estrutura para armazenar a matriz de distâncias e a quantidade de cidades. |
| `TSPBacktracking.java` | Implementação da abordagem de Backtracking. |
| `TSPBranchAndBound.java` | Implementação da abordagem Branch & Bound. |
| `TSPDynamicProgramming.java` | Implementação da abordagem de Programação Dinâmica. |
| `TSPGreedy.java` | Implementação da heurística Gulosa. |
| `Main.java` | Classe principal que gerencia o fluxo da aplicação, captura as entradas do usuário via `JOptionPane`, invoca os algoritmos e exibe os resultados na tela. |

## Como Executar

### Pré-requisitos

- **Java Development Kit (JDK)** versão 8 ou superior instalado.

### Compilação e execução

1. Abra o terminal na pasta onde os arquivos `.java` estão localizados.

2. Compile todos os arquivos:

   ```bash
   javac *.java
   ```

3. Execute o programa pela classe `Main`:

   ```bash
   java Main
   ```

## Formato de Entrada de Dados

Ao rodar a aplicação, uma janela pedirá a **matriz de distâncias**. A entrada deve seguir este formato:

1. A **primeira linha** deve conter um número inteiro **N** indicando a quantidade de cidades.
2. As **próximas N linhas** representam a **matriz de adjacência** (custos ou distâncias entre as cidades), com **N** valores separados por espaço. Custos entre a mesma cidade (diagonal principal) ou arestas inexistentes/inválidas costumam ser preenchidos com **0**.

### Exemplo (copie e cole na caixa de texto do programa)

```
4
0 10 15 20
10 0 35 25
15 35 0 30
20 25 30 0
```

## Formato de Saída

Após o processamento, o programa exibirá uma caixa de diálogo informando:

- O **custo mínimo** calculado para a rota.
- A **sequência da rota** (ex.: `0 1 3 2 0`), com a ordem de visitação das cidades e retorno à cidade inicial (índice 0).

Os resultados também são impressos na **saída padrão** do console (`System.out`).
