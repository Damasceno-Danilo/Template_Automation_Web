package br.com.ddamasceno.Runner;

import io.cucumber.junit.CucumberOptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility para obter a tag configurada no Runner (CucumberOptions)
 * ou, se não disponível, pegar de system property (ex.: -Dcucumber.filter.tags).
 */
public class RunnerInfo {

    private static final Pattern TAG_PATTERN = Pattern.compile("@[A-Za-z0-9_\\-]+");

    /**
     * Retorna a primeira tag encontrada no @CucumberOptions(tags = ...) do RunnerTests,
     * ou o valor da system property 'cucumber.filter.tags', ou "SemTag".
     */
    public static String getRunnerTag() {
        // 1) tenta ler annotation do RunnerTests
        try {
            CucumberOptions opts = RunnerTests.class.getAnnotation(CucumberOptions.class);
            if (opts != null) {
                String[] tags = new String[]{opts.tags()};
                if (tags != null && tags.length > 0) {
                    // cada entry pode conter expressões lógicas (ex: "(@a or @b) and not @c")
                    for (String raw : tags) {
                        String extracted = extractFirstTag(raw);
                        if (extracted != null) {
                            return extracted;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // continua para fallback
        }

        // 2) fallback: system property (usado por execuções via CLI)
        try {
            String prop = System.getProperty("cucumber.filter.tags");
            if (prop != null && !prop.isBlank()) {
                String extracted = extractFirstTag(prop);
                if (extracted != null) return extracted;
                return prop.trim();
            }
        } catch (Exception ignored) {
        }

        // 3) fallback final
        return "SemTag";
    }

    private static String extractFirstTag(String text) {
        if (text == null) return null;
        Matcher m = TAG_PATTERN.matcher(text);
        if (m.find()) {
            return m.group();
        }
        return null;
    }
}