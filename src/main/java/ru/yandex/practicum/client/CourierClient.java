import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.*;

public class CourierClient {
    public ValidatableResponse create(Courier courier) {
        return given().header("Content-type", "application/json").body(courier)
                .when().post("/api/v1/courier").then();
    }

    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given().header("Content-type", "application/json").body(courierCredentials)
                .when().post("/api/v1/courier/login").then();
    }

    public void delete() {

    }
}
