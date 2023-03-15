
# Дипломная работа

В данном проекте реализована автоматизация тестовых сценариев, проверяющих работоспособность сервиса, предоставляющего возможность приобретения тура.

### А именно путем:

1. Оплаты дебетовой картой.
2. Получив кредит на тур.

### Документация по дипломной работе включает в себя:

1. [План автоматизации тестирования веб-сервиса для покупки тура](https://github.com/fshakrun/Diploma-QA-Netology/blob/main/Plan.md)
2. [Отчётные документы по итогам тестирования](https://github.com/fshakrun/Diploma-QA-Netology/blob/main/Report.md)
2. Отчётные документы по итогам автоматизации


### Предусловия и окружение:

* Порты: 8080, 9999, 5432 для Postgres или 3306 MySQL свободны;
* Наличие Docker/Docker Desktop.

### Запуск данного проекта:

1. Запустить Docker Desktop.

2. Открыть проект с помощью IntelliJ IDEA.

3. В терминале IntelliJ IDEA выполнить команду ```docker-compose up -d``` , дождаться запуска контейнеров.

4. В терминале IntelliJ IDEA выполнить команду для запуска приложения, в зависимости от предпочитаемой СУБД:

**MySQL:** ```java -jar artifacts\aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app```

**Postgres:** ```java -jar artifacts\aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app ```

5. В терминале IntelliJ IDEA выполнить команду для запуска автотестов, в зависимости от предпочитаемой СУБД:

**MySQL:** ```.\gradlew clean test -D dbUrl=jdbc:mysql://localhost:3306/app -D dbUser=postgres -D dbPass=630287```

**Postgres:** ```.\gradlew clean test -D dbUrl=jdbc:postgresql://localhost:5432/app -D dbUser=postgres -D dbPass=630287```


6. В терминале IntelliJ IDEA для получения отчета Allure выполнить команду: ```.\gradlew allureServe```
