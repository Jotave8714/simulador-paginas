

 ![Unifor | Universidade de Fortaleza][image1] | UNIVERSIDADE DE FORTALEZA CENTRO DE CIÊNCIAS TECNOLÓGICAS | CURSO: CIÊNCIA DA COMPUTAÇÃO

**SIMULADOR DE ALGORITMOS DE SUBSTITUIÇÃO DE PÁGINAS**

**Autor:** João Victor Feijó Vasconcelos

**Palavras-chave:** Sistemas Operacionais. Memória Virtual. Substituição de Páginas. Algoritmos. Java.

**Resumo**

Este trabalho apresenta o desenvolvimento de um simulador, em linguagem Java com interface gráfica Swing, para avaliar e comparar o desempenho de quatro algoritmos clássicos de substituição de páginas em sistemas de gerenciamento de memória virtual: FIFO (First In, First Out), LRU (Least Recently Used), Relógio (Clock) e Ótimo. O simulador recebe como entrada uma sequência de referências a páginas e o número de quadros de memória física disponíveis, executando cada algoritmo sobre os mesmos parâmetros e contabilizando o número de faltas de página geradas. Os resultados são apresentados em forma de tabela, gráfico de barras comparativo e visualização passo a passo do estado dos quadros, permitindo observar de forma didática o comportamento de cada estratégia. A ferramenta evidencia, na prática, as diferenças teóricas entre os algoritmos e confirma o algoritmo Ótimo como limite inferior de faltas de página, servindo de referência para os métodos implementáveis.

**Introdução**

O gerenciamento eficiente da memória virtual é um dos pilares de desempenho dos sistemas operacionais modernos. Como a memória física é um recurso limitado e os processos em execução costumam exigir, em conjunto, um espaço de endereçamento maior do que a RAM disponível, o sistema operacional precisa decidir, a cada falta de página, qual página deve ser removida da memória para abrir espaço a uma nova. Essa decisão é tomada por um algoritmo de substituição de páginas, e a qualidade dessa escolha impacta diretamente a quantidade de faltas de página, o tempo de resposta dos processos e, em última instância, a experiência do usuário.

A literatura clássica de Sistemas Operacionais descreve diversos algoritmos com diferentes compromissos entre simplicidade de implementação e desempenho. Entre eles, destacam-se: o FIFO, que remove a página mais antiga; o LRU, que remove a menos recentemente utilizada; o algoritmo do Relógio, uma aproximação eficiente do LRU baseada em bit de referência; e o algoritmo Ótimo, que remove a página cuja próxima utilização ocorrerá o mais tarde possível, sendo inviável na prática por exigir conhecimento do futuro, mas servindo como referência teórica para avaliação dos demais.

Diante disso, este trabalho propõe e implementa um simulador que aplica esses quatro algoritmos sobre uma mesma sequência de referências de páginas e os compara em termos de faltas de página, oferecendo ao usuário tanto uma visão sintética (tabela e gráfico) quanto uma visão detalhada (passo a passo) do comportamento de cada algoritmo.

**Metodologia**

O simulador foi desenvolvido em linguagem Java, com interface gráfica construída em Swing, e organizado segundo uma arquitetura em camadas dividida em três pacotes principais: *model* (estruturas de dados que representam o resultado da simulação e o estado dos quadros em cada passo), *algoritmos* (uma interface comum *PageReplacementAlgorithm* e quatro implementações concretas, uma para cada algoritmo) e *ui* (componentes da interface gráfica). Esse desenho favorece a coesão e permite acrescentar novos algoritmos no futuro sem alterar a interface ou a lógica de comparação.

A entrada do programa é composta por dois parâmetros: uma sequência de números inteiros representando as páginas referenciadas, separados por espaço ou vírgula, e um número inteiro indicando a quantidade de quadros de memória física disponíveis (com valor padrão igual a 3). A partir desses dois parâmetros, cada algoritmo é executado de forma independente sobre a mesma sequência, garantindo que a comparação seja justa.

Cada algoritmo foi implementado utilizando a estrutura de dados mais adequada às suas características. O FIFO foi implementado com uma *ArrayDeque* que mantém a ordem de chegada das páginas, removendo sempre a primeira em caso de falta. O LRU foi implementado com *LinkedHashMap* em modo *access-order*, que reordena automaticamente os elementos a cada acesso, fazendo com que a página menos recentemente usada esteja sempre na cabeça da estrutura. O algoritmo do Relógio foi implementado com um arranjo circular acompanhado de um bit de referência por quadro e de um ponteiro que avança a cada falta, concedendo segunda chance a páginas marcadas. Por fim, o algoritmo Ótimo utiliza *look-ahead* sobre a sequência completa, escolhendo para substituição a página cuja próxima ocorrência está mais distante (ou que não voltará a ser usada).

