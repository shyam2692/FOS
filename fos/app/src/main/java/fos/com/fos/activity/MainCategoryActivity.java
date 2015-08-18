package fos.com.fos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import fos.com.fos.R;
import fos.com.fos.adapter.MainCategoryAdapter;
import fos.com.fos.misc.Constants;
import java.util.ArrayList;
import java.util.List;

public class MainCategoryActivity extends AppCompatActivity {

  private ListView listView;
  private MainCategoryAdapter mainCategoryAdapter;
  private ArrayList<ParseObject> mainCatList = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_category);
    listView = (ListView) findViewById(R.id.lv_main_category);
    loadCategory();
  }

  private void loadCategory() {
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.show();
    ParseQuery<ParseObject> query = ParseQuery.getQuery("MainCategory");
    query.orderByAscending("createdAt");
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override public void done(List<ParseObject> list, ParseException e) {
        progressDialog.dismiss();
        if (e == null) {
          mainCatList.addAll(list);
          mainCategoryAdapter = new MainCategoryAdapter(MainCategoryActivity.this, mainCatList);
          listView.setAdapter(mainCategoryAdapter);
          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Intent intent = new Intent(MainCategoryActivity.this, ItemActivity.class);
              intent.putExtra(Constants.main_cat_key, mainCatList.get(position).getObjectId());
              intent.putExtra(Constants.main_cat_name, mainCatList.get(position).getString("name"));
              intent.putExtra(Constants.owner_id,
                  mainCatList.get(position).getParseObject("ownerId").getObjectId());
              startActivity(intent);
            }
          });
        } else {
          Toast.makeText(MainCategoryActivity.this, Constants.unexpected_error, Toast.LENGTH_LONG)
              .show();
        }
      }
    });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main_category, menu);
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
      case R.id.action_my_profile:
        startActivity(new Intent(this, ProfileActivity.class));
        break;
    }

    return super.onOptionsItemSelected(item);
  }
}
