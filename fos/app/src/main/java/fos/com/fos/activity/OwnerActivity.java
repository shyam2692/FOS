package fos.com.fos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.parse.ParsePush;
import com.parse.ParseUser;
import fos.com.fos.R;
import fos.com.fos.misc.Constants;

public class OwnerActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_owner);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_owner, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_logout:
        ParsePush.unsubscribeInBackground(
            Constants.channel_prefix + ParseUser.getCurrentUser().getObjectId());
        ParseUser.logOut();

        Intent iMain = new Intent(this, MainActivity.class);
        iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(iMain);
        break;
    }

    return super.onOptionsItemSelected(item);
  }
}
