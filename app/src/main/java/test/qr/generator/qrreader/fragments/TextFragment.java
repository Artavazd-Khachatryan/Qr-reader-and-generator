package test.qr.generator.qrreader.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import test.qr.generator.qrreader.R;


public class TextFragment extends BaseFragmentView implements View.OnClickListener, TextWatcher {
    EditText etText;
    Button btnCreate, btnDownload;

    ImageView ivQr;


    public static TextFragment newInstance() {
        return new TextFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);

        setView(view);
        etText = view.findViewById(R.id.et_text);
        etText.addTextChangedListener(this);
        btnCreate = view.findViewById(R.id.btn_create);
        ivQr = view.findViewById(R.id.iv_qr);


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrCodeResult = etText.getText().toString();
                createQR(qrCodeResult, IMAGE_CREATED);
            }
        });


        btnDownload = view.findViewById(R.id.btn_download_qr);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCreated)
                    createQR(etText.getText().toString(), IMAGE_CREATED_AND_SAVED);
                else if (isCreated)
                    createQR(etText.getText().toString(), IMAGE_SAVED);
            }
        });


        return view;
    }

    private void createQR(String uri, int tag) {
        String result = uri.trim();
        if (result.isEmpty()) {
            etText.setError("Text is empty");
        } else {
            qrCodeResult = result;
            createQrCode(qrCodeResult, tag);
        }

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
