# Event Management API

API REST para gerenciamento de eventos desenvolvida com Spring Boot.

## Visão geral

A aplicação permite:
- criar eventos
- listar eventos com paginação
- buscar evento por ID
- atualizar evento
- remover evento com soft delete (marca como deletado)

## Tecnologias

- Java 17
- Spring Boot 3.5.16
- Spring Web
- Spring Data JPA
- Spring Validation
- PostgreSQL
- Flyway
- SpringDoc OpenAPI (Swagger)
- Lombok
- H2 (testes)

## Estrutura principal

- API: `src/main/java/com/challenge/event_management/controller/EventController.java`
- Serviço: `src/main/java/com/challenge/event_management/service/impl/EventServiceImpl.java`
- Entidade: `src/main/java/com/challenge/event_management/entity/Event.java`
- DTOs: `src/main/java/com/challenge/event_management/dto/`
- Exceções globais: `src/main/java/com/challenge/event_management/exception/GlobalExceptionHandler.java`
- Migração Flyway: `src/main/resources/db/migration/V1__create_events_table.sql`

## Configuração

### Ambiente padrão (PostgreSQL)
Arquivo: `src/main/resources/application.yaml`

Configuração atual:
- porta da aplicação: `8080`
- banco: `jdbc:postgresql://localhost:5433/eventdb`
- usuário: `postgres`
- senha: `admin`
- `ddl-auto: validate`
- Flyway habilitado

### Ambiente de testes (H2 em memória)
Arquivo: `src/test/resources/application-test.yml`

- banco H2 em memória com compatibilidade PostgreSQL
- Flyway habilitado

## Como executar

No Windows PowerShell, na raiz do projeto:

```powershell
cd "C:\Users\Matheus\Desktop\DESAFIO MIRANTE\event-management"
.\mvnw clean package
.\mvnw spring-boot:run
```

A API ficará disponível em:
- `http://localhost:8080`

Documentação Swagger:
- `http://localhost:8080/swagger-ui/index.html`

## Executar testes

```powershell
cd "C:\Users\Matheus\Desktop\DESAFIO MIRANTE\event-management"
.\mvnw clean test
```

## Endpoints

Base path: `/api/events`

- `GET /api/events?page=0&size=10` - lista eventos não deletados (paginado)
- `GET /api/events/{id}` - busca evento por ID
- `POST /api/events` - cria evento
- `PUT /api/events/{id}` - atualiza evento
- `DELETE /api/events/{id}` - soft delete

### Exemplo de payload para criar/atualizar evento

```json
{
  "title": "Workshop Spring Boot",
  "description": "Introdução prática",
  "eventDateTime": "2026-08-10T19:00:00",
  "location": "Auditório Central"
}
```

## Validações de entrada

`EventRequestDTO`:
- `title`: obrigatório, máximo 100 caracteres
- `description`: opcional, máximo 1000 caracteres
- `eventDateTime`: obrigatório, data/hora presente ou futura
- `location`: obrigatório, máximo 200 caracteres

## Tratamento de erros

A API possui tratamento global de exceções em `GlobalExceptionHandler`:
- `ResourceNotFoundException` -> `404 Not Found`
- `MethodArgumentNotValidException` -> `400 Bad Request`
- `Exception` (genérica) -> `500 Internal Server Error`

Formato padrão de erro:

```json
{
  "timestamp": "2026-07-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação nos campos enviados",
  "path": "/api/events",
  "fieldErrors": {
    "title": "must not be blank"
  }
}
```

## Banco de dados e migrações

A tabela `events` é criada por Flyway na migração:
- `src/main/resources/db/migration/V1__create_events_table.sql`

Campos principais:
- `id`
- `title`
- `description`
- `event_date_time`
- `location`
- `deleted`
- `created_at`
- `updated_at`

## Testes existentes

- `src/test/java/com/challenge/event_management/EventManagementApplicationTests.java`
- `src/test/java/com/challenge/event_management/controller/EventControllerIntegrationTest.java`
- `src/test/java/com/challenge/event_management/service/EventServiceImplTest.java`
