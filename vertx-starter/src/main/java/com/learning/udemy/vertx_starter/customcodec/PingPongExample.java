package com.learning.udemy.vertx_starter.customcodec;

import com.learning.udemy.vertx_starter.verticles.MainVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample {

  private static final Logger LOG = LoggerFactory.getLogger(PingPongExample.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle())
      .onFailure(err -> LOG.error("err", err));

    vertx.deployVerticle(new PongVerticle())
      .onFailure(err -> LOG.error("err", err));
  }

  static class PingVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(PingVerticle.class);
    static final String ADDRESS = PingVerticle.class.getName();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      final Ping MESSAGE = new Ping("Hello World", true);
      LOG.debug("Sending: {} ", MESSAGE);
      // Register the codec only once
      eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
      eventBus.<Pong>request(ADDRESS, MESSAGE)
        .onSuccess(message -> {
          LOG.debug("Response: {}", message.body());
        })
        .onFailure(err -> {
          LOG.error("Request failed", err);
        });
      startPromise.complete();
    }
  }

  static class PongVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(PongVerticle.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      // Register the codec only once
      eventBus.registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));
      eventBus.<Ping>consumer(PingVerticle.ADDRESS, message -> {
        LOG.debug("Received Message: {}", message.body());
        message.reply(new Pong(0));
      });
      startPromise.complete();
    }
  }
}
