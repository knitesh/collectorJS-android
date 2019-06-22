package cs683.kumarnitesh.collectorjs.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post {

    private String id;
    private String title;
    private String description;
    private String link;

    private String userUid;
    private String userName;
    private String submissionDate;

    private int upVote;
    private int downVote;
    private int commentCount;

    public String userImg;



    private long timeInMilliSeconds;

    private List<Comment> comments;

    public Post (){}
    public Post(String id, String title, String link, String userUid, long submissionDate) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.userUid = userUid;
        this.timeInMilliSeconds = submissionDate;
    }

    public Post(String id, String title, String description, String link, String userUid, long submissionDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.userUid = userUid;
        this.timeInMilliSeconds = submissionDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public int getUpVote() {
        return upVote;
    }

    public void setUpVote(int upVote) {
        this.upVote = upVote;
    }

    public int getDownVote() {
        return downVote;
    }

    public void setDownVote(int downVote) {
        this.downVote = downVote;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getTimeInMilliSeconds() {
        return timeInMilliSeconds;
    }

    public void setTimeInMilliSeconds(long timeInMilliSeconds) {
        this.timeInMilliSeconds = timeInMilliSeconds;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", this.id);
        result.put("userUid", this.userUid);
        result.put("userName", this.userName);
        result.put("title", this.title);
        result.put("link", this.link);
        result.put("upVote", this.upVote);
        result.put("downVote", this.downVote);
        result.put("commentCount", this.commentCount);
        result.put("description", this.description);
        result.put("timeInMilliSeconds", this.timeInMilliSeconds);

        return result;
    }

    public void updatePost(){
        FirebaseAuth mAuth;
        String TAG =  "Update";

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();

        this.setUserUid(mAuth.getCurrentUser().getUid());

        Map<String, Object> postValues = this.toMap();

        String key = this.getId();
        if(key != null) {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/posts/" + key, postValues);
            childUpdates.put("/user-posts/" + this.getUserUid() + "/" + key, postValues);

            mDatabaseRef.updateChildren(childUpdates);
        }


    }
    public void updatePostVotes(){
        FirebaseAuth mAuth;
        String TAG =  "Update";

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();

        //this.setUserUid(mAuth.getCurrentUser().getUid());

        Map<String, Object> postValues = this.toMap();

        String key = this.getId();
        if(key != null) {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/posts/" + key, postValues);
            childUpdates.put("/user-posts/" + this.getUserUid() + "/" + key, postValues);

            mDatabaseRef.updateChildren(childUpdates);
        }


    }
}
