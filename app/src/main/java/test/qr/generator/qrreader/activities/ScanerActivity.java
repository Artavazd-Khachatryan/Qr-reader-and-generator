package test.qr.generator.qrreader.activities;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;

import com.google.zxing.Result;

import java.util.regex.Matcher;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import test.qr.generator.qrreader.R;
import test.qr.generator.qrreader.fragments.ResultDialog;

import static android.Manifest.permission.CAMERA;


public class ScanerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    public static String barcode_format = "";

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission()){

            }
            else {
                requestPermission();
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission()){
                if (scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                    scannerView.resumeCameraPreview(ScanerActivity.this);
                }

                scannerView.setResultHandler(this);
                scannerView.startCamera();

            }
            else {
                requestPermission();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.startCamera();
    }


    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(ScanerActivity.this,CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA},REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_CAMERA:
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){

                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            finish();

                        }
                    }
                }
                break;
        }
    }


    @Override
    public void handleResult(final Result result) {

        final String scanResult = result.getText();

        barcode_format = result.getBarcodeFormat().toString();

        if (isEvent(scanResult)){
            showResultDialog(scanResult,"event");
        }
        else if (isWIFI(scanResult)){
            showWifiDialog(scanResult,"wifi");
        }
        else if (isSms(scanResult)){
            showSmsDialog(scanResult);
        }
        else if (isUri(scanResult)){
            showUriDialog(scanResult);
        }
        else if(isMail(scanResult)){
            showEmailDialog(scanResult);
        }
        else if(isPhone(scanResult)){
            showPhoneDialog(scanResult);
        }
        else if (isLocation(scanResult)){
            showUriDialog(scanResult);

        }
        else {
            showResultDialog(scanResult,"text");
        }


    }

    private boolean isLocation(String scanResult) {
        if (scanResult.startsWith("geo:")){
            return true;
        }
        return false;
    }

    private boolean isPhone(String scanResult) {
        if (scanResult.startsWith("tel:")){
            return true;
        }
        return false;
    }

    private void showUriDialog(final String scanResult) {
        showResultDialog(scanResult,"uri");
    }

    private void showEmailDialog(final String scanResult) {
        showResultDialog(scanResult,"mail");
    }

    private void showPhoneDialog(final String scanResult) {
        showResultDialog(scanResult,"phone");
    }


    private void showSmsDialog(final String scanResult) {
        showResultDialog(scanResult,"sms");
    }

    private void showWifiDialog(String scanResult,String tag) {
        showResultDialog(scanResult,tag);
    }

    private boolean isSms(String scanResult) {
        Log.d("ScanerActivity", scanResult);
        if (scanResult.startsWith("SMSTO:")){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isWIFI(String scanResult) {
        boolean isWiFi = true;
        if (!scanResult.startsWith("WIFI")){
            isWiFi = false;
        }
        if (!scanResult.startsWith(":S:",4))
            isWiFi = false;

        return isWiFi;
    }

    private boolean isEvent(String result) {
        boolean isEvent = result.contains("BEGIN") &&
                result.contains("SUMMARY") &&
                result.contains("LOCATION") &&
                result.contains("DTSTART") &&
                result.contains("DTEND") &&
                result.contains("END");

        return isEvent;

    }

    private boolean isMail(String scanResult) {
        return scanResult.startsWith("mailto");
    }

    private boolean isUri(String scanResult) {
        Matcher m = Patterns.WEB_URL.matcher(scanResult);
        if (m.find() && m.group().length() == scanResult.length()){
            return true;
        }
        return false;
    }

    private void showResultDialog(String result, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        ResultDialog resultDialog = ResultDialog.newInstance(result,tag);
        resultDialog.show(fm, "result_dialog");

        resultDialog.setCancelListener(new ResultDialog.FragmentCancelListener() {
            @Override
            public void cancel() {
                scannerView.resumeCameraPreview(ScanerActivity.this);
            }
        });
    }

}
