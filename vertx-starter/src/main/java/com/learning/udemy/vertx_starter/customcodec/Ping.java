package com.learning.udemy.vertx_starter.customcodec;

public class Ping {

  private String message;
  private Boolean isEnabled;

  public Ping () {

  }

  public Ping(String message, Boolean isEnabled) {
    this.message = message;
    this.isEnabled = isEnabled;
  }

  public String getMessage() {
    return message;
  }

  public Boolean getEnabled() {
    return isEnabled;
  }

  @Override
  public String toString() {
    return "Ping{" +
      "message='" + message + '\'' +
      ", isEnabled=" + isEnabled +
      '}';
  }
}
