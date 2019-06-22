package cs683.kumarnitesh.collectorjs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cs683.kumarnitesh.collectorjs.Model.User;
import cs683.kumarnitesh.collectorjs.Util.LoadProfileImage;
import cs683.kumarnitesh.collectorjs.db.UsersDb;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProfilePage extends AppCompatActivity implements View.OnClickListener, OnLikeListener {

    private static final int ACTIVITY_NUM = 2;
    private final String TAG = "User_Profile_edit";

    private LikeButton _cancelProfileEdit;
    private LikeButton _saveProfileEdit;

    private TextView _changeProfileImage;

    private static int RESULT_LOAD_IMAGE = 1;

    //FireBase
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mCurrentUser;
    private StorageReference mStorageRef;
    private String currentUserGuid;

    private User mUser;
    private User _editUserDetail;

    private EditText _mDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.getMenu().getItem(2).setChecked(true);

        initializedFireBaseVariable();

        _cancelProfileEdit = findViewById(R.id.btn_cancel_profile_edit);
        _cancelProfileEdit.setOnLikeListener(this);

        _saveProfileEdit = findViewById(R.id.btn_save_profile_edit);
        _saveProfileEdit.setOnLikeListener(this);

        _changeProfileImage = findViewById(R.id.btn_change_profile_image);
        _changeProfileImage.setOnClickListener(this);

        _mDisplayName = findViewById(R.id.profile_edit_name);
    }

    private void initializedFireBaseVariable() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserGuid = mCurrentUser.getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserGuid);

        mDatabaseRef.keepSynced(true);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUser = dataSnapshot.getValue(User.class);

                populateEditProfileField(mUser);
                String imageUrl ="";
                if(dataSnapshot.child("image").getValue() !=null)
                    imageUrl = dataSnapshot.child("image").getValue().toString();

                setProfileImageView(Uri.parse(imageUrl));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void populateEditProfileField(User userData) {
        _editUserDetail = new User();
        if(userData !=null){
            _editUserDetail.image = userData.image;
            _editUserDetail.name = userData.name;
            _mDisplayName.setText(_editUserDetail.name);
        }



    }

    private void goBack() {
        Intent profileIntent = new Intent(ProfilePage.this, MainActivity.class);
        // profileIntent.putExtra("CALLING_ACTIVITY", "SELF_VIEW");
        startActivity(profileIntent);
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_change_profile_image:
                Log.d(TAG, "changeProfileImage:onclick");
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            default:
                break;
        }

    }

    @Override
    public void liked(LikeButton likeButton) {
        switch (likeButton.getId()) {
            case R.id.btn_save_profile_edit:
                saveCurrentProfileEdit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goBack();
                    }
                }, 800);
                break;
            case R.id.btn_cancel_profile_edit:
                goBack();
                break;
            default:
                break;

        }

    }

    @Override
    public void unLiked(LikeButton likeButton) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            //Start Crop Activity
            // start cropping activity for pre-acquired image saved on the device
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        } else {


            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = result.getUri();
                    File selectedImageFile = new File(selectedImage.getPath());

                    // setProfileImageView(selectedImage);

                    final StorageReference storageRef = mStorageRef.child("CollectorJS").child("profile_image/" + mCurrentUser.getUid() + ".jpg");

                    new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)
                            .compressToBitmapAsFlowable(selectedImageFile)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Bitmap>() {
                                @Override
                                public void accept(Bitmap bitmap) throws Exception {
                                    UploadProfileImage(storageRef, bitmap);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) {
                                    throwable.printStackTrace();
                                    showError(throwable.getMessage());
                                }
                            });


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }

        }


    }

    private void UploadProfileImage(final StorageReference storageRef, Bitmap bitmap) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);

        uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Upload", taskSnapshot.getMetadata().toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d("StorageRef", downloadUri.toString());
                    mDatabaseRef.child("image").setValue(downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void showError(String message) {
    }

    private void saveCurrentProfileEdit() {
      //  Log.d(TAG, "saveCurrentProfileEdit" + _editUserDetail.toString());

        //Update User Data
        mUser.setName(_mDisplayName.getText().toString());

        UsersDb usersDb = UsersDb.getInstance();

        usersDb.setUserProfile(currentUserGuid, mUser);

    }

    /**
     * Set Profile Image
     */
    private void setProfileImageView(final Uri selectedImage) {
        final CircleImageView imageView = findViewById(R.id.profile_image);
        LoadProfileImage.setImageView(selectedImage,imageView);
    }
}
