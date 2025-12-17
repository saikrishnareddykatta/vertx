package com.learning.udemy.vertx_starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
  private final Random random = new Random();

//  public static void main(String[] args) {
//    System.out.println("Main Method Started");
//    var vertx = Vertx.vertx();
//    // A Verticle can be deployed in multiple ways
//    // vertx.deployVerticle("com.learning.udemy.vertx_starter.MainVerticle");
//    // vertx.deployVerticle(MainVerticle.class.getName());
//    vertx.deployVerticle(new MainVerticle());
//  }


  @Override
  public void start(Promise<Void> startPromise) {
    vertx.createHttpServer()
      .requestHandler(req -> req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello World Four"))
      .listen(8888)
      .onSuccess(server -> startPromise.complete())
      .onFailure(startPromise::fail);

    vertx.setPeriodic(100, id -> LOG.info("Random: {}", Math.random()));
  }
}
