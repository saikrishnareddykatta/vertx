package com.learning.udemy.broker.assets;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class GetAssetsHandler implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(GetAssetsHandler.class);

  // The handler interface defines exactly one method, and we need to override the handle method.
  @Override
  public void handle(RoutingContext context) {
      final JsonArray response = new JsonArray();
      AssetsRestApi.ASSETS.stream().map(Asset::new).forEach(response::add);
      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      // Uncomment only when load testing
     // artificialSleep(context);
    context.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
        .putHeader("my-header", "my-value") // Custom Header
        .end(response.toBuffer());
  }

  /**
   * Used to showcase scaling and load testing
   */
  private static void artificialSleep(RoutingContext context) {
    try {
      // Simulate Delay
      final var randomMillis = ThreadLocalRandom.current().nextInt(100, 300);
      if (randomMillis % 2 == 0) {
        Thread.sleep(randomMillis);
        context.response()
          .setStatusCode(500)
          .end("Sleeping...");
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
