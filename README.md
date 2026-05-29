# QA Framework Web — Template de Automação

Template de automação web com **Selenium 4**, **Cucumber 7** (BDD) e **JUnit 5**,
focado em boas práticas: Page Object Model, relatórios PDF com evidências e execução parametrizada por perfis Maven.

---

## Pré-requisitos

| Ferramenta | Versão mínima |
|------------|---------------|
| Java       | 17 (LTS)      |
| Maven      | 3.9+          |
| Chrome / Edge / Firefox | versão atual |
| Git        | 2.x           |

> O [WebDriverManager](https://github.com/bonigarcia/webdrivermanager) faz o download do driver automaticamente — não é necessário instalar ChromeDriver manualmente.

---

## Configuração inicial

### 1. Clone o repositório

```bash
git clone https://github.com/Damasceno-Danilo/Template_Automation_Web.git
cd Template_Automation_Web
```

### 2. Configure os dados de teste

Copie o arquivo de exemplo e preencha com suas credenciais:

```bash
cp src/test/resources/test-data.properties.example \
   src/test/resources/test-data.properties
```

Edite `test-data.properties`:

```properties
test.user.valid=SEU_USUARIO
test.password.valid=SUA_SENHA
test.user.invalid=usuario_invalido_123
test.password.invalid=senha_errada_123
```

> **Segurança:** `test-data.properties` está no `.gitignore` e **nunca** será commitado.
> Em pipelines de CI, use os **Secrets** do repositório (veja a seção CI/CD).

---

## Executando os testes

### Perfis disponíveis

| Perfil      | Comando                   | O que executa                              |
|-------------|---------------------------|--------------------------------------------|
| *(padrão)*  | `mvn test`                | Tag definida em `junit-platform.properties`|
| `local`     | `mvn test -P local`       | Todos os cenários `@loginteste`            |
| `smoke`     | `mvn test -P smoke`       | Cenários críticos rápidos                  |
| `regression`| `mvn test -P regression`  | Tudo exceto `@wip`                         |
| `ci`        | `mvn test -P ci`          | Igual a regression (usado no pipeline)     |

### Tag avulsa (sem alterar código)

```bash
mvn test -Dcucumber.filter.tags="@loginTodosCampos"
mvn test -Dcucumber.filter.tags="@loginCamposObg or @loginInvalidPassword"
```

### Gerar relatório HTML

```bash
mvn verify -P regression
# Relatório em: target/cucumber-html-reports/
```

---

## Estrutura do projeto

```
src/
└── test/
    ├── java/br/com/ddamasceno/
    │   ├── core/                  # WebDriver, WebActions, Hooks, TestReport
    │   │   └── config/            # TestDataConfig (resolução de credenciais)
    │   ├── maps/                  # Page Objects (localizadores)
    │   │   └── advantageShopping/
    │   ├── logic/                 # Lógica de negócio dos testes
    │   │   └── advantageShopping/
    │   ├── steps/                 # Step definitions Cucumber
    │   │   └── advantageShopping/
    │   └── Runner/                # RunnerTests (JUnit 5 Suite)
    └── resources/
        ├── features/
        │   ├── advantage/         # Cenários de login
        │   └── google/            # Cenários Google (@wip — pendente)
        ├── junit-platform.properties   # Config central do Cucumber
        ├── test-data.properties        # Credenciais (GITIGNORED)
        ├── test-data.properties.example
        ├── report.properties           # Metadados do relatório PDF
        └── log4j2.xml
```

---

## Convenções de nomenclatura

### Tags Cucumber

| Tag | Propósito |
|-----|-----------|
| `@loginteste` | Agrupa todos os cenários de login |
| `@loginCamposObg` | Login com campos obrigatórios |
| `@loginTodosCampos` | Login com todos os campos |
| `@iconeFechaModal` | Fecha modal de login |
| `@loginInvalidUser` | Login com usuário inválido |
| `@loginInvalidPassword` | Login com senha inválida |
| `@wip` | Cenário em desenvolvimento — excluído do CI |

### Tokens de dados de teste (nos `.feature`)

Use tokens `[CHAVE]` nos arquivos `.feature` para referenciar dados sem expor credenciais:

| Token | Dado resolvido |
|-------|---------------|
| `[VALID_USER]` | Usuário válido (`test.user.valid`) |
| `[VALID_PASSWORD]` | Senha válida (`test.password.valid`) |
| `[INVALID_USER]` | Usuário inválido (`test.user.invalid`) |
| `[INVALID_PASSWORD]` | Senha inválida (`test.password.invalid`) |

---

## Relatórios e evidências

- **PDF por cenário:** `src/evidencias/{data}/{Passed|Failed}/`
- **Relatório HTML:** `target/cucumber-html-reports/` (gerado com `mvn verify`)
- Screenshots ficam em `src/evidencias/screenshots/` (ignorado pelo Git)

---

## CI/CD (GitHub Actions)

### Workflows

| Workflow | Arquivo | Trigger |
|----------|---------|---------|
| PR Smoke | `pr.yml` | Pull Request → `main` |
| Regression CI | `ci.yml` | Push → `main`/`develop` ou manual |

### Configurar Secrets no GitHub

Acesse **Settings → Secrets and variables → Actions** e adicione:

| Secret | Descrição |
|--------|-----------|
| `TEST_USER_VALID` | Usuário válido para login |
| `TEST_PASSWORD_VALID` | Senha válida |
| `TEST_USER_INVALID` | Usuário inválido para teste negativo |
| `TEST_PASSWORD_INVALID` | Senha inválida para teste negativo |

Os relatórios HTML e PDFs são publicados como **Artifacts** de cada execução (30 dias de retenção).

---

## Stack de tecnologias

| Tecnologia | Versão | Finalidade |
|------------|--------|-----------|
| Java | 17 | Linguagem |
| Selenium | 4.20 | WebDriver API |
| Cucumber | 7.18 | BDD framework |
| JUnit | 5.10 | Test runner |
| Maven | 3.9 | Build & lifecycle |
| WebDriverManager | 5.9 | Driver automático |
| iText7 | 7.1 | Relatórios PDF |
| Log4j2 | 2.24 | Logging |
| Lombok | 1.18 | Redução de boilerplate |

---

## Autor

**Danilo Augusto Damasceno**
