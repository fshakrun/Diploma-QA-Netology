package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class DataMaker {
    private static final String validCard = "4444 4444 4444 4441";
    private static final String invalidCard = "4444 4444 4444 4442";
    private static final String[] numbers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    private DataMaker() {
    }

    private static String getIncompleteCardNumber() {
        //Номер карты без одной цифры:
        return validCard.substring(0, 18);
    }


    private static String getZeroMonthValue() {
        return "00";
    }

    private static String getThirteenMonthValue() {
        return "13";
    }

    private static String getValidMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    private static String getPastYear() {    //Прошлый год:
        return LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    private static String getTenYears() {    //Текущий год + 10 лет:
        return LocalDate.now().plusYears(10).format(DateTimeFormatter.ofPattern("yy"));
    }

    private static String getValidYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    private static String getSpecialSymbols() {
        Random random = new Random();
        final String[] specialSymbols = new String[]{"!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+",
                ",", ".", "/", ":", ";", "<", "=", ">", "?", "@", "[", "\\", "]", "^", "_", "`", "{", "|", "}", "~"};
        var fistSymbol = specialSymbols[random.nextInt(31)];
        var secondSymbol = specialSymbols[random.nextInt(31)];
        return fistSymbol + secondSymbol;
    }

    private static String getNumbers() {
        Random random = new Random();
        var fistNumber = numbers[random.nextInt(10)];
        var secondNumber = numbers[random.nextInt(9) + 1];
        //Диапазон от 01 до 100:
        return fistNumber + secondNumber;
    }

    private static String getOwner() {
        Faker faker = new Faker();
        return faker.name().fullName();
    }

    private static String getCVC() {
        Random random = new Random();
        var firstNumber = numbers[random.nextInt(10)];
        var secondNumber = getNumbers();
        //Диапазон от 001 до 1000:
        return firstNumber + secondNumber;
    }

    public static CardInfo getApprovedCard() {
        return new CardInfo(validCard, getValidMonth(), getValidYear(), getOwner(), getCVC());
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo(invalidCard, getValidMonth(), getValidYear(), getOwner(), getCVC());
    }

    public static CardInfo getCardWithIncompleteCardNumber() {
        return new CardInfo(getIncompleteCardNumber(), getValidMonth(), getValidYear(), getOwner(), getCVC());
    }


    public static CardInfo getCardWithZeroMonthValue() {
        return new CardInfo(validCard, getZeroMonthValue(), getValidYear(), getOwner(), getCVC());
    }

    public static CardInfo getCardWithThirteenMonth() {
        return new CardInfo(validCard, getThirteenMonthValue(), getValidYear(), getOwner(), getCVC());
    }

    public static CardInfo getCardWithPastYear() {
        return new CardInfo(validCard, getValidMonth(), getPastYear(), getOwner(), getCVC());
    }

    public static CardInfo getCardWithTenYearsAfter() {
        return new CardInfo(validCard, getValidMonth(), getTenYears(), getOwner(), getCVC());
    }

    public static CardInfo getCardWithSpecialSymbolsOwner() {
        return new CardInfo(validCard, getValidMonth(), getValidYear(), getSpecialSymbols(), getCVC());
    }

    public static CardInfo getCardWithNumbersOwner() {
        return new CardInfo(validCard, getValidMonth(), getValidYear(), getNumbers(), getCVC());
    }

    public static CardInfo getCardWithIncompleteCVC() {
        return new CardInfo(validCard, getValidMonth(), getValidYear(), getOwner(), getNumbers());
    }

    @Value
    public static class CardInfo {
        private String numberCard;
        private String month;
        private String year;
        private String owner;
        private String cvc;
    }
}