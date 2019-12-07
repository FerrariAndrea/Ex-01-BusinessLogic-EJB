@echo off
echo "Eseguo due build di fila a distanza di 10s così che"
echo "Jboss faccia il deploy due volte e faccia il flush del .ear desiderato"
echo "build 1..."
call gradle build
timeout 10
echo "build 2..."
call gradle build