A saída do simulador é apresentada em duas abas. A primeira, denominada “Resumo & Gráfico”, exibe uma tabela com o número de faltas de página, número de acertos e taxa de acerto para cada algoritmo, acompanhada de um gráfico de barras comparativo. A segunda, “Passo a Passo”, permite ao usuário selecionar um algoritmo e visualizar, para cada referência da sequência, o estado dos quadros, com destaque em vermelho para faltas de página e em verde para acertos. Adicionalmente, o resultado é também impresso no console no formato textual solicitado pelo enunciado.

**Resultados e Discussão**

Para validação do simulador, foi utilizada a sequência de referências clássica encontrada em livros-texto de Sistemas Operacionais: *7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1*, com 3 quadros de memória física. Os resultados obtidos são apresentados na Tabela 1\.

**Tabela 1\.** Comparação do número de faltas de página por algoritmo (sequência de 20 referências, 3 quadros).

| Algoritmo | Faltas de página | Acertos | Taxa de acerto |
| :---: | :---: | :---: | :---: |
| FIFO | 15 | 5 | 25% |
| LRU | 12 | 8 | 40% |
| Relógio | 14 | 6 | 30% |
| Ótimo | 9 | 11 | 55% |

Os resultados confirmam o comportamento previsto pela teoria. O algoritmo FIFO, por considerar apenas o tempo de chegada da página à memória e ignorar seu padrão de uso, apresentou o maior número de faltas de página, mostrando-se a estratégia menos eficiente entre as testadas. O algoritmo do Relógio, por sua vez, alcançou um resultado intermediário: ao introduzir o bit de referência, ele consegue dar segunda chance a páginas recém-utilizadas, aproximando-se do comportamento do LRU sem a sobrecarga de manter a ordem completa de uso. O LRU obteve um desempenho significativamente melhor que o FIFO, pois leva em conta o histórico recente de utilização das páginas, o que faz mais sentido em programas com localidade temporal.

Como esperado, o algoritmo Ótimo apresentou o menor número de faltas de página, atuando como limite inferior teórico contra o qual os demais foram comparados. O fato de os algoritmos implementáveis (FIFO, LRU e Relógio) ficarem todos acima desse mínimo evidencia que existe sempre uma margem de melhoria, mas também que o Ótimo é inviável na prática por exigir o conhecimento prévio das futuras referências. A interface passo a passo do simulador permitiu inspecionar visualmente o motivo de cada falta, mostrando, por exemplo, situações em que o FIFO substitui uma página que ainda seria utilizada em breve enquanto o LRU e o Ótimo a preservam.

A interface gráfica e o gráfico de barras facilitaram bastante a comparação visual entre os algoritmos, tornando o simulador adequado tanto para fins didáticos quanto para experimentação com diferentes sequências de páginas e quantidades de quadros.

**Conclusão**

O simulador desenvolvido cumpriu todos os objetivos propostos, implementando quatro algoritmos clássicos de substituição de páginas e fornecendo uma análise comparativa clara em termos de faltas de página. Os resultados experimentais reforçaram o que a teoria prevê: algoritmos que consideram o padrão de uso (LRU e Relógio) superam estratégias puramente baseadas em ordem de chegada (FIFO), e nenhum algoritmo implementável é capaz de superar o Ótimo, que se mantém como referência ideal.

Além de atender ao requisito funcional, a adoção de uma interface gráfica com tabela, gráfico comparativo e visualização passo a passo agregou valor pedagógico ao trabalho, permitindo observar não apenas o resultado final, mas também o processo de tomada de decisão de cada algoritmo. Como trabalhos futuros, sugere-se a inclusão dos algoritmos NFU (Not Frequently Used) e Aging (Envelhecimento), bem como suporte a geração automática de sequências de referências com diferentes graus de localidade, ampliando o escopo de análise.

**Referências**

TANENBAUM, A. S.; BOS, H. **Sistemas Operacionais Modernos**. 4\. ed. São Paulo: Pearson, 2016\.

SILBERSCHATZ, A.; GALVIN, P. B.; GAGNE, G. **Fundamentos de Sistemas Operacionais**. 9\. ed. Rio de Janeiro: LTC, 2015\.

ORACLE. **Java Platform, Standard Edition Documentation**. Disponível em: https://docs.oracle.com/javase/. Acesso em: 8 maio 2026\.

ORACLE. **Trail: Creating a GUI With Swing**. Disponível em: https://docs.oracle.com/javase/tutorial/uiswing/. Acesso em: 8 maio 2026\.

DEVMEDIA. **Introdução à Interface GUI no Java**. Disponível em: https://www.devmedia.com.br/introducao-a-interface-gui-no-java/25646. Acesso em: 8 maio 2026\.

**Anexo — Repositório do Projeto**

Código-fonte completo disponível em:

[**https://github.com/Jotave8714/simulador-paginas**](https://github.com/Jotave8714/simulador-paginas)

[image1]: <data:image/png;base64,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>