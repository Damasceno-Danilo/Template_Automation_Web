package br.com.ddamasceno.Runner;

import br.com.ddamasceno.core.BaseRunner;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features   = "classpath:features",
        tags       = "@loginteste or @compras or @carrinhoCompras or @pagamentos or @contactUs or @menuHeader",
        glue       = { "br.com.ddamasceno.steps.advantageShopping", "br.com.ddamasceno.steps.saucedemo", "br.com.ddamasceno.core"},
        plugin     = {"json:target/reports/CucumberReports.json", "pretty"},
        monochrome = true,
        snippets   = CucumberOptions.SnippetType.CAMELCASE
)
public class RunnerTests extends BaseRunner {
}
