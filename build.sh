#!/bin/bash
cd "$(dirname "$0")"
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt && echo "Compilacao concluida." || echo "Erro na compilacao."
rm -f sources.txt
