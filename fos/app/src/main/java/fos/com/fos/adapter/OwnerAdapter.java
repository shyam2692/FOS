package fos.com.fos.adapter;

import android.content.Context;
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

public class OwnerAdapter extends BaseAdapter {
  private Context context;
  private ArrayList<ParseObject> orderList;

  public OwnerAdapter(Context context, ArrayList<ParseObject> orderList) {
    super();
    this.context = context;
    this.orderList = orderList;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final ParseObject orderObject = (ParseObject) this.getItem(position);
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = LayoutInflater.from(context).inflate(R.layout.owner_row, parent, false);
      holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
      holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
      holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
      holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
      holder.btnStatus = (Button) convertView.findViewById(R.id.btn_order);
      holder.btnReady = (Button) convertView.findViewById(R.id.btn_ready);
      holder.btnDelivered = (Button) convertView.findViewById(R.id.btn_delivered);
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
    holder.btnStatus.setText(orderObject.getString("status"));
    if (orderObject.getString("status").equals("Notified")) {
      holder.btnReady.setEnabled(true);
      holder.btnReady.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          orderObject.put("status", "Ready");
          orderObject.saveInBackground();
          notifyDataSetChanged();
          sendReadyNotification(orderObject.getParseObject("userId").getObjectId());
          Toast.makeText(context, "Notified successfully!", Toast.LENGTH_SHORT).show();
        }
      });
    } else {
      holder.btnReady.setEnabled(false);
    }
    holder.btnDelivered.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        orderObject.put("status", "Delivered");
        orderObject.saveInBackground();
        notifyDataSetChanged();
      }
    });
    Picasso.with(context)
        .load(orderObject.getParseObject("itemId").getString("image"))
        .resize(250, 250)
        .centerCrop()
        .into(holder.ivImage);
    return convertView;
  }

  private void sendReadyNotification(String userId) {
    ParsePush push = new ParsePush();
    push.setChannel(Constants.channel_prefix + userId);
    push.setMessage("Your order is ready!");
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
    Button btnStatus, btnReady, btnDelivered;
  }
}
