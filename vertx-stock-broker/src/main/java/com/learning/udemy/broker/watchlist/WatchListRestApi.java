package com.learning.udemy.broker.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class WatchListRestApi {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListRestApi.class);
  public static void attach(Router parent){
    final String path = "/account/watchlist/:accountId";
    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<UUID, WatchList>();

    // REQUIRED: enable request body parsing for PUT/POST/PATCH
    parent.route().handler(BodyHandler.create());

    parent.get(path).handler(context -> {
      UUID accountId = parseAccountIdOrFail(context);
      if (accountId == null) return;
      LOG.debug("{} for account {}", context.normalizedPath(), accountId);
      var watchList = Optional.ofNullable(watchListPerAccount.get(accountId));
      if(watchList.isEmpty()) {
        context.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(new JsonObject()
            .put("message", "watch list for " + accountId + " is not available")
            .put("path", context.normalizedPath())
            .toBuffer()
          );
        return;
      }
      context.response().end(watchList.get().toJsonObject().toBuffer());
    });

    parent.put(path).handler(context -> {
      UUID accountId = parseAccountIdOrFail(context);
      if (accountId == null) return;
      LOG.debug("{} for account {}", context.normalizedPath(), accountId);

      JsonObject json;
      try {
        json = context.body().asJsonObject();   // safest for Vert.x 5
      } catch (Exception e) {
        context.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
          .end(new JsonObject()
            .put("message", "Request body must be valid JSON")
            .put("path", context.normalizedPath())
            .toBuffer());
        return;
      }
      var watchList = json.mapTo(WatchList.class);
      watchListPerAccount.put (accountId, watchList);
      context.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HttpResponseStatus.OK.code())
        .end(json.toBuffer());
    });

    parent.delete(path).handler(context -> {
    });
  }

  private static UUID parseAccountIdOrFail(io.vertx.ext.web.RoutingContext ctx) {
    String raw = ctx.pathParam("accountId");
    try {
      return UUID.fromString(raw);
    } catch (IllegalArgumentException e) {
      ctx.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
        .end(new JsonObject()
          .put("message", "accountId must be a UUID")
          .put("accountId", raw)
          .put("path", ctx.normalizedPath())
          .toBuffer());
      return null;
    }
  }
}
