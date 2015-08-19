package fos.com.fos.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import fos.com.fos.R;
import fos.com.fos.adapter.ProfileAdapter;
import fos.com.fos.misc.Constants;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

  private ListView lvItem;
  private ArrayList<ParseObject> orderList = new ArrayList<>();
  private ProfileAdapter profileAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    lvItem = (ListView) findViewById(R.id.lv_item);

    setTitle(ParseUser.getCurrentUser().getUsername() + " - your order(s)");
    ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Order");
    parseQuery.whereEqualTo("userId", ParseUser.getCurrentUser());
    parseQuery.include("itemId");
    parseQuery.orderByDescending("updatedAt");
    parseQuery.findInBackground(new FindCallback<ParseObject>() {
      @Override public void done(List<ParseObject> list, ParseException e) {
        if (e == null) {
          orderList.addAll(list);
          profileAdapter = new ProfileAdapter(ProfileActivity.this, orderList);
          lvItem.setAdapter(profileAdapter);
        } else {
          Toast.makeText(ProfileActivity.this, Constants.unexpected_error, Toast.LENGTH_LONG)
              .show();
        }
      }
    });
  }
}
