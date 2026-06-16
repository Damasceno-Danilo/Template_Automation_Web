package br.com.ddamasceno.Runner;

import org.junit.platform.suite.api.ConfigurationParameter;
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
 * <p>O glue (pacotes de steps e hooks) é configurado em
 * {@code src/test/resources/junit-platform.properties}.
 *
 * <p>O plugin de relatório JSON é declarado diretamente aqui via
 * {@code @ConfigurationParameter}: como o Cucumber roda como engine aninhada
 * dentro do {@code @Suite}, esse é o mecanismo oficialmente suportado para
 * garantir que a configuração chegue ao engine — system property e
 * {@code junit-platform.properties} podem não propagar de forma confiável
 * através do Suite engine.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = "cucumber.plugin",
        value = "json:target/reports/CucumberReports.json, pretty"
)
public class RunnerTests {
    // Glue e tags: junit-platform.properties / system property (ver Javadoc acima)
}
