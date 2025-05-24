package ru.praktikum;

public class Orders { // Класс, представляющий информацию о заказе

    private String firstName; // Имя получателя
    private String lastName; // Фамилия получателя
    private String address; // Адрес доставки
    private String metroStation; // Станция метро
    private String phone; // Номер телефона получателя
    private int renTime; // Время аренды
    private String deliveryDate; // Дата доставки
    private String comment; // Комментарий к заказу
    private String[] color; // Массив цветов

    public Orders(String firstName, String lastName, String address, String metroStation, String phone, int renTime, String deliveryDate, String comment, String[] color) { // Конструктор класса Orders
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.renTime = renTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    public String getFirstName() { // Метод для получения имени получателя
        return firstName;
    }

    public String getLastName() { // Метод для получения фамилии получателя
        return lastName;
    }

    public String getAddress() { // Метод для получения адреса доставки
        return address;
    }

    public String getMetroStation() { // Метод для получения станции метро
        return metroStation;
    }

    public String getPhone() { // Метод для получения номера телефона получателя
        return phone;
    }

    public int getRenTime() { // Метод для получения времени аренды
        return renTime;
    }

    public String getDeliveryDate() { // Метод для получения даты доставки
        return deliveryDate;
    }

    public String getComment() { // Метод для получения комментария к заказу
        return comment;
    }

    public String[] getColor() { // Метод для получения массива цветов
        return color;
    }

    public void setFirstName(String firstName) { // Метод для установки имени получателя
        this.firstName = firstName;
    }

    public void setLastName(String lastName) { // Метод для установки фамилии получателя
        this.lastName = lastName;
    }

    public void setAddress(String address) { // Метод для установки адреса доставки
        this.address = address;
    }

    public void setMetroStation(String metroStation) { // Метод для установки станции метро
        this.metroStation = metroStation;
    }

    public void setPhone(String phone) { // Метод для установки номера телефона получателя
        this.phone = phone;
    }

    public void setRenTime(int renTime) { // Метод для установки времени аренды
        this.renTime = renTime;
    }

    public void setDeliveryDate(String deliveryDate) { // Метод для установки даты доставки
        this.deliveryDate = deliveryDate;
    }

    public void setComment(String comment) { // Метод для установки комментария к заказу
        this.comment = comment;
    }

    public void setColor(String[] color) { // Метод для установки массива цветов
        this.color = color;
    }
}
