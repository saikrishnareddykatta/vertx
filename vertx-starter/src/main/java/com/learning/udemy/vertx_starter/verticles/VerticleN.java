package com.learning.udemy.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleN extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(VerticleN.class);

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    // System.out.println("Start " + getClass().getName() + "on thread " + Thread.currentThread().getName());
    // System.out.println("Start " + getClass().getName() + " with config " + config().toString());
    LOG.debug("Start {}", getClass().getName());
    startPromise.complete();
  }
}
