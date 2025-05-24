package ru.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CourierSteps { // Класс, содержащий шаги для работы с API курьеров

    private static final String COURIER_CREATE = "/api/v1/courier"; // Ручка для создания курьера
    private static final String COURIER_LOGIN = "/api/v1/courier/login"; // Ручка для логина курьера

    @Step("Создание курьера") // Описание шага для Allure отчетов
    public Response sendPostRequestCourierCreate(Courier courier) { // Метод для отправки POST-запроса на создание курьера
        return
                given() // Начинаем построение запроса
                        .log().ifValidationFails() // Логируем запрос и ответ, если валидация не прошла
                        .filter(new AllureRestAssured()) // Добавляем фильтр AllureRestAssured для Allure-отчетов
                        .contentType(ContentType.JSON) // Устанавливаем Content-Type как JSON
                        .body(courier) // Устанавливаем тело запроса (объект Courier)
                        .when() // Переходим к действию
                        .post(COURIER_CREATE); // Отправляем POST-запрос на ручку COURIER_CREATE
    }

    @Step("Авторизация курьера, получение ID") // Описание шага для Allure отчетов
    public Response sendPostRequestCourierLogin(Courier courier) { // Метод для отправки POST-запроса на авторизацию курьера
        return
                given() // Начинаем построение запроса
                        .log().ifValidationFails() // Логируем запрос и ответ, если валидация не прошла
                        .contentType(ContentType.JSON) // Устанавливаем Content-Type как JSON
                        .body(courier) // Устанавливаем тело запроса (объект Courier)
                        .when() // Переходим к действию
                        .post(COURIER_LOGIN); // Отправляем POST-запрос на ручку COURIER_LOGIN
    }

    public Response sendRequestDeleteCourier(int id) { // Метод для отправки DELETE-запроса на удаление курьера
        return
                given() // Начинаем построение запроса
                        .log().ifValidationFails() // Логируем запрос и ответ, если валидация не прошла
                        .contentType(ContentType.JSON) // Устанавливаем Content-Type как JSON
                        .baseUri("https://qa-scooter.praktikum-services.ru/") // Устанавливаем базовый URI
                        .pathParams("id", id) // Устанавливаем параметр пути "id"
                        .when() // Переходим к действию
                        .delete("/api/v1/courier/{id}"); // Отправляем DELETE-запрос на ручку /api/v1/courier/{id}
    }
}
