package cs683.kumarnitesh.collectorjs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailLoginForm extends AppCompatActivity implements View.OnClickListener {

    private TextView mEmail, mPassword;
    private Button mLoginBtn;

    private TextView mRegisterBtn;

    private static String TAG =EmailLoginForm.class.getSimpleName();

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login_form);

        mAuth = FirebaseAuth.getInstance();


        mEmail = findViewById(R.id.emailTextBox);
        mPassword = findViewById(R.id.pwdTextBox);

        findViewById(R.id.loginBtn).setOnClickListener(this);

        findViewById(R.id.login_registerBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.loginBtn:
                emailSignIn(mEmail.getText().toString(),mPassword.getText().toString());
                break;
            case R.id.login_registerBtn:
                Intent _registerActivity = new Intent(this,RegisterPage.class);
                startActivity(_registerActivity);
                break;
            default: break;
        }

    }

    public void emailSignIn(String email,String password){

        if(email!=null && email.length() >0 && password!=null && password.length() >0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(EmailLoginForm.this, "Authentication success.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null){
                                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(mainIntent);
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(EmailLoginForm.this, "Authentication failed:"+task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }else{
            Toast.makeText(EmailLoginForm.this, "Invalid UserId or Password",
                    Toast.LENGTH_SHORT).show();

        }

    }

}
