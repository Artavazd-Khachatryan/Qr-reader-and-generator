package test.qr.generator.qrreader.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.Matcher;

import test.qr.generator.qrreader.R;


public class SMSFragment extends BaseFragmentView implements TextWatcher {

    EditText etPhone, etMessage;
    ImageView ivQR;
    Button btnCreate, btnDownload;


    public static SMSFragment newInstance() {

        return new SMSFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms, container, false);
        setView(view);

        etPhone = view.findViewById(R.id.et_phone);
        etMessage = view.findViewById(R.id.et_message);
        etPhone.addTextChangedListener(this);
        etMessage.addTextChangedListener(this);

        btnCreate = view.findViewById(R.id.btn_create);
        ivQR = view.findViewById(R.id.iv_qr);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review()) {
                    createQR(IMAGE_CREATED);
                }
            }
        });


        btnDownload = view.findViewById(R.id.btn_download_qr);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review()) {
                    if (!isCreated)
                        createQR(IMAGE_CREATED_AND_SAVED);
                    else if (isCreated) {
                        createQR(IMAGE_SAVED);
                    }
                }
            }
        });
        return view;
    }

    private void createQR(int tag) {
        String phone = "SMSTO:" + etPhone.getText().toString().trim() + ":";
        String message = etMessage.getText().toString().trim();

        qrCodeResult = phone + message;
        createQrCode(qrCodeResult, tag);


    }

    private boolean review() {
        if (etPhone.getText().toString().trim().isEmpty()) {
            etPhone.setError("Phone number is empty");
            return false;
        }
        if (etMessage.getText().toString().trim().isEmpty()) {
            etMessage.setError("message is empty");
        } else {
            String phone = etPhone.getText().toString();
            Matcher m = Patterns.PHONE.matcher(phone.trim());
            if (m.find() && m.group().length() == phone.length()) {
                return true;
            }
            return false;
        }

        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isCreated = false;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
