package com.learning.udemy.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPointExample {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    // vertx.deployVerticle(new Sender());
    // vertx.deployVerticle(new Receiver());
    // Deploying Reciever first and then the sender to make sure message being lost
    vertx.deployVerticle(new Receiver())
      .compose(id -> vertx.deployVerticle(new Sender()))
      .onFailure(Throwable::printStackTrace);
  }

  static class Sender extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();
      // eventBus.send(Sender.class.getName(), "Sending an Message");
      // send after a small delay, or just rely on the compose in main
      // vertx.setTimer(500, id ->
      //   eventBus.send(Sender.class.getName(), "Sending a Message")
      // );
      // Below is the example when firing the message for every second
      vertx.setPeriodic(1000, id -> {
        eventBus.send(Sender.class.getName(), "Sending a Message");
      });
    }
  }

  static class Receiver extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      eventBus.consumer(Sender.class.getName(), message -> {
        LOG.debug("Message Received: {}", message.body());
      });
      startPromise.complete();
    }
  }
}
