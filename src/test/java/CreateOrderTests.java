import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.OrderSteps;
import ru.praktikum.Orders;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateOrderTests {

    private final String[] color; // Массив цветов для параметризованного теста
    private final int statusCode;
    private Orders order;
    private Integer trackId; // Переменная для хранения track-номера заказа
    private OrderSteps orderSteps; // Экземпляр класса для работы с API

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/"; // Устанавливаем базовый URI
        // Создаем объект Orders с данными, которые не меняются
        order = new Orders(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                null // Цвет будет установлен в параметризованном тесте
        );
    }

    @After
    public void tearDown() {
        try {
            if (trackId != null) {
                // Отменяем заказ по trackId
                Response cancelResponse = given()
                        .log().all()
                        .contentType(ContentType.JSON)
                        .put("/api/v1/orders/cancel?track=" + trackId);

                if (cancelResponse.getStatusCode() == 200) {
                    System.out.println("Заказ успешно отменен, trackId: " + trackId);
                } else {
                    System.out.println("Не удалось отменить заказ. Код: " + cancelResponse.getStatusCode());
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при отмене заказа: " + e.getMessage());
        }
    }

    // Параметризация теста: передаем разные наборы цветов
    @Parameterized.Parameters
    public static Collection<Object[]> getColorData() {
        return Arrays.asList(new Object[][] {
                {new String[]{"BLACK"}, 201}, // Один цвет: черный
                {new String[]{"GREY"}, 201}, // Один цвет: серый
                {new String[]{"BLACK", "GREY"}, 201}, // Оба цвета
                {null, 201}, // Без указания цвета
                {new String[]{}, 201} // Без указания цвета (пустой массив)
        });
    }

    public CreateOrderTests(String[] color, int statusCode) {
        this.color = color; // Инициализируем массив цветов из параметров
        this.statusCode = statusCode;
    }

    @Test
    public void createOrderTest() {
        order.setColor(color); // Устанавливаем цвет в объекте Orders

        // Создаем заказ через OrderSteps
        Response response = orderSteps.createOrder(order);
        response.then().log().all();

        // Логируем все детали ответа
        response.then().log().all();

        // Проверяем статус код ответа
        assertEquals(statusCode, response.getStatusCode()); // Проверяем, что статус код соответствует ожидаемому (201)
        // Проверяем, что в теле ответа есть поле "track"
        response.then().assertThat().body("track", notNullValue()); // Проверяем, что поле "track" не равно null
        // Сохраняем trackId созданного заказа для последующей отмены
        trackId = response.jsonPath().getInt("track");
    }
}
