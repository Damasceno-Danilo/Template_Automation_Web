package br.com.ddamasceno.Runner;

import br.com.ddamasceno.core.BaseRunner;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features   = "classpath:features",
        tags       = "@saucedemoLoginBloqueado",
        glue       = { "br.com.ddamasceno" },
        plugin     = {"json:target/reports/CucumberReports.json"},
        monochrome = true,
        snippets   = CucumberOptions.SnippetType.CAMELCASE
)
public class RunnerTests extends BaseRunner {
}
