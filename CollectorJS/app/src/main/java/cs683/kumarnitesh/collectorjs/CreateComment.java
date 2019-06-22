package cs683.kumarnitesh.collectorjs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs683.kumarnitesh.collectorjs.Model.Comment;
import cs683.kumarnitesh.collectorjs.Model.Post;
import cs683.kumarnitesh.collectorjs.Model.User;

public class CreateComment extends BaseActivity {

    private FirebaseAuth mAuth;
    private static final String TAG =  CreateNewPost.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String mPostID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);

        if (getIntent().getStringExtra("POST_ID")!= "")
            this.mPostID = getIntent().getStringExtra("POST_ID");

        findViewById(R.id.create_comment_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"New Comment Button clicked");
                createNewComment();
            }
        });

        getExistingComment();

    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.getMenu().getItem(0).setChecked(true);
    }

    private void createNewComment() {

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabaseRef = database.getReference();


        // Data Entered by user
        EditText _commentView = findViewById(R.id.editText_create_comment);

        String _comment = _commentView.getText().toString();


        if(_comment!= null && _comment.length() > 0 ) {
            if (mAuth.getCurrentUser() != null) {



                final Comment comment = new Comment();

                final String key = mDatabaseRef.child("Comments").push().getKey();
                final String userID = mAuth.getCurrentUser().getUid();
                // String userName = mAuth.getCurrentUser().getDisplayName();

                comment.setUserId(userID);
                comment.setComment(_comment);
                comment.setTimeStamp(System.currentTimeMillis());

                // Get a reference to our posts
                //final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userDbRef = database.getReference().child("Users").child(userID);

                userDbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User _user = dataSnapshot.getValue(User.class);
                        comment.setUserName(_user.name);
                        comment.setPostId(mPostID);


                        Map<String, Object> commentMapValues = comment.toMap();

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/comments/"+mPostID+"/" + key, commentMapValues);
                        childUpdates.put("/user-comments/" + userID + "/" +mPostID+"/" + key, commentMapValues);

                        mDatabaseRef.updateChildren(childUpdates);

                        final DatabaseReference mPostReference = database.getReference().child("posts/"+mPostID);

                        ValueEventListener postListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get Post object and use the values to update the UI
                                Post post = dataSnapshot.getValue(Post.class);

                                mPostReference.child("commentCount").setValue(post.getCommentCount() + 1);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message
                                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                // ...
                            }
                        };
                        mPostReference.addListenerForSingleValueEvent(postListener);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Toast.makeText(getApplicationContext(),"Comment Saved Successfully.",Toast.LENGTH_SHORT).show();
                _commentView.setText("");


            } else {
                Log.d(TAG, "user not logged in");
                Intent loginIntent = new Intent(this, LoginPageActivity.class);

                startActivity(loginIntent);
                finish();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Invalid inputs. Please re enter data.",Toast.LENGTH_SHORT).show();
        }
    }

    private void getExistingComment(){

        mAuth = FirebaseAuth.getInstance();

        //Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mCommentDatabaseRef = database.getReference("/comments/"+ mPostID);



        final List<Comment> commentList = new ArrayList<>();
        mCommentDatabaseRef.orderByChild("-timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Comment comment = postSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                    //Log.d(TAG,comment.getTitle());

                }

                mAdapter = new CommentListAdapter(getApplicationContext(), commentList);

                recyclerView = findViewById(R.id.comment_recycler_view);

                // specify an adapter (see also next example)

                recyclerView.setAdapter(mAdapter);

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                recyclerView.setHasFixedSize(true);

                // use a linear layout manager
                layoutManager = new LinearLayoutManager(getApplicationContext());

                ((LinearLayoutManager) layoutManager).setReverseLayout(true);
                ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

    }
}
