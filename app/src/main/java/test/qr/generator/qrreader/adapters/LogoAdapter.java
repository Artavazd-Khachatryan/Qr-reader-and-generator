package test.qr.generator.qrreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import test.qr.generator.qrreader.R;

public class LogoAdapter extends BaseAdapter {

    List<Integer> logoList;

    public LogoAdapter(List logoList) {
        this.logoList = logoList;
    }

    @Override
    public int getCount() {
        return logoList.size();
    }

    @Override
    public Object getItem(int position) {
        return logoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_logo_list, parent, false);
        ImageView imageView = itemView.findViewById(R.id.iv_logo);
        //ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(context.getResources().getDrawable(logoList.get(position)));
        return imageView;
    }
}
