package test.qr.generator.qrreader.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.regex.Matcher;

import test.qr.generator.qrreader.R;


public class EmailFragment extends BaseFragmentView implements TextWatcher {
    EditText etEmail, etSubject, etMessage;
    Button btnCreate, btnDownload;
    ImageView ivQR;

    public static EmailFragment newInstance() {
        return new EmailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email, container, false);
        setView(view);
        etEmail = view.findViewById(R.id.et_email);
        etSubject = view.findViewById(R.id.et_subject);
        etMessage = view.findViewById(R.id.et_message);


        etEmail.addTextChangedListener(this);
        etSubject.addTextChangedListener(this);
        etMessage.addTextChangedListener(this);

        ivQR = view.findViewById(R.id.iv_qr);

        btnCreate = view.findViewById(R.id.btn_create);
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

    private boolean isEmail(String email) {
        Matcher m = Patterns.EMAIL_ADDRESS.matcher(email.trim());
        if (m.find() && m.group().length() == email.length()) {
            return true;
        }
        return false;
    }

    private boolean review() {
        boolean review = true;
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Is empty");
            review = false;
        }

        if (!isEmail(etEmail.getText().toString())) {
            etEmail.setError("do not email");
            review = false;
        }

        return review;
    }


    private void createQR(int tag) {
        String mail = "mailto:" + etEmail.getText().toString().trim() + "?";
        String subject = "subject=" + etSubject.getText().toString().trim();
        String message = "&body=" + etMessage.getText().toString().trim();

        qrCodeResult = mail + subject + message;
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
