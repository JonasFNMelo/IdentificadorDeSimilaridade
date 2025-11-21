#!/usr/bin/env bash
# Compila todos os .java em src/ para a pasta bin/
set -e
SRC_DIR="src"
OUT_DIR="bin"

if ! command -v javac >/dev/null 2>&1; then
  echo "Erro: javac não encontrado. Instale o JDK e tente novamente." >&2
  exit 1
fi

mkdir -p "$OUT_DIR"

# Compila recursivamente todos os arquivos .java
JAVA_FILES=$(find "$SRC_DIR" -name "*.java")
if [ -z "$JAVA_FILES" ]; then
  echo "Nenhum arquivo .java encontrado em $SRC_DIR" >&2
  exit 1
fi

echo "Compilando fontes..."
javac -d "$OUT_DIR" $JAVA_FILES

echo "Compilação concluída. Artefatos em: $OUT_DIR"