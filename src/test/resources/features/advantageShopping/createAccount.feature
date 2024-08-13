@Create
Feature: Criar uma conta com sucesso
  como um usuario do sistema
  quero criar uma conta de usuario
  para utilizar o sistema

  Background:
   Given Que acesse a aplicacao de login do site advantage shopping
    When clicar em create new account

  @createNewAccount
  Scenario: Clicar no link create new account
    Then validar acesso a pagina create account

    Scenario Outline: Criar usuario valido com campos <cenario>
    |cenario        |<cenario>        |
    |usename        |<usename>        |
    |email          |<email>          |
    |password       |<password>       |
    |confirmPassword|<confirmPassword>|
    |firstName      |<firstName>      |
    |lastName       |<lastName>       |
    |phone number   |<phone number>   |
    |country        |<country>        |
    |city           |<city>           |
    |address        |<address>        |
    |state          |<state>          |
    |postalCode     |<postalCode>     |
    |iAgree         |<iAgree>         |
    And cliccar em I agree to the www.AdvantageOnlineShopping.com Conditions of Use and Privacy Notice
    Then validar usuario criado com sucesso
      Examples:
        |cenario            |usename      |email           |password|confirmPassword|firstName | lastName | phone number| country| city    | address  | state | postalCode | iAgree |
        |campos obrigatorios|DanDamasceno |danilo@gmail.com|Dan$1234|Dan$1234       |          |          |             |        |         |          |       |            | false  |
        |todos os campos    |DanDamasceno |danilo@gmail.com|Dan$1234|Dan$1234       | Danilo   |Damasceno | 11934256789 | Brasil |São Paulo|Gongorismo|  SP   | 02841170   | false  |



    @createAccountCamposBranco
  Scenario: Realizar criação de usuario dados obrigatorios em branco
    When preencher campos obrigatorios em ACCOUNT DETAILS "username", "email", "password", confirmPassword" em branco
    And cliccar em I agree to the www.AdvantageOnlineShopping.com Conditions of Use and Privacy Notice
    Then validar que botao register desabilitado