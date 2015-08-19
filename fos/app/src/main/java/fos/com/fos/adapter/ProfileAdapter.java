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
import com.squareup.picasso.Picasso;
import fos.com.fos.R;
import fos.com.fos.misc.Constants;
import java.util.ArrayList;

public class ProfileAdapter extends BaseAdapter {
  private Context context;
  private ArrayList<ParseObject> orderList;

  public ProfileAdapter(Context context, ArrayList<ParseObject> orderList) {
    super();
    this.context = context;
    this.orderList = orderList;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final ParseObject orderObject = (ParseObject) this.getItem(position);
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = LayoutInflater.from(context).inflate(R.layout.profile_row, parent, false);
      holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
      holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
      holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
      holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
      holder.btnStatus = (Button) convertView.findViewById(R.id.btn_order);
      holder.btnCancel = (Button) convertView.findViewById(R.id.btn_cancel);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.tvName.setText(orderObject.getParseObject("itemId").getString("name"));
    holder.tvPrice.setText(
        "Price: " + orderObject.getParseObject("itemId").get("price").toString() + " A.E.D");
    holder.tvTime.setText(
        "Estimated Time: " + orderObject.getParseObject("itemId").get("duration").toString()
            + " mins");

    String orderStatus = orderObject.getString("status");
    holder.btnStatus.setText(orderStatus);

    if (orderStatus.equals("Notified")) {
      holder.btnCancel.setVisibility(View.VISIBLE);
      holder.btnCancel.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
          alertDialog.setTitle("Cancel Order!");
          alertDialog.setMessage("Do you really want to cancel the order?");
          alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              orderObject.put("status", "Cancelled");
              orderObject.saveInBackground();
              notifyDataSetChanged();
              sendCancelNotification(orderObject.getParseObject("itemId").getString("name"),
                  orderObject.getParseObject("userId").getObjectId());
              Toast.makeText(context, "Your order cancellation has been successfully notified!",
                  Toast.LENGTH_SHORT).show();
            }
          });
          alertDialog.setNegativeButton("No", null);
          alertDialog.create().show();
        }
      });
    } else {
      holder.btnCancel.setVisibility(View.GONE);
    }

    Picasso.with(context)
        .load(orderObject.getParseObject("itemId").getString("image"))
        .resize(250, 250)
        .centerCrop()
        .into(holder.ivImage);
    return convertView;
  }

  private void sendCancelNotification(String itemName, String userId) {
    ParsePush push = new ParsePush();
    push.setChannel(Constants.channel_prefix + userId);
    push.setMessage(itemName + " - item order has been cancelled!");
    push.sendInBackground();
  }

  @Override public Object getItem(int position) {
    return orderList.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getCount() {
    return orderList.size();
  }

  class ViewHolder {
    ImageView ivImage;
    TextView tvName, tvTime, tvPrice;
    Button btnStatus, btnCancel;
  }
}
