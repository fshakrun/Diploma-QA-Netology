# План автоматизации тестирования веб-сервиса для покупки тура

## Автоматизируемые сценарии

### Предусловия:

* Валидный номер карты - 4444 4444 4444 4441
* Невалидный номер карты - 4444 4444 4444 4442
* Установлен Docker либо Node.js
* SUT запущена

### Позитивные сценарии:

#### 1. Оплата картой:

1.1 Открыть страницу "Путешествие дня" \
1.2 Кликнуть на кнопку "Купить" \
1.3 Во всех полях формы ввести валидные данные, а именно: 

* Валидный номер карты (см. "Предусловия"), 
* Месяц в формате от 01 до 12, но не раньше текущего,
* Год в двузначном представлении, но не раньше текущего и не старше более чем на 5,
* Имя и фамилия латинскими буквами,
* CVC/CVV в трехзначном представлении от 100 до 999

1.4 Кликнуть на кнопку "Продолжить"

**Ожидаемый результат:**

* Данные успешно отправлены, появляется поп-ап в правом верхнем углу экрана с сообщением: "Успешно Операция одобрена банком";

#### 2. Покупка в кредит:

2.1 Открыть страницу "Путешествие дня" \
2.2 Кликнуть на кнопку "Купить в кредит" \
2.3 В появившихся полях ввести валидные данные\
2.4 Кликнуть на кнопку "Продолжить"

**Ожидаемый результат:**

* Данные успешно отправлены, появляется поп-ап в правом верхнем углу экрана с сообщением: "Успешно Операция одобрена банком";

### Негативные сценарии:

#### 1. Незаполенная данными форма:

1.1 Открыть страницу "Путешествие дня" \
1.2 Кликнуть на кнопку "Купить" \
1.3 Кликнуть на кнопку "Продолжить"

**Ожидаемый результат**:

Данные не отправлены;
* под полем "Номер карты" появляется сообщение "Неверный формат";
* под полем "Месяц" появляется сообщение "Неверный формат";
* под полем "Год" появляется сообщение "Неверный формат";
* под полем "Владелец" появляется сообщение "Поле обязательно для заполнения";
* под полем "CVC/CVV" появляется сообщение "Неверный формат".

#### 2. Поле "Номер карты":

##### 2.1 Незаполенная данными форма:
2.1.1 Открыть страницу "Путешествие дня" \
2.1.2 Кликнуть на кнопку "Купить" \
2.1.3 Поле "Номер карты" не заполнять, а остальные поля заполнить валидными данными\
2.1.4 Кликнуть на кнопку "Продолжить"

**Ожидаемый результат**:

* Данные не отправлены;
* Под полем "Номер карты" появляется сообщение "Неверный формат".

##### 2.2 Невалидный (неполный номер) номер:

2.2.1 Открыть страницу "Путешествие дня" \
2.2.2 Кликнуть на кнопку "Купить" \
2.2.3 В поле "Номер карты" ввести неполный номер (например, 4444 4444 4444 444), а остальные поля заполнить валидными данными\
2.2.4 Кликнуть на кнопку "Продолжить"

**Ожидаемый результат**:

* Данные не отправлены;
* Под полем "Номер карты" появляется сообщение "Неверный формат".

##### 2.3 Невалидный (полный номер) номер:

2.3.1 Открыть страницу "Путешествие дня" \
2.3.2 Кликнуть на кнопку "Купить" \
2.3.3 В поле "Номер карты" ввести невалидный номер карты (то есть 4444 4444 4444 4442), а остальные поля заполнить валидными данными\
2.3.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:

* Появляется поп-ап в правом верхнем углу экрана с сообщением об ошибке и отказе проведения операции банком

#### 3.1 Поле "Месяц":

3.1.1 Открыть страницу "Путешествие дня" \
3.1.2 Кликнуть на кнопку "Купить" \
3.1.3 Поле "Месяц" оставить незаполенным, остальные поля заполнить валидными данными \
3.1.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:
* Данные не отправлены;
* Под полем "Месяц" появляется сообщение "Неверный формат"

#### 3.2 Поле "Месяц":

3.2.1 Открыть страницу "Путешествие дня" \
3.2.2 Кликнуть на кнопку "Купить" \
3.2.3 В поле "Месяц" ввести значение 00, а остальные поля заполнить валидными данными\
3.2.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:
* Данные не отправлены;
* Под полем "Месяц" появляется сообщение "Неверно указан срок действия карты".

#### 3.3 Поле "Месяц":

3.3.1 Открыть страницу "Путешествие дня" \
3.3.2 Кликнуть на кнопку "Купить" \
3.3.3 В поле "Месяц" ввести невалидное для месяца значение (в диапазоне от 13 до 99), остальные поля заполнить валидными данными\
3.3.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:
* Данные не отправлены;
* Под полем "Месяц" появляется сообщение "Неверно указан срок действия карты".

