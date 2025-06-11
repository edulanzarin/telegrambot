# 🤖 Telegram Bot - Sistema de Assinaturas

Bot do Telegram para gerenciamento de assinaturas com integração Firebase e processamento de pagamentos.

## 📋 Sobre o Projeto

Este é um bot do Telegram desenvolvido em Java que oferece um sistema completo de gerenciamento de assinaturas. O bot permite que usuários se cadastrem, escolham planos de assinatura, processem pagamentos e tenham acesso a conteúdos exclusivos baseados em suas assinaturas ativas.

### 🎯 Funcionalidades Principais

- **Cadastro Automático**: Usuários são automaticamente cadastrados ao iniciar o bot
- **Planos de Assinatura**: 4 tipos de planos disponíveis (Mensal, Trimestral, Semestral, Vitalício)
- **Processamento de Pagamentos**: Sistema preparado para integração com Mercado Pago
- **Controle de Acesso**: Verificação automática de assinaturas ativas
- **Persistência de Dados**: Armazenamento seguro no Firebase Firestore
- **Respostas Dinâmicas**: Mensagens configuráveis no banco de dados

### 💰 Planos Disponíveis

| Plano | Duração | Valor | Desconto |
|-------|---------|-------|----------|
| Mensal | 1 mês | R$ 29,90 | - |
| Trimestral | 3 meses | R$ 79,90 | 10% |
| Semestral | 6 meses | R$ 149,90 | 15% |
| Vitalício | 100 anos | R$ 999,90 | Melhor custo-benefício |

## 🚀 Tecnologias Utilizadas

- **Java 24**: Linguagem principal
- **Maven**: Gerenciamento de dependências
- **Telegram Bots API**: Interface com o Telegram
- **Firebase Firestore**: Banco de dados NoSQL
- **Logback**: Sistema de logging profissional
- **Gson**: Processamento JSON

## 📁 Estrutura do Projeto

```
src/main/java/io/github/edulanzarin/
├── 📁 config/          # Configurações centralizadas
├── 📁 core/            # Classes principais do bot
├── 📁 handlers/        # Manipuladores de mensagens
├── 📁 models/          # Modelos de dados
│   ├── Usuario.java
│   ├── Assinatura.java
│   ├── Pagamento.java
│   ├── TipoPlano.java
│   ├── Evento.java
│   └── Mensagem.java
├── 📁 services/        # Serviços externos
│   ├── Firebase.java
│   └── MercadoPago.java
└── 📁 utils/           # Utilitários e helpers
    ├── CarregarEnv.java
    └── Respostas.java
```

## ⚙️ Configuração

### 1. Pré-requisitos

- Java 24 ou superior instalado
- Maven 3.6+ instalado
- Conta no Firebase com projeto criado
- Bot do Telegram criado via @BotFather

### 2. Configuração do Firebase

