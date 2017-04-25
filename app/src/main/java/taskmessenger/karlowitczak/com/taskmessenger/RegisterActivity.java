package taskmessenger.karlowitczak.com.taskmessenger;

import android.app.ProgressDialog;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton;
    private EditText passwordText, emailText, passwordText2;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    public String email, password, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        registerButton = (Button) findViewById(R.id.registerButton);
        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.pwd);
        passwordText2 = (EditText) findViewById(R.id.pwd2);

        registerButton.setOnClickListener(this);
    }

    private void registerUser() {
        email = emailText.getText().toString().trim();
        password = passwordText.getText().toString().trim();
        password2 = passwordText2.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please, enter the email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please, enter the password", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password2)){
            Toast.makeText(this, "Please, repeat the password", Toast.LENGTH_LONG).show();
            return;
        }

        if(!TextUtils.equals(password, password2)){
            Toast.makeText(this, "Repeated password is not the same", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                        if (task.isSuccessful()) {
                            finish();
                            intent.putExtra("userName", email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Could not register. Please try again", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.hide();
                    }
                });
    }


    @Override
    public void onClick(View view) {
        if (view == registerButton) {
            registerUser();
        }
    }
}
