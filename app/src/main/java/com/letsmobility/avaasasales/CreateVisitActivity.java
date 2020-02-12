package com.letsmobility.avaasasales;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.letsmobility.avaasasales.model.Visitdetail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreateVisitActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager pager;
    AppCompatImageView ivBack;
    Visitdetail mVisitDetail;
    List<Uri> mUriList;
    List<File> mFileList;
    FirebaseAuth mAuth;
    TextView tvLogout;
    Visitdetail editVisitDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_visit);
        mAuth = FirebaseAuth.getInstance();
        pager = findViewById(R.id.pager);
        ivBack = findViewById(R.id.iv_back);
        tvLogout = findViewById(R.id.tv_sign_out);
        ivBack.setOnClickListener(this);
        mUriList = new ArrayList<>();
        mFileList = new ArrayList<>();
        mVisitDetail = new Visitdetail();
        editVisitDetail = getIntent().getParcelableExtra("property_data");

        assert pager != null;
        tvLogout.setOnClickListener(this);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        final StepperIndicator indicator = findViewById(R.id.stepper_indicator);

        indicator.setViewPager(pager, pager.getAdapter().getCount());


//        indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
//            @Override
//            public void onStepClicked(int step) {
//                pager.setCurrentItem(step, true);
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    public void getNextPage(int position) {
        pager.setCurrentItem(position);
    }

    public void setFile(List<File> fileList) {
        for (File file : fileList)
            mFileList.add(file);

    }
    public Visitdetail getEditVisitDetail(){
        return editVisitDetail;
    }

    public Visitdetail getVisitObject() {
        return mVisitDetail;
    }

    public List<File> getmFileList() {
        return mFileList;
    }

    @Override
    public void onClick(View v) {
        if (v == tvLogout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }else {
            if (pager.getCurrentItem() == 0) {
                finish();
            } else pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PropertyInfoFragment();
                case 1:
                    return new ContactPersonInfoFragment();
                case 2:
                    return new VisitorInfoFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}


