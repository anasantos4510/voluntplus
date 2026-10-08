# VoluntPlus API

Backend da plataforma VoluntPlus, desenvolvido com Spring Boot para oferecer os recursos de gestão de voluntariado.

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Spring Security e OAuth 2.0 Resource Server
- PostgreSQL 17
- Clerk para autenticação JWT
- OpenAPI/Swagger UI
- Maven
- Docker Compose

## Pré-requisitos

Antes de iniciar, instale:

- JDK 21 ou superior;
- Maven 3.9 ou superior;
- Docker Desktop, com o Docker Compose habilitado;
- Git.

Confirme que as ferramentas estão disponíveis antes de continuar:

```bash
java -version
mvn -version
docker --version
docker compose version
```

O Docker Desktop deve estar aberto e com o mecanismo de contêineres em execução.

## Configuração do ambiente

1. Clone o repositório e entre na pasta criada:

   ```bash
   git clone https://github.com/anasantos4510/voluntplus.git
   cd voluntplus
   ```

   Se o repositório já estiver baixado ou tiver sido clonado com outro nome, entre na pasta que contém o `pom.xml` e o `docker-compose.yml`.

2. Crie o arquivo local de variáveis de ambiente a partir do exemplo:

   No Windows (PowerShell):

   ```powershell
   Copy-Item .env.example .env
   ```

   No macOS ou Linux:

   ```bash
   cp .env.example .env
   ```

3. Preencha o `.env`, principalmente `CLERK_ISSUER_URI`, com a URL do emissor JWT da instância do Clerk usada pelo frontend.

   No painel do Clerk, configure também a custom session claim abaixo. O backend usa esses dados autenticados e não aceita o e-mail informado livremente pelo cliente:

   ```json
   {
     "primaryEmail": "{{user.primary_email_address}}"
   }
   ```

   ```dotenv
   POSTGRES_DB=voluntplus
   POSTGRES_USER=voluntplus
   POSTGRES_PASSWORD=change-me
   POSTGRES_PORT=5432

   DB_URL=jdbc:postgresql://localhost:5432/voluntplus
   DB_USERNAME=voluntplus
   DB_PASSWORD=change-me

   FRONTEND_URL=http://localhost:3000
   CLERK_ISSUER_URI=https://seu-dominio.clerk.accounts.dev
   APP_BUSINESS_ZONE=America/Sao_Paulo
   ```

   | Variável | Finalidade |
   | --- | --- |
   | `POSTGRES_DB` | Nome do banco criado pelo contêiner PostgreSQL. |
   | `POSTGRES_USER` | Usuário administrador do banco no contêiner. |
   | `POSTGRES_PASSWORD` | Senha do usuário do banco. |
   | `POSTGRES_PORT` | Porta do PostgreSQL exposta na máquina local. O padrão é `5432`. |
   | `DB_URL` | URL JDBC utilizada pela aplicação. |
   | `DB_USERNAME` | Usuário utilizado pela aplicação. |
   | `DB_PASSWORD` | Senha utilizada pela aplicação. |
   | `FRONTEND_URL` | Origem autorizada pelo CORS e usada na validação do claim `azp`. |
   | `CLERK_ISSUER_URI` | URL do emissor dos tokens JWT fornecida pelo Clerk. |
   | `APP_BUSINESS_ZONE` | Fuso usado nas regras de negócio dependentes da data, com padrão `America/Sao_Paulo`. |

> O arquivo `.env` contém credenciais locais e não deve ser versionado. Ele já está incluído no `.gitignore`.

## Executando localmente

### 1. Inicie o PostgreSQL

Na pasta que contém o `docker-compose.yml`, execute:

```bash
docker compose up -d --wait postgres
```

Confira se o contêiner ficou saudável:

```bash
docker compose ps
```

### 2. Inicie a aplicação com o perfil local

Em um novo terminal, na mesma pasta, execute:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

