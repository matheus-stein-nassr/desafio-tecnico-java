# Desafio Técnico - Gestão de Eventos

Aplicação full-stack para cadastro e gerenciamento de eventos, desenvolvida como solução para o desafio técnico com Java, Spring Boot, Angular, PostgreSQL, Docker e Git.

O sistema permite:

- listar eventos com paginação;
- consultar os detalhes de um evento;
- cadastrar e editar eventos;
- excluir eventos por soft delete;
- validar os dados enviados;
- executar toda a aplicação com Docker Compose.

## Tecnologias

### Backend

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Flyway
- SpringDoc OpenAPI
- JUnit, Mockito e MockMvc
- H2 para testes

### Frontend

- Angular 17
- Angular Material
- Reactive Forms
- HttpClient
- SCSS
- Nginx

### Infraestrutura

- Docker
- Docker Compose
- Rede dedicada entre os containers
- Volume persistente para o PostgreSQL

## Arquitetura

O repositório utiliza uma estrutura de monorepo:

```text
desafio-tecnico-java/
├── backend/                 # API REST Spring Boot
├── frontend/                # Aplicação Angular
├── docker-compose.yml       # Orquestração dos serviços
├── .env.example             # Modelo das variáveis de ambiente
└── README.md
```

No backend, o fluxo principal segue:

```text
Controller -> Service -> Repository -> PostgreSQL
```

O frontend está organizado em:

- `core`: modelos, serviços globais e interceptor HTTP;
- `shared`: componentes, pipes e módulos reutilizáveis;
- `features/events`: listagem, formulário, detalhes e rotas de eventos.

Em produção, o Nginx serve os arquivos Angular e encaminha as requisições `/api` para o backend.

## Pré-requisitos

Para executar a aplicação completa:

- Docker Desktop
- Docker Compose

Para desenvolvimento local sem reconstruir as imagens:

- Java 17
- Node.js 20 ou superior
- npm

## Executar com Docker

Na raiz do repositório, crie o arquivo local de variáveis de ambiente:

### Windows PowerShell

```powershell
Copy-Item .env.example .env
```

### Linux, macOS ou Git Bash

```bash
cp .env.example .env
```

Se necessário, altere a senha presente no arquivo `.env`. Esse arquivo não deve ser versionado.

Construa as imagens e inicie os serviços:

```bash
docker compose up -d --build
```

Confira o estado dos containers:

```bash
docker compose ps
```

Serviços disponíveis:

| Serviço | Endereço |
|---|---|
| Frontend | http://localhost |
| Backend | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| Health check | http://localhost:8080/actuator/health |
| PostgreSQL | localhost:5433 |

Para acompanhar os logs:

```bash
docker compose logs -f
```

Para encerrar sem apagar os dados:

```bash
docker compose down
```

Para encerrar e remover também o volume do PostgreSQL:

```bash
docker compose down -v
```

> O comando com `-v` apaga os dados cadastrados no banco Docker.

## Desenvolvimento local

### Backend com PostgreSQL

Suba somente o banco de dados:

```bash
docker compose up -d db
```

Configure as variáveis de ambiente e execute o backend.

No Linux, macOS ou Git Bash:

```bash
export DB_URL=jdbc:postgresql://localhost:5433/eventdb
export DB_USERNAME=postgres
export DB_PASSWORD=change-me

cd backend
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5433/eventdb"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="change-me"

cd backend
.\mvnw.cmd spring-boot:run
```

Use em `DB_PASSWORD` o mesmo valor configurado no arquivo `.env`.

### Frontend com recarregamento automático

Em outro terminal:

```bash
cd frontend
npm install
npm start
```

O frontend ficará disponível em `http://localhost:4200`. O proxy de desenvolvimento encaminha `/api` para o backend na porta `8080`.

## Testes

### Backend

No Linux, macOS ou Git Bash:

```bash
cd backend
./mvnw clean test
```

No Windows PowerShell:

```powershell
cd backend
.\mvnw.cmd clean test
```

Os testes incluem testes unitários da camada de serviço e teste de integração com MockMvc e H2.

### Frontend

```bash
cd frontend
npm test -- --watch=false --browsers=ChromeHeadless
npm run build
```

## Endpoints principais

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/events?page=0&size=10` | Lista eventos não excluídos |
| GET | `/api/events/{id}` | Consulta um evento |
| POST | `/api/events` | Cadastra um evento |
| PUT | `/api/events/{id}` | Atualiza um evento |
| DELETE | `/api/events/{id}` | Realiza soft delete |

Exemplo de payload:

```json
{
  "title": "Workshop Spring Boot",
  "description": "Introdução prática ao Spring Boot",
  "eventDateTime": "2026-08-10T19:00:00",
  "location": "Auditório Central"
}
```

## Rotas do frontend

| Rota | Descrição |
|---|---|
| `/events` | Lista paginada |
| `/events/new` | Cadastro |
| `/events/:id` | Detalhes |
| `/events/:id/edit` | Edição |

## Banco de dados e migrations

O schema é gerenciado pelo Flyway. A migration inicial está em:

```text
backend/src/main/resources/db/migration/V1__create_events_table.sql
```

Migrations já executadas não devem ser alteradas. Mudanças futuras no schema devem ser adicionadas em novos arquivos, como `V2__nome_da_alteracao.sql`.

## Documentação adicional

- [Documentação do backend](backend/README.md)
- [Documentação do frontend](frontend/README.md)
- [Arquitetura do frontend](frontend/ARCHITECTURE.md)
