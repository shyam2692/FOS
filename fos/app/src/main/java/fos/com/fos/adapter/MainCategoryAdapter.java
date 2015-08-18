package fos.com.fos.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import fos.com.fos.R;
import fos.com.fos.misc.Constants;
import java.util.ArrayList;

public class MainCategoryAdapter extends BaseAdapter {
  private Context context;
  ArrayList<ParseObject> catList;

  public MainCategoryAdapter(Context context, ArrayList<ParseObject> catList) {
    super();
    this.context = context;
    this.catList = catList;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ParseObject categoryObject = (ParseObject) this.getItem(position);
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = LayoutInflater.from(context).inflate(R.layout.main_category_row, parent, false);
      holder.tvCatName = (TextView) convertView.findViewById(R.id.tv_cat_name);
      holder.ivCatImg = (ImageView) convertView.findViewById(R.id.iv_cat_img);
      holder.pbDownloadingImage = (ProgressBar) convertView.findViewById(R.id.pb_downloading_image);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.tvCatName.setText(categoryObject.getString("name"));
    Picasso.with(context)
        .load(categoryObject.getString("image"))
        .resize(Constants.main_cat_img_width, Constants.main_cat_img_height)
        .centerCrop()
        .into(holder.target);
    return convertView;
  }

  @Override public Object getItem(int position) {
    return catList.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getCount() {
    return catList.size();
  }

  class ViewHolder {
    ImageView ivCatImg;
    TextView tvCatName;
    ProgressBar pbDownloadingImage;

    private Target target = new Target() {
      @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        ivCatImg.setImageBitmap(bitmap);
        pbDownloadingImage.setVisibility(View.INVISIBLE);
      }

      @Override public void onBitmapFailed(Drawable errorDrawable) {
        ivCatImg.setImageResource(R.drawable.placeholder);
      }

      @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
        pbDownloadingImage.setVisibility(View.VISIBLE);
      }
    };
  }
}
