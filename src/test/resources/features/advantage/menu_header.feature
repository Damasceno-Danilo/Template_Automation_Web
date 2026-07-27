# =============================================================================
# Cenários de validação do menu header — Advantage Shopping
# =============================================================================

@menuHeader
Feature: Validar menu header no Advantage Shopping
  Como um usuario do sistema
  Quero navegar pelo menu principal da aplicacao
  Para acessar as diferentes secoes do site

  Background:
    Given Que acesse a aplicacao de login do site advantage

  @menuHeaderOurProducts
  Scenario: Validar direcionamento para pagina Our Products
    When clicar no menu Our Products
    Then validar que a pagina de Our Products foi carregada

  @menuHeaderSpecialOffer
  Scenario: Validar direcionamento para pagina Special Offer
    When clicar no menu Special Offer
    Then validar que a pagina de Special Offer foi carregada

  @menuHeaderContactUs
  Scenario: Validar direcionamento para pagina Contact Us
    When clicar no menu Contact Us
    Then validar que a pagina de Contact Us foi carregada

  @menuHeaderPopularItems
  Scenario: Validar direcionamento para pagina Popular Items
    When clicar no menu Popular Items
    Then validar que a pagina de Popular Items foi carregada
