package br.com.ddamasceno.Runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(

        features = "classpath:features",
        tags = "@loginTodosCampos",
        glue = {"br.com.ddamasceno.steps", "br.com.ddamasceno.core"},
        plugin = {"json:target/reports/CucumberReports.json"},
        monochrome = false,
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunnerTests {
    public static final String RUNNER_TAG = "loginTodosCampos";
}
