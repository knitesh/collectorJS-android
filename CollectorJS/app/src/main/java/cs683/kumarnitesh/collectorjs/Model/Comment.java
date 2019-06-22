package cs683.kumarnitesh.collectorjs.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    private String comment;
    private String postId;
    private String userId;
    private String userName;
    private long timeStamp;

    public Comment() {
    }

    public Comment(String comment, String postId, String userId) {
        this.comment = comment;
        this.postId = postId;
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("postId", this.postId);
        result.put("userId", this.userId);
        result.put("timeStamp", this.timeStamp);
        result.put("userName", this.userName);
        result.put("comment", this.comment);

        return result;
    }
}
