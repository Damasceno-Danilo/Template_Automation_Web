# =============================================================================
# @wip = Work In Progress — excluído das execuções de CI/regression
# Para executar: mvn test -Dcucumber.filter.tags="@googleSearch"
# =============================================================================

@wip @googleSearch
Feature: Pesquisa Google
  Como usuario
  Quero pesquisar termos no Google
  Para encontrar resultados relevantes

  @googleSearch
  Scenario: Pesquisa por termo Java
    Given que acesse a aplicacao google
    When Pesquise por Java
    Then valido a pesquisa Java
