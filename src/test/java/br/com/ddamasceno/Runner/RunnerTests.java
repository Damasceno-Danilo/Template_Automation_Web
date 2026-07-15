package br.com.ddamasceno.Runner;

import br.com.ddamasceno.core.BaseRunner;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Runner principal — executa os cenários de login da Advantage Shopping.
 *
 * <p>Estende {@link BaseRunner} que define as configurações compartilhadas
 * ({@code features}, {@code glue}, {@code plugin}, {@code monochrome}, {@code snippets}).
 * Aqui, apenas a {@code tag} diferencia este runner dos demais.
 *
 * <p>Para rodar com outra tag sem alterar o código:
 * <pre>
 *   mvn test -Dcucumber.filter.tags="@loginTodosCampos"
 *   mvn test -P smoke
 *   mvn test -P regression
 * </pre>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features   = "classpath:features",
        tags       = "@loginInvalidPassword",
        glue       = {
                "br.com.ddamasceno.steps.advantageShopping",
                "br.com.ddamasceno.core"
        },
        plugin     = {"json:target/reports/CucumberReports.json", "pretty"},
        monochrome = true,
        snippets   = CucumberOptions.SnippetType.CAMELCASE
)
public class RunnerTests extends BaseRunner {
}
