package cs683.kumarnitesh.collectorjs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent _homeIntent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(_homeIntent);
                    return true;
//                case R.id.navigation_search:
//                    Toast.makeText(getApplicationContext(),"Search Not implemented yet",Toast.LENGTH_SHORT).show();
//                    return true;
                case R.id.navigation_create:
                    Intent _createIntent = new Intent(getApplicationContext(),CreateNewPost.class);
                    startActivity(_createIntent);
                    Toast.makeText(getApplicationContext(),"Not implemented yet",Toast.LENGTH_SHORT);
                    return true;
                case R.id.navigation_profile:
                    Intent _profileIntent = new Intent(getApplicationContext(),ProfilePage.class);
                    startActivity(_profileIntent);

                    return true;
            }
            return false;
        }
    };


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        findViewById(R.id.toolbar_logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                Intent i=new Intent(getApplicationContext(), LoginPageActivity.class);
                i.putExtra("finish", true);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                //startActivity(i);
                startActivity( i );
                Toast.makeText(getApplicationContext(),"You are successfully logged out", Toast.LENGTH_SHORT);




                finish();

            }
        });
    }
}
