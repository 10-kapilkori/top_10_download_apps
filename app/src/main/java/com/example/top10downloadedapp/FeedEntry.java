package com.example.top10downloadedapp;

public class FeedEntry {

    String title;
    String description;
    String link;
    String publishDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return "\n" +
                "Title= " + title + "\n" +
                "Link= " + link + "\n" +
                "Publish Date= " + publishDate + "\n" ;
    }
}
