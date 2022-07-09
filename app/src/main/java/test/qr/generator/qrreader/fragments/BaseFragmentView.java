package test.qr.generator.qrreader.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import dmax.dialog.SpotsDialog;
import test.qr.generator.qrreader.R;
import test.qr.generator.qrreader.adapters.LogoAdapter;
import yuku.ambilwarna.AmbilWarnaDialog;

public class BaseFragmentView extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener {

    private TextView tvColorDialog, tvGradientDialog;


    private static final String IMAGE_DIRECTORY = "/QRCode";
    public static final int GET_FROM_GALLERY = 1001;
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1002;
    List<Integer> logoList = Arrays.asList(R.drawable.ezgif_com_gif_maker,
            R.drawable.google_plus,
            R.drawable.linkedin,
            R.drawable.pinterest,
            R.drawable.shere,
            R.drawable.soundcloud,
            R.drawable.tumblr,
            R.drawable.twitter,
            R.drawable.ezgif_gif,
            R.drawable.ezgif_marker,
            R.drawable.vk);

    View view;
    ViewGroup btnEnterContent, vgEnterContent;
    ViewGroup btnSetColors, vgSetColors, clSetColor;
    ViewGroup btnAddLogo, vgAddLogo, clAddLogoMain;
    ImageView ivArrowContent, ivArrowColor, ivArrowLogo, ivColorDialog, ivGradientDialog;
    Button btnDownloadQr;
    ImageView iv_qr;
    LinearLayout llColorGradient;
    RadioButton rbColorGradient;
    SeekBar sbSize;
    TextView tvQrSize;
    GridView gvLogoList;
    ImageView ivLogo;
    Button btnUploadImage, btnRemuveLogo;
    Handler handler;


    boolean isLogoRemuved = true;
    boolean isCreated = false;


    String qrCodeResult = "";

    public static final int IMAGE_CREATED_AND_SAVED = 101;
    public static final int IMAGE_CREATED = 102;
    public static final int IMAGE_SAVED = 103;
    int qrColor = Color.BLACK;
    int qrGradient = Color.BLACK;
    int bitmapSize = 800;

    final int color1 = 1;
    final int color2 = 2;


