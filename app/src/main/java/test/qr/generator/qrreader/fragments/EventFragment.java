package test.qr.generator.qrreader.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import test.qr.generator.qrreader.R;


public class EventFragment extends BaseFragmentView implements TextWatcher {

    EditText etEvent, etLocation;
    Button btnCreate, btnDanload;

    ImageView ivQr;
    TextView tvStartTime, tvEndTime;
    TextView tvBtnStartTime, tvBtnEndTime;

    TextView tvDate;

    private Calendar mSelectedDate = Calendar.getInstance();

    public static EventFragment newInstance() {
        return new EventFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        setView(view);

        init(view);
        setDefaultDateAndTime();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQr(IMAGE_CREATED);
            }
        });

        tvBtnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvDate = tvStartTime;
                openDatePicker();
            }
        });

        tvBtnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDate = tvEndTime;
                openDatePicker();
            }
        });

        btnDanload = view.findViewById(R.id.btn_download_qr);
        btnDanload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCreated)
                    createQr(IMAGE_CREATED_AND_SAVED);
                else if (isCreated) {
                    createQr(IMAGE_SAVED);
                }
            }
        });

        return view;
    }


    public void init(View view) {
        ivQr = view.findViewById(R.id.iv_qr);
        etEvent = view.findViewById(R.id.et_event);
        etLocation = view.findViewById(R.id.et_location);

        etEvent.addTextChangedListener(this);
        etLocation.addTextChangedListener(this);

        btnCreate = view.findViewById(R.id.btn_create);
        tvStartTime = view.findViewById(R.id.tv_start_time);
        tvEndTime = view.findViewById(R.id.tv_end_time);
        tvBtnStartTime = view.findViewById(R.id.tv_btn_start_time);
        tvBtnEndTime = view.findViewById(R.id.tv_btn_end_time);
    }

    private void createQr(int tag) {
        if (review()) {
            String begin = "BEGIN:" + "VEVENT" + "\n";
            String summary = "SUMMARY:" + etEvent.getText().toString() + "\n";
            String location = "LOCATION:" + etLocation.getText().toString() + "\n";
            String dtstart = "DTSTART:" + tvStartTime.getText().toString().replace(' ', 'T') + "\n";
            String dtend = "DTEND:" + tvEndTime.getText().toString().replace(' ', 'T') + "\n";
            String end = "END:" + "VEVENT";

            qrCodeResult = begin + summary + location + dtstart + dtend + end;

            createQrCode(qrCodeResult, tag);
        }
    }

    private boolean review() {
        if (etEvent.getText().toString().trim().isEmpty()) {
            etEvent.setError("event is empty");
            return false;
        }
        return true;
    }

    private void setDefaultDateAndTime() {
        String date = DateFormat.format("dd.mm.yyyy", mSelectedDate).toString();
        String time = DateFormat.format("kk:mm", mSelectedDate).toString();
        String dateAndTime = date + " " + time;
        tvStartTime.setText(dateAndTime);
        tvEndTime.setText(dateAndTime);
    }

    private void openDatePicker() {
        new DatePickerDialog(getActivity(), mOnDateSetListener, mSelectedDate.get(Calendar.YEAR),
                mSelectedDate.get(Calendar.MONTH),
                mSelectedDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void openTimePicker() {
        new TimePickerDialog(getActivity(), mOnTimeSetListener, mSelectedDate.get(Calendar.HOUR_OF_DAY),
                mSelectedDate.get(Calendar.MINUTE), true).show();
    }

    DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mSelectedDate.set(Calendar.YEAR, year);
            mSelectedDate.set(Calendar.MONTH, monthOfYear);
            mSelectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            openTimePicker();
            updateDateLabel();
        }
    };

    TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mSelectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mSelectedDate.set(Calendar.MINUTE, minute);

            updateTimeLabel();
        }
    };

    private void updateDateLabel() {
        String date = DateFormat.format("dd MMM yyyy", mSelectedDate).toString();
        tvDate.setText(date);

    }

    private void updateTimeLabel() {
        String time = DateFormat.format("kk:mm", mSelectedDate).toString();
        String date = tvDate.getText().toString() + " " + time;
        tvDate.setText(date);
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
