package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataMaker;


import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class BookingPage {

    MainPage mainPage = new MainPage();
    //Форма оплаты, поля формы, возможные ошибки, кнопка "Продолжить":
    private final SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    public final SelenideElement cardNumberFieldError = $x("//*[text()='Номер карты']/..//*[@class='input__sub']");
    private final SelenideElement monthField = $("input[placeholder='08']");
    public final SelenideElement monthFieldError = $x("//*[text()='Месяц']/..//*[@class='input__sub']");
    private final SelenideElement yearField = $("input[placeholder='22']");
    public final SelenideElement yearFieldError = $x("//*[text()='Год']/..//*[@class='input__sub']");
    private final SelenideElement ownerField = $(byText("Владелец")).parent().$("input");
    public final SelenideElement ownerFieldError = $x("//*[text()='Владелец']/..//*[@class='input__sub']");
    private final SelenideElement cvcField = $("input[placeholder='999']");
    public final SelenideElement cvcFieldError = $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']");
    private final SelenideElement notificationSuccessfully = $(".notification_status_ok");
    private final SelenideElement notificationError = $(".notification_status_error");
    private final SelenideElement continueButton = $("form button");


// дебетовая карта
    public void cardPayment() {
        mainPage.buyButton.click();
        mainPage.buyHeading.shouldBe(visible);
    }
    // кредитная карта
    public void cardCredit() {
        mainPage.creditButton.click();
        mainPage.creditHeading.shouldBe(visible);
    }

    public void sendingData(DataMaker.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
    }


    public void cardNumberFieldErrorHidden() {
        monthFieldError.shouldBe(Condition.hidden);
        yearFieldError.shouldBe(Condition.hidden);
        ownerFieldError.shouldBe(Condition.hidden);
        cvcFieldError.shouldBe(Condition.hidden);
    }


    public void monthFieldErrorHidden() {
        cardNumberFieldError.shouldBe(Condition.hidden);
        yearFieldError.shouldBe(Condition.hidden);
        ownerFieldError.shouldBe(Condition.hidden);
        cvcFieldError.shouldBe(Condition.hidden);
    }


    public void yearFieldErrorHidden() {
        cardNumberFieldError.shouldBe(Condition.hidden);
        monthFieldError.shouldBe(Condition.hidden);
        ownerFieldError.shouldBe(Condition.hidden);
        cvcFieldError.shouldBe(Condition.hidden);
    }


    public void ownerFieldErrorHidden() {
        cardNumberFieldError.shouldBe(Condition.hidden);
        monthFieldError.shouldBe(Condition.hidden);
        yearFieldError.shouldBe(Condition.hidden);
        cvcFieldError.shouldBe(Condition.hidden);
    }


    public void cvcFieldErrorHidden() {
        cardNumberFieldError.shouldBe(Condition.hidden);
        monthFieldError.shouldBe(Condition.hidden);
        yearFieldError.shouldBe(Condition.hidden);
        ownerFieldError.shouldBe(Condition.hidden);
    }


    public void invalidCardNumberField(DataMaker.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
        cardNumberFieldError.shouldBe(Condition.visible);
        cardNumberFieldErrorHidden();
    }

    public void invalidMonthField(DataMaker.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
        monthFieldError.shouldBe(Condition.visible);
        monthFieldErrorHidden();
    }

    public void invalidYearField(DataMaker.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
        yearFieldError.shouldBe(Condition.visible);
        yearFieldErrorHidden();
    }

    public void invalidOwnerField(DataMaker.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
        ownerFieldError.shouldBe(Condition.visible);
        ownerFieldErrorHidden();
    }

    public void invalidCVCField(DataMaker.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
        cvcFieldError.shouldBe(Condition.visible);
        cvcFieldErrorHidden();
    }

    //Время ожидания ответа 15 секунд.
    public void bankApproved() {
        notificationSuccessfully.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void bankDeclined() {
        notificationError.shouldBe(visible, Duration.ofSeconds(15));
    }
}