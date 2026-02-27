
# 🚀 EventHub Management Service

> Serviço responsável pelo gerenciamento centralizado de eventos dentro
> do ecossistema **EventHub**,
> disponibilizando APIs REST para cadastro, consulta e administração de
> eventos e participantes.

------------------------------------------------------------------------

## 🧱 Arquitetura

O projeto segue o modelo **Arquitetura Hexagonal (Ports & Adapters)**:

- Separação entre domínio e infraestrutura
- Baixo acoplamento
- Alta testabilidade
- Evolução sustentável

------------------------------------------------------------------------

## ⚙️ Configuração do Ambiente Local

Antes de executar o projeto, é necessário criar os arquivos de
configuração locais.

------------------------------------------------------------------------

## 📄 1. Arquivo `.env` (Docker)

Crie um arquivo chamado:

    .env

na raiz do projeto contendo:

```env
# PostgreSQL
POSTGRES_CONTAINER_NAME=postgres
POSTGRES_USER=seu_usuario
POSTGRES_PASSWORD=sua_senha_forte
POSTGRES_DB=eventhub_db
POSTGRES_PORT=5432

# Redis
REDIS_CONTAINER_NAME=redis
REDIS_PORT=6379
REDIS_URL=redis://usuario:sua_senha@localhost:6379

# Keycloak Database
KEYCLOAK_DB_CONTAINER_NAME=keycloak-db
KEYCLOAK_DB_ROOT_PASSWORD=senha_root
KEYCLOAK_DB_NAME=keycloak_db
KEYCLOAK_DB_USERNAME=usuario_keycloak
KEYCLOAK_DB_PASSWORD=senha_keycloak
KEYCLOAK_DB_URL=jdbc:mysql://keycloak-db:3306/keycloak_db

# Keycloak
KEYCLOAK_CONTAINER_NAME=keycloak
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=admin
KEYCLOAK_PORT=8080

# RabbitMQ
RABBITMQ_ADMIN_USER=admin
RABBITMQ_ADMIN_PASSWORD=admin
```

⚠️ **Nunca versionar o arquivo `.env` no Git.**  
Adicione-o ao `.gitignore`.

------------------------------------------------------------------------

## 📄 2. Arquivo `application-local.properties`

Crie o arquivo:

    src/main/resources/application-local.properties

com um exemplo de configuração:

```properties
server.port=8081

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/seu_database
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=none
spring.flyway.locations=classpath:db/migration

spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.connection-timeout=30000

spring.jpa.show-sql=true

# Security (Keycloak)
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/app-eventhub-management-realm
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8080/realms/app-eventhub-management-realm/protocol/openid-connect/certs

# Keycloak Admin Client
keycloak.auth-server-url=http://localhost:8080
keycloak.realm=app-eventhub-management-realm
keycloak.client-id=eventhub-admin-cli
keycloak.client-secret=${KEYCLOAK_CLIENT_SECRET}

# Observability
management.datadog.metrics.export.apiKey=seu_token_datadog

# Redis
spring.cache.type=redis
spring.cache.redis.time-to-live=24h
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.username=usuario_redis
spring.data.redis.password=senha_redis
spring.data.redis.timeout=2s

# RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=admin
spring.rabbitmq.password=admin
spring.rabbitmq.listener.simple.prefetch=1
spring.rabbitmq.listener.simple.concurrency=1
spring.rabbitmq.listener.simple.max-concurrency=1

```

------------------------------------------------------------------------

## 🐳 Subir Infraestrutura

```bash
docker-compose up -d
```

------------------------------------------------------------------------

## ▶️ Executar Aplicação

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

------------------------------------------------------------------------

## 🔐 Segurança

- Keycloak
- OAuth2
- JWT
- Controle de acesso por roles

------------------------------------------------------------------------

## 🛠 Tecnologias

- Java 21
- Spring Boot
- PostgreSQL
- Flyway
- Redis
- Keycloak
- Docker
- Kubernetes
- JUnit 5
- Testcontainers

------------------------------------------------------------------------

## 🎯 Objetivo

Centralizar o gerenciamento de eventos do **EventHub**, garantindo
segurança, escalabilidade e execução em ambientes cloud-native.

------------------------------------------------------------------------
------------------------------------------------------------------------

# 🔐 Configuração Completa do Keycloak

## 1️⃣ Criar Realm

Nome do Realm:
app-eventhub-management-realm

---

## 2️⃣ Criar Realm Roles

Criar as seguintes roles:

- ADMIN
- MANAGER
- USER

---

## 3️⃣ Criar Clients

### Client 1 – eventhub-api

- Client Authentication: OFF
- Standard Flow: ON

### Client 2 – eventhub-admin-cli

- Client Authentication: ON
- Service Account Roles: ON

Atribuir no Service Account:

- manage-users
- view-users
- query-users

---

## 4️⃣ Criar Usuário ADMIN

Criar usuário conforme migration V3__create_default_admin_user.sql.

Atribuir a role:

- ADMIN

---

## 5️⃣ Gerar Token do ADMIN

Endpoint:

POST http://localhost:8080/realms/app-eventhub-management-realm/protocol/openid-connect/token

Body (x-www-form-urlencoded):

- grant_type=password
- client_id=eventhub-api
- username=admin@eventhub.local
- password=Admin@123

---

## 6️⃣ Criar Usuários MANAGER

Utilizar o token ADMIN como Bearer Token nas chamadas da API.

Apenas usuários com role ADMIN podem criar MANAGER.
