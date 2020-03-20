package com.IrisBICS.lonelysg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    RelativeLayout loginStuff, passwordSignUpBar;

    private EditText Username;
    private EditText Password;
    private Button SignIn;
    private FirebaseAuth mAuth;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loginStuff.setVisibility(View.VISIBLE);
            passwordSignUpBar.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        loginStuff = (RelativeLayout) findViewById(R.id.loginStuff);
        passwordSignUpBar = (RelativeLayout) findViewById(R.id.passwordSignUpBar);

        ImageView logo = (ImageView)findViewById(R.id.logo);
        logo.animate().alpha(0f).setDuration(1900);

        handler.postDelayed(runnable, 2000); // Timeout for the splash

        mAuth = FirebaseAuth.getInstance();
        Username = (EditText)findViewById(R.id.usernameInput);
        Password = (EditText)findViewById(R.id.passwordInput);
        SignIn = (Button)findViewById(R.id.signInButton);

        //If logged in, go straight to next page
        FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(MainActivity.this, "You are logged in.", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(MainActivity.this, LoginPage.class);
                    //startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Please log in.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String email = Username.getText().toString();
                String pwd = Password.getText().toString();
                //to do: error handling. for now just assume input will be correct
                if (!email.isEmpty() && !pwd.isEmpty()) {
                    //FIREBASE LOGIN AUTHENTICATION
                    mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("MainActivity", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("MainActivity", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
