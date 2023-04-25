package ru.netology.data;


import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataHelper {
    private static Connection connection;
    private static QueryRunner runner;


    @SneakyThrows
    public static void initConnection() {
        runner = new QueryRunner();
        connection = DriverManager.getConnection(System.getProperty("url"), ("user"), ("password"));
    }


    @SneakyThrows
    public static void databaseCleanUp() {
        initConnection();
        var runner = new QueryRunner();
        var deleteFromOrder = "DELETE FROM order_entity;";
        var deleteFromCredit = "DELETE FROM credit_request_entity;";
        var deleteFromPayment = "DELETE FROM payment_entity;";
        runner.update(connection, deleteFromOrder);
        runner.update(connection, deleteFromCredit);
        runner.update(connection, deleteFromPayment);
    }

    @SneakyThrows
    public static CreditData getCreditRequestInfo() {
        initConnection();
        var runner = new QueryRunner();
        var creditRequestInfo = "SELECT * FROM credit_request_entity WHERE created = (SELECT MAX(created) FROM credit_request_entity);";
        return runner.query(connection, creditRequestInfo, new BeanHandler<>(CreditData.class));

    }

    @SneakyThrows
    public static TransactionData getBookingInfo() {
        initConnection();
        var runner = new QueryRunner();
        var paymentInfo = "SELECT * FROM payment_entity WHERE created = (SELECT MAX(created) FROM payment_entity);";
        return runner.query(connection, paymentInfo, new BeanHandler<>(TransactionData.class));
    }

    @SneakyThrows
    public static OrderData getOrderInfo() {
        initConnection();
        var runner = new QueryRunner();
        var orderInfo = "SELECT * FROM order_entity WHERE created = (SELECT MAX(created) FROM order_entity);";
        return runner.query(connection, orderInfo, new BeanHandler<>(OrderData.class));

    }
}