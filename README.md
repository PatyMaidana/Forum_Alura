# ForumHub (desafio)

Projeto Desafio FórumHub Alura (API REST)

Resumo
- Backend em Java com Spring Boot.
- Persistência com Spring Data JPA + MySQL.
- Migrações gerenciadas com Flyway.
- Segurança com Spring Security e autenticação JWT.
- Documentação OpenAPI gerada por springdoc (Swagger UI).

Funcionalidades principais
- Autenticação: endpoint `/login` que retorna token JWT.
- Gerenciamento de usuários (autores), cursos, tópicos e respostas via endpoints REST.
- Regras de segurança: a maior parte das rotas exige autenticação JWT; somente `/login` e rotas do OpenAPI/SWAGGER são permitidas sem autenticação.
- Migrations automaticas: Flyway aplica scripts em `src/main/resources/db/migration`.

Arquitetura / pacotes importantes
- `br.com.alura.forumhub` - aplicação principal
- `controller` - controllers REST (AuthController, AutorController, CursoController, TopicoController)
- `domain` - modelos/DTOs/exceções
- `infra.security` - configuração de segurança, filtro JWT e geração/validação de tokens
- `infra.springdoc` - configuração do OpenAPI (adiciona esquema de segurança Bearer)
- `config` - configuração global (CORS)

Dependências principais (ver `pom.xml`)
- Spring Boot (web, data-jpa, security, validation)
- springdoc-openapi-starter-webmvc-ui
- com.auth0:java-jwt
- MySQL Connector/J
- Flyway
- Lombok (apenas para desenvolvimento/compilação)

Configuração / Prérequisitos
- Java 17+ (o `pom.xml` indica Java 17, em execução Java mais recente também funciona).
- Maven 3.6+
- MySQL rodando (ou use outro banco trocando a URL/propriedades).

Arquivo de configuração (exemplo em `src/main/resources/application.properties`):
- spring.datasource.url=jdbc:mysql://localhost:3306/forumhub
- spring.datasource.username=patricia
- spring.datasource.password=MySQL@02
- api.security.token.secret=12345678

Observação: os valores acima são exemplos; altere conforme seu ambiente.

Como clonar e rodar (passo a passo)
1. Clonar o repositório

   git clone <repo-url>
   cd desafio_forumhub

2. Configurar o banco de dados MySQL
- Criar um banco `forumhub` (ou ajustar `spring.datasource.url` em `application.properties`).
- Garantir que o usuário e senha configurados em `application.properties` existam.
- As migrations Flyway em `src/main/resources/db/migration` serão aplicadas automaticamente na primeira execução.

3. Buildar e rodar com Maven
- Build (opcional):

   mvn clean package

- Rodar direto com o Maven (padrão porta 8080):

   mvn -DskipTests spring-boot:run

- Rodar em outra porta (ex.: 8081) se 8080 estiver ocupada:

   mvn -DskipTests spring-boot:run -Dserver.port=8081

- Ou rodar o jar gerado:

   java -jar target/*.jar

4. Acessar a API e a documentação
- Swagger UI (OpenAPI): http://localhost:8080/swagger-ui/index.html  (ou substituir a porta se alterar com `-Dserver.port`)
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Possíveis problemas e soluções (inclui o erro: "Fetch error response status is 403 /v3/api-docs")

Cenários e verificações rápidas

- Aplicação não inicia / porta 8080 ocupada:
  - Diagnosticar o processo que está usando a porta no Windows (PowerShell):

    Get-NetTCPConnection -LocalPort 8080 | Format-List
    Stop-Process -Id <PID>

  - Ou rodar a aplicação em outra porta: `-Dserver.port=8081`.

- Erro 403 ao acessar `/v3/api-docs` pelo Swagger UI ("Fetch error: response status is 403 /v3/api-docs") — causas e correções:
  1. Security bloqueando: no projeto existe `SecurityConfig` que explicitamente permite (`permitAll`) as rotas relacionadas ao OpenAPI (`/v3/api-docs`, `/v3/api-docs/**`, `/swagger-ui/**`, etc.). Portanto, em princípio, essas rotas não deveriam retornar 403. Verifique:
     - Se você customizou `SecurityConfig`, confirme que os padrões e paths estão corretos.
     - Se existe outro filtro/instância de Security em runtime que possa estar aplicando regras diferentes.

  2. Header Authorization indevido:
     - O Swagger UI faz chamadas ao backend para pegar o JSON. Se, por alguma configuração do navegador ou extensão, o header `Authorization` for incluído com um token inválido, a API pode responder com 403/401. Abra o DevTools (Network) e verifique a requisição para `/v3/api-docs` e os headers enviados.
     - No projeto há um `SecurityFilter` que tenta validar o token caso o header `Authorization` exista. Se o token for inválido, o filtro apenas registra a exceção e o fluxo continua, mas dependendo da lógica de autenticação do Spring Security, um token inválido ou credenciais podem causar 401/403. Para testar, remova temporariamente o token do Swagger UI (no botão Authorize) e recarregue.

  3. CORS / Origem diferente:
     - Esse projeto tem `CorsConfiguration` que permite apenas `http://localhost:8080` como origem. Se você estiver acessando o Swagger UI a partir de outro host/porta (por exemplo `http://127.0.0.1:8080` ou de uma UI separada), a preflight OPTIONS pode falhar.
     - Para teste rápido permitir todas origens (não recomendado em produção):

       registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");

  4. Springdoc e mapeamento de paths:
     - Versões diferentes da dependência `springdoc-openapi` têm pequenas diferenças no path do Swagger UI. No projeto está usando `springdoc-openapi-starter-webmvc-ui` (versão 2.8.4). URLs esperadas:
       - /v3/api-docs
       - /swagger-ui/index.html

  5. Logs do servidor:
     - Abra os logs (console) e procure por mensagens de Spring Security quando a requisição ocorre. Geralmente a razão do 403 está detalhada nos logs.

Como desabilitar temporariamente a segurança (apenas para debug)
- Editar `SecurityConfig` e, no método `securityFilterChain`, usar `authorizeHttpRequests(req -> req.anyRequest().permitAll())` e reiniciar. Não deixe assim em produção.

Melhorias sugeridas
- Permitir múltiplas origens no CORS durante desenvolvimento (ex.: localhost em diferentes portas).
- Adicionar variável de ambiente para `api.security.token.secret` e não manter segredo em plain-text em `application.properties`.
- Adicionar instruções de como gerar usuário inicial (seed) caso necessário.

Quality gates / checagem rápida
- Build: mvn clean package — OK (ver `pom.xml`).
- Migrations: aplicadas automaticamente via Flyway ao iniciar a app.
- Swagger: configurado via `springdoc` e `SpringDocConfig` adicionando esquema Bearer.
- Segurança: JWT com `api.security.token.secret` e `SecurityFilter` para validar tokens.

Contribuição
- Abra um issue descrevendo o bug ou feature, ou envie um PR.

Licença
- Projeto exemplo - ver mantenedor.

---
Se quiser, eu posso também:
- Adicionar instruções para criar um usuário inicial no banco (script SQL ou data seeding).
- Ajustar o `CorsConfiguration` para permitir múltiplas origens durante desenvolvimento.
- Adicionar um `application-dev.properties` de exemplo com variáveis de ambiente.


