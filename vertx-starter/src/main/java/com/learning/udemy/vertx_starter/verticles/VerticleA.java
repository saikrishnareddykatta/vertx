package com.learning.udemy.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleA extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(VerticleA.class);

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    // System.out.println("Start " + getClass().getName());
    LOG.debug("Start {}", getClass().getName());
    vertx.deployVerticle(new VerticleAA())
      .onSuccess(deploymentId -> {
        // System.out.println("Deployed " + VerticleAA.class.getName());
        LOG.debug("Deployed {}", VerticleAA.class.getSimpleName());
        vertx.undeploy(deploymentId);
      })
      .onFailure(Throwable::printStackTrace);
    vertx.deployVerticle(new VerticleAB());
    startPromise.complete();
  }
}
