package com.letsmobility.avaasasales;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.letsmobility.avaasasales.model.Visitdetail;
import com.letsmobility.avaasasales.utils.AndroidUtil;
import com.letsmobility.avaasasales.utils.ImagePickerUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.letsmobility.avaasasales.utils.PermissionUtils.REQUEST_CAMERA;
import static com.letsmobility.avaasasales.utils.PermissionUtils.REQUEST_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyInfoFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener,PhotosAdapter.ItemRemoveListener {

    public static final int ACCESS_LOCATION = 1991;
    boolean mLocationPermissionGranted;
    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private ImagePickerUtils imagePickerUtils;
    private List<File> localImageFiles;
    private List<File> mImageFiles;
    private TextView tvAddPhoto;
    private AppCompatImageView ivAddPhoto;
    private RecyclerView rvPhoto;
    private PhotosAdapter mAdapter;
    private List<String> mPropertyUrl;
    private List<String> editPropertyUrl;
    Button btnContinue;
    EditText etName, etAdd, etArea, etPin;
    SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    boolean isFromEdit;

    Visitdetail visitdetail,editDetail;


    public PropertyInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_info, container, false);
        getLocationPermission();
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        tvAddPhoto = view.findViewById(R.id.tv_add_photo);
        ivAddPhoto = view.findViewById(R.id.iv_add_photo);
        btnContinue = view.findViewById(R.id.btn_continue);
        etName = view.findViewById(R.id.et_prop_name);
        etAdd = view.findViewById(R.id.et_prop_add);
        etArea = view.findViewById(R.id.et_area);
        etPin = view.findViewById(R.id.et_pin);
        mImageFiles = new ArrayList<>();
        mPropertyUrl = new ArrayList<>();
        btnContinue.setOnClickListener(this);
        ivAddPhoto.setOnClickListener(this);
        tvAddPhoto.setOnClickListener(this);
        rvPhoto = view.findViewById(R.id.rv_photo);
        rvPhoto.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        editDetail = ((CreateVisitActivity) getActivity()).getEditVisitDetail();
        if (editDetail!=null){
            isFromEdit = true;
            updateData(editDetail);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.getMapAsync(this);
    }

    void updateData(Visitdetail editDetail){
        etName.setText(editDetail.getPropertyName());
        etAdd.setText(editDetail.getPropertyAddress());
        etArea.setText(editDetail.getArea());
        etPin.setText(editDetail.getPincode());
        editPropertyUrl = editDetail.getPropertyPhotosUrl();
        if (editPropertyUrl!=null && editPropertyUrl.size()>0){
            mPropertyUrl = editPropertyUrl;
            localImageFiles = new ArrayList<>();
            for (String propertyUrl : editPropertyUrl){
                try {
                    File file = new File(new URL(propertyUrl).toURI());
                    localImageFiles.add(file);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            mImageFiles =localImageFiles;
            mAdapter = new PhotosAdapter(getContext(), localImageFiles,this);
            rvPhoto.setAdapter(mAdapter);
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_LOCATION);
        } else {

            mLocationPermissionGranted = true;


        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
    private void getDeviceLocation(){

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try{
            if(mLocationPermissionGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation!=null) {
                                if (isFromEdit){
                                    latitude = Double.parseDouble(editDetail.getLatitute());
                                    longitude = Double.parseDouble(editDetail.getLongitude());
                                }else {
                                    latitude = currentLocation.getLatitude();
                                    longitude = currentLocation.getLongitude();
                                }


                                moveCamera(new LatLng(latitude, longitude),
                                        16f);
                            }

                        }else{
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){

        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions mp = new MarkerOptions();

                mp.position(latLng);

                mp.title("my position");

                mMap.addMarker(mp);
    }


    @Override
    public void onClick(View v) {

        if (v == tvAddPhoto || v == ivAddPhoto) {
            imagePickerUtils = new ImagePickerUtils();
            imagePickerUtils.setVideoDisabled(true);
            imagePickerUtils.showDialog(this, true);
        } else if (v == btnContinue) {
            if (isValid()) {
                visitdetail = ((CreateVisitActivity) getActivity()).getVisitObject();
                ((CreateVisitActivity) getActivity()).setFile(mImageFiles);
                visitdetail.setPropertyName(etName.getText().toString());
                visitdetail.setArea(etArea.getText().toString());
                visitdetail.setPropertyAddress(etAdd.getText().toString());
                visitdetail.setPincode(etPin.getText().toString());
                visitdetail.setLatitute(String.valueOf(latitude));
                visitdetail.setLongitude(String.valueOf(longitude));
                visitdetail.setPropertyPhotosUrl((ArrayList<String>) mPropertyUrl);
                ((CreateVisitActivity) getActivity()).getNextPage(1);
                Log.d("visit_detail", visitdetail.toString());
            }
        }
    }

    public boolean isValid() {
        if (etName.getText().toString().isEmpty() || etArea.getText().toString().isEmpty() || etAdd.getText().toString().isEmpty() || etPin.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "All fields are Mandatory", Toast.LENGTH_LONG).show();
            return false;
        } else if (mImageFiles.size() < 1) {
            Toast.makeText(getContext(), "Minimum one image should be there", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ImagePickerUtils.SELECT_IMAGE_ACTIVITY_REQUEST_CODE: {
                ArrayList<String> imageList = data.getStringArrayListExtra("selectedImages");
                if (localImageFiles == null) {
                    localImageFiles = new ArrayList<>();
                }

                for (String path : imageList) {
                    Uri uri = Uri.parse(path);
                    File file = new File(AndroidUtil.getRealPathFromURI(getActivity(), uri));
                    localImageFiles.add(file);
                    mImageFiles = localImageFiles;
                    mPropertyUrl.add(String.valueOf(uri));
                    mAdapter = new PhotosAdapter(getContext(), localImageFiles,this);
                    rvPhoto.setAdapter(mAdapter);

                    //             loadImageFromLocal(file, ivPhoto);
                }
                if (localImageFiles.size() > 0) {
                }
                break;
            }
            case ImagePickerUtils.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: {
                if (imagePickerUtils != null) {
                    File file = imagePickerUtils.getPhotoFile();
                    if (localImageFiles == null) {
                        localImageFiles = new ArrayList<>();
                    }
                    localImageFiles.add(file);
                    Uri uri = Uri.fromFile(file);
                    mPropertyUrl.add(String.valueOf(uri));
                    mImageFiles = localImageFiles;
                    mAdapter = new PhotosAdapter(getContext(), localImageFiles, this);
                    rvPhoto.setAdapter(mAdapter);
                    //           loadImageFromLocal(file, ivPhoto);
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        if (requestCode == ACCESS_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }

        } else if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        imagePickerUtils.selectImageFromGallery(this, false);
                    }
                }
            }
        } else if (requestCode == REQUEST_CAMERA) {
            boolean hasCameraPermission = false;
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.CAMERA)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        hasCameraPermission = true;
                    }
                } else if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED && hasCameraPermission) {
                        imagePickerUtils.dispatchTakePhotoIntent(this);
                    }
                }
            }
        }
    }


    @Override
    public void onRemoveClick(int position) {
        mPropertyUrl.remove(position);
    }
}

