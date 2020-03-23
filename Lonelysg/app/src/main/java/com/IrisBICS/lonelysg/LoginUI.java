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

public class LoginUI extends AppCompatActivity {
    RelativeLayout loginStuff, passwordSignUpBar;

    private EditText Username;
    private EditText Password;
    private Button SignIn;
    private Button signUp;
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
        logo.animate().alpha(0f).setDuration(2600);

        handler.postDelayed(runnable, 3000); // Timeout for the splash

        mAuth = FirebaseAuth.getInstance();
        Username = (EditText)findViewById(R.id.usernameInput);
        Password = (EditText)findViewById(R.id.passwordInput);
        SignIn = (Button)findViewById(R.id.signInButton);
        signUp = (Button)findViewById(R.id.signUpButton);

        //If logged in, go straight to next page
        //might want to change this to onStart instead of onCreate
        FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginUI.this, "You are logged in.", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(MainActivity.this, LoginPage.class);
                    //startActivity(intent);
                }
                else {
                    Toast.makeText(LoginUI.this, "Please log in.", Toast.LENGTH_SHORT).show();
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
                    mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginUI.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("MainActivity", "signInWithEmail:success");
                                Toast.makeText(LoginUI.this, "Sign in success!", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginUI.this, NavigationBarUI.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("MainActivity", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginUI.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intentS = new Intent(LoginUI.this, SignUpPage.class);
                startActivity(intentS);
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
