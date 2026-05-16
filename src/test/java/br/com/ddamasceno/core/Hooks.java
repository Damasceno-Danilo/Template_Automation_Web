package br.com.ddamasceno.core;

import br.com.ddamasceno.Runner.RunnerInfo;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.Properties;

/**
 * Hooks atualizados: extrai tag do próprio Scenario quando possível
 * (mais confiável) e faz fallback para RunnerInfo.
 */
public class Hooks {

    private static Properties reportProperties = new Properties();

    @Before
    public void beforeScenario(Scenario scenario) {
        String runnerTag = "SemTag";

        // 1) Tenta obter tag diretamente do Scenario (API do Cucumber)
        try {
            // Em versões recentes do Cucumber, existe getSourceTagNames()
            try {
                @SuppressWarnings("unchecked")
                java.util.Collection<String> sourceTags = (java.util.Collection<String>) scenario.getClass()
                        .getMethod("getSourceTagNames")
                        .invoke(scenario);

                if (sourceTags != null && !sourceTags.isEmpty()) {
                    // pega a primeira tag (por padrão)
                    runnerTag = sourceTags.iterator().next();
                } else {
                    // fallback para RunnerInfo
                    runnerTag = RunnerInfo.getRunnerTag();
                }
            } catch (NoSuchMethodException nsme) {
                // getSourceTagNames não existe na API disponível: usa RunnerInfo
                runnerTag = RunnerInfo.getRunnerTag();
            }
        } catch (Exception e) {
            // Qualquer erro: fallback para RunnerInfo
            runnerTag = RunnerInfo.getRunnerTag();
        }

        // Salva no reportProperties
        reportProperties.setProperty("tag.name", runnerTag);
        reportProperties.setProperty("scenario.name", scenario.getName());

        System.out.println(">>> Rodou o @Before");
        System.out.println(">>> Tag resolvida: " + runnerTag);
        System.out.println(">>> Nome do cenário: " + scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        try {
            System.out.println(">>> Rodou o @After (Cucumber). Cenário falhou? " + scenario.isFailed());

            if (DriverManager.getDriver() != null) {
                TestReport report = new TestReport(DriverManager.getDriver());

                // Marca status do teste (true = passed, false = failed)
                report.setTestStatus(!scenario.isFailed());

                // Se falhou, captura evidência extra com nome do cenário
                if (scenario.isFailed()) {
                    report.captureScreenshot("Evidência - " + scenario.getName());
                }

                // Gera o PDF (usa propriedades de reportProperties internas; o TestReport lê Hooks.getReportProperties())
                report.createPdfReport();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Garante fechamento do navegador em qualquer situação
            DriverManager.quitDriver();
        }
    }

    // Getter para permitir que outras classes usem
    public static Properties getReportProperties() {
        return reportProperties;
    }
}