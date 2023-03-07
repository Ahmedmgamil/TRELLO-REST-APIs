import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

public class trello_Api_testing {

    private String baseUrl = "https://api.trello.com";
    private String key = "801ab202bd593b9bdfbc29fa56ec7233";
    private String token = "ATTA43c6c3f7d42014812fab876b88f778013fa4eacf2c6677017f33a63d6f7c77ad248120DE";
    private String organizationId;
    private String boardId;
    private String listId;

    @Given("I have created a new organization")
    public void i_have_created_a_new_organization() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response = request.body("{\"displayName\": \"My Test Organization\"}").post(baseUrl + "/1/organizations");
        Assert.assertEquals(200, response.getStatusCode());
        organizationId = response.jsonPath().getString("id");
    }

    @Given("I have created a board inside the organization")
    public void i_have_created_a_board_inside_the_organization() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response = request.body("{\"name\": \"My Test Board\", \"idOrganization\": \"" + organizationId + "\"}").post(baseUrl + "/1/boards");
        Assert.assertEquals(200, response.getStatusCode());
        boardId = response.jsonPath().getString("id");
    }

    @Given("I have created a new list inside the board")
    public void i_have_created_a_new_list_inside_the_board() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response = request.body("{\"name\": \"My Test List\", \"idBoard\": \"" + boardId + "\"}").post(baseUrl + "/1/lists");
        Assert.assertEquals(200, response.getStatusCode());
        listId = response.jsonPath().getString("id");
    }

    @When("I get the organizations for the member")
    public void i_get_the_organizations_for_the_member() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response = request.get(baseUrl + "/1/members/me/organizations");
        Assert.assertEquals(200, response.getStatusCode());
    }

    @When("I get the boards in the organization")
    public void i_get_the_boards_in_the_organization() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response = request.get(baseUrl + "/1/organizations/" + organizationId + "/boards");
        Assert.assertEquals(200, response.getStatusCode());
    }

    @When("I get all the lists on the board")
    public void i_get_all_the_lists_on_the_board() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response = request.get(baseUrl + "/1/boards/" + boardId + "/lists");
        Assert.assertEquals(200, response.getStatusCode());
    }

    @When("I archive the list")
    public void i_archive_the_list() {
        RequestSpecification request = RestAssured.given().queryParam("key", key).queryParam("token", token);
        Response response = request.put(baseUrl + "/1/lists/" + listId + "/closed");
        Assert.assertEquals(200, response.getStatusCode());
    }
}
