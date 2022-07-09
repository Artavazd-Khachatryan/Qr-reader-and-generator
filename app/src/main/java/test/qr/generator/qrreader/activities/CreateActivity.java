package test.qr.generator.qrreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import test.qr.generator.qrreader.R;
import test.qr.generator.qrreader.factories.FragmentFactory;

public class CreateActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentPagerAdapter pagerAdapter;
    Toolbar toolbar;
    ImageView ivCreateToScanner;
    int fragmentPosition = 0;

    String[] titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ivCreateToScanner = findViewById(R.id.iv_create_to_scaner);
        ivCreateToScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, ScanerActivity.class);
                startActivity(intent);
            }
        });

        titleList = getResources().getStringArray(R.array.create_list_item);
        initViewPager();
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
            if (i == 0) {
                View view = tab.getCustomView();
                TextView tvTitle = view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(getColor(R.color.tabTextSelectedColor));
            }
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView tvTitle = view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(getColor(R.color.tabTextSelectedColor));

                fragmentPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView tvTitle = view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(getColor(R.color.tabTextDefaltColor));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private class FragmentPagerAdapter extends FragmentStatePagerAdapter {

        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            return FragmentFactory.getItemFragment(titleList[i]);
        }

        @Override
        public int getCount() {
            return titleList.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList[position];
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
            TextView tv = v.findViewById(R.id.tv_title);
            tv.setText(titleList[position]);
            return v;
        }
    }
}


