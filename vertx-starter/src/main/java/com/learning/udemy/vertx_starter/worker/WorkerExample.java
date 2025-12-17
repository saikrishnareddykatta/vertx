package com.learning.udemy.vertx_starter.worker;

import com.learning.udemy.vertx_starter.verticles.MainVerticle;
import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerExample extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    var vertx =  Vertx.vertx();
    vertx.deployVerticle(new WorkerExample());
  }

  @Override
  public void start(Promise<Void> startPromise) {
    LOG.debug("Start {}", getClass().getName());
    vertx.deployVerticle(new WorkerVerticle(), new DeploymentOptions()
      .setThreadingModel(ThreadingModel.WORKER) // a way of telling Vert.x to deploy a verticle as a worker verticle
      .setWorkerPoolSize(1) // to set the worker pool size
      .setWorkerPoolName("my-worker-verticle") // to set the name for worker thread
    );
    startPromise.complete();
    executeBlockingCode();
  }

  private void executeBlockingCode() {
    vertx
      .executeBlocking(() -> {
        LOG.debug("Blocking Code Execution Started");
        try {
          Thread.sleep(5000);
          // simulate failure
          // throw new RuntimeException("Something went wrong!");
        } catch (Exception e) {
          LOG.error("Exception raised during Blocking Code Execution", e);
          // Important: rethrow so the Future fails
          throw e;
        }
        // return result (or null if you don’t care)
        return (Void) null;
      })
      .onSuccess(v -> {
        LOG.debug("Blocking Call Operation Completed Successfully");
      })
      .onFailure(err -> {
        LOG.debug("Blocking Call Operation Failed due to: ", err);
      });
  }
}
