package com.IrisBICS.lonelysg.Activities;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.IrisBICS.lonelysg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityLogin extends AppCompatActivity {
    RelativeLayout loginStuff, passwordSignUpBar;

    private EditText username;
    private EditText password;
    private Button signIn;
    private Button signUp;
    private Button forgotPW;
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
    public void onStart() {
        super.onStart();
        //If logged in, go straight to next page
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            Toast.makeText(ActivityLogin.this, "You are logged in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivityLogin.this, ActivityNavigationBar.class);
            startActivity(intent);
        } else {
            Toast.makeText(ActivityLogin.this, "Please log in.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginStuff = (RelativeLayout) findViewById(R.id.loginStuff);
        passwordSignUpBar = (RelativeLayout) findViewById(R.id.passwordSignUpBar);

        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.animate().alpha(0f).setDuration(2600);

        handler.postDelayed(runnable, 3000); // Timeout for the splash

        mAuth = FirebaseAuth.getInstance();
        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);
        signIn = (Button) findViewById(R.id.signInButton);
        signUp = (Button) findViewById(R.id.signUpButton);
        forgotPW = (Button) findViewById(R.id.forgotPasswordButton);

        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = username.getText().toString();
                String pwd = password.getText().toString();
                //to do: error handling. for now just assume input will be correct
                if (!email.isEmpty() && !pwd.isEmpty()) {
                    //FIREBASE LOGIN AUTHENTICATION
                    mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("LoginUI", "signInWithEmail:success");
                                Toast.makeText(ActivityLogin.this, "Sign in success!", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(ActivityLogin.this, ActivityNavigationBar.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("LoginUI", "signInWithEmail:failure", task.getException());
                                Toast.makeText(ActivityLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentS = new Intent(ActivityLogin.this, ActivitySignUp.class);
                startActivity(intentS);
            }
        });

        forgotPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = username.getText().toString();
                if (email.isEmpty()) { //dk why this isn't showing
                    Toast.makeText(ActivityLogin.this, "Please enter your registered email first.", Toast.LENGTH_SHORT).show();
                }
                if (!email.isEmpty()) {
                    //call password reset api
                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("LoginUI", "Email sent.");
                                Toast.makeText(ActivityLogin.this, "Password reset sent to your email!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ActivityLogin.this, "Please make sure email is already registered.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ActivityLogin.this, "Error occurred :(", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        PushNotifications.start(getApplicationContext(), "211e38a9-4bc8-40c5-958a-4a7f9aa91547");
//        PushNotifications.addDeviceInterest("debug-apple");
//        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
//            @Override
//            public void onMessageReceived(RemoteMessage remoteMessage) {
//                String messagePayload = remoteMessage.getData().get("inAppNotificationMessage");
//                if (messagePayload == null) {
//                    // Message payload was not set for this notification
//                    Log.i("MyActivity", "Payload was missing");
//                } else {
//                    Log.i("MyActivity", messagePayload);
//                    Toast.makeText(LoginUI.this, "You received a request", Toast.LENGTH_SHORT).show();
//                    // Now update the UI based on your message payload!
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
//            @Override
//            public void onMessageReceived(RemoteMessage remoteMessage) {
//                String messagePayload = remoteMessage.getData().get("inAppNotificationMessage");
//                if (messagePayload == null) {
//                    // Message payload was not set for this notification
//                    Log.i("MyActivity", "Payload was missing");
//                } else {
//                    Log.i("MyActivity", messagePayload);
//                    Toast.makeText(LoginUI.this, "You received a request", Toast.LENGTH_SHORT).show();
//                    // Now update the UI based on your message payload!
//                }
//            }
//        });
//    }

}