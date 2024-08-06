package br.com.ddamasceno.maps;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class FormularioMap {

    @FindBy(id = "elementosForm:nome")
    private WebElement textNome;

    @FindBy(id = "elementosForm:sobrenome")
    private WebElement sobrenome;

    @FindBy(id = "elementosForm:sexo:0")
    private WebElement clickSexoMasculino;

    @FindBy(id = "elementosForm:comidaFavorita:0")
    private WebElement comidaFavoritaCarne;

    @FindBy(id = "elementosForm:escolaridade")
    private WebElement escolaridadeSuperior;

    @FindBy(id = "elementosForm:escolaridade")
    private WebElement escolaridadeMestrado;

    @FindBy(id = "elementosForm:escolaridade")
    private WebElement escolaridadeSuperiortexto;

    @FindBy(id = "alert")
    private WebElement alerta;

    @FindBy(id = "confirm")
    private WebElement confirm;

    @FindBy(id = "prompt")
    private WebElement prompt;

    
}
