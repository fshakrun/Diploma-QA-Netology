package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataMaker;
import ru.netology.pages.BookingPage;

import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.DataMaker.*;

public class CreditFormTest {

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
        @SneakyThrows
        @DisplayName("2. Покупка в кредит")
        public void shouldGetCreditValidCard() {
            var bookingPage = new BookingPage();
            bookingPage.cardCredit();
            var info = getApprovedCard();
            bookingPage.sendingData(info);
            //Время отправки данных в БД:
            TimeUnit.SECONDS.sleep(15);
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
        @SneakyThrows
        @DisplayName("Невалидный (полный номер) номер карты (получение кредита).")
        public void shouldNoCreditInvalidCard() {
            var bookingPage = new BookingPage();
            bookingPage.cardCredit();
            var info = getDeclinedCard();
            bookingPage.sendingData(info);
            //Время отправки данных в БД:
            TimeUnit.SECONDS.sleep(15);
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
            bookingPage.cardCredit();
        }

        @Test
        @DisplayName("Незаполенная данными форма")
        public void shouldCheckVoid() {
            var bookingPage = new BookingPage();
            val emptyForm = DataMaker.getEmptyCardInfo();
            bookingPage.sendingData(emptyForm);
            bookingPage.cardNumberFieldError.shouldBe(visible);
            bookingPage.monthFieldError.shouldBe(visible);
            bookingPage.yearFieldError.shouldBe(visible);
            bookingPage.ownerFieldError.shouldBe(visible);
            bookingPage.cvcFieldError.shouldBe(visible);
        }

        @Test
        @DisplayName("Поле 'Номер карты' остается пустым")
        public void shouldVoidCardNumber() {
            var bookingPage = new BookingPage();
            var info = getEmptyCardNumber();
            bookingPage.sendingData(info);
            bookingPage.cardNumberFieldErrorHidden();
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
            var info = getCardWithEmptyMonthValue();
            bookingPage.monthFieldErrorHidden();
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
            var info = getCardWithEmptyYearValue();
            bookingPage.yearFieldErrorHidden();
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
            var info = getCardWithEmptyOwnerValue();
            bookingPage.ownerFieldErrorHidden();
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
            var info = getCardWithEmptyCVC();
            bookingPage.cvcFieldErrorHidden();
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