Feature: Login na aplicacao
  como um usuario do sistema
  quero fazer login no sistema
  para acessar minha conta

  @iconeFechaModal
  Scenario: Clicar no icone fechar na modal
    Given Que acesse a aplicacao de login do site advantage
    When clicar no icone fechar
    Then validar que modal login fechou

  @clickForaModal
    Scenario: Clicar fora da modal
      Given Que acesse a aplicacao de login do site advantage
      When clico fora da modal
      Then validar que modal login fechou

  @loginCamposObrigatorios
  Scenario: Login com campos obrigatorios
    Given Que acesse a aplicacao de login do site advantage
    When inserir "username" e "password" validos
    Then validar que modal login fechou

  @loginTodosCampos
  Scenario: Login com todos os campos
    Given Que acesse a aplicacao de login do site advantage
    When inserir "username" e "password" validos
    And clico em Remember-me
    Then validar que modal login fechou

  @usernameBranco
  Scenario: Login com username em branco e todos os campos
    Given Que acesse a aplicacao de login do site advantage
    When inserir username branco e "password"
    And clico em Remember-me
    Then validar que modal login fechou

  @passwordBranco
  Scenario: Login com password em branco e todos os campos
    Given Que acesse a aplicacao de login do site advantage
    When inserir "username" e password em branco
    And clico em Remember-me
    Then validar que modal login fechou

  @usernameInvalido
  Scenario: Login com password em branco e todos os campos
    Given Que acesse a aplicacao de login do site advantage
    When inserir "username" invalido e "password" valido
    And clico em Remember-me
    Then validar que modal login fechou

  @passwordInvalido
  Scenario: Login com password em branco e todos os campos
    Given Que acesse a aplicacao de login do site advantage
    When inserir "username" valido e "password" invalido
    And clico em Remember-me
    Then validar que modal login fechou














