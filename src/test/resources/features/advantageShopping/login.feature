
@loginteste
Feature: Login na aplicacao
  como um usuario do sistema
  quero fazer login no sistema
  para acessar minha conta

  Background:
    Given Que acesse a aplicacao de login do site advantage
    When acessar o menu login

  @iconeFechaModal
  Scenario: Clicar no icone fechar na modal
    When clicar no icone fechar
    Then validar que modal login fechou

  @clickForaModal
    Scenario: Clicar fora da modal
      When clico fora da modal
      Then validar que modal login fechou

  @loginCamposObg
  Scenario: Login com usuario validos e campos obrigatórios
    When inserir dados com "username" e "password"
    Then valido que login foi realizado com sucesso

  @loginTodosCampos
  Scenario: Login com usuario validos e todos os campos
    When inserir dados com "username" e "password"
    And clicar em remember ME
    Then valido que login foi realizado com sucesso
















