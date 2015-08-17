package fos.com.fos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import fos.com.fos.R;
import fos.com.fos.misc.Validation;

public class LoginActivity extends AppCompatActivity {

  EditText edtUser, edtPassword;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    edtUser = (EditText) findViewById(R.id.edtUserName);
    edtPassword = (EditText) findViewById(R.id.edtPassword);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_login, menu);
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

  public void performLogin(View v) {

    if (isValid()) {
      ParseUser.logInInBackground(edtUser.getText().toString(), edtPassword.getText().toString(),
          new LogInCallback() {
            @Override public void done(ParseUser parseUser, ParseException e) {
              if (e == null) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, MainCategoryActivity.class));
              } else {
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
