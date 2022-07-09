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


public class YoutubeFragment extends BaseFragmentView implements TextWatcher {

    EditText etYoutubeUri;
    Button btnCreate, btnDownload;

    ImageView ivQR;

    public YoutubeFragment() {

    }


    public static YoutubeFragment newInstance() {
        return new YoutubeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);
        setView(view);
        etYoutubeUri = view.findViewById(R.id.et_youtube_uri);
        etYoutubeUri.addTextChangedListener(this);

        btnCreate = view.findViewById(R.id.btn_create);
        ivQR = view.findViewById(R.id.iv_qr);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQR(etYoutubeUri.getText().toString(), IMAGE_CREATED);
            }
        });


        btnDownload = view.findViewById(R.id.btn_download_qr);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCreated)
                    createQR(etYoutubeUri.getText().toString(), IMAGE_CREATED_AND_SAVED);
                else if (isCreated) {
                    createQR(etYoutubeUri.getText().toString(), IMAGE_SAVED);
                }

            }
        });
        return view;
    }

    private void createQR(String uri, int tag) {
        if (uri.trim().isEmpty()) {
            etYoutubeUri.setError("uri is empty");
        } else if (uri.startsWith("https://www.youtube.com/") ||
                uri.startsWith("https://m.youtube.com/") ||
                uri.contains("www.youtube.com") ||
                uri.contains("m.youtube.com")) {


            qrCodeResult = uri;
            createQrCode(qrCodeResult, tag);

        } else {
            etYoutubeUri.setError("is not youtube uri");
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
