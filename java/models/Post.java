package models;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    private String postType;
    private String documentId;
    private String userProfile;
    private String userName;
    private String userId;
    private String title;
    private String content;
    @ServerTimestamp
    private Date date;


    public Post() {} //빈 생성자

    public Post(String postType, String documentId, String userProfile, String userName, String userId, String title, String content) { //생성자
        this.postType = postType;
        this.documentId = documentId;
        this.userProfile = userProfile;
        this.userName = userName;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userIdId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "Post{" +
                "postType='" + postType + '\'' +
                ", documentId='" + documentId + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }
}
