package fos.com.fos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import fos.com.fos.R;
import fos.com.fos.misc.Constants;
import fos.com.fos.misc.Validation;

public class SignupActivity extends AppCompatActivity {

  EditText edtUser, edtPassword, edtEmail;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    initializeUI();
  }

  private void initializeUI() {
    edtUser = (EditText) findViewById(R.id.edtUserName);
    edtPassword = (EditText) findViewById(R.id.edtPassword);
    edtEmail = (EditText) findViewById(R.id.edtEmail);
  }

  public void performSignup(View v) {
    if (isValid()) {
      ParseUser user = new ParseUser();
      user.setUsername(edtUser.getText().toString());
      user.setEmail(edtEmail.getText().toString());
      user.setPassword(edtPassword.getText().toString());
      user.signUpInBackground(new SignUpCallback() {
        @Override public void done(ParseException e) {
          if (e == null) {
            ParsePush.subscribeInBackground(
                Constants.channel_prefix + ParseUser.getCurrentUser().getObjectId());
            Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_LONG).show();
            ParseUser.getCurrentUser().put("isApproved", true);
            ParseUser.getCurrentUser().put("isOwner", false);
            ParseUser.getCurrentUser().saveInBackground();
            startActivity(new Intent(SignupActivity.this, MainCategoryActivity.class));
          } else {
            Toast.makeText(SignupActivity.this, Constants.unexpected_error, Toast.LENGTH_LONG)
                .show();
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
