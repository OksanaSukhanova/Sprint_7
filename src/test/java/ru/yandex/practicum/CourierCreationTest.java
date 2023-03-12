import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CourierCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Courier creation test")
    @Description("Check that courier can be created")
    public void courierCreationIsPossible() {
        Courier courier = new Courier("SomeLogin", "qwerty123", "SomeName");
        given()
                .header("Content-type", "application/json")
                .and().body(courier).when().post("/api/v1/courier")
                .then().assertThat().statusCode(201).and().body("ok", equalTo(true));
    }

}
