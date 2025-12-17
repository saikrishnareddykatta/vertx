package com.learning.udemy.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PublishSubscribeExample {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
     vertx.deployVerticle(new Publisher());
     vertx.deployVerticle(new Subscriber1());
     vertx.deployVerticle(Subscriber2.class.getName(), new DeploymentOptions().setInstances(2));
    // Deploying Publisher after deploying Subscribers in order to protect the message being lost
    // vertx.deployVerticle(new Subscriber1())
      // .compose(id1 -> vertx.deployVerticle(new Subscriber2()))
      // .compose(id2 -> vertx.deployVerticle(new Publisher()))
      // .onSuccess(id -> {
      //   System.out.println("All verticles deployed in correct order.");
      // })
      // .onFailure(Throwable::printStackTrace);
  }

  static class Publisher extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();
      vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id -> {
        eventBus.publish(Publisher.class.getName(), "Hello World");
      });
    }
  }

  static class Subscriber1 extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(Subscriber1.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      eventBus.consumer(Publisher.class.getName(), message -> {
        LOG.debug("Response in Subscriber1: {}", message.body());
      });
      startPromise.complete();
    }
  }

  public static class Subscriber2 extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(Subscriber2.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      eventBus.consumer(Publisher.class.getName(), message -> {
        LOG.debug("Response in Subscriber2: {}", message.body());
      });
      startPromise.complete();
    }
  }
}