    public BaseFragmentView() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        onClick(btnEnterContent);
        return view;
    }

    public void setView(View view) {
        this.view = view;
        initViews();
        setListeners();


    }


    private void initViews() {
        btnEnterContent = view.findViewById(R.id.btn_enter_content);
        vgEnterContent = view.findViewById(R.id.ll_enter_content);
        ivArrowContent = view.findViewById(R.id.iv_arrow_content);

        btnSetColors = view.findViewById(R.id.btn_set_colors);
        vgSetColors = view.findViewById(R.id.ll_set_colors);
        clSetColor = view.findViewById(R.id.cl_set_color);
        ivArrowColor = view.findViewById(R.id.iv_arrow_color);

        btnAddLogo = view.findViewById(R.id.btn_add_logo);
        vgAddLogo = view.findViewById(R.id.vg_add_logo);
        clAddLogoMain = view.findViewById(R.id.cl_add_logo_main);
        ivArrowLogo = view.findViewById(R.id.iv_arrow_logo);

        btnDownloadQr = view.findViewById(R.id.btn_download_qr);
        iv_qr = view.findViewById(R.id.iv_qr);
        llColorGradient = view.findViewById(R.id.ll_color_gradient);
        rbColorGradient = view.findViewById(R.id.rb_color_gradient);

        ivColorDialog = view.findViewById(R.id.iv_color_dialog);
        ivGradientDialog = view.findViewById(R.id.iv_gradient_dialog);

        tvColorDialog = view.findViewById(R.id.tv_color_dialog);
        tvGradientDialog = view.findViewById(R.id.tv_gradient_dialog);

        sbSize = view.findViewById(R.id.sb_qr_size);
        sbSize.setProgress(bitmapSize);

        tvColorDialog.setText(String.format("#%06X", (0xFFFFFF & qrColor)));
        tvGradientDialog.setText(String.format("#%06X", (0xFFFFFF & qrGradient)));

        tvQrSize = view.findViewById(R.id.tv_qr_size);
        tvQrSize.setText("" + (sbSize.getProgress() + 200) + "x" + (sbSize.getProgress() + 200) + " PX");

        gvLogoList = view.findViewById(R.id.gv_logo_list);
        gvLogoList.setAdapter(new LogoAdapter(logoList));
        gvLogoList.setOnItemClickListener(this);

        ivLogo = view.findViewById(R.id.iv_logo);
        ivLogo.setBackground(getResources().getDrawable(R.drawable.no_logo));

        btnUploadImage = view.findViewById(R.id.btn_upload_image);
        btnRemuveLogo = view.findViewById(R.id.btn_remuve_logo);


    }

    public void setQrColor(int qrColor) {
        this.qrColor = qrColor;
        tvColorDialog.setText(String.format("#%06X", (0xFFFFFF & qrColor)));
        ivColorDialog.setBackgroundColor(qrColor);
    }

    public void setQrGradient(int qrGradient) {
        this.qrGradient = qrGradient;
        tvGradientDialog.setText(String.format("#%06X", (0xFFFFFF & qrGradient)));
        ivGradientDialog.setBackgroundColor(qrGradient);
    }

    private void setListeners() {
        btnEnterContent.setOnClickListener(this);
        btnSetColors.setOnClickListener(this);
        btnAddLogo.setOnClickListener(this);
        btnDownloadQr.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);
        btnRemuveLogo.setOnClickListener(this);

        rbColorGradient.setOnCheckedChangeListener(this);

        ivColorDialog.setOnClickListener(this);
        ivGradientDialog.setOnClickListener(this);

        sbSize.setOnSeekBarChangeListener(this);
    }

    public View getView() {
        return view;
    }

    protected void showLayout(ViewGroup layout, ImageView ivArrow) {
        layout.setVisibility(View.VISIBLE);
        //ivArrow.setRotation(90);
        ivArrow.startAnimation(AnimationUtils.loadAnimation(layout.getContext(), R.anim.arrow_button_rotate_to_left));


    }

    protected void goneLayout(ViewGroup layout, ImageView ivArrow) {
        layout.setVisibility(View.GONE);

        ivArrow.startAnimation(AnimationUtils.loadAnimation(layout.getContext(), R.anim.arrow_button_rotate_to_right));
        ivArrow.setRotation(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enter_content:
                if (vgEnterContent.getVisibility() == View.VISIBLE)
                    goneLayout(vgEnterContent, ivArrowContent);
                else if (vgEnterContent.getVisibility() == View.GONE)
                    showLayout(vgEnterContent, ivArrowContent);
                break;
            case R.id.btn_set_colors:
                if (vgSetColors.getVisibility() == View.VISIBLE)
                    goneLayout(vgSetColors, ivArrowColor);
                else if (vgSetColors.getVisibility() == View.GONE)
                    showLayout(vgSetColors, ivArrowColor);
                break;
            case R.id.btn_add_logo:
                if (vgAddLogo.getVisibility() == View.VISIBLE)
                    goneLayout(vgAddLogo, ivArrowLogo);
                else if (vgAddLogo.getVisibility() == View.GONE)
                    showLayout(vgAddLogo, ivArrowLogo);
                break;


            case R.id.iv_color_dialog:
                showColorDialog(color1, tvColorDialog, ivColorDialog);
                break;
            case R.id.iv_gradient_dialog:
                showColorDialog(color2, tvGradientDialog, ivGradientDialog);
                break;
            case R.id.btn_upload_image:
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        GET_FROM_GALLERY);
                break;
            case R.id.btn_remuve_logo:
                ivLogo.setImageDrawable(null);
                isLogoRemuved = true;
                break;

        }
    }

    private boolean writPermissionChecked() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }
    }


    private void showColorDialog(final int colorId, final TextView tvColor, final View vColor) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getContext(), qrColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                isCreated = false;
                vColor.setBackgroundColor(qrColor);
                switch (colorId) {
                    case color1:
                        setQrColor(color);
                        break;
                    case color2:
                        setQrGradient(color);
                }
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }
        });
        dialog.show();

    }


    public String saveImage(Context context, ImageView qrImage) {

        if (!writPermissionChecked()) {
            return "";
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) qrImage.getDrawable()).getBitmap();
        //bitmap = setBimapSize(bitmap);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".png");
            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context,
                    new String[]{f.getPath()},
                    new String[]{"image/png"}, null);
            fo.close();
            Toast.makeText(getActivity(), "QR Image Saved", Toast.LENGTH_LONG).show();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private Bitmap setBimapSize(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmapSize, bitmapSize, false);
        return resizedBitmap;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_color_gradient:
                showGradientLayout(isChecked);

                isCreated = false;
        }
    }

    private void showGradientLayout(boolean isChecked) {
        if (isChecked) {
            TransitionManager.beginDelayedTransition(llColorGradient);
            llColorGradient.setVisibility(View.VISIBLE);
        } else if (!isChecked) {
            TransitionManager.beginDelayedTransition(llColorGradient);
            llColorGradient.setVisibility(View.GONE);
        }
    }

    protected Bitmap setQRColor(BitMatrix bitMatrix) {
        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, 0, bmp.getHeight(), qrColor, qrGradient, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, bmp.getWidth(), bmp.getHeight(), paint);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (rbColorGradient.isChecked())
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? bmp.getPixel(x, y) : Color.WHITE);
                else
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? qrColor : Color.WHITE);
            }
        }

        return bmp;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvQrSize.setText("" + (progress + 200) + "x" + (progress + 200) + " PX");
        bitmapSize = progress + 200;

        isCreated = false;


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ivLogo.setImageBitmap(BitmapFactory.decodeResource(getResources(), logoList.get(position)));
        isLogoRemuved = false;
        isCreated = false;
    }

    public void createQrCode(final String qrCode, final int tag) {
        hideKeyboard(getActivity());

        if (tag == IMAGE_SAVED) {
            saveImage(getContext(), iv_qr);
            return;
        } else if (tag == IMAGE_CREATED) {
            if (isCreated) {
                return;
            }
        }

        final AlertDialog dialog = new SpotsDialog(getContext(), "Generating");
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Hashtable hints = new Hashtable();
                    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                    MultiFormatWriter writer = new MultiFormatWriter();
                    BitMatrix bitMatrix = writer.encode(qrCode, BarcodeFormat.QR_CODE, bitmapSize + 200, bitmapSize + 200, hints);
                    final Bitmap bitmap = setQRColor(bitMatrix);

                    Bitmap customLogo = null;

                    if (!isLogoRemuved)
                        customLogo = ((BitmapDrawable) ivLogo.getDrawable()).getBitmap();

                    final Bitmap finalCustomLogo = customLogo;
                    iv_qr.post(new Runnable() {
                        @Override
                        public void run() {
                            iv_qr.setImageBitmap(bitmap);

                            if (finalCustomLogo != null) {
                                iv_qr.setImageBitmap(mergeBitmaps(finalCustomLogo, bitmap));
                            }

                            if (tag == IMAGE_CREATED_AND_SAVED) {
                                saveImage(getContext(), iv_qr);
                            }

                            isCreated = true;
                            dialog.dismiss();

                        }
                    });


                } catch (WriterException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        }).start();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                isCreated = false;
                ivLogo.setImageBitmap(bitmap);
                isLogoRemuved = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("onRequestPermissionsResult", "      " + requestCode);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage(getContext(), iv_qr);
                    Log.d("onRequestPermissionsResult", "      " + requestCode);
                }
                return;
        }
    }


    public Bitmap mergeBitmaps(Bitmap logo, Bitmap qrcode) {
        Bitmap combined = Bitmap.createBitmap(qrcode.getWidth(), qrcode.getHeight(), qrcode.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.drawBitmap(qrcode, new Matrix(), null);

        Bitmap resizeLogo = Bitmap.createScaledBitmap(logo, canvasWidth / 7, canvasHeight / 7, false);

        int centreX = (canvasWidth - resizeLogo.getWidth()) / 2;
        int centreY = (canvasHeight - resizeLogo.getHeight()) / 2;
        canvas.drawBitmap(resizeLogo, centreX, centreY, null);
        return combined;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
