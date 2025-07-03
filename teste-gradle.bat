@echo off
echo ========================================
echo    TESTANDO GRADLE - CONTATOS EMERGENCIA
echo ========================================
echo.

echo 1. Verificando Java...
java -version
if %errorlevel% neq 0 (
    echo ERRO: Java nao encontrado!
    pause
    exit /b 1
)

echo.
echo 2. Verificando pasta do Gradle...
if exist "C:\Users\arthu\.gradle\wrapper\dists" (
    echo Pasta do Gradle encontrada!
) else (
    echo Criando pasta do Gradle...
    mkdir "C:\Users\arthu\.gradle\wrapper\dists" 2>nul
)

echo.
echo 3. Testando Gradle...
echo Por favor, no Android Studio:
echo 1. Feche o projeto se estiver aberto
echo 2. Abra o projeto novamente
echo 3. Aguarde o Android Studio baixar o Gradle
echo 4. Se aparecer erro, clique em "Open Gradle Settings"
echo 5. Selecione "Use Gradle from: gradle-wrapper.properties file"
echo.
echo Se ainda der erro, tente:
echo - Reiniciar o Android Studio
echo - Reiniciar o computador
echo.
pause 