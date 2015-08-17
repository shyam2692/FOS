package fos.com.fos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import fos.com.fos.R;
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

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_signup, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
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
            Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SignupActivity.this, MainCategoryActivity.class));
          } else {
            Toast.makeText(SignupActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
