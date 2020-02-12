package com.letsmobility.avaasasales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SucessUploadActivity extends AppCompatActivity {

    private Button btnCreate, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess_upload);
        btnCreate = findViewById(R.id.btn_create);
        btnExit = findViewById(R.id.btn_exit);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SucessUploadActivity.this, CreateVisitActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
