package br.com.ddamasceno.Runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(

        features = "classpath:features",
        tags = "@loginCamposObg",
        glue = "br.com.ddamasceno.steps.advantageShopping",
        plugin = {"json:target/reports/CucumberReports.json",
        "pretty"},
        monochrome = false,
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunnerTests {

}
