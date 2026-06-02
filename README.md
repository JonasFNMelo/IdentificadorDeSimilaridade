# Identificador de Similaridade

Aplicação em **Java** que compara dois ou mais textos/documentos e calcula o grau de similaridade entre eles. Projeto desenvolvido para a disciplina de **Engenharia de Software 2 (ES2)**.

## Sobre o projeto

A proposta foi construir, de ponta a ponta, uma ferramenta capaz de receber documentos como entrada, processá-los e devolver uma métrica que indique o quão parecidos eles são — passando por todas as etapas que se espera em uma disciplina de ES2: levantamento de requisitos, modelagem, implementação, testes e documentação.

A documentação completa do processo de engenharia está disponível no arquivo [`Relatorio Projeto ES2.pdf`](./Relatorio%20Projeto%20ES2.pdf).

## Funcionalidades

- 📄 Leitura e processamento de arquivos de texto
- 🔍 Comparação entre documentos para identificação de similaridade
- 📊 Geração de resultados em arquivo de saída (`resultado.txt`)

## Tecnologias

- **Java**

## Estrutura do projeto

```
.
├── src/                         # código-fonte Java
├── bin/                         # arquivos compilados (.class)
├── docs/                        # documentação do projeto
├── Relatorio Projeto ES2.pdf    # relatório completo da disciplina
├── resultado.txt                # exemplo de saída da execução
└── LICENSE                      # licença MIT
```

## Como rodar

1. Clone o repositório:
   ```bash
   git clone https://github.com/JonasFNMelo/IdentificadorDeSimilaridade.git
   cd IdentificadorDeSimilaridade
   ```
2. Compile os fontes (a partir da raiz):
   ```bash
   javac -d bin src/**/*.java
   ```
3. Execute a aplicação:
   ```bash
   java -cp bin Main
   ```

> Substitua `Main` pelo nome da classe principal do projeto, caso seja diferente.

## Documentação

O relatório com a análise completa do projeto — incluindo requisitos, modelagem e decisões de design — está disponível em [`Relatorio Projeto ES2.pdf`](./Relatorio%20Projeto%20ES2.pdf).

## Autor

- [**Jonas Melo**](https://github.com/JonasFNMelo)

## Licença

Distribuído sob a licença MIT. Veja o arquivo [LICENSE](./LICENSE) para mais informações.

---

> Projeto desenvolvido para a disciplina de **Engenharia de Software 2**.
