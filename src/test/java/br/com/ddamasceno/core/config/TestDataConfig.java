package br.com.ddamasceno.core.config;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centraliza o acesso aos dados de teste (credenciais, URLs, etc.).
 *
 * <p>Resolução de valores em cascata:
 * <ol>
 *   <li>System property {@code -Dchave=valor}</li>
 *   <li>Variável de ambiente {@code CHAVE_EM_MAIUSCULO} (pontos → underscores)</li>
 *   <li>Arquivo {@code src/test/resources/test-data.properties} (ignorado pelo Git)</li>
 *   <li>Retorna a própria chave como fallback (útil para depuração)</li>
 * </ol>
 *
 * <p>Nos arquivos {@code .feature}, use chaves simbólicas entre colchetes:
 * <pre>
 *   When inserir dados com "[VALID_USER]" e "[VALID_PASSWORD]"
 * </pre>
 * O método {@link #resolve(String)} detecta o padrão {@code [CHAVE]} e substitui
 * pelo valor real sem expor credenciais no código-fonte.
 */
@Log4j2
public final class TestDataConfig {

    private static final String PROPS_FILE = "test-data.properties";
    private static final Properties props = new Properties();

    static {
        try (InputStream is = TestDataConfig.class.getClassLoader().getResourceAsStream(PROPS_FILE)) {
            if (is != null) {
                props.load(is);
                log.info("test-data.properties carregado com sucesso.");
            } else {
                log.warn("{} não encontrado no classpath. Use variáveis de ambiente ou system properties.", PROPS_FILE);
            }
        } catch (IOException e) {
            log.error("Erro ao carregar {}: {}", PROPS_FILE, e.getMessage());
        }
    }

    private TestDataConfig() {
        // Utilitário estático — não instanciável
    }

    // ─── API pública ─────────────────────────────────────────────────────────

    /**
     * Retorna o valor de uma chave de propriedade.
     * Segue a cascata: system property → env var → arquivo → chave como fallback.
     */
    public static String get(String key) {
        // 1. System property (-Dkey=value)
        String value = System.getProperty(key);
        if (value != null) return value;

        // 2. Variável de ambiente (test.user.valid → TEST_USER_VALID)
        String envKey = key.toUpperCase().replace('.', '_');
        value = System.getenv(envKey);
        if (value != null) return value;

        // 3. Arquivo test-data.properties
        value = props.getProperty(key);
        if (value != null) return value;

        log.warn("Chave '{}' não encontrada em nenhuma fonte de configuração. Retornando a própria chave.", key);
        return key;
    }

    /**
     * Resolve um valor de feature: se for um token {@code [CHAVE]}, retorna o
     * dado real; caso contrário, devolve o valor sem alteração.
     *
     * <p>Mapeamento de tokens disponíveis:
     * <ul>
     *   <li>{@code [VALID_USER]}      → {@code test.user.valid}</li>
     *   <li>{@code [VALID_PASSWORD]}  → {@code test.password.valid}</li>
     *   <li>{@code [INVALID_USER]}    → {@code test.user.invalid}</li>
     *   <li>{@code [INVALID_PASSWORD]}→ {@code test.password.invalid}</li>
     * </ul>
     */
    public static String resolve(String featureValue) {
        if (featureValue == null) return null;
        if (!featureValue.startsWith("[") || !featureValue.endsWith("]")) {
            return featureValue; // valor literal — usa como está
        }
        String token = featureValue.substring(1, featureValue.length() - 1).toUpperCase();
        return switch (token) {
            case "VALID_USER"       -> get("test.user.valid");
            case "VALID_PASSWORD"   -> get("test.password.valid");
            case "INVALID_USER"     -> get("test.user.invalid");
            case "INVALID_PASSWORD" -> get("test.password.invalid");
            default -> {
                log.warn("Token desconhecido: '{}'. Retornando valor original.", featureValue);
                yield featureValue;
            }
        };
    }
}
