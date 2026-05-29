package br.com.ddamasceno.core;

import br.com.ddamasceno.Runner.RunnerInfo;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

import java.util.Properties;

/**
 * Hooks Cucumber — ciclo de vida por cenário.
 *
 * <p>O {@code @After} busca o {@link TestReport} acumulado via {@link ReportContext},
 * garantindo que todos os passos registrados pela lógica de teste (via
 * {@code report().registerStep(...)}) sejam incluídos no PDF final.
 */
public class Hooks {

    private static final Properties reportProperties = new Properties();

    @Before
    public void beforeScenario(Scenario scenario) {
        String runnerTag = resolveTag(scenario);

        reportProperties.setProperty("tag.name", runnerTag);
        reportProperties.setProperty("scenario.name", scenario.getName());

        System.out.println(">>> [Before] Tag: " + runnerTag + " | Cenário: " + scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        System.out.println(">>> [After] Cenário falhou? " + scenario.isFailed());
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver == null) return;

            // Reutiliza o relatório acumulado pela lógica de teste (se disponível)
            TestReport report = ReportContext.get();
            if (report == null) {
                report = new TestReport(driver);
            }

            // Captura evidência extra em caso de falha
            if (scenario.isFailed()) {
                report.captureScreenshot("Evidência de falha — " + scenario.getName());
            }

            report.setTestStatus(!scenario.isFailed());
            report.createPdfReport();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReportContext.clear();
            DriverManager.quitDriver();
        }
    }

    public static Properties getReportProperties() {
        return reportProperties;
    }

    // ─── Utilitário interno ───────────────────────────────────────────────────

    private String resolveTag(Scenario scenario) {
        // 1. Tenta API do Cucumber (getSourceTagNames — Cucumber 7+)
        try {
            @SuppressWarnings("unchecked")
            java.util.Collection<String> sourceTags = (java.util.Collection<String>)
                    scenario.getClass().getMethod("getSourceTagNames").invoke(scenario);
            if (sourceTags != null && !sourceTags.isEmpty()) {
                return sourceTags.iterator().next();
            }
        } catch (NoSuchMethodException ignored) {
        } catch (Exception ignored) {
        }

        // 2. Fallback: getTags() padrão do Cucumber
        try {
            var tags = scenario.getSourceTagNames();
            if (tags != null && !tags.isEmpty()) {
                return tags.iterator().next();
            }
        } catch (Exception ignored) {
        }

        // 3. Fallback: system property / runner
        return RunnerInfo.getRunnerTag();
    }
}
