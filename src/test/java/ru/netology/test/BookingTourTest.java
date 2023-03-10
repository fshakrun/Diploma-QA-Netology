package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.pages.BookingPage;

import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataMaker.*;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.DataHelper.getBookingInfo;

public class BookingTourTest {

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {
        open("http://localhost:8080");
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        databaseCleanUp();
    }

    @Nested
    //Позитивные тестовые сценарии оплаты картой и покупки в кредит
    public class ValidCard {

        @Test
        @SneakyThrows
        @DisplayName("1.Оплата картой")
        public void shouldBookValidCard() {
            var bookingPage = new BookingPage();
            bookingPage.cardPayment();
            var info = getApprovedCard();
            bookingPage.sendingData(info);
            //Время отправки данных в БД:
            TimeUnit.SECONDS.sleep(10);
            var expected = "APPROVED";
            var paymentInfo = getBookingInfo();
            var orderInfo = getOrderInfo();
            //Проверка соответствия статуса в БД в таблице брони:
            assertEquals(expected, paymentInfo.getStatus());
            //Проверка соответствия в БД id в таблице брони и в таблице заявок:
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            //Проверка наличия соответствующего уведомления пользователю на странице брони:
            bookingPage.bankApproved();
        }

        @Test
        @SneakyThrows
        @DisplayName("2. Покупка в кредит")
        public void shouldGetCreditValidCard() {
            var bookingPage = new BookingPage();
            bookingPage.cardCredit();
            var info = getApprovedCard();
            bookingPage.sendingData(info);
            //Время отправки данных в БД:
            TimeUnit.SECONDS.sleep(10);
            var expected = "APPROVED";
            var creditRequestInfo = getCreditRequestInfo();
            var orderInfo = getOrderInfo();
            //Проверка соответствия статуса в БД в таблице запросов кредита:
            assertEquals(expected, creditRequestInfo.getStatus());
            //Проверка соответствия в БД id в таблице запросов кредита и в таблице заявок:
            assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());
            //Проверка наличия соответствующего уведомления пользователю на странице брони:
            bookingPage.bankApproved();
        }
    }

    @Nested
    //Негативные тестовые сценарии:
    public class InvalidCard {

        @Test
        @SneakyThrows
        @DisplayName("2.3 Невалидный (полный номер) номер карты(покупка).")
        public void shouldDeclineInvalidCard() {
            var bookingPage = new BookingPage();
            bookingPage.cardPayment();
            var info = getDeclinedCard();
            bookingPage.sendingData(info);
            //Время отправки данных в БД:
            TimeUnit.SECONDS.sleep(10);
            var expected = "DECLINED";
            var paymentInfo = getBookingInfo();
            var orderInfo = getOrderInfo();
            //Проверка соответствия статуса в БД в таблице брони:
            assertEquals(expected, paymentInfo.getStatus());
            //Проверка соответствия в БД id в таблице брони и в таблице заявок:
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            //Проверка наличия соответствующего уведомления пользователю на странице брони:
            bookingPage.bankDeclined();
        }

        @Test
        @SneakyThrows
        @DisplayName("Невалидный (полный номер) номер карты (получение кредита).")
        public void shouldNoCreditInvalidCard() {
            var bookingPage = new BookingPage();
            bookingPage.cardCredit();
            var info = getDeclinedCard();
            bookingPage.sendingData(info);
            //Время отправки данных в БД:
            TimeUnit.SECONDS.sleep(10);
            var expected = "DECLINED";
            var creditRequestInfo = getCreditRequestInfo();
            var orderInfo = getOrderInfo();

            assertEquals(expected, creditRequestInfo.getStatus());

            assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());

            bookingPage.bankApproved();
        }
    }

    @Nested
    //Тесты на валидацию полей формы покупки по карте:
    public class PaymentFormFieldValidation {

        @BeforeEach
        public void setPayment() {
            var bookingPage = new BookingPage();
            bookingPage.cardPayment();
        }

        @Test
        @DisplayName("1. Незаполенная данными форма")
        public void shouldCheckVoid() {
            var bookingPage = new BookingPage();
            bookingPage.voidForm();
        }

        @Test
        @DisplayName("2.1 Поле 'Номер карты' остается пустым")
        public void shouldVoidCardNumber() {
            var bookingPage = new BookingPage();
            var info = getApprovedCard();
            bookingPage.voidCardNumber(info);
        }

        @Test
        @DisplayName("2.2 Невалидный (неполный номер) номер карты")
        public void shouldCheckIncompleteNumber() {
            var bookingPage = new BookingPage();
            var info = getCardWithIncompleteCardNumber();
            bookingPage.invalidCardNumberField(info);
        }

        @Test
        @DisplayName("3.1 Поле 'Месяц' остается пустым")
        public void shouldCheckVoidMonth() {
            var bookingPage = new BookingPage();
            var info = getApprovedCard();
            bookingPage.voidMonthField(info);
        }

        @Test
        @DisplayName("3.2 Поле 'Месяц'заполнено значением '00'")
        public void shouldCheckZeroMonth() {
            var bookingPage = new BookingPage();
            var info = getCardWithZeroMonthValue();
            bookingPage.invalidMonthField(info);
        }

        @Test
        @DisplayName("3.3 Поле 'Месяц'заполнено значением '13'")
        public void shouldCheckThirteenMonth() {
            var bookingPage = new BookingPage();
            var info = getCardWithThirteenMonth();
            bookingPage.invalidMonthField(info);
        }

        @Test
        @DisplayName("4.1 Поле 'Год' остается незаполненным")
        public void shouldCheckVoidYear() {
            var bookingPage = new BookingPage();
            var info = getApprovedCard();
            bookingPage.voidYearField(info);
        }

        @Test
        @DisplayName("4.2 Поле 'Год' заполняется невалидным значением — прошедшего года")
        public void shouldCheckPastYear() {
            var bookingPage = new BookingPage();
            var info = getCardWithPastYear();
            bookingPage.invalidYearField(info);
        }

        @Test
        @DisplayName("4.3 Поле 'Год' заполняется невалидным значением (текущий год + 10)")
        public void shouldCheckTenYears() {
            var bookingPage = new BookingPage();
            var info = getCardWithTenYearsAfter();
            bookingPage.invalidYearField(info);
        }

        @Test
        @DisplayName("5.1 Поле 'Владелец' остается пустым")
        public void shouldCheckVoidOwner() {
            var bookingPage = new BookingPage();
            var info = getApprovedCard();
            bookingPage.voidOwnerField(info);
        }

        @Test
        @DisplayName("5.2 Поле 'Владелец' содержит несколько произвольных спецсимволов")
        public void shouldCheckSpecialSymbolsOwner() {
            var bookingPage = new BookingPage();
            var info = getCardWithSpecialSymbolsOwner();
            bookingPage.invalidOwnerField(info);
        }

        @Test
        @DisplayName("5.3 Поле 'Владелец' содержит числовые значения")
        public void shouldCheckDigitsOwner() {
            var bookingPage = new BookingPage();
            var info = getCardWithNumbersOwner();
            bookingPage.invalidOwnerField(info);
        }

        @Test
        @DisplayName("6.1 Поле 'CVC/CVV' остается пустым")
        public void shouldCheckVoidCVC() {
            var bookingPage = new BookingPage();
            var info = getApprovedCard();
            bookingPage.voidCVCField(info);
        }

        @Test
        @DisplayName("6.2 Поле 'CVC/CVV' — неполный номер")
        public void shouldCheckIncompleteCVC() {
            var bookingPage = new BookingPage();
            var info = getCardWithIncompleteCVC();
            bookingPage.invalidCVCField(info);
        }
    }

    @Nested
    //Тесты на валидацию полей формы покупки в кредит:
    public class CreditFormValidation {

        @BeforeEach
        public void setPayment() {
            var bookingPage = new BookingPage();
            bookingPage.cardCredit();
        }

        @Test
        @DisplayName("Незаполенная данными форма")
        public void shouldCheckVoid() {
            var bookingPage = new BookingPage();
            bookingPage.voidForm();
        }

        @Test
        @DisplayName("Поле 'Номер карты' остается пустым")
        public void shouldVoidCardNumber() {
            var bookingPage = new BookingPage();
            var info = getApprovedCard();
            bookingPage.voidCardNumber(info);
        }

        @Test
        @DisplayName("Невалидный (неполный номер) номер карты")
        public void shouldCheckIncompleteNumber() {
            var bookingPage = new BookingPage();
            var info = getCardWithIncompleteCardNumber();
            bookingPage.invalidCardNumberField(info);
        }

        @Test
        @DisplayName("Поле 'Месяц' остается пустым")
        public void shouldCheckVoidMonth() {
            var bookingPage = new BookingPage();
            var info = getApprovedCard();
            bookingPage.voidMonthField(info);
        }


        @Test
        @DisplayName("Поле 'Месяц'заполнено значением '00'")
        public void shouldCheckZeroMonth() {
            var bookingPage = new BookingPage();
            var info = getCardWithZeroMonthValue();
            bookingPage.invalidMonthField(info);
        }

        @Test
        @DisplayName("Поле 'Месяц'заполнено значением'13'")
        public void shouldCheckThirteenMonth() {
            var bookingPage = new BookingPage();
            var info = getCardWithThirteenMonth();
            bookingPage.invalidMonthField(info);
        }

        @Test
        @DisplayName("Поле 'Год' остается незаполненным")
        public void shouldCheckVoidYear() {
            var bookingPage = new BookingPage();
            var info = getApprovedCard();
            bookingPage.voidYearField(info);
        }

        @Test
        @DisplayName("Поле 'Год' заполняется невалидным значением — прошедшего года")
        public void shouldCheckPastYear() {
            var bookingPage = new BookingPage();
            var info = getCardWithPastYear();
            bookingPage.invalidYearField(info);
        }

        @Test
        @DisplayName("Поле 'Год' заполняется невалидным значением (текущий год + 10)")
        public void shouldCheckTenYears() {
            var bookingPage = new BookingPage();
            var info = getCardWithTenYearsAfter();
            bookingPage.invalidYearField(info);
        }

        @Test
        @DisplayName("Поле 'Владелец' остается пустым")
        public void shouldCheckVoidOwner() {
            var bookingPage = new BookingPage();
            var info = getApprovedCard();
            bookingPage.voidOwnerField(info);
        }


        @Test
        @DisplayName("Поле 'Владелец' содержит несколько произвольных спецсимволов")
        public void shouldCheckSpecialSymbolsOwner() {
            var bookingPage = new BookingPage();
            var info = getCardWithSpecialSymbolsOwner();
            bookingPage.invalidOwnerField(info);
        }

        @Test
        @DisplayName("Поле 'Владелец' содержит числовые значения")
        public void shouldCheckDigitsOwner() {
            var bookingPage = new BookingPage();
            var info = getCardWithNumbersOwner();
            bookingPage.invalidOwnerField(info);
        }

        @Test
        @DisplayName("Поле 'CVC/CVV' остается пустым")
        public void shouldCheckVoidCVC() {
            var bookingPage = new BookingPage();
            var info = getApprovedCard();
            bookingPage.voidCVCField(info);
        }

        @Test
        @DisplayName("Поле 'CVC/CVV' — неполный номер")
        public void shouldCheckIncompleteCVC() {
            var bookingPage = new BookingPage();
            var info = getCardWithIncompleteCVC();
            bookingPage.invalidCVCField(info);
        }
    }
}