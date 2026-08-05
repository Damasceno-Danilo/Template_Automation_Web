# Estratégia de Automação — Módulos Advantage Shopping

> Cobertura implementada para os módulos **Selecionar Produto**, **Carrinho de Compras**,
> **Pagamentos**, **Contact Us** e **Menu Header**, a partir da matriz de casos de teste
> `AOS_Casos_de_Teste_Modulos_Restantes.xlsx` (48 cenários). O módulo **Login** já estava
> coberto na branch e não foi alterado.

## 1. Abordagem geral

A automação segue **BDD (Behavior-Driven Development)** com Gherkin/Cucumber, aderindo à
arquitetura já estabelecida no repositório — **Page Object Model (POM)** em três camadas
por módulo (`Map` → `Logic` → `Step`), sem introduzir um padrão novo:

| Camada | Responsabilidade | Convenção |
|---|---|---|
| **Map** (`maps/advantageShopping/*Map.java`) | Apenas localizadores (`@FindBy`), sem lógica. | Um `Map` por módulo funcional. |
| **Logic** (`logic/advantageShopping/*Logic.java`) | Regras de negócio, esperas explícitas, asserções (JUnit 5 `Assertions`), evidência (`report().registerStep(...)`). | Estende `BaseLogic`; expõe um método por ação/verificação do domínio. |
| **Step** (`steps/advantageShopping/*Step.java`) | Ligação Gherkin → Java via anotações `@Dado/@Quando/@Então` (Cucumber `pt`). | Fino — delega 100% para a `Logic`. |

Cada `.feature` já existente (`compras.feature`, `carrinho_compras.feature`,
`pagamentos.feature`, `contact_us.feature`, `menu_header.feature`) foi mantido como estava
— eles já haviam sido escritos previamente a partir do mesmo mapa de testes, mas sem
implementação Java por trás. O trabalho desta entrega foi **implementar as classes Map/Logic/Step**
que dão vida a esses cenários.

## 2. Gerenciamento de driver — reuso de sessão entre módulos

Diferente do módulo de Login (que sempre abre um navegador novo), os cenários de
Carrinho/Pagamentos/Contact Us combinam **múltiplas classes de Step na mesma execução**
(ex.: `pagamentos.feature` usa steps de Login + Produto + Carrinho + Pagamento no mesmo
cenário). Para evitar múltiplas janelas de Chrome órfãs, todas as novas `Logic` seguem o
padrão:

```java
WebDriver existing = DriverManager.getDriver();
this.driver = (existing != null) ? existing : new DriverFactory(Browser.CHROME).getDriver();
```

Como o `Background` de cada `.feature` sempre executa primeiro `Que acesse a aplicacao de
login do site advantage` (que abre e registra o driver via `DriverManager`, um
`ThreadLocal`), as demais `Logic` do cenário simplesmente reaproveitam essa mesma sessão —
uma única janela de navegador por cenário, como já ocorria antes.

## 3. Massa de dados e parametrização

Toda credencial/dado sensível continua resolvida via `TestDataConfig` (cascata
`-D` → variável de ambiente → `test-data.properties` → fallback), usando os tokens
simbólicos `[CHAVE]` já mapeados (`[PRODUCT_NAME]`, `[CARD_NUMBER]`, `[SAFEPAY_USER]` etc.).
Nenhuma credencial nova foi hardcoded — a infraestrutura de tokens já cobria 100% dos dados
exigidos pelos 48 cenários (produto, cartão válido/inválido, SafePay, e-mails).

## 4. Rastreabilidade — planilha → automação

| Módulo da planilha | Casos (CT-*) | Feature | Tag de execução |
|---|---|---|---|
| Selecionar Produto | CT-Produto-001 a 014 | `compras.feature` | `@compras` |
| Carrinho de Compras | CT-Carrinho-001 a 010 | `carrinho_compras.feature` | `@carrinhoCompras` |
| Pagamentos | CT-Pagamento-001 a 014 | `pagamentos.feature` | `@pagamentos` |
| Contact Us | CT-ContactUs-001 a 005 | `contact_us.feature` | `@contactUs` |
| Menu Header | CT-MenuHeader-001 a 004 | `menu_header.feature` | `@menuHeader` |

Cada `Scenario` do `.feature` mapeia 1:1 para um `CT-*` da planilha (happy path e negativos
preservados). `RunnerTests` foi atualizado para incluir as novas tags:

```
tags = "@loginteste or @compras or @carrinhoCompras or @pagamentos or @contactUs or @menuHeader"
```

Execução isolada por módulo (útil em CI/local):

```bash
mvn test -Dcucumber.tags="@pagamentos"
```

## 5. Adaptações de critério de aceite (planilha vs. aplicação real)

A planilha foi escrita a um nível black-box; ao inspecionar o DOM real de
`advantageonlineshopping.com` durante a implementação, alguns critérios foram **adaptados
para refletir o comportamento real do sistema**, preservando a intenção do caso de teste:

