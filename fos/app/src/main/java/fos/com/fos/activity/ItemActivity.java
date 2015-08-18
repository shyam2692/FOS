package fos.com.fos.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import fos.com.fos.R;
import fos.com.fos.adapter.ItemAdapter;
import fos.com.fos.misc.Constants;
import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity {

  private String mainCategoryId, mainCategoryName, ownerId;
  private ListView lvItem;
  private ArrayList<ParseObject> itemList = new ArrayList<>();
  private ItemAdapter itemAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item);
    lvItem = (ListView) findViewById(R.id.lv_item);

    if (getIntent() != null) {
      mainCategoryId = getIntent().getStringExtra(Constants.main_cat_key);
      mainCategoryName = getIntent().getStringExtra(Constants.main_cat_name);
      ownerId = getIntent().getStringExtra(Constants.owner_id);
      setTitle(mainCategoryName);
      ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Item");
      parseQuery.whereEqualTo("categoryId",
          ParseObject.createWithoutData("MainCategory", mainCategoryId));
      parseQuery.findInBackground(new FindCallback<ParseObject>() {
        @Override public void done(List<ParseObject> list, ParseException e) {
          if (e == null) {
            itemList.addAll(list);
            itemAdapter = new ItemAdapter(ItemActivity.this, itemList, ownerId);
            lvItem.setAdapter(itemAdapter);
          } else {
            Toast.makeText(ItemActivity.this, Constants.unexpected_error, Toast.LENGTH_LONG).show();
          }
        }
      });
    }
  }
}
