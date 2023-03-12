import com.google.gson.Gson;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierLoginTest {
    public void login(String login, String password) {
        String json = "{\"login\": " + login + ", \"password\": " + password + "}";
        Response response = given().header("Content-type", "application/json").body(json)
                .post("/api/v1/courier/login");
        Gson gson = new Gson();
        Courier courierId = gson.fromJson(response, Courier.class);
    }
}
