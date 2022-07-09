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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import test.qr.generator.qrreader.R;


public class FacebookFragment extends BaseFragmentView implements TextWatcher {

    RadioGroup radioGroup;
    RadioButton rbFacebookUri, rbFacebookShare;
    EditText etFacebookUri;
    Button btnCreate, btnDownload;

    ImageView ivQR;


    public static FacebookFragment newInstance() {
        return new FacebookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook, container, false);
        setView(view);

        rbFacebookUri = view.findViewById(R.id.rb_facebook_uri);
        rbFacebookShare = view.findViewById(R.id.rb_facebook_share_uri);

        etFacebookUri = view.findViewById(R.id.et_facebook_uri);
        etFacebookUri.addTextChangedListener(this);
        btnCreate = view.findViewById(R.id.btn_create);
        ivQR = view.findViewById(R.id.iv_qr);


        if (rbFacebookUri.isChecked()) {
            etFacebookUri.setHint("Your Facebook URL");
        } else if (rbFacebookShare.isChecked()) {
            etFacebookUri.setHint("Share your URL on Facebook");
        }

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_facebook_uri:
                        etFacebookUri.setHint("Your Facebook URL");
                        break;
                    case R.id.rb_facebook_share_uri:
                        etFacebookUri.setHint("Share your URL on Facebook");
                        break;

                }

                isCreated = false;
            }
        });


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review()) {
                    createQr(etFacebookUri.getText().toString(), IMAGE_CREATED);
                }

            }
        });

        btnDownload = view.findViewById(R.id.btn_download_qr);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review()) {
                    if (!isCreated)
                        createQr(etFacebookUri.getText().toString(), IMAGE_CREATED_AND_SAVED);
                    else if (isCreated) {
                        createQr(etFacebookUri.getText().toString(), IMAGE_SAVED);
                    }
                }
            }
        });
        return view;
    }

    private boolean review() {
        String url = etFacebookUri.getText().toString();
        if (url.isEmpty()) {
            etFacebookUri.setError("facbook uri empty");
            return false;
        } else if (url.startsWith("https://www.facebook.com/") ||
                url.startsWith("https://m.facebook.com/") ||
                url.contains("www.facebook.com") ||
                url.contains("")) {
            return true;
        }
        etFacebookUri.setError("is not facebook uri");
        return false;
    }

    private void createQr(String uri, int tag) {
        String url = uri.trim();
        if (rbFacebookUri.isChecked()) {
            qrCodeResult = uri;
            createQrCode(qrCodeResult, tag);

        } else if (rbFacebookShare.isChecked()) {
            String parseUri = getFacebookParseUri(url);
            qrCodeResult = parseUri;
            createQrCode(qrCodeResult, tag);
        }
    }

    private String getFacebookParseUri(String url) {
        int indexSlash = url.indexOf('/', 9);
        String basUri = url.substring(0, indexSlash + 1);
        String str = "sharer/sharer.php?u=https%3A%2F%2Fwww.facebook.com%2F";

        int indexToo = url.lastIndexOf('2');
        String share = url.substring(indexToo + 1);

        return basUri + str + share;
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
