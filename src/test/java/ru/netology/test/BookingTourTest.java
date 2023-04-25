package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataMaker;
import ru.netology.pages.BookingPage;
import ru.netology.pages.MainPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.DataMaker.*;

public class BookingTourTest {

    MainPage mainPage = new MainPage();

    BookingPage bookingPage = new BookingPage();

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
        @DisplayName("1.Оплата картой")
        public void shouldBookValidCard() {
            var bookingPage = new BookingPage();
            mainPage.cardPayment();
            var info = getApprovedCard();
            bookingPage.sendingData(info);
            //Время отправки данных в БД:
//            TimeUnit.SECONDS.sleep(15);
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

        @Nested
        //Негативные тестовые сценарии:
        public class InvalidCard {

            @Test
            @DisplayName("2.3 Невалидный (полный номер) номер карты(покупка).")
            public void shouldDeclineInvalidCard() {
                var bookingPage = new BookingPage();
                mainPage.cardPayment();
                var info = getDeclinedCard();
                bookingPage.sendingData(info);
                //Время отправки данных в БД:
//                TimeUnit.SECONDS.sleep(10);
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

        }

        @Nested
        //Тесты на валидацию полей формы покупки по карте:
        public class PaymentFormFieldValidation {

            @BeforeEach
            public void setPayment() {
                var bookingPage = new BookingPage();
                mainPage.cardPayment();
            }

            @Test
            @DisplayName("1. Незаполенная данными форма")
            public void shouldCheckVoid() {
                var bookingPage = new BookingPage();
                var emptyForm = DataMaker.getEmptyCardInfo();
                bookingPage.sendingData(emptyForm);
                bookingPage.notificationErrorNum();
                bookingPage.notificationInvalidMonthValue();
                bookingPage.notificationInvalidYearValue();
                bookingPage.notificationMandatoryOwnerField();
                bookingPage.notificationIvalidCvcValue();
            }

            @Test
            @DisplayName("2.1 Поле 'Номер карты' остается пустым")
            public void shouldVoidCardNumber() {
                var bookingPage = new BookingPage();
                var info = getEmptyCardNumber();
                bookingPage.sendingData(info);
                bookingPage.notificationErrorNum();
            }

            @Test
            @DisplayName("2.2 Невалидный (неполный номер) номер карты")
            public void shouldCheckIncompleteNumber() {
                var bookingPage = new BookingPage();
                var info = getCardWithIncompleteCardNumber();
                bookingPage.sendingData(info);
                bookingPage.notificationErrorNum();
            }

            @Test
            @DisplayName("3.1 Поле 'Месяц' остается пустым")
            public void shouldCheckVoidMonth() {
                var bookingPage = new BookingPage();
                var emptyMonth = getCardWithEmptyMonthValue();
                bookingPage.sendingData(emptyMonth);
                bookingPage.notificationInvalidMonthValue();
            }

            @Test
            @DisplayName("3.2 Поле 'Месяц'заполнено значением '00'")
            public void shouldCheckZeroMonth() {
                var bookingPage = new BookingPage();
                var info = getCardWithZeroMonthValue();
                bookingPage.sendingData(info);
                bookingPage.notificationInvalidMonth();
            }

            @Test
            @DisplayName("3.3 Поле 'Месяц'заполнено значением '13'")
            public void shouldCheckThirteenMonth() {
                var bookingPage = new BookingPage();
                var info = getCardWithThirteenMonth();
                bookingPage.sendingData(info);
                bookingPage.notificationInvalidMonth();
            }

            @Test
            @DisplayName("4.1 Поле 'Год' остается незаполненным")
            public void shouldCheckVoidYear() {
                var bookingPage = new BookingPage();
                var info = getCardWithEmptyYearValue();
                bookingPage.sendingData(info);
                bookingPage.notificationInvalidYearValue();
            }

            @Test
            @DisplayName("4.2 Поле 'Год' заполняется невалидным значением — прошедшего года")
            public void shouldCheckPastYear() {
                var bookingPage = new BookingPage();
                var info = getCardWithPastYear();
                bookingPage.sendingData(info);
                bookingPage.notificationExpiredYearField();
            }

            @Test
            @DisplayName("4.3 Поле 'Год' заполняется невалидным значением (текущий год + 10)")
            public void shouldCheckTenYears() {
                var bookingPage = new BookingPage();
                var info = getCardWithTenYearsAfter();
                bookingPage.sendingData(info);
                bookingPage.notificationInvalidYear();
            }

            @Test
            @DisplayName("5.1 Поле 'Владелец' остается пустым")
            public void shouldCheckVoidOwner() {
                var bookingPage = new BookingPage();
                var info = getCardWithEmptyOwnerValue();
                bookingPage.sendingData(info);
                bookingPage.notificationMandatoryOwnerField();
            }

            @Test
            @DisplayName("5.2 Поле 'Владелец' содержит несколько произвольных спецсимволов")
            public void shouldCheckSpecialSymbolsOwner() {
                var bookingPage = new BookingPage();
                var info = getCardWithSpecialSymbolsOwner();
                bookingPage.sendingData(info);
                bookingPage.notificationMandatoryOwnerField();
            }

            @Test
            @DisplayName("5.3 Поле 'Владелец' содержит числовые значения")
            public void shouldCheckDigitsOwner() {
                var bookingPage = new BookingPage();
                var info = getCardWithNumbersOwner();
                bookingPage.sendingData(info);
                bookingPage.notificationMandatoryOwnerField();
            }

            @Test
            @DisplayName("6.1 Поле 'CVC/CVV' остается пустым")
            public void shouldCheckVoidCVC() {
                var bookingPage = new BookingPage();
                var info = getCardWithEmptyCVC();
                bookingPage.sendingData(info);
                bookingPage.notificationIvalidCvcValue();
            }

            @Test
            @DisplayName("6.2 Поле 'CVC/CVV' — неполный номер")
            public void shouldCheckIncompleteCVC() {
                var bookingPage = new BookingPage();
                var info = getCardWithIncompleteCVC();
                bookingPage.sendingData(info);
                bookingPage.notificationIvalidCvcValue();
            }
        }
    }
}

