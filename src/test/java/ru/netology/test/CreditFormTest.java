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

public class CreditFormTest {

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
    public class ValidCreditCard {

        @Test
        @DisplayName("2. Покупка в кредит")
        public void shouldGetCreditValidCard() {
            var bookingPage = new BookingPage();
            mainPage.cardCredit();
            var info = getApprovedCard();
            bookingPage.sendingData(info);
            //Время отправки данных в БД:
//            TimeUnit.SECONDS.sleep(15);
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

        @Test
        @DisplayName("Невалидный (полный номер) номер карты (получение кредита).")
        public void shouldNoCreditInvalidCard() {
            var bookingPage = new BookingPage();
            mainPage.cardCredit();
            var info = getDeclinedCard();
            bookingPage.sendingData(info);
            //Время отправки данных в БД:
//            TimeUnit.SECONDS.sleep(15);
            var expected = "DECLINED";
            var creditRequestInfo = getCreditRequestInfo();
            var orderInfo = getOrderInfo();

            assertEquals(expected, creditRequestInfo.getStatus());

            assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());

            bookingPage.bankApproved();
        }
    }

    //Тесты на валидацию полей формы покупки в кредит:

    @Nested
    public class InvalidCreditCard {

        @BeforeEach
        public void setCredit() {
            var bookingPage = new BookingPage();
            mainPage.cardCredit();
        }

        @Test
        @DisplayName("Незаполенная данными форма")
        public void shouldCheckVoid() {
            var bookingPage = new BookingPage();
            val emptyForm = DataMaker.getEmptyCardInfo();
            bookingPage.sendingData(emptyForm);
            bookingPage.notificationErrorNum();
            bookingPage.notificationInvalidMonthValue();
            bookingPage.notificationInvalidYearValue();
            bookingPage.notificationMandatoryOwnerField();
            bookingPage.notificationIvalidCvcValue();
        }

        @Test
        @DisplayName("Поле 'Номер карты' остается пустым")
        public void shouldVoidCardNumber() {
            var bookingPage = new BookingPage();
            var info = getEmptyCardNumber();
            bookingPage.sendingData(info);
            bookingPage.notificationErrorNum();
        }

        @Test
        @DisplayName("Невалидный (неполный номер) номер карты")
        public void shouldCheckIncompleteNumber() {
            var bookingPage = new BookingPage();
            var info = getCardWithIncompleteCardNumber();
            bookingPage.sendingData(info);
            bookingPage.notificationErrorNum();
        }

        @Test
        @DisplayName("Поле 'Месяц' остается пустым")
        public void shouldCheckVoidMonth() {
            var bookingPage = new BookingPage();
            var emptyMonth = getCardWithEmptyMonthValue();
            bookingPage.sendingData(emptyMonth);
            bookingPage.notificationInvalidMonthValue();
        }


        @Test
        @DisplayName("Поле 'Месяц'заполнено значением '00'")
        public void shouldCheckZeroMonth() {
            var bookingPage = new BookingPage();
            var info = getCardWithZeroMonthValue();
            bookingPage.sendingData(info);
            bookingPage.notificationInvalidMonth();
        }

        @Test
        @DisplayName("Поле 'Месяц'заполнено значением'13'")
        public void shouldCheckThirteenMonth() {
            var bookingPage = new BookingPage();
            var info = getCardWithThirteenMonth();
            bookingPage.sendingData(info);
            bookingPage.notificationInvalidMonth();
        }

        @Test
        @DisplayName("Поле 'Год' остается незаполненным")
        public void shouldCheckVoidYear() {
            var bookingPage = new BookingPage();
            var info = getCardWithEmptyYearValue();
            bookingPage.sendingData(info);
            bookingPage.notificationInvalidYearValue();
        }

        @Test
        @DisplayName("Поле 'Год' заполняется невалидным значением — прошедшего года")
        public void shouldCheckPastYear() {
            var bookingPage = new BookingPage();
            var info = getCardWithPastYear();
            bookingPage.sendingData(info);
            bookingPage.notificationExpiredYearField();
        }

        @Test
        @DisplayName("Поле 'Год' заполняется невалидным значением (текущий год + 10)")
        public void shouldCheckTenYears() {
            var bookingPage = new BookingPage();
            var info = getCardWithTenYearsAfter();
            bookingPage.sendingData(info);
            bookingPage.notificationInvalidYear();
        }

        @Test
        @DisplayName("Поле 'Владелец' остается пустым")
        public void shouldCheckVoidOwner() {
            var bookingPage = new BookingPage();
            var info = getCardWithEmptyOwnerValue();
            bookingPage.sendingData(info);
            bookingPage.notificationMandatoryOwnerField();
        }


        @Test
        @DisplayName("Поле 'Владелец' содержит несколько произвольных спецсимволов")
        public void shouldCheckSpecialSymbolsOwner() {
            var bookingPage = new BookingPage();
            var info = getCardWithSpecialSymbolsOwner();
            bookingPage.sendingData(info);
            bookingPage.notificationMandatoryOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец' содержит числовые значения")
        public void shouldCheckDigitsOwner() {
            var bookingPage = new BookingPage();
            var info = getCardWithNumbersOwner();
            bookingPage.sendingData(info);
            bookingPage.notificationMandatoryOwnerField();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV' остается пустым")
        public void shouldCheckVoidCVC() {
            var bookingPage = new BookingPage();
            var info = getCardWithEmptyCVC();
            bookingPage.sendingData(info);
            bookingPage.notificationIvalidCvcValue();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV' — неполный номер")
        public void shouldCheckIncompleteCVC() {
            var bookingPage = new BookingPage();
            var info = getCardWithIncompleteCVC();
            bookingPage.sendingData(info);
            bookingPage.notificationIvalidCvcValue();
        }
    }
}