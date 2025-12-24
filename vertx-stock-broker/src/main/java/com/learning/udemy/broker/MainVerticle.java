package com.learning.udemy.broker;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  public static final int PORT = 8888;
  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);


  public static void main(String[] args) {
   var vertx = Vertx.vertx();
   vertx.exceptionHandler(error -> {
     LOG.error("Unhandled {}: ", error);
   });
    vertx.deployVerticle(new MainVerticle())
      .onSuccess(deploymentId ->
        LOG.info("Deployed {} with id={}", MainVerticle.class.getSimpleName(), deploymentId)
      )
      .onFailure(err ->
        LOG.error("Failed to deploy {}", MainVerticle.class.getSimpleName(), err)
      );
  }
  @Override
  public void start(Promise<Void> startPromise) {
    vertx.deployVerticle(RestApiVerticle.class.getName(), new DeploymentOptions().setInstances(getInstances()))
      .onFailure(err ->
        LOG.error("Failed to deploy {}", RestApiVerticle.class.getSimpleName(), err)
      )
      .onSuccess(deploymentId ->
        {
          LOG.info("Deployed {} with id={}", RestApiVerticle.class.getSimpleName(), deploymentId);
          startPromise.complete();
        }
      );
  }

  private static int getInstances() {
    return Math.max(1, Runtime.getRuntime().availableProcessors());
  }

}
