package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;

import static com.codeborne.selenide.Selenide.$;


public class MainPage {
    final SelenideElement buyButton = $(byText("Купить"));
    final SelenideElement buyHeading = $(byText("Оплата по карте"));
    //Находим кнопку "Купить в кредит":
    final SelenideElement creditButton = $(byText("Купить в кредит"));
    final SelenideElement creditHeading = $(byText("Кредит по данным карты"));

}