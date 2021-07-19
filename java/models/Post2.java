package models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post2 {
    private String documentId;
    private String title;
    private String content;
    @ServerTimestamp
    private Date date;


    public Post2() {} //빈 생성자

    public Post2(String documentId, String title, String content) { //생성자
        this.documentId = documentId;
        this.title = title;
        this.content = content;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return "Post{" +
                "documentId='" + documentId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }
}
