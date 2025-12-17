package com.learning.udemy.vertx_starter.jsonandarrays;

import io.vertx.core.json.JsonArray;
import org.junit.jupiter.api.Test;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonObjectExample {

  @Test
  void jsonObjectCanBeMapped() {
    final JsonObject myJsonObject = new JsonObject();
    myJsonObject.put("id", 1); // Integer
    myJsonObject.put("name", "Alice"); // String
    myJsonObject.put("loves_vertex", true); // Boolean
    // System.out.println internally calls encode() method and produces the similar output
    System.out.println("myJsonObject:" + myJsonObject); // myJsonObject:{"id":1,"name":"Alice","loves_vertex":true}

    final String encoded =  myJsonObject.encode();
    assertEquals("{\"id\":1,\"name\":\"Alice\",\"loves_vertex\":true}", encoded);

    // We can create anotherJsonObject similar to myJsonObject with the below command
    // They would contain same data, but they are two different objects in JAVA perspective
    final JsonObject anotherJsonObject = new JsonObject(encoded);
    System.out.println("anotherJsonObject:" + anotherJsonObject); // anotherJsonObject:{"id":1,"name":"Alice","loves_vertex":true}
    assertEquals(myJsonObject, anotherJsonObject);
  }

  @Test
  void jsonObjectCanBeCreatedFromMap() {
    final Map<String, Object> myMap = new HashMap<>();
    myMap.put("id", 1); // Integer
    myMap.put("name", "Alice"); // String
    myMap.put("loves_vertex", true); // Boolean
    final JsonObject asJsonObject = new JsonObject(myMap);
    assertEquals(asJsonObject.getMap(), myMap);
    assertEquals(1, asJsonObject.getInteger("id"));
    assertEquals("Alice", asJsonObject.getString("name"));
    assertEquals(true, asJsonObject.getBoolean("loves_vertex"));
  }

  @Test
  void convertJsonObjectToJava() {
    JsonObject myJsonObject = new JsonObject()
      .put("id", 1)
      .put("name", "Alice")
      .put("loves_vertex", true);
    // Converting a JSON Object to Java Object
    User user = myJsonObject.mapTo(User.class);
    assertEquals(1, user.id);
    assertEquals("Alice", user.name);
    assertTrue(user.loves_vertex);
  }

  @Test
  void convertJavaObjectToJsonObject() {
    User user = new User();
    user.id = 1;
    user.name = "Alice";
    user.loves_vertex = true;
    // Converting a Java Object to JSON Object
    JsonObject myJsonObject = JsonObject.mapFrom(user);
    final String encoded = myJsonObject.encode();
    assertEquals("{\"id\":1,\"name\":\"Alice\",\"loves_vertex\":true}", encoded);
  }

  @Test
  void jsonArrayCanBeMapped(){
    final JsonArray myJsonArray = new JsonArray();
    myJsonArray.add(new JsonObject().put("id", 1));
    myJsonArray.add(new JsonObject().put("id", 2));
    myJsonArray.add(new JsonObject().put("id", 3));
    myJsonArray.add("randomValue");
    assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3},\"randomValue\"]", myJsonArray.encode());
  }
}









