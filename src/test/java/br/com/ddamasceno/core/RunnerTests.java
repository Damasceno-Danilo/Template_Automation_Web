package br.com.ddamasceno.core;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features   = "classpath:features",
        tags       = "@saucedemoLogin",
        glue       = { "br.com.ddamasceno" },
        plugin     = {"json:target/reports/CucumberReports.json", "pretty"},
        monochrome = true,
        snippets   = CucumberOptions.SnippetType.CAMELCASE
)
public class RunnerTests extends BaseRunner {
}
