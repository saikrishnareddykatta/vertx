package com.learning.udemy.vertx_starter.customcodec;

public class Pong {

  private Integer id;

  public Pong () {

  }

  public Pong(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Pong{" +
      "id=" + id +
      '}';
  }
}
