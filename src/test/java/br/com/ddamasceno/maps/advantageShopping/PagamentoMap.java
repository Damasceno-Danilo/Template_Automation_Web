package br.com.ddamasceno.maps.advantageShopping;

import lombok.Getter;
import lombok.Setter;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
@Setter
public class PagamentoMap {

    // ─── Etapa de endereço/entrega ──────────────────────────────────────────

    @FindBy(id = "next_btn")
    private WebElement btnNext;

    // ─── Seleção do método de pagamento ─────────────────────────────────────

    @FindBy(xpath = "//div[contains(@class,'imgRadioButton')][contains(@data-ng-click,'imgRadioButtonClicked(1)')]")
    private WebElement radioSafePay;

    @FindBy(xpath = "//div[contains(@class,'imgRadioButton')][contains(@data-ng-click,'imgRadioButton = 2')]")
    private WebElement radioMasterCredit;

    // ─── SafePay ─────────────────────────────────────────────────────────────

    @FindBy(xpath = "//*[@name='savePay.username']")
    private WebElement inpSafePayUsuario;

    @FindBy(xpath = "//*[@name='savePay.password']")
    private WebElement inpSafePaySenha;

    @FindBy(id = "payNowSPDrtv")
    private WebElement btnPayNowSafePay;

    @FindBy(id = "payNowSPErrorLabel")
    private WebElement labelErroSafePay;

    // ─── MasterCredit (cartão) ───────────────────────────────────────────────

    @FindBy(xpath = "//*[@name='card.number']")
    private WebElement inpNumeroCartao;

    @FindBy(xpath = "//*[@name='card.cvv']")
    private WebElement inpCvv;

    @FindBy(xpath = "//*[@name='card.expirationDate.month']")
    private WebElement selMesVencimento;

    @FindBy(xpath = "//*[@name='card.expirationDate.year']")
    private WebElement selAnoVencimento;

    @FindBy(xpath = "//*[@name='card.name']")
    private WebElement inpNomeTitular;

    @FindBy(xpath = "//*[@data-ng-click='payNow_manual()'] | //*[@sec-send='payNow_masterCredit()']")
    private WebElement btnPayNowMasterCredit;

    @FindBy(xpath = "//*[@id='payNowMCErrorLabel' or @id='payNowMCErrorLabelExpended']")
    private WebElement labelErroMasterCredit;

    // ─── Confirmação do pedido ───────────────────────────────────────────────

    @FindBy(id = "orderNumberLabel")
    private WebElement labelNumeroPedido;

    @FindBy(id = "trackingNumberLabel")
    private WebElement labelNumeroRastreio;

    // ─── Histórico de pedidos ────────────────────────────────────────────────

    @FindBy(xpath = "//a[@translate='MY_ORDERS' or contains(text(),'My Orders') or contains(text(),'MY ORDERS')]")
    private WebElement linkMyOrders;

    @FindBy(xpath = "//table//tr[position()=2]")
    private WebElement primeiraLinhaPedidos;
}
