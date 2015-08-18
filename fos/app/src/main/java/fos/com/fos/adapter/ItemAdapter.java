package fos.com.fos.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import fos.com.fos.R;
import fos.com.fos.misc.Constants;
import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
  private Context context;
  private ArrayList<ParseObject> itemList;
  private String ownerId;

  public ItemAdapter(Context context, ArrayList<ParseObject> itemList, String ownerId) {
    super();
    this.context = context;
    this.itemList = itemList;
    this.ownerId = ownerId;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final ParseObject itemObject = (ParseObject) this.getItem(position);
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
      holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
      holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
      holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
      holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
      holder.btnOrder = (Button) convertView.findViewById(R.id.btn_order);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.tvName.setText(itemObject.getString("name"));
    holder.tvPrice.setText("Price: " + itemObject.get("price").toString() + " A.E.D");
    holder.tvTime.setText("Estimated Time: " + itemObject.get("duration").toString() + " mins");

    int qty = itemObject.getInt("quantity");
    if (qty == 0) {
      holder.btnOrder.setText("Sold out");
    } else {
      holder.btnOrder.setText("ORDER NOW");
      holder.btnOrder.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
          alertDialog.setTitle("Confirm order!");
          alertDialog.setMessage("Proceed order?");
          alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              confirmOrder(itemObject.getObjectId(), itemObject.getString("name"));
            }
          });
          alertDialog.setNegativeButton("No", null);
          alertDialog.create().show();
        }
      });
    }
    Picasso.with(context)
        .load(itemObject.getString("image"))
        .resize(250, 250)
        .centerCrop()
        .into(holder.ivImage);
    return convertView;
  }

  private void confirmOrder(String itemId, String itemName) {
    ParseObject order = new ParseObject("Order");
    order.put("userId", ParseUser.getCurrentUser());
    order.put("itemId", ParseObject.createWithoutData("Item", itemId));
    order.put("ownerId", ParseObject.createWithoutData("_User", ownerId));
    order.put("status", "Notified");
    order.saveInBackground();

    ParsePush push = new ParsePush();
    push.setChannel(Constants.channel_prefix + ownerId);
    push.setMessage("Ordered " + itemName);
    push.sendInBackground();

    Toast.makeText(context, "Your order has been notified", Toast.LENGTH_SHORT).show();
  }

  @Override public Object getItem(int position) {
    return itemList.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getCount() {
    return itemList.size();
  }

  class ViewHolder {
    ImageView ivImage;
    TextView tvName, tvTime, tvPrice;
    Button btnOrder;
  }
}
