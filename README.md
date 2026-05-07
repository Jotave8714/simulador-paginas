# Simulador de Substituição de Páginas

Simulador em Java com interface gráfica (Swing) para comparar quatro algoritmos de substituição de páginas em memória virtual.

## Algoritmos implementados

| # | Algoritmo | Estrutura-chave |
|---|-----------|-----------------|
| 1 | **FIFO** | `ArrayDeque` — substitui a página mais antiga |
| 2 | **LRU** | `LinkedHashMap` access-order — substitui a menos recentemente usada |
| 3 | **Relógio** | Array circular + bit de referência — segunda chance antes de substituir |
| 4 | **Ótimo** | Look-ahead — substitui a página usada mais longe no futuro |

## Requisitos

- Java 8 ou superior (JDK)
- Terminal com suporte a Bash (Linux/macOS) ou adaptação para Windows

## Como compilar e executar

```bash
# Compilar
bash build.sh

# Executar
bash run.sh
```

Ou manualmente:

```bash
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out simulador.Main
```

## Como usar o simulador

1. No campo **Sequência de páginas**, informe os números separados por espaço ou vírgula.  
   Exemplo: `7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1`
2. Defina o número de **Quadros** (frames) de memória física (padrão: 3).
3. Clique em **Simular** ou pressione `Enter`.

### Abas da interface

- **Resumo & Gráfico** — tabela com faltas/acertos/taxa por algoritmo + gráfico de barras comparativo.
- **Passo a Passo** — selecione o algoritmo no menu e veja o estado dos quadros em cada referência (vermelho = falta, verde = acerto).

### Saída no terminal

Além da GUI, o resultado também é impresso no console:

```
--- Resultados (quadros=3, paginas=20) ---
FIFO         - 15 faltas de página
LRU          - 12 faltas de página
Relógio      - 14 faltas de página
Ótimo        - 9 faltas de página
```

## Estrutura do projeto

```
src/simulador/
├── Main.java
├── model/
│   ├── SimulationResult.java
│   └── StepState.java
├── algoritmos/
│   ├── PageReplacementAlgorithm.java  (interface)
│   ├── FifoAlgorithm.java
│   ├── LruAlgorithm.java
│   ├── ClockAlgorithm.java
│   └── OptimalAlgorithm.java
└── ui/
    ├── MainWindow.java
    ├── InputPanel.java
    ├── ResultsPanel.java
    ├── BarChartPanel.java
    └── StepTablePanel.java
```
