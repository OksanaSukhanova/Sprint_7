package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.practicum.client.CourierClient;
import ru.yandex.practicum.model.Courier;
import ru.yandex.practicum.model.CourierCredentials;
import ru.yandex.practicum.model.CourierGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierCreationTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
    }

    @Test
    @DisplayName("Courier creation with valid data")
    @Description("Check that courier can be created")
    public void courierCreationWithValidDataIsPossible() {
        ValidatableResponse createResponse = courierClient.create(courier);

        int statusCode = createResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 201, statusCode);

        boolean isCourierCreated = createResponse.extract().path("ok");
        assertTrue("Courier is not created", isCourierCreated);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        courierId = loginResponse.extract().path("id");
        assertTrue("Courier id is not created", courierId != 0);
    }

    @Test
    @DisplayName("Duplicate courier creation")
    @Description("Check that second courier with same data can't be created")
    public void courierCreationWithExistingDataIsImpossible() {
        courierClient.create(courier);

        Courier secondCourier = new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName());

        ValidatableResponse secondCreateResponse = courierClient.create(secondCourier);

        int statusCode = secondCreateResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 409, statusCode);

        String message = secondCreateResponse.extract().path("message");
        // Текст сообщения в документации не соответствует реальному ("Этот логин уже используется. Попробуйте другой."), тест падает
        assertEquals("Received message is incorrect", "Этот логин уже используется", message);
    }

    @Test
    @DisplayName("Courier creation without login")
    @Description("Check that courier without login can't be created")
    public void courierWithoutLoginCreationIsImpossible() {
        courier.setLogin(null);

        ValidatableResponse createResponse = courierClient.create(courier);

        int statusCode = createResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 400, statusCode);

        String message = createResponse.extract().path("message");
        assertEquals("Received message is incorrect", "Недостаточно данных для создания учетной записи", message);
    }

    @Test
    @DisplayName("Courier creation without password")
    @Description("Check that courier without password can't be created")
    public void courierWithoutPasswordCreationIsImpossible() {
        courier.setPassword(null);

        ValidatableResponse createResponse = courierClient.create(courier);

        int statusCode = createResponse.extract().statusCode();
        assertEquals("Status code is incorrect", 400, statusCode);

        String message = createResponse.extract().path("message");
        assertEquals("Received message is incorrect", "Недостаточно данных для создания учетной записи", message);
    }

    @After
    public void clearData() {
        courierClient.delete(courierId);
    }
}
