@echo off
echo ========================================
echo    GERANDO APK - CONTATOS EMERGENCIA
echo ========================================
echo.

echo 1. Verificando Java...
java -version
if %errorlevel% neq 0 (
    echo ERRO: Java nao encontrado!
    echo Instale o JDK 8 ou superior
    pause
    exit /b 1
)

echo.
echo 2. Verificando Android SDK...
if not exist "%ANDROID_HOME%" (
    echo ERRO: ANDROID_HOME nao configurado!
    echo Configure a variavel de ambiente ANDROID_HOME
    pause
    exit /b 1
)

echo.
echo 3. Compilando projeto...
echo Por favor, abra o projeto no Android Studio e:
echo 1. Vá em Build -> Build Bundle(s) / APK(s) -> Build APK(s)
echo 2. Ou use Ctrl+F9 para fazer build
echo 3. O APK será gerado em: app/build/outputs/apk/debug/
echo.
echo Alternativamente, você pode:
echo 1. Abrir o projeto no Android Studio
echo 2. Clicar em "Run" (botão verde)
echo 3. Selecionar um dispositivo ou emulador
echo.
pause 