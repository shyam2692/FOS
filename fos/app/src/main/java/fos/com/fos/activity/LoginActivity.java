package fos.com.fos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import fos.com.fos.R;
import fos.com.fos.misc.Constants;
import fos.com.fos.misc.Validation;

public class LoginActivity extends AppCompatActivity {

  EditText edtUser, edtPassword;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    edtUser = (EditText) findViewById(R.id.edtUserName);
    edtPassword = (EditText) findViewById(R.id.edtPassword);
  }

  public void performLogin(View v) {

    if (isValid()) {
      ParseUser.logInInBackground(edtUser.getText().toString(), edtPassword.getText().toString(),
          new LogInCallback() {
            @Override public void done(ParseUser parseUser, ParseException e) {
              if (e == null) {
                if (ParseUser.getCurrentUser().getBoolean("isApproved")) {
                  ParsePush.subscribeInBackground(
                      Constants.channel_prefix + ParseUser.getCurrentUser().getObjectId());
                  Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                  if (ParseUser.getCurrentUser().getBoolean("isOwner")) {
                    startActivity(new Intent(LoginActivity.this, OwnerActivity.class));
                  } else {
                    startActivity(new Intent(LoginActivity.this, MainCategoryActivity.class));
                  }
                } else {
                  Toast.makeText(LoginActivity.this, "You're not approved yet", Toast.LENGTH_LONG)
                      .show();
                }
              } else {
                Toast.makeText(LoginActivity.this, "Invalid username or password",
                    Toast.LENGTH_LONG).show();
              }
            }
          });
    }
  }

  private boolean isValid() {
    boolean valid;
    valid = Validation.isValid(edtUser);
    valid = Validation.isValid(edtPassword);
    return valid;
  }
}
