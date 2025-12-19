package com.learning.udemy.broker.quotes;

import com.learning.udemy.broker.assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Quote {
    Asset asset;
    BigDecimal bid;
    BigDecimal ask;
    BigDecimal lastPrice;
    BigDecimal volume;

    public JsonObject toJsonObject(){
      // Here we are converting a Quote Object into Json Object
      return JsonObject.mapFrom(this);
    }
}
