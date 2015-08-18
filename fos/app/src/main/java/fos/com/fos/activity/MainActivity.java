package fos.com.fos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.parse.ParsePush;
import com.parse.ParseUser;
import fos.com.fos.R;
import fos.com.fos.misc.Constants;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (ParseUser.getCurrentUser() != null) {
      finish();
      ParsePush.subscribeInBackground(
          Constants.channel_prefix + ParseUser.getCurrentUser().getObjectId());
      if (ParseUser.getCurrentUser().getBoolean("isOwner")) {
        startActivity(new Intent(this, OwnerActivity.class));
      } else {
        startActivity(new Intent(this, MainCategoryActivity.class));
      }
    }
  }

  public void startLogin(View v) {
    startActivity(new Intent(this, LoginActivity.class));
  }

  public void startSignup(View v) {
    startActivity(new Intent(this, SignupActivity.class));
  }
}
