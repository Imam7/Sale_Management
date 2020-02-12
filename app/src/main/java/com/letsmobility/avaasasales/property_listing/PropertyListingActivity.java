package com.letsmobility.avaasasales.property_listing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.letsmobility.avaasasales.CreateVisitActivity;
import com.letsmobility.avaasasales.LoginActivity;
import com.letsmobility.avaasasales.R;
import com.letsmobility.avaasasales.firebase.FirebaseRepository;
import com.letsmobility.avaasasales.model.Visitdetail;
import com.letsmobility.avaasasales.room.VisitDetailRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class PropertyListingActivity extends AppCompatActivity {

    List<Visitdetail> visitdetailList;
    RecyclerView rvHostels;
    FloatingActionButton fabAddDetail;
    TextView tvSync, visitCount, tvLogout;
    RelativeLayout rlNoVisit;
    Button btnCreateVisit;
    VisitDetailRepository visitDetailRepository;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseRepository repository;
    boolean isLastHostel, isLastPhoto;
    PropertyAdapter propertyAdapter;
    int a = 0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_listing);

        progressDialog = new ProgressDialog(this);

        tvSync = findViewById(R.id.tv_sync);
        fabAddDetail = findViewById(R.id.fab_add);
        rvHostels = findViewById(R.id.rv_hostel);
        visitCount = findViewById(R.id.tv_visit_count);
        tvLogout = findViewById(R.id.tv_sign_out);
        rlNoVisit = findViewById(R.id.rl_no_visit_today);
        btnCreateVisit = findViewById(R.id.btn_create_visit);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(PropertyListingActivity.this, LoginActivity.class));
            }
        });

        fabAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropertyListingActivity.this, CreateVisitActivity.class);
                startActivity(intent);
            }
        });
        tvSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visitdetailList != null && visitdetailList.size() > 0) {
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);

                    saveData();
                }
            }
        });

        rvHostels.setLayoutManager(new LinearLayoutManager(this));

        visitDetailRepository = new VisitDetailRepository(PropertyListingActivity.this);
        try {
            visitdetailList = visitDetailRepository.readTask();
            propertyAdapter = new PropertyAdapter(visitdetailList, this);
            rvHostels.setAdapter(propertyAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        repository = new FirebaseRepository();

        if (visitdetailList != null)
            visitCount.setText("Total Hostels Visited today : " + visitdetailList.size());
        btnCreateVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropertyListingActivity.this, CreateVisitActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            visitdetailList = visitDetailRepository.readTask();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (visitdetailList == null || visitdetailList.size() == 0) {
            tvSync.setVisibility(View.INVISIBLE);
            rvHostels.setVisibility(View.INVISIBLE);
            fabAddDetail.hide();
            rlNoVisit.setVisibility(View.VISIBLE);
        } else {
            rlNoVisit.setVisibility(View.INVISIBLE);
            rvHostels.setVisibility(View.VISIBLE);
            fabAddDetail.show();
            tvSync.setVisibility(View.VISIBLE);
            propertyAdapter = new PropertyAdapter(visitdetailList, this);
            rvHostels.setAdapter(propertyAdapter);
            visitCount.setText("Total Hostels Visited today : " + visitdetailList.size());
        }
    }


    private void uploadImage(Visitdetail visitDetail) {
        for (String photoUrl : visitDetail.getPropertyPhotosUrl()) {
            try {
                isLastPhoto = false;
                if (photoUrl != null && !photoUrl.isEmpty())
                    imageLoading(visitDetail, new File(new URL(photoUrl).toURI()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
        try {
            isLastPhoto = true;
            if (visitDetail.getContactPersonPicUrl() != null && !visitDetail.getContactPersonPicUrl().isEmpty())
                imageLoading(visitDetail, new File(new URL(visitDetail.getContactPersonPicUrl()).toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void imageLoading(Visitdetail visitDetail, File file) {
        StorageReference ref = storageReference.child("images/" + visitDetail.getPropertyName() + UUID.randomUUID().toString());
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();
        //uploading the image
        UploadTask uploadTask2 = ref.putBytes(data);
        //  ref.putFile(Uri.fromFile(file))
        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //     count++;
                Toast.makeText(PropertyListingActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                if (isLastHostel && isLastPhoto) {
                    visitDetailRepository.deleteAllTask();
                    try {
                        visitdetailList = visitDetailRepository.readTask();
                        progressDialog.dismiss();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (visitdetailList == null || visitdetailList.size() == 0) {
                        tvSync.setVisibility(View.INVISIBLE);
                        rvHostels.setVisibility(View.INVISIBLE);
                        fabAddDetail.hide();
                        rlNoVisit.setVisibility(View.VISIBLE);
                    } else {
                        rlNoVisit.setVisibility(View.INVISIBLE);
                        rvHostels.setVisibility(View.VISIBLE);
                        fabAddDetail.show();
                        tvSync.setVisibility(View.VISIBLE);
                        propertyAdapter = new PropertyAdapter(visitdetailList, getBaseContext());
                        rvHostels.setAdapter(propertyAdapter);
                        visitCount.setText("Total Hostel Visited today : " + visitdetailList.size());
                    }

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(PropertyListingActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
    }

    private void saveData() {
        for (Visitdetail visitdetails : visitdetailList) {
            repository.saveVisitInfo(visitdetails);
            a++;
            isLastHostel = a == visitdetailList.size();
            uploadImage(visitdetails);
        }

    }
}