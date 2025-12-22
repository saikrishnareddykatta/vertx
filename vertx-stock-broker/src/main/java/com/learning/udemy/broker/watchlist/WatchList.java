package com.learning.udemy.broker.watchlist;

import com.learning.udemy.broker.assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchList {
  List<Asset> assets;

  // Here we are converting a WatchList Object into Json Object by using the built-in mapping functionality
  JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }
}
