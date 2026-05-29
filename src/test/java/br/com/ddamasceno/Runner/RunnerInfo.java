package br.com.ddamasceno.Runner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility para obter a tag ativa de execução do Cucumber.
 *
 * <p>Com JUnit 5 + Cucumber 7, a tag é configurada via system property
 * {@code cucumber.filter.tags} (definida pelo surefire ou pela CLI).
 * A leitura de {@code @CucumberOptions} não é mais necessária.
 */
public class RunnerInfo {

    private static final Pattern TAG_PATTERN = Pattern.compile("@[A-Za-z0-9_\\-]+");

    private RunnerInfo() { }

    /**
     * Retorna a primeira tag encontrada na system property {@code cucumber.filter.tags},
     * ou "SemTag" como fallback.
     */
    public static String getRunnerTag() {
        try {
            String prop = System.getProperty("cucumber.filter.tags");
            if (prop != null && !prop.isBlank()) {
                String extracted = extractFirstTag(prop);
                return extracted != null ? extracted : prop.trim();
            }
        } catch (Exception ignored) {
        }
        return "SemTag";
    }

    private static String extractFirstTag(String text) {
        if (text == null) return null;
        Matcher m = TAG_PATTERN.matcher(text);
        return m.find() ? m.group() : null;
    }
}
