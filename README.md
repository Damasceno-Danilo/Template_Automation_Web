# QA Framework Web — Template de Automação

Framework de automação web com **Selenium 4**, **Cucumber 7** (BDD) e **JUnit 5**,
focado em boas práticas: Page Object Model, relatórios PDF com evidências e execução parametrizada por perfis Maven.

Todo o código de infraestrutura (driver, actions, hooks, relatório, config) vive em
`br.com.ddamasceno.core` e é **agnóstico de site** — o repositório traz apenas um
fluxo de exemplo (login no [Sauce Demo](https://www.saucedemo.com/)) para demonstrar
o padrão. Para testar uma nova aplicação, basta criar `steps/`, `logic/` e `maps/`
para ela; nenhuma configuração do runner precisa mudar (veja "Adicionando uma nova frente").

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

O exemplo já vem preenchido com as credenciais públicas de teste do Sauce Demo,
então o framework roda "out of the box" sem nenhuma configuração adicional.

> **Segurança:** `test-data.properties` está no `.gitignore` e **nunca** será commitado.
> Para uma frente com credenciais reais, use os **Secrets** do repositório em CI (veja a seção CI/CD).

---

## Executando os testes

### Perfis disponíveis

| Perfil      | Comando                   | O que executa                              |
|-------------|---------------------------|--------------------------------------------|
| *(padrão)*  | `mvn test`                | Tag definida em `junit-platform.properties`|
| `local`     | `mvn test -P local`       | Todos os cenários `@saucedemoLogin`        |
| `smoke`     | `mvn test -P smoke`       | Cenários críticos rápidos                  |
| `regression`| `mvn test -P regression`  | Tudo exceto `@wip`                         |
| `ci`        | `mvn test -P ci`          | Igual a regression (usado no pipeline)     |

### Tag avulsa (sem alterar código)

```bash
mvn test -Dcucumber.filter.tags="@saucedemoLoginValido"
mvn test -Dcucumber.filter.tags="@saucedemoLoginBloqueado or @saucedemoLoginSenhaInvalida"
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
    │   ├── core/                  # WebDriver, WebActions, Hooks, TestReport,
    │   │   │                      # RunnerTests, RunnerInfo, BaseRunner
    │   │   ├── config/            # TestDataConfig / EnvConfig
    │   │   └── report/            # ReportProperties
    │   ├── maps/                  # Page Objects (localizadores)
    │   │   └── saucedemo/         # exemplo
    │   ├── logic/                 # Lógica de negócio dos testes
    │   │   └── saucedemo/         # exemplo
    │   └── steps/                 # Step definitions Cucumber
    │       └── saucedemo/         # exemplo
    └── resources/
        ├── features/
        │   └── saucedemo/         # Cenários de login (exemplo)
        ├── junit-platform.properties   # Config central do Cucumber
        ├── test-data.properties        # Credenciais (GITIGNORED)
        ├── test-data.properties.example
        ├── report.properties           # Metadados do relatório PDF
        └── log4j2.xml
```

---

## Adicionando uma nova frente de teste

O `RunnerTests` (em `core/RunnerTests.java`) usa `glue = { "br.com.ddamasceno" }` —
um único prefixo de pacote. O Cucumber escaneia recursivamente todos os subpacotes,
então basta seguir o padrão existente para uma nova aplicação, sem tocar no runner:

1. `maps/<novaFrente>/` — Page Objects (localizadores)
2. `logic/<novaFrente>/` — lógica de negócio, reutilizando `core/WebActions`
3. `steps/<novaFrente>/` — step definitions Cucumber
4. `resources/features/<novaFrente>/` — arquivos `.feature`, com uma tag própria (ex: `@minhaFrenteLogin`)
5. Se precisar de credenciais, adicione a chave em `test-data.properties(.example)` e
   um novo `case` em `TestDataConfig.resolve(...)`

Depois, execute só a tag nova: `mvn test -Dcucumber.filter.tags="@minhaFrenteLogin"`.

---

## Convenções de nomenclatura

### Tags Cucumber (exemplo Sauce Demo)

| Tag | Propósito |
|-----|-----------|
| `@saucedemoLogin` | Agrupa todos os cenários de login do Sauce Demo |
| `@saucedemoLoginValido` | Login com usuário e senha válidos |
| `@saucedemoLoginBloqueado` | Login com usuário bloqueado |
| `@saucedemoLoginSenhaInvalida` | Login com senha inválida |
| `@saucedemoLoginUsuarioEmBranco` | Login com usuário em branco |
| `@saucedemoLoginSenhaEmBranco` | Login com senha em branco |
| `@wip` | Cenário em desenvolvimento — excluído do CI |

### Tokens de dados de teste (nos `.feature`)

Use tokens `[CHAVE]` nos arquivos `.feature` para referenciar dados sem expor credenciais:

| Token | Dado resolvido |
|-------|---------------|
| `[SAUCEDEMO_VALID_USER]` | Usuário válido (`test.saucedemo.user.valid`) |
| `[SAUCEDEMO_LOCKED_USER]` | Usuário bloqueado (`test.saucedemo.user.locked`) |
| `[SAUCEDEMO_VALID_PASSWORD]` | Senha válida (`test.saucedemo.password.valid`) |

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

Como o exemplo do repositório usa as credenciais **públicas** do Sauce Demo, os
workflows não dependem de nenhum Secret — `test-data.properties` é gerado a partir
do próprio `test-data.properties.example` no pipeline. Para uma frente real com
credenciais sensíveis, configure Secrets em **Settings → Secrets and variables →
Actions** e passe-os via `-D` no `mvn test` (o `TestDataConfig` já prioriza system
properties sobre o arquivo).

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