No PowerShell, se o argumento do perfil não for interpretado corretamente, coloque-o entre aspas: `"-Dspring-boot.run.profiles=local"`.

A API ficará disponível em `http://localhost:8080`.

O perfil `local` habilita a documentação interativa:

- Swagger UI: <http://localhost:8080/swagger-ui/index.html>
- Especificação OpenAPI: <http://localhost:8080/v3/api-docs>

As rotas da documentação e as consultas públicas de serviços e avaliações não exigem autenticação. As rotas protegidas exigem um token JWT válido no cabeçalho:

```http
Authorization: Bearer <token-do-clerk>
```

### Execução pelo IntelliJ IDEA

1. Abra a pasta que contém o `pom.xml` como projeto Maven.
2. Configure o SDK do projeto para o JDK 21 ou superior.
3. Inicie o PostgreSQL com `docker compose up -d --wait postgres`.
4. Crie uma configuração Maven com o comando `spring-boot:run` e, em **Run parameters**, informe `-Dspring-boot.run.profiles=local`.

O diretório de trabalho da configuração deve ser a pasta que contém o `pom.xml` e o arquivo `.env`.

## Testes e build

Os testes de persistência usam Testcontainers. Por isso, mantenha o Docker Desktop em execução e execute:

```bash
mvn test
```

Gere o pacote da aplicação:

```bash
mvn clean package
```

O arquivo JAR será criado em `target/`. Como a fase `package` também executa os testes, ela igualmente precisa de acesso ao Docker.

## Comandos úteis do banco de dados

```bash
# Acompanhar os logs do PostgreSQL
docker compose logs -f postgres

# Parar o contêiner sem apagar os dados
docker compose stop postgres

# Parar e remover os contêineres, preservando o volume
docker compose down

# Parar e apagar também o volume e todos os dados locais
docker compose down -v
```

> O último comando apaga permanentemente o banco local. Use-o somente quando quiser recriar o ambiente do zero.

## Estrutura principal

```text
src/
├── main/
│   ├── java/br/com/voluntplus/
│   │   ├── config/        # Segurança, CORS e OpenAPI
│   │   └── users/         # Domínio e integrações de usuários
│   └── resources/         # Configurações da aplicação
└── test/                  # Testes automatizados
```

## Observações importantes

- O Hibernate está configurado com `ddl-auto=none`; a criação e a evolução do esquema são feitas pelas migrações do Flyway ao iniciar a aplicação.
- O backend não mantém sessão no servidor; a autenticação é stateless e baseada em JWT.
- O Swagger fica desabilitado por padrão e é habilitado somente pelo perfil `local`.
- Se a porta `5432` estiver ocupada, altere `POSTGRES_PORT` e atualize a porta correspondente em `DB_URL`.
- Se o frontend usar outra porta ou origem, atualize `FRONTEND_URL` e reinicie o backend.

## Solução de problemas

### Falha de conexão com o banco

Verifique se o contêiner está em execução com `docker compose ps` e confirme se `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` correspondem aos valores `POSTGRES_*`.

Se as credenciais `POSTGRES_*` forem alteradas depois da primeira inicialização, o volume existente continuará usando as credenciais antigas. Para recriar o banco local, execute `docker compose down -v` e inicie o serviço novamente. Essa operação apaga os dados locais.

### Erros `401 Unauthorized`

Confirme se:

- o cabeçalho `Authorization` contém um token JWT válido;
- `CLERK_ISSUER_URI` corresponde ao issuer do token;
- o claim `azp` do token, quando presente, corresponde exatamente a `FRONTEND_URL`.

### Erro de CORS

Defina `FRONTEND_URL` com a origem completa do frontend, incluindo protocolo e porta, sem caminhos adicionais. Exemplo: `http://localhost:3000`.

### Docker indisponível nos testes

Se os testes exibirem `Could not find a valid Docker environment`, abra o Docker Desktop, aguarde o mecanismo iniciar e confirme o acesso com `docker info` antes de executar `mvn test` novamente.
