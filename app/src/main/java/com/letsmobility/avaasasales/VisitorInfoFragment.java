package com.letsmobility.avaasasales;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.letsmobility.avaasasales.model.Visitdetail;
import com.letsmobility.avaasasales.room.VisitDetailRepository;
import com.letsmobility.avaasasales.utils.AndroidUtil;
import com.letsmobility.avaasasales.utils.DatePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class VisitorInfoFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RatingBar ratingBar;
    private RelativeLayout rlSchedule;
    private RadioGroup rgLike;
    private TextView tvDate, tvTime;
    private String date, ratingValue, time, isLikeProduct;
    private int endTimeHours = 18;
    private int endTimeMins = 00;
    private Button btnComplete;
    private EditText etRemark;
    private DatePickerFragment datePickerFragment = new DatePickerFragment();
    boolean isFollowUp;
    Visitdetail visitDetail;
    FirebaseAuth mAuth;


    public VisitorInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visitor_info, container, false);
        mAuth = FirebaseAuth.getInstance();
        ratingBar = view.findViewById(R.id.ratingBar);
        rlSchedule = view.findViewById(R.id.rl_schedule_follow_up);
        btnComplete = view.findViewById(R.id.btn_complete);
        etRemark = view.findViewById(R.id.et_sales_remark);
        btnComplete.setOnClickListener(this);
        ratingValue = "4";
        isLikeProduct = "Yes";
        rgLike = view.findViewById(R.id.rg_like);
        tvDate = view.findViewById(R.id.tv_date_value);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtil.hideKeyBoard(getActivity());
                datePickerFragment.disablePast();

                datePickerFragment.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String monthString = String.format("%02d", month);
                        String dayString = String.format("%02d", day);
                        Log.d("DOB", year + "-" + monthString + "-" + dayString);
                        date = year + "-" + monthString + "-" + dayString;
                        tvDate.setText(dayString + "/" + monthString + "/" + year);
                    }
                });
                Calendar calendar = Calendar.getInstance();
                datePickerFragment.setDate(calendar.getTime());
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        tvTime = view.findViewById(R.id.tv_time_value);
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(endTimeHours, endTimeMins, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        endTimeHours = hourOfDay;
                        endTimeMins = minute;
                        time = getTimeInFormat(hourOfDay, minute);
                        tvTime.setText(time);
                    }
                });
            }
        });
        rgLike.setOnCheckedChangeListener(this);
        addListenerOnRatingBar();

        Visitdetail editDetail = ((CreateVisitActivity) getActivity()).getEditVisitDetail();
        if (editDetail != null) {
            updateData(editDetail);
        }
        return view;
    }

    void updateData(Visitdetail editDetail) {
        etRemark.setText(editDetail.getSalesPersonRemark());
        ratingBar.setRating(Float.parseFloat(editDetail.getRate()));
        ((RadioButton) rgLike.getChildAt(getRadioButtonIndex(editDetail.getIsLikeProduct()))).setChecked(true);
        date = editDetail.getFollowDate();
        time = editDetail.getFollowTime();
            tvDate.setText(date);
            tvTime.setText(time);
    }


    int getRadioButtonIndex(String status) {
        switch (status) {
            case "Yes":
                return 0;
            case "No":
                return 1;
            case "Need to Follow up":
                return 2;
            default:
                return 0;
        }

    }

    public void addListenerOnRatingBar() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingValue = String.valueOf((int)rating);

            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_need_follow) {
            isFollowUp = true;
            isLikeProduct = "Need to Follow up";
            rlSchedule.setVisibility(View.VISIBLE);
        } else if (checkedId == R.id.rb_no) {
            isFollowUp = false;
            rlSchedule.setVisibility(View.GONE);
            isLikeProduct = "No";
        } else {
            isFollowUp = false;
            rlSchedule.setVisibility(View.GONE);
            isLikeProduct = "Yes";
        }
    }

    private void showTimePicker(int hour, int minute, TimePickerDialog.
            OnTimeSetListener onTimeSetListener) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener,
                hour, minute, false);
        timePickerDialog.show();
    }

    private String getTimeInFormat(int hour, int minute) {
        DateFormat outputformat = new SimpleDateFormat("h:mm aa");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        return outputformat.format(c.getTime());
    }

    public boolean isValid() {
        if (etRemark.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Remark should not be empty", Toast.LENGTH_LONG).show();
            return false;
        } else if (isFollowUp && (date == null || time == null)) {
            Toast.makeText(getContext(), "Date and time should not be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        if (isValid()) {
            visitDetail = ((CreateVisitActivity) getActivity()).getVisitObject();
            saveData();


        }
    }

    public void saveToLocal(Visitdetail visitdetail) {
        VisitDetailRepository visitDetailRepository = new VisitDetailRepository(getContext());
            visitDetailRepository.insertTask(visitdetail);
    }

    public void saveData() {

        if (isFollowUp) {
            visitDetail.setFollowDate(date);
            visitDetail.setFollowTime(time);
        }

        visitDetail.setRate(ratingValue);
        visitDetail.setSalesPersonRemark(etRemark.getText().toString());
        visitDetail.setIsLikeProduct(isLikeProduct);
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            visitDetail.setSalesPersonEmail(user.getEmail());
        }
        saveToLocal(visitDetail);
        Toast.makeText(getContext(), "All Fields Saved Successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getContext(), SucessUploadActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
