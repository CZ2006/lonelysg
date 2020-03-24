package com.IrisBICS.lonelysg.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.IrisBICS.lonelysg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpPage extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private EditText usernameInput;
    private Button back;
    private Button signUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.emailInputS);
        passwordInput = findViewById(R.id.passwordInputS);
        usernameInput = findViewById(R.id.userNameInput);

        back = (Button)findViewById(R.id.backButton);
        signUp = (Button)findViewById(R.id.signUpButtonS);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                final String email = emailInput.getText().toString();
                final String pwd = passwordInput.getText().toString();
                final String username = usernameInput.getText().toString();

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
//                                String userID = FirebaseAuthHelper.getUserID();
                                String userID = mAuth.getCurrentUser().getUid();
                                createUser(userID);
                                Intent intent = new Intent(SignUpPage.this, EditProfileUI.class);
                                startActivity(intent);
                                finish();
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

    private void createUser(String userUserID){
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", emailInput.getText().toString());
            jsonBody.put("username",usernameInput.getText().toString());
            jsonBody.put("password",passwordInput.getText().toString());
            String URL = "https://us-central1-lonely-4a186.cloudfunctions.net/app/XQ/addUser/"+userUserID;
            JsonObjectRequest createUserRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", error.toString());
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(createUserRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}