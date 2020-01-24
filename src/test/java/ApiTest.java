import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ApiTest {

    private final String url = "http://data.fixer.io/api";
    private RequestSpecification requestSpecification;

    @Before
    public void setUp() {
        String apiAccessKey = "9b00f2faf87060519d0b0725a6f4af16";
        requestSpecification = given()
                .relaxedHTTPSValidation()
                .log().uri()
                .urlEncodingEnabled(false)
                .param("access_key", apiAccessKey);
    }

    @Test // Take the same scenario as above
    public void testGetPricesForFiveCurrencies_statusCodeIsOk() {
        requestSpecification.when().get(url + "/latest&symbols=USD,AUD,CAD,PLN,MXN")
                .then().statusCode(200);
    }

    @Test // Take the same scenario as above
    public void testGetPricesForFiveCurrencies_bodySuccessIsTrue() {
        requestSpecification.when().get(url + "/latest&symbols=USD,AUD,CAD,PLN,MXN")
                .then().body("success", equalTo(true));
    }

    @Test // Take the same scenario as above
    public void testGetPricesForFiveCurrencies_mediaTypeIsJson() {
        requestSpecification.when().get(url + "/latest&symbols=USD,AUD,CAD,PLN,MXN")
                .then().contentType(ContentType.JSON);
    }

    @Test // Take the same scenario as above
    public void testGetPricesForFiveCurrencies_responseTimeLessThanTwoSec() {
        requestSpecification.when().get(url + "/latest&symbols=USD,AUD,CAD,PLN,MXN")
                .then().time(Matchers.lessThan(2000L));
    }

    @Test // Take the same scenario as above
    public void testGetPricesForFiveCurrencies_schemaValidation() {
        requestSpecification.when().get(url + "/latest&symbols=USD,AUD,CAD,PLN,MXN")
                .then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/getCurrenciesExchangeRate.json"));
    }

    @Test // Check the site is responsive
    public void testSiteIsResponsive() {
        requestSpecification.when().get(url).then().statusCode(200);
    }

    @Test // Run the query to return all Currencies
    public void testGetAllCurrencies() {
        requestSpecification.when().get(url + "/latest")
                .then()
                    .statusCode(200)
                    .body("success", equalTo(true));
    }

    @Test // Parse the JSON for 10 random Currencies and store the key and value into a CSV file
    public void testTenRandomCurrenciesToCSV() throws Exception {
        CsvHelper csvHelper = new CsvHelper();
        Map<String, Float> currenciesMap;

        currenciesMap = requestSpecification.when().get(url + "/latest")
                .then()
                    .statusCode(200)
                    .body("success", equalTo(true))
                    .extract().path("rates");

        Random rand = new Random();
        for (int x = 0; x < 10; x++) {
            Object randomCurrency = currenciesMap.keySet().toArray()[rand.nextInt(currenciesMap.size())];
            csvHelper.writeToCSV(randomCurrency.toString(), currenciesMap.get(randomCurrency));
        }

    }

}
