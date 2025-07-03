# Contatos Emergência - Aplicativo Android

Um aplicativo Android completo para gerenciar contatos de emergência e detectar quedas através de um módulo Bluetooth HC-05.

## Funcionalidades

### 🏠 Tela Principal (Home)
- Lista todos os contatos cadastrados vindos do backend
- Status de conexão Bluetooth em tempo real
- Botão para conectar/desconectar do módulo HC-05
- Botão flutuante para adicionar novos contatos

### ➕ Tela de Cadastro
- Formulário para cadastrar novos contatos
- Campos: Nome, Telefone e Descrição
- Validação de campos obrigatórios
- Envio automático para o backend

### 📱 Sistema de Emergência
- Conexão automática com módulo Bluetooth HC-05
- Detecção de sinal "queda" do acelerômetro
- Envio automático de SMS para todos os contatos cadastrados
- Mensagem: "Atenção! Queda detectada"

## Configuração

### 1. Endereço MAC do HC-05
No arquivo `MainActivity.kt`, linha 25, altere o endereço MAC:
```kotlin
private const val HC05_MAC_ADDRESS = "SEU_MAC_AQUI" // Ex: "98:D3:31:F5:B9:E7"
```

### 2. Backend
O aplicativo está configurado para usar o backend em:
```
https://backend-arduino-damp-hill-3.fly.dev/contatos
```

## Permissões Necessárias

O aplicativo solicita automaticamente as seguintes permissões:
- **Bluetooth**: Para conectar com o módulo HC-05
- **SMS**: Para enviar mensagens de emergência
- **Localização**: Necessária para Bluetooth no Android 6+
- **Internet**: Para comunicação com o backend

## Como Usar

1. **Instalar o APK** no dispositivo Android
2. **Conceder permissões** quando solicitado
3. **Cadastrar contatos** usando o botão "+"
4. **Conectar Bluetooth** clicando no botão "Conectar Bluetooth"
5. **Testar queda** movendo o acelerômetro bruscamente

## Estrutura do Projeto

```
app/
├── src/main/
│   ├── java/com/contatosemergencia/
│   │   ├── MainActivity.kt              # Tela principal
│   │   ├── CadastroContatoActivity.kt   # Tela de cadastro
│   │   ├── Contato.kt                   # Modelo de dados
│   │   ├── ApiService.kt                # Interface da API
│   │   ├── ContatosAdapter.kt           # Adaptador da lista
│   │   ├── BluetoothManager.kt          # Gerenciador Bluetooth
│   │   └── SmsManager.kt                # Gerenciador SMS
│   ├── res/
│   │   ├── layout/                      # Layouts das telas
│   │   ├── values/                      # Strings, cores, temas
│   │   └── drawable/                    # Recursos visuais
│   └── AndroidManifest.xml              # Configuração do app
├── build.gradle                         # Dependências
└── proguard-rules.pro                   # Regras de otimização
```

## Tecnologias Utilizadas

- **Kotlin**: Linguagem principal
- **Android SDK**: Framework nativo
- **Material Design**: Interface moderna
- **Retrofit**: Comunicação com API
- **Coroutines**: Programação assíncrona
- **RecyclerView**: Lista de contatos
- **ViewBinding**: Binding de views

## Build e Instalação

### Pré-requisitos
- Android Studio ou Gradle
- JDK 8 ou superior
- Android SDK API 24+

### Gerar APK
```bash
# Build debug
./gradlew assembleDebug

# Build release
./gradlew assembleRelease
```

O APK será gerado em:
```
app/build/outputs/apk/debug/app-debug.apk
app/build/outputs/apk/release/app-release.apk
```

## API Endpoints

### GET /contatos
Retorna lista de todos os contatos cadastrados.

### POST /contatos
Cadastra um novo contato.
```json
{
  "nome": "João Silva",
  "telefone": "(11) 99999-9999",
  "descricao": "Familiar próximo"
}
```

## Troubleshooting

### Problemas de Bluetooth
1. Verifique se o HC-05 está pareado com o dispositivo
2. Confirme o endereço MAC correto
3. Certifique-se que o Bluetooth está ativado

### Problemas de SMS
1. Verifique se a permissão de SMS foi concedida
2. Confirme se há crédito no chip
3. Teste o envio manual de SMS

### Problemas de Conexão
1. Verifique a conectividade com a internet
2. Confirme se o backend está online
3. Verifique os logs do aplicativo

## Suporte

Para dúvidas ou problemas, verifique:
1. Logs do aplicativo no Android Studio
2. Permissões concedidas nas configurações
3. Status do Bluetooth e internet

---

**Desenvolvido com ❤️ para seu projeto de robótica!** 