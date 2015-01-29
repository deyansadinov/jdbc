package com.clouway.task5;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class Article {

  private final int id;
  private final String title;

  public Article(int id,String title) {
    this.id = id;
    this.title = title;
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }
}
