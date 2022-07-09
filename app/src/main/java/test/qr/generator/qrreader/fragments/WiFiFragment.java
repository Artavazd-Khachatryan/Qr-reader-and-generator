package test.qr.generator.qrreader.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import test.qr.generator.qrreader.R;


public class WiFiFragment extends BaseFragmentView {

    EditText etWiFiSSID, etPassword;
    Spinner spEncryption;
    Button btnCreate, btnDanload;

    ImageView ivQr;
    String encription = "WEP";


    public WiFiFragment() {

    }


    public static WiFiFragment newInstance() {
        return new WiFiFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        setView(view);
        etWiFiSSID = view.findViewById(R.id.et_wifi_ssid);
        etPassword = view.findViewById(R.id.et_wifi_password);
        spEncryption = view.findViewById(R.id.sp_encryption);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.wifi_encryption, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spEncryption.setAdapter(adapter);

        spEncryption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                encription = getContext().getResources().getStringArray(R.array.wifi_encryption)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ivQr = view.findViewById(R.id.iv_qr);

        btnCreate = view.findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review()) {
                    createQr(IMAGE_CREATED);
                }
            }
        });


        btnDanload = view.findViewById(R.id.btn_download_qr);
        btnDanload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review()) {
                    if (!isCreated)
                        createQr(IMAGE_CREATED_AND_SAVED);
                    else if (isCreated) {
                        createQr(IMAGE_SAVED);
                    }

                }
            }
        });
        return view;
    }

    private boolean review() {
        boolean review = true;
        if (etWiFiSSID.getText().toString().isEmpty()) {
            etWiFiSSID.setError("fiwi ssdi is empty");
            review = false;
        }
        return review;
    }


    private void createQr(int tag) {
        String wifi_ssdi = "S:" + etWiFiSSID.getText().toString().trim() + ";";

        int index = encription.indexOf("/");
        if (index > 0)
            encription = encription.substring(0, index);
        String wifi_encription = "T:" + encription + ";";
        String wifi_password = "P:" + etPassword.getText().toString().trim();

        qrCodeResult = "WIFI:" + wifi_ssdi + wifi_encription + wifi_password + ";;";
        createQrCode(qrCodeResult, tag);
    }
}
