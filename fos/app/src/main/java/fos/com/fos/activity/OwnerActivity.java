package fos.com.fos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import fos.com.fos.R;
import fos.com.fos.adapter.OwnerAdapter;
import fos.com.fos.misc.Constants;
import java.util.ArrayList;
import java.util.List;

public class OwnerActivity extends AppCompatActivity {

  private ListView lvItem;
  private ArrayList<ParseObject> orderList = new ArrayList<>();
  private OwnerAdapter ownerAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_owner);
    lvItem = (ListView) findViewById(R.id.lv_item);
    ownerAdapter = new OwnerAdapter(OwnerActivity.this, orderList);
    lvItem.setAdapter(ownerAdapter);

    setTitle(ParseUser.getCurrentUser().getUsername() + " - your order(s)");
    loadOrders();
  }

  private void loadOrders() {
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.show();
    ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Order");
    parseQuery.whereEqualTo("ownerId", ParseUser.getCurrentUser());
    parseQuery.include("itemId");
    parseQuery.orderByDescending("updatedAt");
    parseQuery.findInBackground(new FindCallback<ParseObject>() {
      @Override public void done(List<ParseObject> list, ParseException e) {
        if (e == null) {
          orderList.clear();
          orderList.addAll(list);
          ownerAdapter.notifyDataSetChanged();
          progressDialog.dismiss();
        } else {
          Toast.makeText(OwnerActivity.this, Constants.unexpected_error, Toast.LENGTH_LONG).show();
        }
      }
    });
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
      case R.id.action_refresh:
        loadOrders();
        break;
    }

    return super.onOptionsItemSelected(item);
  }
}
