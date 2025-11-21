#!/usr/bin/env bash
# Executa a classe Main compilada em bin/
set -e
OUT_DIR="bin"
# Classe principal com o pacote conforme os arquivos em `src/` (veja `package projeto;` em Main.java)
MAIN_CLASS="projeto.Main"

# Verifica argumentos mínimos antes de executar a JVM
if [ "$#" -lt 2 ]; then
  echo "Uso: $0 <pasta_docs> <similaridade> [lista | topK K | busca arquivoA arquivoB]"
  echo "Exemplos:" 
  echo "  $0 docs 0.5 lista"
  echo "  $0 docs 0.7 topK 10"
  echo "  $0 docs 0.5 busca arquivoA.txt arquivoB.txt"
  exit 1
fi

if [ ! -d "$OUT_DIR" ]; then
  echo "Diretório $OUT_DIR não existe. Rode ./build.sh antes." >&2
  exit 1
fi

if ! command -v java >/dev/null 2>&1; then
  echo "Erro: java não encontrado. Instale o JRE/JDK e tente novamente." >&2
  exit 1
fi

echo "Executando $MAIN_CLASS..."
java -cp "$OUT_DIR" "$MAIN_CLASS" "$@"