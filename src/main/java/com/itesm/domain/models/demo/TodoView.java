package com.itesm.domain.models.demo;

import java.util.List;

public class TodoView {
    private String id;
    private String title;
    private boolean completed;
    private String ownerName;
    private String ownerEmail;
    private List<String> categories;
    private List<CommentView> comments;

    public TodoView() {}

    public TodoView(String id, String title, boolean completed, String ownerName, String ownerEmail,
                    List<String> categories, List<CommentView> comments) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.categories = categories;
        this.comments = comments;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }
    public List<CommentView> getComments() { return comments; }
    public void setComments(List<CommentView> comments) { this.comments = comments; }

    public static class CommentView {
        private String content;
        private String authorName;

        public CommentView() {}
        public CommentView(String content, String authorName) {
            this.content = content;
            this.authorName = authorName;
        }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getAuthorName() { return authorName; }
        public void setAuthorName(String authorName) { this.authorName = authorName; }
    }
}