1. Acesse o [Console do Firebase](https://console.firebase.google.com/)
2. Crie um novo projeto ou use um existente
3. Ative o Firestore Database
4. Gere uma chave de conta de serviço (Service Account)
5. Baixe o arquivo JSON das credenciais

### 3. Configuração do Bot do Telegram

1. Converse com [@BotFather](https://t.me/BotFather) no Telegram
2. Crie um novo bot com `/newbot`
3. Copie o token fornecido
4. Configure o username do bot

### 4. Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
# Telegram Bot
TELEGRAM_BOT_TOKEN=seu_token_aqui
TELEGRAM_BOT_USERNAME=seu_username_aqui

# Firebase
FIREBASE_TYPE=service_account
FIREBASE_PROJECT_ID=seu_projeto_id
FIREBASE_PRIVATE_KEY_ID=sua_private_key_id
FIREBASE_PRIVATE_KEY="-----BEGIN PRIVATE KEY-----\nSUA_CHAVE_PRIVADA_AQUI\n-----END PRIVATE KEY-----\n"
FIREBASE_CLIENT_EMAIL=firebase-adminsdk-xxxxx@projeto.iam.gserviceaccount.com
FIREBASE_CLIENT_ID=sua_client_id
FIREBASE_AUTH_URI=https://accounts.google.com/o/oauth2/auth
FIREBASE_TOKEN_URI=https://oauth2.googleapis.com/token
FIREBASE_AUTH_PROVIDER_CERT_URL=https://www.googleapis.com/oauth2/v1/certs
FIREBASE_CLIENT_CERT_URL=https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-xxxxx%40projeto.iam.gserviceaccount.com
FIREBASE_UNIVERSE_DOMAIN=googleapis.com
```

## 🚀 Instalação e Execução

### 1. Clone o repositório
```bash
git clone https://github.com/edulanzarin/telegrambot.git
cd telegrambot
```

### 2. Configure as variáveis de ambiente
```bash
# Copie e edite o arquivo .env com suas credenciais
cp .env.example .env
```

### 3. Compile o projeto
```bash
mvn clean compile
```

### 4. Execute o bot
```bash
mvn exec:java -Dexec.mainClass="io.github.edulanzarin.App"
```

## 🎮 Como Usar

### Comandos Disponíveis

- `/start` - Inicia o bot e realiza cadastro automático
- `/help` - Mostra informações de ajuda e comandos

### Fluxo de Uso

1. **Iniciar Conversa**: Digite `/start` para começar
2. **Cadastro Automático**: O bot registra seus dados automaticamente
3. **Escolher Plano**: Selecione um dos planos disponíveis
4. **Processar Pagamento**: Siga as instruções para pagamento
5. **Aguardar Confirmação**: O pagamento é processado automaticamente
6. **Acesso Liberado**: Após confirmação, você terá acesso ao conteúdo

## 🗄️ Estrutura do Banco de Dados

### Coleção: `usuarios`
```json
{
  "id": "123456789",
  "usuario": "username_telegram",
  "nome": "Nome Completo",
  "assinaturaId": "id_assinatura_ativa"
}
```

### Coleção: `pagamentos`
```json
{
  "id": "pagamento_123",
  "usuarioId": "123456789",
  "vencimento": "2024-01-15T15:30:00Z",
  "status": "PENDENTE",
  "plano": "MENSAL",
  "valor": 29.90
}
```

### Coleção: `assinaturas`
```json
{
  "id": "assinatura_456",
  "usuarioId": "123456789",
  "pagamentoId": "pagamento_123",
  "dataInicio": "2024-01-01T00:00:00Z",
  "dataFim": "2024-02-01T00:00:00Z",
  "tipoPlano": "MENSAL",
  "ativa": true
}
```

### Coleção: `respostas`
```json
{
  "bem_vindo": "Olá %s! Bem-vindo ao nosso bot.",
  "help": "Comandos disponíveis:\n/start - Iniciar\n/help - Ajuda",
  "comando_nao_reconhecido": "Comando não reconhecido. Digite /help para ver os comandos disponíveis."
}
```

## 📊 Logs e Monitoramento

O sistema gera logs detalhados em:

- **Console**: Durante desenvolvimento
- **logs/telegrambot.log**: Log principal com rotação automática
- **logs/error.log**: Apenas erros críticos

### Níveis de Log
- `INFO`: Operações normais
- `WARN`: Situações que merecem atenção
- `ERROR`: Erros que precisam ser corrigidos
- `DEBUG`: Informações detalhadas para desenvolvimento

## 🔒 Segurança

- Todas as credenciais são carregadas via variáveis de ambiente
- Validação robusta de entrada em todos os métodos
- Tratamento adequado de exceções
- Logs não expõem informações sensíveis
- Conexões seguras com Firebase e Telegram

## 🛠️ Desenvolvimento

### Adicionar Nova Funcionalidade

1. Crie os modelos necessários em `models/`
2. Implemente a lógica no `services/`
3. Adicione handlers em `handlers/`
4. Configure as constantes em `config/Config.java`
5. Atualize a documentação

### Executar Testes
```bash
mvn test
```

### Gerar Relatório de Cobertura
```bash
mvn jacoco:report
```

## 🐛 Solução de Problemas

### Bot não inicia
- Verifique se o token do bot está correto
- Confirme se todas as variáveis de ambiente estão configuradas
- Verifique os logs para erros específicos

### Erro de conexão com Firebase
- Valide as credenciais do Firebase
- Confirme se o projeto está ativo
- Verifique a conectividade com a internet

### Pagamentos não processam
- Implemente a integração com Mercado Pago
- Verifique as configurações de webhook
- Monitore os logs de pagamento

## 📝 Licença

Este projeto está sob a licença MIT. Consulte o arquivo `LICENSE` para mais informações.

## 👨‍💻 Autor

**Eduardo Lanzarin**
- GitHub: [@edulanzarin](https://github.com/edulanzarin)

## 🤝 Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para:

1. Fazer fork do projeto
2. Criar uma branch para sua feature
3. Fazer commit das alterações
4. Enviar um pull request

## 📞 Suporte

Para suporte ou dúvidas:
- Abra uma issue no GitHub
- Entre em contato através do email do projeto

---

⭐ **Se este projeto foi útil, considere dar uma estrela no GitHub!**