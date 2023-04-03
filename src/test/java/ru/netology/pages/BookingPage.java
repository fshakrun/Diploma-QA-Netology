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
    //Находим кнопку "Купить":
//    private final SelenideElement buyButton = $(byText("Купить"));
//    private final SelenideElement buyHeading = $(byText("Оплата по карте"));
//    //Находим кнопку "Купить в кредит":
//    private final SelenideElement creditButton = $(byText("Купить в кредит"));
//    private final SelenideElement creditHeading = $(byText("Кредит по данным карты"));

    //Форма оплаты, поля формы, возможные ошибки, кнопка "Продолжить":
    private final SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    private final SelenideElement cardNumberFieldError = $x("//*[text()='Номер карты']/..//*[@class='input__sub']");
    private final SelenideElement monthField = $("input[placeholder='08']");
    private final SelenideElement monthFieldError = $x("//*[text()='Месяц']/..//*[@class='input__sub']");
    private final SelenideElement yearField = $("input[placeholder='22']");
    private final SelenideElement yearFieldError = $x("//*[text()='Год']/..//*[@class='input__sub']");
    private final SelenideElement ownerField = $(byText("Владелец")).parent().$("input");
    private final SelenideElement ownerFieldError = $x("//*[text()='Владелец']/..//*[@class='input__sub']");
    private final SelenideElement cvcField = $("input[placeholder='999']");
    private final SelenideElement cvcFieldError = $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']");
    private final SelenideElement notificationSuccessfully = $(".notification_status_ok");
    private final SelenideElement notificationError = $(".notification_status_error");
    private final SelenideElement continueButton = $("form button");

    private final SelenideElement improperFormat =  $(byText("Неверный формат"));
    private final SelenideElement emptyField =  $(byText("Поле обязательно для заполнения"));
    private final SelenideElement invalidExpiredDate =  $(byText("Неверно указан срок действия карты"));
    private final SelenideElement expiredDatePass =  $(byText("Истёк срок действия карты"));
//    private final SelenideElement successNote =  $(byText("Операция одобрена Банком."));
//    private final SelenideElement failureNote =  $(byText("Ошибка! Банк отказал в проведении операции."));


    public void cardPayment() {
        mainPage.buyButton.click();
        mainPage.buyHeading.shouldBe(visible);
    }

    public void cardCredit() {
        mainPage.creditButton.click();
        mainPage.creditHeading.shouldBe(visible);
    }
    public void voidForm() {
        continueButton.click();
        cardNumberFieldError.shouldBe(Condition.visible);
        monthFieldError.shouldBe(Condition.visible);
        yearFieldError.shouldBe(Condition.visible);
        ownerFieldError.shouldBe(Condition.visible);
        cvcFieldError.shouldBe(Condition.visible);
    }

    private void cardNumberFieldErrorHidden() {
        monthFieldError.shouldBe(Condition.hidden);
        yearFieldError.shouldBe(Condition.hidden);
        ownerFieldError.shouldBe(Condition.hidden);
        cvcFieldError.shouldBe(Condition.hidden);
    }

    public void voidCardNumber(DataMaker.CardInfo info) {
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
        cardNumberFieldError.shouldBe(Condition.visible);
        cardNumberFieldErrorHidden();
    }

    private void monthFieldErrorHidden() {
        cardNumberFieldError.shouldBe(Condition.hidden);
        yearFieldError.shouldBe(Condition.hidden);
        ownerFieldError.shouldBe(Condition.hidden);
        cvcFieldError.shouldBe(Condition.hidden);
    }

    public void voidMonthField(DataMaker.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
        monthFieldError.shouldBe(Condition.visible);
        monthFieldErrorHidden();
    }

    private void yearFieldErrorHidden() {
        cardNumberFieldError.shouldBe(Condition.hidden);
        monthFieldError.shouldBe(Condition.hidden);
        ownerFieldError.shouldBe(Condition.hidden);
        cvcFieldError.shouldBe(Condition.hidden);
    }

    public void voidYearField(DataMaker.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
        yearFieldError.shouldBe(Condition.visible);
        yearFieldErrorHidden();
    }

    private void ownerFieldErrorHidden() {
        cardNumberFieldError.shouldBe(Condition.hidden);
        monthFieldError.shouldBe(Condition.hidden);
        yearFieldError.shouldBe(Condition.hidden);
        cvcFieldError.shouldBe(Condition.hidden);
    }

    public void voidOwnerField(DataMaker.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        cvcField.setValue(info.getCvc());
        continueButton.click();
        ownerFieldError.shouldBe(Condition.visible);
        ownerFieldErrorHidden();
    }

    private void cvcFieldErrorHidden() {
        cardNumberFieldError.shouldBe(Condition.hidden);
        monthFieldError.shouldBe(Condition.hidden);
        yearFieldError.shouldBe(Condition.hidden);
        ownerFieldError.shouldBe(Condition.hidden);
    }

    public void voidCVCField(DataMaker.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        continueButton.click();
        cvcFieldError.shouldBe(Condition.visible);
        cvcFieldErrorHidden();
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

    public void sendingData(DataMaker.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
    }

    //Время ожидания ответа 15 секунд.
    public void bankApproved() {
        notificationSuccessfully.shouldBe(visible,Duration.ofSeconds(15));
    }

    public void bankDeclined() {
        notificationError.shouldBe(visible, Duration.ofSeconds(15));
    }
}