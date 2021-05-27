package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.Response;
import models.AuthResponse;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.*;

public class BookStoreTests {
    @Test
    void noLogsTest() {
        given()
                .get("https://demoqa.com/BookStore/v1/Books")
                .then()
                .body("books", hasSize(greaterThan(0)));

    }

    @Test
    void withAllLogsTest() {
        given()
                .log().all()
                .get("https://demoqa.com/BookStore/v1/Books")
                .then()
                .log().all()
                .body("books", hasSize(greaterThan(0)));

    }

    @Test
    void withSomeLogsTest() {
        given()
                .log().uri()
                .get("https://demoqa.com/BookStore/v1/Books")
                .then()
                .log().body()
                .body("books", hasSize(greaterThan(0)));

    }

    @Test
    void generateTokenTest() {
        Map<String, String> data = new HashMap<>();
        data.put("userName","alex");
        data.put("password","asdsad#frew_DFS2");
        given()
                .contentType(JSON)
                .body(data.toString())
                .when()
                .log().uri()
                .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }

    @Test
    void withAllureListenerTest() {
        given()
                .filter(new AllureRestAssured())
                .contentType(JSON)
                .body("{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }")
                .when()
                .log().uri()
                .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }

    @Test
    void withFilterCustomTest() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body("{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }")
                .when()
                .log().uri()
                .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }

    @Test
    void withAssertJTest() {
        String response =
        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body("{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }")
                .when()
                .log().uri()
                .post("https://demoqa.com/Account/v1/GenerateToken")
                .then()
                .log().body()
                .extract().asString();

        assertThat(response).contains("\"status\":\"Success\"");
        assertThat(response).contains("\"result\":\"User authorized successfully.\"");
    }

    @Test
    void withModelTest() {
        AuthResponse response =
                given()
                        .filter(customLogFilter().withCustomTemplates())
                        .contentType(JSON)
                        .body("{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }")
                        .when()
                        .log().uri()
                        .post("https://demoqa.com/Account/v1/GenerateToken")
                        .then()
                        .log().body()
                        .extract().as(AuthResponse.class);

        assertThat(response.getStatus()).isEqualTo("Success");
        assertThat(response.getResult()).isEqualTo("User authorized successfully.");
    }
}
