package com.learning.udemy.broker.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.UUID;

public class PutWatchListHandler implements Handler<RoutingContext> {

  private static final Logger LOG = LoggerFactory.getLogger(PutWatchListHandler.class);
  private final HashMap<UUID, WatchList> watchListPerAccount;

  public PutWatchListHandler(HashMap<UUID, WatchList> watchListPerAccount) {
    this.watchListPerAccount = watchListPerAccount;
  }


  @Override
  public void handle(RoutingContext context) {
    UUID accountId = WatchListRestApi.parseAccountIdOrFail(context);
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
  }
}
