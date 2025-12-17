package com.learning.udemy.vertx_starter.eventbus;

import com.learning.udemy.vertx_starter.verticles.MainVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExampleJSON {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }

  static class RequestVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
    static final String ADDRESS = "my.request.address";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();
      final var MESSAGE = new JsonObject()
        .put("message", "Hello World")
        .put("version", 1);
      LOG.debug("Sending: {} ", MESSAGE);
      eventBus.<JsonArray>request(ADDRESS, MESSAGE)
        .onSuccess(message -> {
          LOG.debug("Response: {}", message.body());
        })
        .onFailure(err -> {
          LOG.error("Request failed", err);
        });
    }
  }

  static class ResponseVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<JsonObject>consumer(RequestVerticle.ADDRESS, message -> {
        LOG.debug("Received Message: {}", message.body());
        message.reply(new JsonArray().add("one").add("two").add("three"));
      });
    }
  }
}
