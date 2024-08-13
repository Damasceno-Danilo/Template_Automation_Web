#
@loginteste
Feature: Login na aplicacao
  como um usuario do sistema
  quero fazer login no sistema
  para acessar minha conta

  Background:
    Given Que acesse a aplicacao de login do site advantage

  @iconeFechaModal
  Scenario: Clicar no icone fechar na modal
    When clicar no icone fechar
    Then validar que modal login fechou

  @clickForaModal
    Scenario: Clicar fora da modal
      When clico fora da modal
      Then validar que modal login fechou

  @loginCamposObrigatorios
    Scenario Outline: Login com usuario validos e <cenario>
      When inserir dados da seguinte forma
      |login    | <login>   |
      |password | <password>|
      | remember| <remember>|
      Then validar que modal login fechou
      Examples:
      |cenario            | login        |password | remember|
      |campos obrigatorios| DanDamasceno | Dan$1418| false   |
      |todos os campos    | DanDamasceno | Dan$1418| true    |

  @loginEmBranco
  Scenario Outline: Login com usuario validos e <cenario>
    When inserir dados da seguinte forma
      |login    | <login>   |
      |password | <password>|
      | remember| <remember>|
    Then validar que modal login fechou
    Examples:
      |cenario            | login        |password | remember|
      |username em branco |              | Dan$1418| false   |
      |password em branco | DanDamasceno |         | true    |

  @loginInvalido
  Scenario Outline: Login com usuario validos e <cenario>
    When inserir dados da seguinte forma
      |login    | <login>   |
      |password | <password>|
      | remember| <remember>|
    Then validar que modal login fechou
    Examples:
      |cenario            | login        |password | remember|
      |username invalido  |   Dan20      | Dan$1418| false   |
      |password invalido  | DanDamasceno | Dan$14  | true    |


















