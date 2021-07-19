package models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Comment {
    private String commentId;
    private String name;
    private String content;
    @ServerTimestamp
    private Date date;

    public Comment() {}

    public Comment(String commentId, String name, String content) {
        this.commentId = commentId;
        this.name = name;
        this.content = content;

    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }
}


