package test.qr.generator.qrreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import test.qr.generator.qrreader.R;

public class MainActivity extends AppCompatActivity {

    Button btnCreate, btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setBtnCreate();
        setBtnScan();


    }


    private void initView() {
        btnCreate = findViewById(R.id.btn_create);
        btnScan = findViewById(R.id.btn_scan);
    }

    private void setBtnCreate() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setBtnScan() {
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanerActivity.class);
                startActivity(intent);
            }
        });
    }
}
