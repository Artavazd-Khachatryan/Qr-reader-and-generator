package test.qr.generator.qrreader.fragments;


import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;

import test.qr.generator.qrreader.R;
import test.qr.generator.qrreader.activities.MainActivity;
import test.qr.generator.qrreader.activities.ScanerActivity;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ResultDialog extends DialogFragment implements View.OnClickListener {

    Button btnScanAgain, btnVisit, btnCancel, btnCopy;
    TextView tvQrResult, tvBarcodeFormat;
    ProgressBar pbWiFiConnect;

    String result;
    String tag = " ";

    FragmentCancelListener cancelListener = new FragmentCancelListener() {
        @Override
        public void cancel() {

        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isNetworkAvailable()) {
                if (pbWiFiConnect != null) {
                    pbWiFiConnect.setVisibility(View.GONE);
                }
            }


        }
    };

    public ResultDialog(String result, String tag) {
        this.result = result;
        this.tag = tag;
    }

    public static ResultDialog newInstance(String result, String tag) {
        ResultDialog dialog = new ResultDialog(result, tag);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.result_layout, container, false);
        initView(view);


        showResult(tag);

        Log.d("ResultDialog", tag);

        return view;
    }

    private void showResult(String tag) {
        switch (tag) {
            case "text":
                showText();
                break;
            case "event":
                showEvent();
                break;
            case "wifi":
                showWifi();
                break;
            case "sms":
                showSms();
                break;
            case "mail":
            case "phone":
            case "uri":
                showUri();
                break;
        }
    }

    private void showUri() {
        tvQrResult.setText(result);
    }

    private void showSms() {
        //String smsResult = result.replace("SMSTO","smsto");
        final int index = result.lastIndexOf(":") + 1;
        final String message = result.substring(index);


        tvQrResult.setText("Phone " + result.substring(6, index - 1) + "\n" + "Message " + message);


    }

    private void initView(View view) {
        btnVisit = view.findViewById(R.id.btn_visit);
        if (tag.equals("wifi"))
            btnVisit.setText("Connect");
        if (tag.equals("sms") || tag.equals("mail"))
            btnVisit.setText("Send");
        if (tag.equals("phone"))
            btnVisit.setText("Call");


        btnCancel = view.findViewById(R.id.btn_cancel);
        btnScanAgain = view.findViewById(R.id.btn_scan_again);
        btnCopy = view.findViewById(R.id.btn_copy);
        btnCopy.setOnClickListener(this);
        tvQrResult = view.findViewById(R.id.tv_qr_result);
        tvBarcodeFormat = view.findViewById(R.id.tv_barcode_format);
        tvBarcodeFormat.setText(ScanerActivity.barcode_format);

        btnVisit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnScanAgain.setOnClickListener(this);

        pbWiFiConnect = view.findViewById(R.id.pb_wifi_connect);
        switch (tag) {
            case "event":
            case "text":
                btnVisit.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                intentToMainActivity();
                break;
            case R.id.btn_scan_again:
                cancelListener.cancel();
                dismiss();
                break;
            case R.id.btn_visit:
                visit();
                break;
            case R.id.btn_copy:
                copyResult();
                break;

        }

    }

    private void copyResult() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        String getstring = tvQrResult.getText().toString();
        ClipData clip = ClipData.newPlainText("label", getstring);
        clipboard.setPrimaryClip(clip);
    }

    private void visit() {
        switch (tag) {
            case "wifi":
                conect();
                break;
            case "sms":
                visitSms();
                break;

            case "mail":
            case "phone":
            case "uri":
                visitUri();
                break;
        }
    }

    private void visitUri() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
        startActivity(intent);
    }

    private void visitSms() {
        final int index = result.lastIndexOf(":") + 1;
        final String message = result.substring(index);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", result.substring(6, index - 1));
        intent.putExtra("sms_body", message);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);

    }

    public void setCancelListener(FragmentCancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    private void conect() {

        String wifiParsing = result.substring(5);

        StringTokenizer stringTokenizer = new StringTokenizer(wifiParsing, ";");
        String ssid = (stringTokenizer.hasMoreTokens()) ? stringTokenizer.nextToken() : "";
        String encryption = (stringTokenizer.hasMoreTokens()) ? stringTokenizer.nextToken() : "";
        String password = (stringTokenizer.hasMoreTokens()) ? stringTokenizer.nextToken() : "";

        int index = ssid.indexOf(":");
        ssid = ssid.substring(index + 1);

        index = encryption.indexOf(":");
        encryption = encryption.substring(index + 1);

        index = password.indexOf(":");
        password = password.substring(index + 1);

        pbWiFiConnect.setVisibility(View.VISIBLE);
        connectWiFi(encryption, ssid, password);

        Log.d("ResultDialog", ssid);
        Log.d("ResultDialog", encryption);
        Log.d("ResultDialog", password);
    }


    private void showWifi() {
        String wifiParsing = result.substring(5);

        StringTokenizer stringTokenizer = new StringTokenizer(wifiParsing, ";");
        String ssid = (stringTokenizer.hasMoreTokens()) ? stringTokenizer.nextToken() : "";
        String encryption = (stringTokenizer.hasMoreTokens()) ? stringTokenizer.nextToken() : "";
        String password = (stringTokenizer.hasMoreTokens()) ? stringTokenizer.nextToken() : "";


        int index = ssid.indexOf(":");
        ssid = ssid.substring(index + 1);

        index = encryption.indexOf(":");
        encryption = encryption.substring(index + 1);

        index = password.indexOf(":");
        password = password.substring(index + 1);


        String resultWifi = "SSID - " + ssid + "\n";
        resultWifi += "Enciption - " + encryption + "\n";
        resultWifi += "Password - " + password + "\n";
        tvQrResult.setText(resultWifi);
    }

    private void showEvent() {
        tvQrResult.setText(result);
    }

    private void showText() {
        tvQrResult.setText(result);
    }

    private void intentToMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        dismiss();
        Log.d("ResultDialog", "cance");
    }


    private void connectWiFi(String ApiTaype, String networkSSID, String networkPass) {
        try {

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.priority = 40;

            if (ApiTaype.equals("WEP")) {
                Log.v("rht", "Configuring WEP");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

                if (networkPass.matches("^[0-9a-fA-F]+$")) {
                    conf.wepKeys[0] = networkPass;
                } else {
                    conf.wepKeys[0] = "\"".concat(networkPass).concat("\"");
                }

                conf.wepTxKeyIndex = 0;

            } else if (ApiTaype.equals("WPA")) {
                Log.v("rht", "Configuring WPA");

                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                conf.preSharedKey = "\"" + networkPass + "\"";

            } else {
                Log.v("rht", "Configuring OPEN network");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.clear();
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }

            WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int networkId = wifiManager.addNetwork(conf);

            Log.v("rht", "Add result " + networkId);

            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    Log.v("rht", "WifiConfiguration SSID " + i.SSID);

                    boolean isDisconnected = wifiManager.disconnect();
                    Log.v("rht", "isDisconnected : " + isDisconnected);

                    boolean isEnabled = wifiManager.enableNetwork(i.networkId, true);
                    Log.v("rht", "isEnabled : " + isEnabled);

                    boolean isReconnected = wifiManager.reconnect();
                    Log.v("rht", "isReconnected : " + isReconnected);

                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        } else return false;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getContext().unregisterReceiver(broadcastReceiver);
        cancelListener.cancel();

    }

    public interface FragmentCancelListener {
        void cancel();
    }
}
