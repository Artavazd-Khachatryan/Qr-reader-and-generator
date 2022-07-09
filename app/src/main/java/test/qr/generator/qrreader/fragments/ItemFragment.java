package test.qr.generator.qrreader.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import test.qr.generator.qrreader.R;

@SuppressLint("ValidFragment")
public class ItemFragment extends BaseFragmentView {

    TextView tv_title;
    String title;

    public ItemFragment(String title){
        this.title = title;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment,container,false);

        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(title);
        return view;
    }
}
