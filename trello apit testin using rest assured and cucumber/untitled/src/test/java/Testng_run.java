import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.json.*;

public class Testng_run {

    private String baseUrl = "https://api.trello.com";
    private String key = "801ab202bd593b9bdfbc29fa56ec7233";
    private String token = "ATTA43c6c3f7d42014812fab876b88f778013fa4eacf2c6677017f33a63d6f7c77ad248120DE";
    private String organizationId;
    private String boardId;
    private String listId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
    }

    @Test(priority = 1)
    public void createOrganizationTest() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        JSONObject requestParams = new JSONObject();
        requestParams.put("displayName", "Automation_testing");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());
        Response response = request.post(baseUrl + "/1/organizations");
        Assert.assertEquals(200, response.getStatusCode());
        organizationId = response.jsonPath().getString("id");
        System.out.println("Organization ID is: " + organizationId);

    }
    @Test
    public void retrieveOrganizationsTest() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response = request.get(baseUrl + "/1/members/me/organizations");
        Assert.assertEquals(200, response.getStatusCode());

        // Parse the response to JSON
        JSONArray jsonArray = new JSONArray(response.getBody().asString());

        // Verify that the user is a member of at least one organization
        Assert.assertTrue(jsonArray.length() > 0);

        // Save the first organization ID to a variable
        JSONObject firstOrganization = jsonArray.getJSONObject(0);
        String organizationId = firstOrganization.getString("id");

        // Print the ID to the console
        System.out.println("Organization ID: " + organizationId);
    }
    @Test(priority = 2)
    public void createBoardTest() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "New Board");
        requestBody.put("desc", "description");
        requestBody.put("idOrganization", organizationId);

        // Send create board request
        RequestSpecification request = RestAssured.given()
                .queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString());
        Response response = request.post(baseUrl + "/1/boards");

        // Assert response
        Assert.assertEquals(200, response.getStatusCode());

        // Extract board ID from response body
         boardId = response.jsonPath().getString("id");
        System.out.println("Created board ID: " + boardId);
    }

    @Test(priority = 3)
    public void createListTest() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "Test List");
        requestParams.put("idBoard", boardId);
        requestParams.put("pos", "top");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());
        Response response = request.post(baseUrl + "/1/lists");
        Assert.assertEquals(200, response.getStatusCode());
        listId = response.jsonPath().getString("id");
    }

    @Test(priority = 4)
    public void getOrganizationsTest() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response = request.get("/1/members/me/organizations");
        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test(priority = 5)
    public void getBoardsTest() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response = request.get("/1/organizations/" + organizationId + "/boards");
        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test(priority = 6)
    public void getListsTest() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response = request.get("/1/boards/" + boardId + "/lists");
        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test(priority = 7)
    public void archiveListTest() {


        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token)
                .queryParam("value", "true");
        Response response = request.put(baseUrl + "/1/lists/" + listId + "/closed");

        // Assert that the list has been archived successfully
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals("true", response.jsonPath().get("closed").toString());
    }

    @Test(priority = 8)
    public void deletelist() {


        RequestSpecification deleteBoardRequest = RestAssured.given()
                .queryParam("key", key)
                .queryParam("token", token);
        Response deleteBoardResponse = deleteBoardRequest.delete(baseUrl + "/1/boards/" + boardId);
        Assert.assertEquals(200, deleteBoardResponse.getStatusCode());
    }
    @Test(priority = 8)
    public void deleteorg() {
        RequestSpecification request2 = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response2 = request2.delete(baseUrl + "/1/organizations/" + organizationId);
        Assert.assertEquals(200, response2.getStatusCode());
    }
}


