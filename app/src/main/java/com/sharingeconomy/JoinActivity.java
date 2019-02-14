package com.sharingeconomy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class JoinActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText editTextLoginPassword;
    EditText editTextEmailId;
    EditText editTextUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Button btnSubmit = (Button) findViewById(R.id.btnSignUp);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
        editTextEmailId = (EditText) findViewById(R.id.editTextEmailId);
        editTextUserName = (EditText)findViewById(R.id.editTextUserName);
        mAuth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(editTextEmailId.getText().toString(),editTextLoginPassword.getText().toString());
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancel = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentCancel);
            }
        });

    }
    private void submitUser(){
        final String text1 = editTextUserName.getText().toString();
        final String text2 = editTextEmailId.getText().toString();
        final String text3 = editTextLoginPassword.getText().toString();

        if (TextUtils.isEmpty(text1)) {
            editTextEmailId.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(text2)) {
            editTextLoginPassword.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(text2)) {
            editTextUserName.setError("Required");
            return;
        }
        UserData userData = new UserData(text1,text2,text3);
        mDatabase.child("users").push().setValue(userData);

    }

    public void createUser(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"가입 실패!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "가입 성공!", Toast.LENGTH_SHORT).show();  //이메일 회원가입
                            submitUser();
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        // ...
                    }
                });
    }
}
