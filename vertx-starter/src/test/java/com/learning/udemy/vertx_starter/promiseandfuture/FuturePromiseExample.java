package com.learning.udemy.vertx_starter.promiseandfuture;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class FuturePromiseExample {
  // When we add @ExtendWith, we would have access to vertx instance when test is executed and
  // this helps us quite a lot when executing asynchronous code
  private static final Logger LOG = LoggerFactory.getLogger(FuturePromiseExample.class);

  @Test
  void promise_success(Vertx vertx, VertxTestContext context) {
    // One specific thing about vertx test cases is that we can define two parameters
    // First one is the vertx instance and the second one is vertx test context by passing these two parameters
    // We will have access to the functionality of the vertx i.e., we have access to vertx event loops, event bus
    // and also access to the timers.
    final Promise<String> promise = Promise.promise();
    LOG.debug("Test Started");
    vertx.setTimer(1000, id -> {
      promise.complete("Success");
      LOG.debug("Promise is Success");
      context.completeNow();
    });
    LOG.debug("Test Ended");
  }

  @Test
  void promise_failure(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOG.debug("Test Started");
    vertx.setTimer(1000, id -> {
      promise.fail(new RuntimeException("Failed"));
      LOG.debug("Promise is Failed");
      context.completeNow();
    });
    LOG.debug("Test Ended");
  }

  @Test
  void future_success(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOG.debug("Test Started");
    vertx.setTimer(1000, id -> {
      promise.complete("Success");
      LOG.debug("Promise is Success");
    });
    final Future<String> future = promise.future();
    future
      .onSuccess(result -> {
        LOG.debug("Result: {}", result);
        context.completeNow();
      })
      .onFailure(context::failNow);
    LOG.debug("Test Ended");
  }

  @Test
  void future_failure(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOG.debug("Test Started");
    vertx.setTimer(1000, id -> {
      promise.fail(new RuntimeException("Failed"));
      LOG.debug("Promise is Failed");
      // context.completeNow(); This is not needed now because the context should be completed only when future is done
      // not when the timer is done
    });
    final Future<String> future = promise.future();
    future
      .onSuccess(context::failNow)
      .onFailure(error -> {
        LOG.debug("Result: ", error);
        context.completeNow();
      });
    LOG.debug("Test Ended");
  }

  @Test
  void future_map(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOG.debug("Test Started");
    vertx.setTimer(1000, id -> {
      promise.complete("Success");
      LOG.debug("Promise is Success");
    });
    final Future<String> future = promise.future();
    future
      .map(asString -> {
        LOG.debug("Map String to JSON Object");
        return new JsonObject().put("key", asString);
      })
      .map(jsonObject -> new JsonArray().add(jsonObject))
      // .mapEmpty(), we can use this when we don't care about the result
      .onSuccess(result -> {
        LOG.debug("Result: {} of type {}", result, result.getClass().getSimpleName());
        context.completeNow();
      })
      .onFailure(context::failNow);
    LOG.debug("Test Ended");
  }

  @Test
  void future_coordination(Vertx vertx, VertxTestContext context) {
    vertx.createHttpServer()
      .requestHandler(request -> LOG.debug("{}", request))
      .listen(10000)
      .compose(server -> {
        LOG.info("Another task");
        return Future.succeededFuture(server);
      })
      .compose(server -> {
        LOG.info("Even more");
        return Future.succeededFuture(server);
      })
      .onFailure(context::failNow)
      .onSuccess(server -> {
        LOG.debug("Server Started on port {}", server.actualPort());
        context.completeNow();
      });
  }

  @Test
  void future_composition(Vertx vertx, VertxTestContext context) {
    var one = Promise.<Void>promise();
    var two = Promise.<Void>promise();
    var three = Promise.<Void>promise();

    var futureOne = one.future();
    var futureTwo = two.future();
    var futureThree = three.future();

    // all() means all the futures have to be completed, otherwise the result won't be called
    Future.all(futureOne, futureTwo, futureThree)
      .onFailure(context::failNow)
      .onSuccess(result -> {
        LOG.debug("Success");
        context.completeNow();
      });

    // Complete Futures
    vertx.setTimer(500, id -> {
      one.complete();
      two.complete();
      three.complete();
    });
  }
}




