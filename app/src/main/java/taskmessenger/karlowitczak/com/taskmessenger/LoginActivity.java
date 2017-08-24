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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton, loginButton;
    private EditText emailText, passwordText;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    public String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);
        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.pwd);

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    private void userLogin() {
        userEmail = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, getString(R.string.enterEmailToast), Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getString(R.string.enterPasswordToast), Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage(getString(R.string.loggingUser));
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                        if (task.isSuccessful()) {
                            finish();
                            intent.putExtra("userName", userEmail);
                            startActivity(intent);
                            UserPreferences.setLogin(getApplicationContext(),userEmail);
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == registerButton) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        }

        if (view == loginButton) {
            userLogin();
        }
    }
}
