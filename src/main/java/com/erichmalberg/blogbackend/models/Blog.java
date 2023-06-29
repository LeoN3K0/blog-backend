package com.erichmalberg.blogbackend.models;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "blogs")
public class Blog {
    
    @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "title")
  private String title;

  @Column(name = "body")
  private String body;

  @Column(name = "published")
  private boolean published;

  @Column(name = "published_date")
  private LocalDate publishedDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User author;

  public Blog() {

  }

  public Blog(String title, String body, LocalDate publishedDate, User author,  boolean published) {
    this.title = title;
    this.body = body;
    this.published = published;
    this.author = author;
    this.publishedDate = publishedDate;
  }

  public long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }


  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean published) {
    this.published = published;
  }

  public LocalDate getPublishedDate() {
    return publishedDate;
  }

  public void setPublishedDate(LocalDate publishedDate) {
    this.publishedDate = publishedDate;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  @Override
  public String toString() {
    return "Blog [id=" + id + ", title=" + title + ", published=" + published + ", author=" + author + ", publisheddate=" + published + "]";
  }
}
