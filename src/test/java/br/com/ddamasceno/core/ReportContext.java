package br.com.ddamasceno.core;

/**
 * Armazena o {@link TestReport} ativo por thread.
 *
 * <p>Permite que {@link BaseLogic} e {@link Hooks} compartilhem a mesma instância
 * de relatório durante um cenário Cucumber, garantindo que todos os passos
 * registrados via {@code report().registerStep(...)} sejam incluídos no PDF
 * gerado ao final pelo Hook {@code @After}.
 *
 * <pre>
 * Fluxo:
 *  1. BaseLogic.initCore()  → ReportContext.set(new TestReport(driver))
 *  2. LoginLogic.metodo()   → report().registerStep(webActions().getScreenshot(), step, "screenshot")
 *  3. Hooks.afterScenario() → ReportContext.get() → report.createPdfReport()
 *  4. Hooks.afterScenario() → ReportContext.clear()
 * </pre>
 */
public final class ReportContext {

    private static final ThreadLocal<TestReport> REPORT = new ThreadLocal<>();

    private ReportContext() { }

    /** Registra o relatório ativo para a thread corrente. */
    public static void set(TestReport report) {
        REPORT.set(report);
    }

    /**
     * Retorna o relatório ativo da thread corrente, ou {@code null}
     * se nenhum foi registrado ainda.
     */
    public static TestReport get() {
        return REPORT.get();
    }

    /** Remove o relatório da thread corrente (chamado pelo Hook @After). */
    public static void clear() {
        REPORT.remove();
    }
}
