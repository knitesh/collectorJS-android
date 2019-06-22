package cs683.kumarnitesh.collectorjs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import cs683.kumarnitesh.collectorjs.Model.User;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener {


    private TextView mEmail, mPassword, mDisplayName;

    private static String TAG = RegisterPage.class.getSimpleName();

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();
        mUserDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mEmail = findViewById(R.id.register_emailTextBox);
        mPassword = findViewById(R.id.register_pwdTextBox);
        mDisplayName = findViewById(R.id.register_displayNameTextBox);

        // findViewById(R.id.loginBtn).setOnClickListener(this);

        findViewById(R.id.register_loginBtn).setOnClickListener(this);

        Log.d(TAG, "Register page loaded.");
    }

    @Override
    public void onClick(View view) {

        Log.d(TAG, "Register page on clicked " + view.getId());
        switch (view.getId()) {
            case R.id.register_loginBtn:
                String displayName = mDisplayName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(displayName == null || Strings.isEmptyOrWhitespace(displayName) ){
                    Toast.makeText(RegisterPage.this, "Please enter a display name",
                            Toast.LENGTH_SHORT).show();
                }
                else if(email!=null && email.length() >0 && password!=null && password.length() >0) {
                    createNewUser(displayName, email, password);
                }else{
                    Toast.makeText(RegisterPage.this, "Invalid UserId or Password",
                            Toast.LENGTH_SHORT).show();

                }
                break;
            default:
                break;
        }

    }

    private void createNewUser(final String displayName, final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();

                            // Store user display Name, user initials and user Id in FireBase database
                            // Write a message to the database

                            mUserDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

                            HashMap<String, Object> mUserMap = new HashMap<>();

                            User _user = new User();
                            _user.name = displayName;
                            _user.email = email;
                            _user.image ="default";


                            mUserMap.put(userId,_user);

                            mUserDatabaseRef.updateChildren(mUserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Intent mainIntent = new Intent(RegisterPage.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterPage.this, "Registration failed.Please try later.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException ex) {
                                String message = ex.getMessage().toString();
                                Toast.makeText(RegisterPage.this, message,
                                        Toast.LENGTH_SHORT).show();
                            } catch(FirebaseAuthUserCollisionException ex){
                                String message = ex.getMessage().toString();
                                Toast.makeText(RegisterPage.this, message,
                                        Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e) {
                                String message = e.getMessage().toString();
                                Toast.makeText(RegisterPage.this, message,
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                        }

                        // ...
                    }
                });
    }


}
