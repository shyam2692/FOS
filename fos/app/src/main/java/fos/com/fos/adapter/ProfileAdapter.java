package fos.com.fos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;
import fos.com.fos.R;
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
    ParseObject orderObject = (ParseObject) this.getItem(position);
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = LayoutInflater.from(context).inflate(R.layout.profile_row, parent, false);
      holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
      holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
      holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
      holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
      holder.btnStatus = (Button) convertView.findViewById(R.id.btn_order);
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
    Picasso.with(context)
        .load(orderObject.getParseObject("itemId").getString("image"))
        .resize(250, 250)
        .centerCrop()
        .into(holder.ivImage);
    return convertView;
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
    Button btnStatus;
  }
}
