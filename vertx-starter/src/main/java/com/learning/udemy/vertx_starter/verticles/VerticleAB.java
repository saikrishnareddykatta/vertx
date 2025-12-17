package com.learning.udemy.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleAB extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(VerticleAB.class);

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    // System.out.println("Start " + getClass().getName());
    LOG.debug("Start {}", getClass().getName());
    startPromise.complete();
  }

  @Override
  public void stop(final Promise<Void> stopPromise) throws Exception {
    // System.out.println("Stop " + getClass().getName());
    LOG.debug("Stop {}", getClass().getName());
    stopPromise.complete();
  }
}
