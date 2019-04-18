package RachlinBabies;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import RachlinBabies.Model.Intake;
import RachlinBabies.Model.Tag;
import RachlinBabies.Service.IntakeService;
import RachlinBabies.Service.Service;
import spark.Spark;

import static RachlinBabies.TestResponse.request;
import static RachlinBabies.Utils.JsonUtil.toJson;
import static junit.framework.Assert.assertEquals;

public class TestAPI {

  private Field userId;

  @BeforeClass
  public static void startServer() {
    App.main(null);
  }

  @AfterClass
  public static void stopServer() throws SQLException {
    DBTestUtils.initData();
    Spark.stop();
  }

  @Before
  public void initData() throws SQLException, NoSuchFieldException, IllegalAccessException {
    userId = Service.class.getDeclaredField("userId");
    userId.setAccessible(true);
    userId.set(null, 1);
    DBTestUtils.initData();
  }

  @Test
  public void testInsertStockIntakeSuccess() {
    Intake intake = new Intake.IntakeBuilder()
            .sourceId(1)
            .servings(1)
            .intakeDate(Timestamp.from(Instant.now()))
            .intakeType("STK")
            .build();
    String payload = toJson(intake);
    TestResponse res = request("POST", "/intakes", payload);
    if (res != null) {
      assertEquals(200, res.status);
    }
    TestResponse res2 = request("GET", "/intakes");
    List jsonArray = res2.jsonArray();
    assertEquals(5, jsonArray.size());
    if (res != null) {
      assertEquals(200, res.status);
    }
  }

  @Test
  public void testInsertStockIntakeNotEnoughStock() {
    Intake intake = new Intake.IntakeBuilder()
            .sourceId(1)
            .servings(10000)
            .intakeDate(Timestamp.from(Instant.now()))
            .intakeType("STK")
            .build();
    String payload = toJson(intake);
    TestResponse res = request("POST", "/intakes", payload);
    if (res != null) {
      assertEquals(400, res.status);
    }
  }

  @Test
  public void testInsertRecipeIntakeSuccess() {
    Intake intake = new Intake.IntakeBuilder()
            .sourceId(1)
            .servings(1)
            .intakeDate(Timestamp.from(Instant.now()))
            .intakeType("RCP")
            .build();
    String payload = toJson(intake);
    TestResponse res = request("POST", "/intakes", payload);
    if (res != null) {
      assertEquals(200, res.status);
    }
    TestResponse res2 = request("GET", "/intakes");
    List jsonArray = res2.jsonArray();
    assertEquals(5, jsonArray.size());
    if (res != null) {
      assertEquals(200, res.status);
    }
  }

  @Test
  public void testInsertRecipeIntakeNotEnoughStock() {
    Intake intake = new Intake.IntakeBuilder()
            .sourceId(1)
            .servings(1000)
            .intakeDate(Timestamp.from(Instant.now()))
            .intakeType("RCP")
            .build();
    String payload = toJson(intake);
    TestResponse res = request("POST", "/intakes", payload);
    if (res != null) {
      assertEquals(400, res.status);
    }
  }

  @Test
  public void testGetSuccess() {
    TestResponse res = request("GET", "/intakes/1");
    Map json = res.json();
    assertEquals(1.0, json.get("sourceId"));
    assertEquals(1.0, json.get("servings"));
    assertEquals(1.0, json.get("id"));
    assertEquals("STK", json.get("type"));
    assertEquals(200, res.status);
  }

  @Test
  public void testGetFail() {
    TestResponse res = request("GET", "/intakes/-1");
    assertEquals(404, res.status);
  }

  @Test
  public void testGetAllSuccess() {
    TestResponse res = request("GET", "/intakes");
    List jsonArray = res.jsonArray();
    assertEquals(4, jsonArray.size());
    assertEquals(200, res.status);
  }

  @Test
  public void testGetAllFail() throws IllegalAccessException {
    userId.set(null, -1);
    TestResponse res = request("GET", "/intakes/");
    assertEquals(404, res.status);
  }

