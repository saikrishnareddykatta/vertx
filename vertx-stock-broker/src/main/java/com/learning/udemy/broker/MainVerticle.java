package com.learning.udemy.broker;

import com.learning.udemy.broker.assets.AssetsRestApi;
import com.learning.udemy.broker.quotes.QuotesRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
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
        LOG.info("Deployed {} with id={}", MainVerticle.class.getName(), deploymentId)
      )
      .onFailure(err ->
        LOG.error("Failed to deploy {}", MainVerticle.class.getName(), err)
      );
  }
  @Override
  public void start(Promise<Void> startPromise) {
    final Router restApi = Router.router(vertx);
    AssetsRestApi.attach(restApi);
    QuotesRestApi.attach(restApi);
    restApi.route().failureHandler(handleFailure());
    vertx.createHttpServer()
      .requestHandler(restApi)
      .exceptionHandler(error -> LOG.error("HTTP Server error: ", error))
      .listen(PORT)
      .onSuccess(server -> {
          startPromise.complete();
          LOG.info("HTTP Server started on Port 8888");
        })
      .onFailure(startPromise::fail);
  }

  private static Handler<RoutingContext> handleFailure() {
    return errorContext -> {
      if (errorContext.response().ended()) {
        // Ignore completed response
        return;
      }
      LOG.error("Route Error: ", errorContext.failure());
      errorContext.response()
        .setStatusCode(500)
        .end(new JsonObject().put("message", "Something went wrong").toBuffer());
    };
  }
}
