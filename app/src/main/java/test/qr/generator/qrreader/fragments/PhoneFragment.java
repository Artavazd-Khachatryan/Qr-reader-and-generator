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


public class PhoneFragment extends BaseFragmentView implements TextWatcher {

    EditText etPhone;
    Button btnCreate, btnDownload;

    ImageView ivQR;

    public static PhoneFragment newInstance() {
        return new PhoneFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        setView(view);
        etPhone = view.findViewById(R.id.et_phone);
        etPhone.addTextChangedListener(this);

        btnCreate = view.findViewById(R.id.btn_create);
        ivQR = view.findViewById(R.id.iv_qr);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review()) {
                    createQR(etPhone.getText().toString(), IMAGE_CREATED);
                }

            }
        });

        btnDownload = view.findViewById(R.id.btn_download_qr);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review()) {
                    if (!isCreated)
                        createQR(etPhone.getText().toString(), IMAGE_CREATED_AND_SAVED);
                    else if (isCreated) {
                        createQR(etPhone.getText().toString(), IMAGE_SAVED);
                    }

                }
            }
        });
        return view;
    }

    private boolean review() {
        if (etPhone.getText().toString().trim().isEmpty()) {
            etPhone.setError("Phone number is empty");
            return false;
        } else {
            String phone = etPhone.getText().toString();
            Matcher m = Patterns.PHONE.matcher(phone.trim());
            if (m.find() && m.group().length() == phone.length()) {
                return true;
            }
            etPhone.setError("Incorrect phone number");
            return false;
        }
    }

    private void createQR(String s, int tag) {
        String result = "tel:" + etPhone.getText().toString().trim();
        qrCodeResult = result;
        createQrCode(qrCodeResult, tag);
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



