package br.com.ddamasceno.Runner;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Runner principal do Cucumber com JUnit 5 Platform Suite.
 *
 * <p>A tag ativa é lida em cascata:
 * <ol>
 *   <li>System property {@code -Dcucumber.filter.tags=@minhaTag} (CLI ou Maven)</li>
 *   <li>Propriedade Maven {@code cucumber.tags} definida pelo perfil ativo</li>
 *   <li>Valor padrão em {@code junit-platform.properties}</li>
 * </ol>
 *
 * <p>Exemplos de execução:
 * <pre>
 *   mvn test                                              # usa padrão do junit-platform.properties
 *   mvn test -P smoke                                     # perfil smoke
 *   mvn test -P regression                                # regressão completa
 *   mvn test -Dcucumber.filter.tags="@loginTodosCampos"  # tag avulsa
 * </pre>
 *
 * <p>O glue (pacotes de steps e hooks) e o plugin de relatório JSON
 * são configurados em {@code src/test/resources/junit-platform.properties}.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class RunnerTests {
    // Configuração completa em junit-platform.properties
}
