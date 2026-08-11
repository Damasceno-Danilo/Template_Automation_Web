package br.com.ddamasceno.core;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Configuração base do runner Cucumber.
 *
 * <p>Define as propriedades comuns a todos os runners do projeto:
 * {@code features}, {@code glue}, {@code plugin}, {@code monochrome} e {@code snippets}.
 *
 * <p>Para criar um novo runner, basta estender esta classe e sobrescrever
 * apenas as propriedades que diferem — normalmente só a {@code tag}:
 *
 * <pre>
 * {@literal @}RunWith(Cucumber.class)
 * {@literal @}CucumberOptions(
 *     features   = "classpath:features",
 *     tags       = "@minhaSuite",
 *     glue       = {"br.com.ddamasceno.steps.advantageShopping", "br.com.ddamasceno.core"},
 *     plugin     = {"json:target/reports/CucumberReports.json"},
 *     monochrome = true,
 *     snippets   = CucumberOptions.SnippetType.CAMELCASE
 * )
 * public class MeuRunner extends BaseRunner { }
 * </pre>
 *
 * <p><b>Override por linha de comando (sem alterar código):</b>
 * <pre>
 *   mvn test -Dcucumber.filter.tags="@loginTodosCampos"
 *   mvn test -P smoke
 *   mvn test -P regression
 * </pre>
 * O sistema property {@code cucumber.filter.tags} tem precedência sobre
 * a {@code tags} declarada na annotation do runner.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features   = "classpath:features",
        glue       = {
                "br.com.ddamasceno.steps.advantageShopping",
                "br.com.ddamasceno.steps.google",
                "br.com.ddamasceno.core"
        },
        plugin     = {"json:target/reports/CucumberReports.json"},
        monochrome = true,
        snippets   = CucumberOptions.SnippetType.CAMELCASE
)
public abstract class BaseRunner {
    // Classe abstrata — não instanciável diretamente.
    // Subclasses adicionam @CucumberOptions com a tag desejada.
}
