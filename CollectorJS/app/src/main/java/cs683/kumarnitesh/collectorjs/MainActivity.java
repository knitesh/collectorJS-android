package cs683.kumarnitesh.collectorjs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs683.kumarnitesh.collectorjs.Model.Post;
import cs683.kumarnitesh.collectorjs.Model.User;

public class MainActivity extends BaseActivity {
    private TextView mTextMessage;
    private Button mLogoutButton;
    private FirebaseAuth mAuth;
    private static final String TAG =  MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 10;

    protected String[] mDataset;

    List<Post> mPostList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);


        mTextMessage = findViewById(R.id.message);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        if(mAuth.getCurrentUser()!=null){

            initDataset();
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Log.d(TAG,"user not logged in");
            Intent loginIntent = new Intent(this,LoginPageActivity.class);

            startActivity(loginIntent);
        }else{
            Log.d(TAG,"Use logged In");
        }
    }



    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference("/posts");



        mPostList = new ArrayList<>();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mPostList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    final Post post = postSnapshot.getValue(Post.class);


                    String userID = post.getUserUid();


                    // Get a reference to our posts
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference userDbRef = database.getReference().child("Users").child(userID);

                    userDbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User _user = dataSnapshot.getValue(User.class);
                            post.setUserName(_user.name);
                            post.userImg = _user.image;

                            mPostList.add(post);

                            mAdapter = new PostListAdapter(getApplicationContext(), mPostList);

                            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

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
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });

                    Log.d(TAG,post.getTitle());

                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }




}
