package cs683.kumarnitesh.collectorjs.db;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cs683.kumarnitesh.collectorjs.Model.User;

public class UsersDb {
    private static UsersDb instance = null;

    private UsersDb() {}

    public static UsersDb getInstance() {
        if (instance == null) {
            instance = new UsersDb();
        }
        return instance;
    }
    //For Email Login
    public void saveUserInfo(String userId,String name,String email,String deviceToken) {

        HashMap<String, Object> mUserMap = new HashMap<>();
        mUserMap.put("name", name);
        mUserMap.put("email", email);
        mUserMap.put("loginMedium", "Email");
        mUserMap.put("deviceToken", deviceToken);


    }

    // For Google Login
    public void saveUserInfo(final String userId, String name, String email, String deviceToken, String fbUserId) {


        final User user = new User();
        user.setName(name);
        user.setName(email);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){

                    User _existingUser = dataSnapshot.getValue(User.class);
                    HashMap<String, Object> mUpdateUserMap = new HashMap<>();


                    user.setImage( _existingUser.getImage());
                    user.thumb_image = _existingUser.thumb_image;

                    mUpdateUserMap.put(userId,user);
                    updatUserProfile(mUpdateUserMap);
                }else{

                    user.setImage("default");
                    user.thumb_image ="default";
                    HashMap<String, Object> mCreateUserMap = new HashMap<>();
                    mCreateUserMap.put(userId,user);
                    setUserProfile(userId,mCreateUserMap);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                user.image ="default";
                user.thumb_image ="default";
                HashMap<String, Object> mCreateUserMap = new HashMap<>();
                mCreateUserMap.put(userId,user);
                setUserProfile(userId,mCreateUserMap);
            }
        });


    }



    private void updatUserProfile(Map<String, Object> mUserMap) {

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseRef.updateChildren(mUserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("User Profile", "User Profile Updated");
            }
        });
    }
    public void setUserProfile(String userId, User user) {

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> mUserMap = new HashMap<>();

        mUserMap.put(userId,user);

        mDatabaseRef.updateChildren(mUserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("User Profile", "User Profile Updated");
            }
        });
    }

    private void setUserProfile(String userId, Map<String, Object> mUserMap) {

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseRef.updateChildren(mUserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("User Profile", "User Profile Updated");
            }
        });
    }
}
