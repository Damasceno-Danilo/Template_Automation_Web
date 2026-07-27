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
     *   <li>{@code [VALID_USER]}          → {@code test.user.valid}</li>
     *   <li>{@code [VALID_PASSWORD]}      → {@code test.password.valid}</li>
     *   <li>{@code [INVALID_USER]}        → {@code test.user.invalid}</li>
     *   <li>{@code [INVALID_PASSWORD]}    → {@code test.password.invalid}</li>
     *   <li>{@code [SPECIAL_CHARS_USER]}  → {@code test.user.special.chars}</li>
     *   <li>{@code [PASSWORD_NO_SPECIAL]} → {@code test.password.no.special}</li>
     *   <li>{@code [NEW_USER]}            → {@code test.user.new}</li>
     *   <li>{@code [NEW_PASSWORD]}        → {@code test.password.new}</li>
     *   <li>{@code [NEW_EMAIL]}           → {@code test.email.new}</li>
     *   <li>{@code [DIFFERENT_PASSWORD]}  → {@code test.password.different}</li>
     *   <li>{@code [INVALID_EMAIL]}       → {@code test.email.invalid}</li>
     *   <li>{@code [PRODUCT_NAME]}        → {@code test.product.name}</li>
     *   <li>{@code [OUT_OF_STOCK_PRODUCT]}→ {@code test.product.out.of.stock}</li>
     *   <li>{@code [SAFEPAY_USER]}        → {@code test.safepay.user}</li>
     *   <li>{@code [SAFEPAY_PASSWORD]}    → {@code test.safepay.password}</li>
     *   <li>{@code [CARD_NUMBER]}         → {@code test.credit.card.number}</li>
     *   <li>{@code [CARD_CVV]}            → {@code test.credit.card.cvv}</li>
     *   <li>{@code [CARD_MONTH]}          → {@code test.credit.card.month}</li>
     *   <li>{@code [CARD_YEAR]}           → {@code test.credit.card.year}</li>
     *   <li>{@code [CARD_HOLDER]}         → {@code test.credit.card.holder}</li>
     *   <li>{@code [INVALID_CARD_NUMBER]} → {@code test.credit.card.number.invalid}</li>
     *   <li>{@code [INVALID_CARD_CVV]}    → {@code test.credit.card.cvv.invalid}</li>
     *   <li>{@code [INVALID_CARD_MONTH]}  → {@code test.credit.card.month.invalid}</li>
     *   <li>{@code [INVALID_CARD_YEAR]}   → {@code test.credit.card.year.invalid}</li>
     *   <li>{@code [INVALID_CARD_HOLDER]} → {@code test.credit.card.holder.invalid}</li>
     * </ul>
     */
    public static String resolve(String featureValue) {
        if (featureValue == null) return null;
        if (!featureValue.startsWith("[") || !featureValue.endsWith("]")) {
            return featureValue; // valor literal — usa como está
        }
        String token = featureValue.substring(1, featureValue.length() - 1).toUpperCase();
        return switch (token) {
            // ── Login ────────────────────────────────────────────────────────
            case "VALID_USER"           -> get("test.user.valid");
            case "VALID_PASSWORD"       -> get("test.password.valid");
            case "INVALID_USER"         -> get("test.user.invalid");
            case "INVALID_PASSWORD"     -> get("test.password.invalid");
            case "SPECIAL_CHARS_USER"   -> get("test.user.special.chars");
            case "PASSWORD_NO_SPECIAL"  -> get("test.password.no.special");
            // ── Criar Conta ──────────────────────────────────────────────────
            case "NEW_USER"             -> get("test.user.new");
            case "NEW_PASSWORD"         -> get("test.password.new");
            case "NEW_EMAIL"            -> get("test.email.new");
            case "DIFFERENT_PASSWORD"   -> get("test.password.different");
            case "INVALID_EMAIL"        -> get("test.email.invalid");
            // ── Produtos ─────────────────────────────────────────────────────
            case "PRODUCT_NAME"         -> get("test.product.name");
            case "OUT_OF_STOCK_PRODUCT" -> get("test.product.out.of.stock");
            // ── SafePay ──────────────────────────────────────────────────────
            case "SAFEPAY_USER"         -> get("test.safepay.user");
            case "SAFEPAY_PASSWORD"     -> get("test.safepay.password");
            // ── Cartão de Crédito (válidos) ──────────────────────────────────
            case "CARD_NUMBER"          -> get("test.credit.card.number");
            case "CARD_CVV"             -> get("test.credit.card.cvv");
            case "CARD_MONTH"           -> get("test.credit.card.month");
            case "CARD_YEAR"            -> get("test.credit.card.year");
            case "CARD_HOLDER"          -> get("test.credit.card.holder");
            // ── Cartão de Crédito (inválidos) ────────────────────────────────
            case "INVALID_CARD_NUMBER"  -> get("test.credit.card.number.invalid");
            case "INVALID_CARD_CVV"     -> get("test.credit.card.cvv.invalid");
            case "INVALID_CARD_MONTH"   -> get("test.credit.card.month.invalid");
            case "INVALID_CARD_YEAR"    -> get("test.credit.card.year.invalid");
            case "INVALID_CARD_HOLDER"  -> get("test.credit.card.holder.invalid");
            default -> {
                log.warn("Token desconhecido: '{}'. Retornando valor original.", featureValue);
                yield featureValue;
            }
        };
    }
}
