# 🤖 Telegram Bot - Sistema de Assinaturas

Um bot do Telegram desenvolvido em Java para gerenciamento de assinaturas com integração Firebase e processamento de pagamentos.

## 📋 Sobre o Projeto

Este projeto implementa um sistema completo de assinaturas através de um bot do Telegram. O sistema permite que usuários se cadastrem, escolham entre diferentes planos de assinatura, processem pagamentos e tenham controle de acesso baseado em suas assinaturas ativas.

## 🎯 Objetivo

Criar uma plataforma automatizada para venda e gerenciamento de assinaturas digitais, oferecendo:
- Cadastro automático de usuários
- Múltiplos planos de assinatura (Mensal, Trimestral, Semestral, Vitalício)
- Processamento de pagamentos integrado
- Controle de acesso baseado em assinaturas
- Gerenciamento de dados em tempo real

## 🏗️ Arquitetura

### Tecnologias Principais
- **Java 24**: Linguagem de desenvolvimento
- **Maven**: Gerenciamento de dependências
- **Telegram Bots API**: Interface com o Telegram
- **Firebase Firestore**: Banco de dados NoSQL
- **Logback**: Sistema de logging

### Estrutura do Sistema
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Telegram      │    │   Java Bot       │    │   Firebase      │
│   Users         │◄──►│   Application    │◄──►│   Firestore     │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌──────────────────┐
                       │   Payment        │
                       │   System         │
                       └──────────────────┘
```

## 🔧 Funcionalidades Implementadas

### Core Features
- **Cadastro Automático**: Registro de usuários ao primeiro contato
- **Sistema de Comandos**: Processamento de comandos do Telegram (/start, /help)
- **Gerenciador de Respostas**: Mensagens dinâmicas armazenadas no banco
- **Controle de Assinaturas**: Verificação e validação de planos ativos

### Modelos de Dados
- **Usuario**: Informações e vínculos de assinatura
- **Assinatura**: Controle de planos e períodos de validade
- **Pagamento**: Transações e status de processamento
- **TipoPlano**: Enum com os planos disponíveis (valores e durações)

### Serviços
- **Firebase Service**: Operações CRUD completas no Firestore
- **MercadoPago Service**: Estrutura preparada para integração de pagamentos

## 💰 Modelo de Negócio

### Planos Disponíveis
| Tipo | Duração | Valor | Economia |
|------|---------|-------|----------|
| Mensal | 1 mês | R$ 29,90 | - |
| Trimestral | 3 meses | R$ 79,90 | 10% |
| Semestral | 6 meses | R$ 149,90 | 15% |
| Vitalício | 100 anos | R$ 999,90 | Máxima |

## 🛠️ Características Técnicas

### Qualidade de Código
- **Thread-Safe**: Operações seguras para concorrência
- **Validação Robusta**: Verificação de entrada em todos os métodos
- **Tratamento de Exceções**: Exceções customizadas e logging detalhado
- **Configuração Centralizada**: Classe Config com todas as constantes
- **Logging Profissional**: Sistema Logback com rotação de arquivos

### Segurança
- Carregamento seguro de credenciais via variáveis de ambiente
- Validação de dados de entrada
- Tratamento adequado de informações sensíveis
- Conexões criptografadas com APIs externas

### Escalabilidade
- Arquitetura preparada para alta concorrência
- Banco de dados NoSQL para performance
- Sistema de logging para monitoramento
- Estrutura modular para fácil extensão

## 📊 Estado Atual

### ✅ Implementado
- Sistema base do bot com handlers de mensagem
- Integração completa com Firebase Firestore
- Modelos de dados para usuários, assinaturas e pagamentos
- Sistema de logging profissional com Logback
- Configuração centralizada e validações robustas
- Carregamento de variáveis de ambiente

### 🔄 Em Desenvolvimento
- Integração com Mercado Pago para processamento de pagamentos
- Interface de administração para gerenciamento
- Sistema de notificações para renovações
- Métricas e analytics de uso

### 🎯 Próximos Passos
- Implementar webhooks para confirmação de pagamentos
- Adicionar sistema de cupons de desconto
- Criar dashboard administrativo
- Implementar sistema de afiliados
- Adicionar testes automatizados

## 📈 Potencial de Expansão

O projeto foi desenvolvido com arquitetura modular, permitindo:
- Integração com múltiplos gateways de pagamento
- Adição de novos tipos de planos
- Implementação de sistema de afiliados
- Extensão para outros canais de comunicação
- Integração com CRMs e ferramentas de marketing

## 👨‍💻 Desenvolvimento

**Autor**: Eduardo Lanzarin  
**Linguagem**: Java 24  
**Paradigma**: Orientado a Objetos  
**Padrões**: Singleton, Factory, Observer  
**Arquitetura**: MVC com separação de responsabilidades  

---

*Sistema desenvolvido para automatizar vendas de assinaturas digitais através do Telegram, com foco em escalabilidade e confiabilidade.*