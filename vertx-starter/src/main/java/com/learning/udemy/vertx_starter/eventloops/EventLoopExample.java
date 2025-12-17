package com.learning.udemy.vertx_starter.eventloops;

import com.learning.udemy.vertx_starter.verticles.MainVerticle;
import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class EventLoopExample extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    var vertex = Vertx.vertx(
      new VertxOptions()
        .setMaxEventLoopExecuteTime(500) // This s a setting for how long the event loop can
        // be blocked before the blocked thread checker would return a warning, default setting is 2 seconds which is quite high
        // We have set it to 500 ms to see the warning a bit earlier
        .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS) // Setting the time in MILLISECONDS
        .setBlockedThreadCheckInterval(1)
        .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)
        .setEventLoopPoolSize(4) // Always make sure event loop pool at least has threads size equal to verticles created
        // If size of event loop pool is less than verticles, then Multiple verticles share the same event-loop thread
        // If size of event loop pool is greater than verticles, then Each verticle gets its own event-loop thread
    );
    vertex.deployVerticle(EventLoopExample.class.getName(), new DeploymentOptions().setInstances(4));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Start {}", getClass().getName());
    startPromise.complete();
    // Do not this inside a verticle
    // Thread.sleep(5000);
  }
}
