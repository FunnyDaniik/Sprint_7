package ru.praktikum;

public class Courier {
    private String login;       // Логин курьера
    private String password;    // Пароль курьера
    private String firstName;   // Имя курьера

    // Конструктор с логином, паролем и именем
    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    // Конструктор только с логином и паролем
    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Конструктор по умолчанию (необходим для десериализации JSON)
    public Courier() {
    }

    // Геттер для имени
    public String getFirstName() {
        return firstName;
    }

    // Геттер для пароля
    public String getPassword() {
        return password;
    }

    // Геттер для логина
    public String getLogin() {
        return login;
    }

    // Сеттер для логина
    public void setLogin(String login) {
        this.login = login;
    }

    // Сеттер для пароля
    public void setPassword(String password) {
        this.password = password;
    }

    // Сеттер для имени
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
