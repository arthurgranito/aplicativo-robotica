# 🚀 INSTRUÇÕES PARA GERAR O APK

## Método 1: Android Studio (RECOMENDADO)

### 1. Instalar Android Studio
- Baixe e instale o Android Studio: https://developer.android.com/studio
- Durante a instalação, certifique-se de instalar o Android SDK

### 2. Abrir o Projeto
- Abra o Android Studio
- Clique em "Open an existing Android Studio project"
- Navegue até a pasta do projeto e selecione
- Aguarde o Android Studio sincronizar o projeto

### 3. Configurar o Endereço MAC
- Abra o arquivo: `app/src/main/java/com/contatosemergencia/MainActivity.kt`
- Na linha 25, altere o endereço MAC do seu HC-05:
```kotlin
private const val HC05_MAC_ADDRESS = "SEU_MAC_AQUI" // Ex: "98:D3:31:F5:B9:E7"
```

### 4. Gerar APK
- Clique em **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
- Ou use o atalho **Ctrl+F9**
- Aguarde a compilação terminar
- O APK será gerado em: `app/build/outputs/apk/debug/app-debug.apk`

### 5. Instalar no Celular
- Transfira o APK para seu celular
- Instale o APK (permita instalação de fontes desconhecidas)
- Conceda todas as permissões solicitadas

## Método 2: Linha de Comando

### Pré-requisitos
- Java JDK 8 ou superior
- Android SDK instalado
- Variável ANDROID_HOME configurada

### Comandos
```bash
# Navegar até a pasta do projeto
cd "C:\Users\arthu\OneDrive\Desktop\projeto robotica"

# Executar o script de build
.\build-apk.bat

# Ou usar gradle diretamente (se configurado)
.\gradlew.bat assembleDebug
```

## 🔧 CONFIGURAÇÃO DO HC-05

### 1. Encontrar o MAC Address
- Conecte o HC-05 ao Arduino
- Abra o Monitor Serial (9600 baud)
- O MAC address aparecerá na inicialização
- Ou use um app Bluetooth para descobrir

### 2. Parear o Dispositivo
- Vá em Configurações → Bluetooth
- Procure por "HC-05" ou similar
- Pareie o dispositivo
- Anote o endereço MAC

### 3. Atualizar no Código
- Abra `MainActivity.kt`
- Substitua o MAC address na linha 25
- Recompile o APK

## 📱 FUNCIONALIDADES DO APK

### ✅ O que está funcionando:
- ✅ Lista de contatos do backend
- ✅ Cadastro de novos contatos
- ✅ Interface moderna Material Design
- ✅ Conexão Bluetooth com HC-05
- ✅ Detecção de sinal "queda"
- ✅ Envio de SMS para todos os contatos
- ✅ Permissões automáticas

### 🔧 Configurações necessárias:
- Endereço MAC do HC-05
- Permissões de Bluetooth e SMS
- Conexão com internet para o backend

## 🚨 SOLUÇÃO DE PROBLEMAS

### Erro de Compilação
- Verifique se o Android Studio está atualizado
- Sincronize o projeto (File → Sync Project with Gradle Files)
- Limpe o projeto (Build → Clean Project)

### Erro de Bluetooth
- Verifique se o HC-05 está pareado
- Confirme o endereço MAC correto
- Teste a conexão manualmente

### Erro de SMS
- Verifique permissões de SMS
- Confirme se há crédito no chip
- Teste envio manual de SMS

## 📞 SUPORTE

Se encontrar problemas:
1. Verifique os logs no Android Studio
2. Confirme todas as permissões
3. Teste cada funcionalidade separadamente

---

**🎉 Seu aplicativo está pronto para uso!**

**Lembre-se de:**
- Configurar o MAC address correto
- Testar a conexão Bluetooth
- Cadastrar alguns contatos de teste
- Verificar se o backend está online 