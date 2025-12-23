package com.learning.udemy.broker.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.UUID;

public class WatchListRestApi {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListRestApi.class);
  public static void attach(Router parent){
    final String path = "/account/watchlist/:accountId";
    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<UUID, WatchList>();

    // REQUIRED: enable request body parsing for PUT/POST/PATCH
    parent.route().handler(BodyHandler.create());
    parent.get(path).handler(new GetWatchListHandler(watchListPerAccount));
    parent.put(path).handler(new PutWatchListHandler(watchListPerAccount));
    parent.delete(path).handler(new DeleteWatchListHandler(watchListPerAccount));
  }

  static UUID parseAccountIdOrFail(io.vertx.ext.web.RoutingContext ctx) {
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
