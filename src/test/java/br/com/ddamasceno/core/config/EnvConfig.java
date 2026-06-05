package br.com.ddamasceno.core.config;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centraliza as configurações de ambiente (browser, execução, etc.).
 *
 * <p>Resolução de valores em cascata:
 * <ol>
 *   <li>System property {@code -Dchave=valor}</li>
 *   <li>Variável de ambiente {@code CHAVE_EM_MAIUSCULO} (pontos → underscores)</li>
 *   <li>Variável de ambiente {@code CI=true} seta headless automaticamente</li>
 *   <li>Arquivo {@code src/test/resources/environment.properties}</li>
 * </ol>
 */
@Log4j2
public final class EnvConfig {

    private static final String PROPS_FILE = "environment.properties";
    private static final Properties props = new Properties();

    static {
        try (InputStream is = EnvConfig.class.getClassLoader().getResourceAsStream(PROPS_FILE)) {
            if (is != null) {
                props.load(is);
                log.info("environment.properties carregado com sucesso.");
            } else {
                log.warn("{} não encontrado no classpath.", PROPS_FILE);
            }
        } catch (IOException e) {
            log.error("Erro ao carregar {}: {}", PROPS_FILE, e.getMessage());
        }
    }

    private EnvConfig() {}

    /**
     * Retorna true se o browser deve rodar em modo headless.
     *
     * <p>Prioridade:
     * <ol>
     *   <li>{@code -Dbrowser.headless=true/false}</li>
     *   <li>{@code BROWSER_HEADLESS=true/false} (variável de ambiente)</li>
     *   <li>{@code CI=true} (GitHub Actions seta isso automaticamente)</li>
     *   <li>Valor em {@code environment.properties} (padrão: {@code false})</li>
     * </ol>
     */
    public static boolean isHeadless() {
        // 1. System property
        String sysProp = System.getProperty("browser.headless");
        if (sysProp != null) {
            log.info("browser.headless definido via system property: {}", sysProp);
            return Boolean.parseBoolean(sysProp);
        }

        // 2. Variável de ambiente explícita
        String envVar = System.getenv("BROWSER_HEADLESS");
        if (envVar != null) {
            log.info("browser.headless definido via variável de ambiente BROWSER_HEADLESS: {}", envVar);
            return Boolean.parseBoolean(envVar);
        }

        // 3. Ambiente CI (GitHub Actions, Jenkins, etc.)
        if ("true".equalsIgnoreCase(System.getenv("CI"))) {
            log.info("Ambiente CI detectado — headless ativado automaticamente.");
            return true;
        }

        // 4. environment.properties
        String fileProp = props.getProperty("browser.headless", "false").trim();
        log.info("browser.headless lido do environment.properties: {}", fileProp);
        return Boolean.parseBoolean(fileProp);
    }
}
