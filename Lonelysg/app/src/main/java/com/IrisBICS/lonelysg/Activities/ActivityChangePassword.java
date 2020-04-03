package com.IrisBICS.lonelysg.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.IrisBICS.lonelysg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityChangePassword extends AppCompatActivity {
    private EditText oldPasswordInput;
    private EditText newPasswordInput;
    private EditText confirmPasswordInput;
    private Button changePassword;
    private Button forgotPassword;
    private Button back;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();
        oldPasswordInput = findViewById(R.id.oldPasswordInput);
        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        changePassword = (Button)findViewById(R.id.changePasswordButton);
        forgotPassword = (Button)findViewById(R.id.forgotPasswordButton);
        back = (Button)findViewById(R.id.backButton);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oldPw = oldPasswordInput.getText().toString();
                final String newPw = newPasswordInput.getText().toString();
                final String cfmPw = confirmPasswordInput.getText().toString();

                if (!oldPw.isEmpty() && !newPw.isEmpty() && !cfmPw.isEmpty()) {
                    if (newPw == cfmPw) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.updatePassword(newPw)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("ActivityChangePassword", "User password updated.");
                                        }
                                    }
                                });
                    }
                    else {
                        Toast.makeText(ActivityChangePassword.this, "Error: New password does not match confirm password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ActivityChangePassword.this, "Error occured. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("ActivityChangePassword", "Email sent.");
                                Toast.makeText(ActivityChangePassword.this, "Password reset sent to your email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(ActivityChangePassword.this, "Error occurred.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
    };
}
