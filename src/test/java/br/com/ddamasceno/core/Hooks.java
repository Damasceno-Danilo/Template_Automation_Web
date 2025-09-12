package br.com.ddamasceno.core;

import br.com.ddamasceno.Runner.RunnerTests;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.Properties;

public class Hooks {

    private static Properties reportProperties = new Properties();

    @Before
    public void beforeScenario(Scenario scenario) {
        // Pega todas as tags do cenário
        String runnerTag = RunnerTests.RUNNER_TAG;

        // Salva no reportProperties
        reportProperties.setProperty("tag.name", runnerTag);
        reportProperties.setProperty("scenario.name", scenario.getName());

        System.out.println(">>> Rodou o @Before");
        System.out.println(">>> Tag do Runner: " + runnerTag);
        System.out.println(">>> Nome do cenário: " + scenario.getName());
    }

    // Getter para permitir que outras classes usem
    public static Properties getReportProperties() {
        return reportProperties;
    }
}