#### 4.1 Поле "Год":

4.1.1 Открыть страницу "Путешествие дня" \
4.1.2 Кликнуть на кнопку "Купить" \
4.1.3 Поле "Год" оставить незаполненным, остальные поля заполнить валидными данными\
4.1.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:
* Данные не отправлены;
* Под полем "Год" появляется сообщение "Неверный формат".

#### 4.2 Поле "Год":

4.2.1 Открыть страницу "Путешествие дня" \
4.2.2 Кликнуть на кнопку "Купить" \
4.2.3 В поле "Год" ввести невалидный год (текущий год - 1, например, если сейчас 23, то ввести 22)\
4.2.4 Остальные поля заполнить валидными данными\
4.1.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:
* Данные не отправлены;
* Под полем "Год" появляется сообщение "Истёк срок действия карты".

#### 4.3 Поле "Год":

4.3.1 Открыть страницу "Путешествие дня" \
4.3.2 Кликнуть на кнопку "Купить" \
4.3.3 В поле "Год" ввести невалидный год (текущий год + 10, например, если сейчас 23, то ввести 33), а остальные поля заполнить валидными данными\
4.3.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:
* Данные не отправлены;
* Под полем "Год" появляется сообщение "Неверно указан срок действия карты".

#### 5.1 Поле "Владелец":

5.1.1 Открыть страницу "Путешествие дня" \
5.1.2 Кликнуть на кнопку "Купить" \
5.1.3 Поле "Владелец" оставить пустым, а остальные поля заполнить валидными данными\
5.1.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:
* Данные не отправлены;
* Под полем "Владелец" появляется сообщение "Поле обязательно для заполнения".


#### 5.2 Поле "Владелец":

5.2.1 Открыть страницу "Путешествие дня" \
5.2.2 Кликнуть на кнопку "Купить" \
5.2.3 В поле "Владелец" ввести несколько произвольных спецсимволов (например, /#&) , а остальные поля заполнить валидными данными\
5.2.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:
* Данные не отправлены;
* Под полем "Владелец" появляется сообщение "Поле обязательно для заполнения".

#### 5.3 Поле "Владелец":

5.3.1 Открыть страницу "Путешествие дня" \
5.3.2 Кликнуть на кнопку "Купить" \
5.3.3 В поле "Владелец" ввести числовые значения в произвольном диапазоне, а остальные поля заполнить валидными данными\
5.3.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:
* Данные не отправлены;
* Под полем "Владелец" появляется сообщение "Поле обязательно для заполнения".

#### 6.1 Поле "CVC/CVV":

6.1.1 Открыть страницу "Путешествие дня" \
6.1.2 Кликнуть на кнопку "Купить" \
6.1.3 Поле "CVC/CVV" оставить пустым, а остальные поля заполнить валидными данными\
6.1.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:
* Данные не отправлены;
* Под полем "CVC/CVV" появляется сообщение "Неверный формат".

#### 6.2 Поле "CVC/CVV":

6.2.1 Открыть страницу "Путешествие дня" \
6.2.2 Кликнуть кнопку "Купить" (оплата картой)\
6.2.3 В поле "CVC/CVV" ввести двузначное число в диапазоне от 10 до 99, а остальные поля заполнить валидными данными\
6.2.4 Кликнуть на кнопку "Продолжить";

**Ожидаемый результат**:
* Данные не отправлены;
* Под полем "CVC/CVV" появляется сообщение "Неверный формат".

### => Аналогичные тестовые сценарии для формы "Кредит по данным карты", доступной по нажатию кнопки "Купить в кредит" на странице "Путешествие дня".

## Используемые инструменты:

* JDK 11 и IntelliJ IDEA как среда для написания тестов на Java;
* Gradle для автоматизации сборки приложений
* DB Browser для проверки заполнения баз данных без построения запросов
* JUnit Jupiter — удобный модуль для написания тестов
* Selenide — удобный и надежный фреймворк для автоматизированного тестирования веб-приложений
* PostgreSQL для работы с базами данных в рамках автотестов
* Faker — библиотека для генерации для тестов формы заявки
* Github для хранения кода и SUT
* Allure для отчетности, касающейся автотестов.

## Возможные риски

* Отсутствие документации к приложению
* Поддержка двух СУБД: MySQL и PostgreSQL
* Автотесты разработаны под актуальную версию верстки элементов страницы и даже простое переименование их (элементов) может привести к сбою

## Интервальная оценка с учётом рисков в часах

160 рабочих часов

## План выполнения работ

Разработка и внедрение автотестов до 10 марта 2023 года\
Предоставление финальной отчетности до 15 марта 2023 года