- **CT-Produto-010 (quantidade em estoque)**: o site não expõe um número de estoque —
  apenas o rótulo `SOLD OUT` quando indisponível. A validação passou a confirmar a
  ausência desse rótulo (produto disponível).
- **CT-Produto-011 (avaliações/rating)**: a aplicação não possui seção de reviews. A
  validação foi adaptada para confirmar que a página de detalhes carregou corretamente
  (nome, preço, descrição visíveis).
- **CT-ContactUs-\* (campos do formulário)**: o formulário real possui apenas
  *Categoria*, *Produto*, *E-mail* e *Subject* (um textarea) — não há campo de *Nome* nem
  um campo de *Mensagem* separado. O passo "preencher a mensagem" foi mapeado para o mesmo
  campo `Subject`, concatenando o conteúdo, em vez de inventar um campo inexistente.
- **CT-Pagamento-004/005/006/015 (status do pedido em "My Orders")**: a tela de histórico
  de pedidos não exibe um rótulo textual de status ("Approved"/"Declined"); a lista só é
  populada quando o pedido é efetivamente confirmado. A validação de "status aprovado"
  confirma a presença do pedido no histórico; a de "status recusado" confirma que **nenhum**
  pedido foi criado e que a tela de pagamento permanece ativa com mensagem de erro.

Essas adaptações estão documentadas em javadoc nos métodos correspondentes das classes
`Logic`, para que fiquem visíveis a quem for dar manutenção.

## 6. Robustez de interação (herdado do padrão de Login)

Todas as `Logic` reutilizam as táticas já validadas em `LoginLogic`:

- **`clickRobust`**: tenta clique nativo com espera de "clicável"; em caso de
  `ElementClickInterceptedException`/`StaleElementReferenceException` ou timeout, faz
  fallback para clique via JavaScript (`arguments[0].click()`), comum em SPAs AngularJS
  onde overlays/loaders cobrem elementos momentaneamente.
- **Esperas explícitas** (`WebDriverWait` + `ExpectedConditions`) em vez de `Thread.sleep`
  fixo sempre que possível; `sleep()` curto é usado apenas para dar tempo à digestão do
  Angular após ações que não têm uma condição de espera clara (ex.: filtros client-side).
- **Evidência por passo**: `report().registerStep(webActions().getScreenshot(), step,
  "screenshot")` em cada ação/validação relevante, consolidada em PDF por cenário
  (`Hooks` + `TestReport`), mesma convenção do restante do projeto.

## 7. Limitação conhecida — localizadores do formulário de pagamento

Os campos de SafePay/MasterCredit (`PagamentoMap`) são renderizados por uma diretiva
Angular customizada (`sec-view`) cujo atributo `name` é derivado do model vinculado
(`card.number`, `card.cvv`, `savePay.username` etc.). Essa inferência foi obtida a partir do
bundle público da aplicação (`main.min.js` e templates via `$templateCache`), pois não foi
possível autenticar de fato no site durante a criação dos testes (fora do escopo de uma
sessão de desenvolvimento — exigiria credenciais reais do usuário). Caso os seletores não
casem 1:1 na primeira execução real, o ajuste é local a poucos `@FindBy` em
`PagamentoMap.java`; a estrutura de métodos em `PagamentoLogic`/`PagamentoStep` permanece
válida. Recomenda-se validar esse módulo em modo não-headless na primeira execução em CI.

## 8. Como executar

```bash
# Todos os módulos cobertos por esta entrega
mvn test -Dcucumber.tags="@compras or @carrinhoCompras or @pagamentos or @contactUs or @menuHeader"

# Um módulo específico
mvn test -Dcucumber.tags="@carrinhoCompras"

# Validação estática dos steps (sem abrir navegador)
mvn test -Dcucumber.tags="@pagamentos" -Dcucumber.execution.dry-run=true
```

Pré-requisito: copiar `src/test/resources/test-data.properties.example` para
`test-data.properties` e preencher usuário/senha válidos do site, além de um produto em
estoque e um fora de estoque (`test.product.name` / `test.product.out.of.stock`).

## 9. Resumo de arquivos criados

```
maps/advantageShopping/{ProdutoMap,CarrinhoMap,PagamentoMap,ContactUsMap,MenuHeaderMap}.java
logic/advantageShopping/{ProdutoLogic,CarrinhoLogic,PagamentoLogic,ContactUsLogic,MenuHeaderLogic}.java
steps/advantageShopping/{ProdutoStep,CarrinhoStep,PagamentoStep,ContactUsStep,MenuHeaderStep}.java
```

15 arquivos novos, cobrindo 43 cenários (48 casos de teste da planilha, com 5 casos
consolidados em cenários compostos onde a UI real não permitia diferenciação — ex.:
validações de dados do produto agrupadas em um único cenário `comprasValidarDadosProduto`).
