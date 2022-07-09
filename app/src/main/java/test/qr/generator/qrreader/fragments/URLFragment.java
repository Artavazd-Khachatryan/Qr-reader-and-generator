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


public class URLFragment extends BaseFragmentView implements View.OnClickListener, TextWatcher {


    EditText etUri;
    Button btnCreate, btnDownload;

    ImageView ivQR;

    public URLFragment() {

    }


    public static URLFragment newInstance() {
        return new URLFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_url, container, false);
        setView(view);

        etUri = view.findViewById(R.id.et_url);
        etUri.addTextChangedListener(this);

        ivQR = view.findViewById(R.id.iv_qr);
        btnCreate = view.findViewById(R.id.btn_create);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUri(etUri.getText().toString())){
                    createQR(etUri.getText().toString(),IMAGE_CREATED);
                }
            }
        });

        btnDownload =view.findViewById(R.id.btn_download_qr);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUri(etUri.getText().toString())){
                    if (!isCreated)
                        createQR(etUri.getText().toString(),IMAGE_CREATED_AND_SAVED);
                    else if (isCreated){
                        createQR(etUri.getText().toString(),IMAGE_SAVED);
                    }

                }
            }
        });

        setListeners();
        return view;
    }

    private void setListeners() {
        btnEnterContent.setOnClickListener(this);
    }

    private void createQR(String uri, int tag) {
        String result = uri.trim();
        if (!result.startsWith("https//") || !result.startsWith("http//")){
            result = "https://" + result;

        }
        qrCodeResult = result;
        createQrCode(qrCodeResult, tag);

    }

    private boolean isUri(String scanResult) {
        Matcher m = Patterns.WEB_URL.matcher(scanResult);
        if (m.find() && m.group().length() == scanResult.length()){
            return true;
        }
        etUri.setError("uri is incorect");
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
