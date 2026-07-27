package br.com.ddamasceno.Runner;

import io.cucumber.junit.CucumberOptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitário para obter a tag ativa de execução.
 *
 * <p>Resolução em cascata:
 * <ol>
 *   <li>System property {@code cucumber.filter.tags} — definida por perfil Maven ou CLI</li>
 *   <li>Annotation {@code @CucumberOptions(tags = ...)} no {@link RunnerTests}</li>
 *   <li>Fallback: {@code "SemTag"}</li>
 * </ol>
 *
 * <p>A system property tem prioridade porque é o mecanismo usado pelos perfis Maven
 * ({@code -P smoke}, {@code -P regression}) para sobrescrever a tag do runner.
 */
public final class RunnerInfo {

    private static final Pattern TAG_PATTERN = Pattern.compile("@[A-Za-z0-9_\\-]+");

    private RunnerInfo() { }

    public static String getRunnerTag() {
        // 1. System property (perfil Maven ou -D via CLI — maior prioridade)
        String prop = System.getProperty("cucumber.filter.tags");
        if (prop != null && !prop.isBlank()) {
            String extracted = extractFirstTag(prop);
            return extracted != null ? extracted : prop.trim();
        }

        // 2. Annotation @CucumberOptions no RunnerTests
        try {
            CucumberOptions opts = RunnerTests.class.getAnnotation(CucumberOptions.class);
            if (opts != null && !opts.tags().isBlank()) {
                String extracted = extractFirstTag(opts.tags());
                if (extracted != null) return extracted;
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
