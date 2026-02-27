# 🚀 EventHub Management Service

> Serviço responsável pelo gerenciamento centralizado de eventos dentro
> do ecossistema **EventHub**,\
> disponibilizando APIs REST para cadastro, consulta e administração de
> eventos e participantes.

------------------------------------------------------------------------

## 🧱 Arquitetura

O projeto segue o modelo **Arquitetura Hexagonal (Ports & Adapters)**:

-   Separação entre domínio e infraestrutura
-   Baixo acoplamento
-   Alta testabilidade
-   Evolução sustentável

------------------------------------------------------------------------

## ⚙️ Configuração do Ambiente Local

Antes de executar o projeto, é necessário criar os arquivos de
configuração locais.

------------------------------------------------------------------------

## 📄 1. Arquivo `.env` (Docker)

Crie um arquivo chamado:

    .env

na raiz do projeto contendo:

``` env
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

⚠️ **Nunca versionar o arquivo `.env` no Git.**\
Adicione-o ao `.gitignore`.

------------------------------------------------------------------------

## 📄 2. Arquivo `application-local.properties`

Crie o arquivo:

    src/main/resources/application-local.properties

com um exemplo de configuração:

``` properties
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
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/seu-realm
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8080/realms/seu-realm/protocol/openid-connect/certs

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
```

------------------------------------------------------------------------

## 🐳 Subir Infraestrutura

``` bash
docker-compose up -d
```

------------------------------------------------------------------------

## ▶️ Executar Aplicação

``` bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

------------------------------------------------------------------------

## 🔐 Segurança

-   Keycloak
-   OAuth2
-   JWT
-   Controle de acesso por roles

------------------------------------------------------------------------

## 🛠 Tecnologias

-   Java 21
-   Spring Boot
-   PostgreSQL
-   Flyway
-   Redis
-   Keycloak
-   Docker
-   Kubernetes
-   JUnit 5
-   Testcontainers

------------------------------------------------------------------------

## 🎯 Objetivo

Centralizar o gerenciamento de eventos do **EventHub**, garantindo
segurança, escalabilidade e execução em ambientes cloud-native.
