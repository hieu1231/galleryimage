package hieusenpaj.com.task6.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hieusenpaj.com.task6.object.Model_images;
import hieusenpaj.com.task6.R;

public class GirdViewFolderAdapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<Model_images> arrayList;
    public GirdViewFolderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Model_images> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        ImageView imageView = convertView.findViewById(R.id.iv_lv);
        TextView textView = convertView.findViewById(R.id.tv_lv);
        Glide.with(context)
                .load(arrayList.get(position).getAl_imagepath().get(0))
                .centerCrop()
                .into(imageView);
       textView.setText(arrayList.get(position).getStr_folder());
        return convertView;
    }
}
