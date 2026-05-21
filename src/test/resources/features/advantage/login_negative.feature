

@loginteste
Feature: Login negativo na aplicacao
  como um usuario do sistema
  quero validar tentativas de login inválidas
  para garantir mensagens e comportamento esperados

  Background:
    Given Que acesse a aplicacao de login do site advantage
    When acessar o menu login

  @loginInvalidUser
  Scenario: Tentativa de login com usuario inválido
    When inserir dados com do "usuarioInvalido" e "Dan$231418"
    When clico no Botao SignIn
    Then valido que login nao foi realizado

  @loginInvalidPassword
  Scenario: Tentativa de login com senha inválida
    When inserir dados com "DanDama" e "senhaErrada"
    When clico no Botao SignIn
    Then valido que login nao foi realizado