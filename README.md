# Workshop CRM - Sistema de Gerenciamento para Oficinas Mecânicas

Este sistema é um Mini CRM desenvolvido especificamente para oficinas mecânicas, permitindo o gerenciamento completo de clientes, veículos, produtos, ordens de serviço e tipos de serviços.

## Sobre o Projeto

O Workshop CRM é uma aplicação web completa que oferece as seguintes funcionalidades:

- **Gerenciamento de Clientes**: Cadastro e manutenção de dados de clientes (pessoas físicas e jurídicas)
- **Controle de Veículos**: Registro detalhado de veículos com informações como placa, marca, modelo, ano, quilometragem e histórico de manutenções
- **Catálogo de Produtos**: Gerenciamento de peças, óleos, filtros e outros itens utilizados nos serviços
- **Ordens de Serviço**: Criação e acompanhamento de ordens de serviço, desde a abertura até a conclusão
- **Tipos de Serviço**: Configuração de diferentes tipos de serviços oferecidos pela oficina com preços padronizados

O sistema foi desenvolvido utilizando Spring Boot para o backend e Angular para o frontend, oferecendo uma interface moderna e responsiva.

## Roadmap de Funcionalidades

### Já Implementado ✅

- **Cadastro Completo de Clientes e Veículos**: Sistema de cadastro com validações e relacionamentos
- **Gerenciamento de Produtos**: Cadastro de peças e insumos com controle de preços
- **Ordens de Serviço**: Fluxo completo desde a criação até a conclusão
- **Tipos de Serviço**: Configuração de serviços padrão com preços pré-definidos
- **Autenticação e Autorização**: Sistema de login com diferentes níveis de acesso
- **Dados de Exemplo**: Conjunto de dados realistas para testes e demonstração

### Próximas Implementações 🚀

- **Sistema de Notificações**:

  - Envio de notificações via e-mail, WhatsApp e SMS para clientes
  - Alertas sobre status de ordens de serviço
  - Lembretes de manutenções programadas
  - Confirmações de agendamentos

- **Dashboard Analítico**:

  - Implementação de gráficos interativos usando NgxCharts ou Chart.js
  - KPIs de desempenho da oficina (faturamento, serviços mais realizados)
  - Análise de tendências de serviços por período
  - Visualização de clientes mais frequentes

- **Sistema de Feedback**:

  - Criação de modelos de formulários de satisfação
  - Envio automático após conclusão de ordens de serviço
  - Análise de resultados e geração de relatórios
  - Integração com o dashboard para visualização de métricas de satisfação

- **Agendamento Online**:

  - Calendário de disponibilidade da oficina
  - Agendamento de serviços pelo cliente via portal
  - Confirmações automáticas e lembretes

- **Aplicativo Mobile**:
  - Versão para dispositivos móveis para clientes acompanharem seus serviços
  - Notificações push sobre andamento de serviços

## Executando a Aplicação

### Pré-requisitos

- Java 17 ou superior
- Docker e Docker Compose (para o banco de dados PostgreSQL)

### Passos para Execução

1. **Iniciar o banco de dados PostgreSQL**:

```bash
docker compose -f src/main/docker/postgresql.yml up -d
```

2. **Executar a aplicação**:

```bash
./mvnw
```

A aplicação estará disponível em [http://localhost:8080](http://localhost:8080)

### Usuários Padrão para Testes

O sistema vem configurado com dois usuários para testes:

- **Administrador**:

  - Login: admin
  - Senha: admin

- **Usuário Comum**:
  - Login: user
  - Senha: user

## Como Usar o Sistema

### Fluxo Básico de Trabalho

1. **Cadastro de Clientes**:

   - Acesse o menu "Clientes" e clique em "Criar novo Cliente"
   - Preencha os dados do cliente (pessoa física ou jurídica)
   - Salve o cadastro

2. **Cadastro de Veículos**:

   - Acesse o menu "Veículos" e clique em "Criar novo Veículo"
   - Selecione o cliente proprietário do veículo
   - Preencha os dados do veículo (placa, marca, modelo, etc.)
   - Salve o cadastro

3. **Cadastro de Produtos**:

   - Acesse o menu "Produtos" e clique em "Criar novo Produto"
   - Preencha os dados do produto (nome, descrição, marca, preços)
   - Salve o cadastro

4. **Cadastro de Tipos de Serviço**:

   - Acesse o menu "Tipos de Serviço" e clique em "Criar novo Tipo de Serviço"
   - Defina o nome, descrição e preço base do serviço
   - Salve o cadastro

5. **Criação de Ordem de Serviço**:

   - Acesse o menu "Ordens de Serviço" e clique em "Criar nova Ordem de Serviço"
   - Selecione o veículo do cliente
   - Escolha o tipo de serviço a ser realizado
   - Adicione produtos utilizados no serviço
   - Defina custos e observações
   - Salve a ordem de serviço

6. **Acompanhamento de Ordens de Serviço**:
   - Acesse o menu "Ordens de Serviço" para visualizar todas as ordens
   - Utilize os filtros para encontrar ordens específicas
   - Atualize o status das ordens conforme o andamento do serviço

### Dicas de Uso

- Mantenha o cadastro de clientes e veículos sempre atualizado
- Registre detalhadamente as observações nas ordens de serviço
- Utilize os filtros de busca para localizar rapidamente informações
- Acompanhe regularmente as ordens de serviço em andamento

## Dados de Exemplo

O sistema vem pré-configurado com dados de exemplo para facilitar os testes:

- **Clientes**: 10 clientes fictícios (pessoas físicas e jurídicas)
- **Veículos**: 10 veículos de diferentes marcas e modelos
- **Produtos**: 10 produtos automotivos (óleos, filtros, peças)
- **Tipos de Serviço**: 10 tipos de serviços comuns em oficinas
- **Ordens de Serviço**: 10 ordens de serviço em diferentes status

Estes dados permitem testar todas as funcionalidades do sistema imediatamente após a instalação.

## Tecnologias Utilizadas

- **Backend**: Spring Boot, Spring Data JPA, Spring Security
- **Frontend**: Angular, Bootstrap
- **Banco de Dados**: PostgreSQL
- **Ferramentas**: JHipster, Liquibase, Maven
