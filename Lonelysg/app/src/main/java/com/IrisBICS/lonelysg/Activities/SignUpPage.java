package com.IrisBICS.lonelysg.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.IrisBICS.lonelysg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpPage extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button back;
    private Button signUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        mAuth = FirebaseAuth.getInstance();
        username = (EditText)findViewById(R.id.usernameInputS);
        password = (EditText)findViewById(R.id.passwordInputS);
        back = (Button)findViewById(R.id.backButton);
        signUp = (Button)findViewById(R.id.signUpButtonS);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String email = username.getText().toString();
                String pwd = password.getText().toString();

                //to do: error handling. for now just assume input will be correct
                if (!email.isEmpty() && !pwd.isEmpty()) {
                    //FIREBASE LOGIN AUTHENTICATION
                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign up success, update UI with the signed-in user's information
                                Log.d("MainActivity", "createUserWithEmail:success");
                                Toast.makeText(SignUpPage.this, "Sign up success!", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(SignUpPage.this, NavigationBarUI.class);
                                startActivity(intent);
                            } else {
                                // If sign up fails, display a message to the user.
                                Log.w("MainActivity", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SignUpPage.this, LoginUI.class);
                startActivity(intent);
            }
        });

    }
}