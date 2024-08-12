package br.com.ddamasceno.Runner;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(

        features = "src/test/resoruces/features/",
        tags = "",
        glue = "br.com.ddamasceno/steps",
        plugin = {"json: target/reports/CucumberReports.json",
        "pretty"},
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunnerTests {

}
