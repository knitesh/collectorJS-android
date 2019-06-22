package cs683.kumarnitesh.collectorjs;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cs683.kumarnitesh.collectorjs.Model.Post;

public class CreateNewPost extends BaseActivity {

    private FirebaseAuth mAuth;
    private static final String TAG =  CreateNewPost.class.getSimpleName();

    //UI Elements
    private FloatingActionButton create_post_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.getMenu().getItem(1).setChecked(true);

        findViewById(R.id.create_post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"New Post Button clicked");
                createNewPost();
            }
        });

    }

    private void createNewPost() {

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();


        // Data Entered by user
        EditText _titleView = findViewById(R.id.create_post_edit_title);
        EditText _linkView = findViewById(R.id.create_post_link);
        EditText _descriptionView = findViewById(R.id.create_post_edit_description);

        String _title = _titleView.getText().toString();
        String _link = _linkView.getText().toString();
        String _description = _descriptionView.getText().toString();

        if(_title!= null && _title.length() > 0 && _link!=null && _link.length() > 0) {
            if (mAuth.getCurrentUser() != null) {

                if (_description == null)
                    _description = "";

                Post _post = new Post();

                long now = System.currentTimeMillis();

                String key = mDatabaseRef.child("Posts").push().getKey();

                _post.setUserUid(mAuth.getCurrentUser().getUid());

                _post.setTitle(_title);
                _post.setDescription(_description);
                _post.setDownVote(0);
                _post.setCommentCount(0);
                _post.setLink(_link);
                _post.setTimeInMilliSeconds(now);
                _post.setUpVote(0);
                _post.setId(key);

                //Get Display Name and ImageURL for the current user and update



                Map<String, Object> postValues = _post.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/posts/" + key, postValues);
                childUpdates.put("/user-posts/" + _post.getUserUid() + "/" + key, postValues);

                mDatabaseRef.updateChildren(childUpdates);

                Toast.makeText(getApplicationContext(),"Data Saved Successfully.",Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(this, MainActivity.class);

                startActivity(mainIntent);
                finish();

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
}
