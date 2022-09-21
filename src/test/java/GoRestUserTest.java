
import Pojo.GoRestPost;
import Pojo.GoRestUser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUserTest {
    private RequestSpecification reqSpec;
    private GoRestUser user;
    private GoRestPost postUser;


    @BeforeClass
    public void setup(){

        RestAssured.baseURI = "https://gorest.co.in";

        reqSpec = given()
                .log().body()
                .header("Authorization", "Bearer c376ede2e47bccc914fbb5f6745fc698abd2b11bd7b058243320d81b56498fb5")
                .contentType(ContentType.JSON);

        user = new GoRestUser();
        user.setName(" Deniz Derya");
        user.setEmail("testtest0089@yahoo.com");
        user.setGender("Female");
        user.setStatus("Active");

        postUser = new GoRestPost();
        postUser.setId(user.getId());
        postUser.setTitle("Techno474 Study750");
        postUser.setBody("java, Jira");


    }

    @Test(priority = 1)
    public void createNewUserTest(){
       user.setId(given()
                .spec(reqSpec)
                .body(user)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(user.getName()))
                .extract().jsonPath().getString("id"));

    }
    @Test(priority = 2)
    public void createNewUserNegativeTest(){
        given()
                .spec(reqSpec)
                .body(user)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(422);

    }

    @Test(priority = 3)
    public void createPostUserTest(){

        postUser.setId(given()
                .spec(reqSpec)
                .body(postUser)
                .when()
                .post("/public/v2/users/"+ user.getId()+"/posts")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id"));

    }
    @Test(priority = 4)
    public void editPostTest(){
        HashMap<String, String> body = new HashMap<>();
        body.put("title", "learning Java");

        given()
                .spec(reqSpec)
                .body(body)
                .when()
                .put("/public/v2/posts/" + postUser.getId())
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 5)
    public void deletePostTest(){
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/posts/"+ postUser.getId())
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(priority = 6)
    public void deletePostNegativeTest(){
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/posts/"+ postUser.getId())
                .then()
                .log().body()
                .statusCode(404);
    }


    @Test(priority = 7)
    public void deleteUserTest(){
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/"+ user.getId())
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(priority = 8)
    public void deleteUserNegativeTest(){
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/"+ user.getId())
                .then()
                .log().body()
                .statusCode(404);

    }


}