  @Test
  public void testUpdateIntakeTimeSuccess() {
    Timestamp newTime = Timestamp.from(Instant.now());
    Intake intake = new Intake.IntakeBuilder()
            .id(1)
            .intakeDate(newTime)
            .intakeType("STK")
            .build();
    String payload = toJson(intake);
    TestResponse res = request("PUT", "/intakes/time", payload);
    if (res != null) {
      assertEquals(200, res.status);
    }
    assertEquals(newTime.toString().substring(0, 19),
            new IntakeService().get(1).getIntakeDate().toString().substring(0, 19));
  }

  @Test
  public void testUpdateIntakeTimeSuccessRecipe() {
    Timestamp newTime = Timestamp.from(Instant.now());
    Intake intake = new Intake.IntakeBuilder()
            .id(12)
            .intakeDate(newTime)
            .intakeType("RCP")
            .build();
    String payload = toJson(intake);
    TestResponse res = request("PUT", "/intakes/time", payload);
    if (res != null) {
      assertEquals(200, res.status);
    }
    assertEquals(newTime.toString().substring(0, 19),
            new IntakeService().get(12).getIntakeDate().toString().substring(0, 19));
  }

  @Test
  public void testUpdateIntakeTimeNotFoundRecipe() {
    Timestamp newTime = Timestamp.from(Instant.now());
    Intake intake = new Intake.IntakeBuilder()
            .id(1)
            .intakeDate(newTime)
            .intakeType("RCP")
            .build();
    String payload = toJson(intake);
    TestResponse res = request("PUT", "/intakes/time", payload);
    if (res != null) {
      assertEquals(400, res.status);
    }
  }

  @Test
  public void testUpdateIntakeTimeNotFoundStock() {
    Timestamp newTime = Timestamp.from(Instant.now());
    Intake intake = new Intake.IntakeBuilder()
            .id(12)
            .intakeDate(newTime)
            .intakeType("STK")
            .build();
    String payload = toJson(intake);
    TestResponse res = request("PUT", "/intakes/time", payload);
    if (res != null) {
      assertEquals(400, res.status);
    }
  }

  @Test
  public void testProductRequestNDBSuccess(){
    TestResponse res = request("GET", "/products/45001530");
    Map json = res.json();
    assertEquals("BARBECUE SAUCE", json.get("longName"));
  }

  @Test
  public void testProductRequestNDBFailure(){
    TestResponse res = request("GET", "/products/00000000");
    assertEquals(404, res.status);
  }

  @Test
  public void testProductRequestStringSuccess(){
    TestResponse res = request("GET", "/products/search/milk");
    List jsonArray = res.jsonArray();
    assertEquals(7363, jsonArray.size());
  }

  @Test
  public void testProductRequestStringSuccessLimit(){
    TestResponse res = request("GET", "/products/search/milk/20");
    List jsonArray = res.jsonArray();
    assertEquals(20, jsonArray.size());
  }

  @Test
  public void testProductRequestStringFailure(){
    TestResponse res = request("GET", "/products/search/commiegobblediegook");
    Map json = res.json();
    assertEquals(0, json.size());
  }

  @Test
  public void testGetTag() {
    TestResponse res = request("GET", "/tags/1");
    assertEquals(1.0, res.json().get("id"));
    assertEquals("Dessert", res.json().get("name"));
  }

  @Test
  public void testGetTag404() {
    TestResponse res = request("GET", "/tags/-1");
    assertEquals(404, res.status);
  }

  @Test
  public void testGetTags() {
    TestResponse res = request("GET", "/tags");
    assertEquals(7, res.jsonArray().size());
  }

  @Test
  public void testSearchTags() {
    TestResponse res = request("GET", "/tags/search/D");
    assertEquals(3, res.jsonArray().size());
  }

  @Test
  public void createTag() {
    Tag tag = new Tag(null, "Asian");
    String payload = toJson(tag);
    TestResponse res = request("POST", "/tags", payload);
    assertEquals(200, res.status);
  }

  @Test
  public void createTagFail() {
    Tag tag = new Tag(null, "dessert");
    String payload = toJson(tag);
    TestResponse res = request("POST", "/tags", payload);
    assertEquals(400, res.status);
  }
}
