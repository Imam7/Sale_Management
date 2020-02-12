package com.letsmobility.avaasasales;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.letsmobility.avaasasales.image.PicassoManager;
import com.letsmobility.avaasasales.model.Visitdetail;
import com.letsmobility.avaasasales.utils.AndroidUtil;
import com.letsmobility.avaasasales.utils.ImagePickerUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.letsmobility.avaasasales.utils.PermissionUtils.REQUEST_CAMERA;
import static com.letsmobility.avaasasales.utils.PermissionUtils.REQUEST_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactPersonInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private ImagePickerUtils imagePickerUtils;
    private List<File> localImageFiles;
    private List<File> mImageFiles;
    private AppCompatImageView ivContactPerson, ivAdd, ivClose;
    private TextView tvAdd;
    Button btnContinue;
    Visitdetail visitdetail;
    Spinner designationList;
    String mDesignation;
    String contactPersonUrl;
    private EditText etName, etContact, etReview, etAlternateContact;


    public ContactPersonInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_person_info, container, false);
        designationList = view.findViewById(R.id.sp_designation);
        ivAdd = view.findViewById(R.id.iv_add_pic);
        ivClose = view.findViewById(R.id.iv_close);
        tvAdd = view.findViewById(R.id.tv_add_pic);
        btnContinue = view.findViewById(R.id.btn_continue);
        etName = view.findViewById(R.id.et_con_per_name);
        etContact = view.findViewById(R.id.et_con_per_contact);
        etAlternateContact = view.findViewById(R.id.et_con_per_alt_contact);
        etReview = view.findViewById(R.id.et_review);
        btnContinue.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        mImageFiles = new ArrayList<>();
        ivContactPerson = view.findViewById(R.id.iv_contact_person);
        designationList.setOnItemSelectedListener(this);

        ArrayAdapter mAdapter = ArrayAdapter.createFromResource(getContext(), R.array.designation, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        designationList.setAdapter(mAdapter);
        Visitdetail editDetail = ((CreateVisitActivity) getActivity()).getEditVisitDetail();
        if (editDetail != null) {
            updateData(editDetail);
        }
        return view;
    }

    void updateData(Visitdetail editDetail) {
        etName.setText(editDetail.getContactedPersonName());
        etContact.setText(editDetail.getConatctedPersonContact());
        etAlternateContact.setText(editDetail.getAlternateContactNumber());
        etReview.setText(editDetail.getContactedPersonReview());
        contactPersonUrl = editDetail.getContactPersonPicUrl();
        if (contactPersonUrl != null) {
            PicassoManager.loadLocalImage(getActivity(), ivContactPerson, contactPersonUrl);
            tvAdd.setText(R.string.update_photo);
            ivClose.setVisibility(View.VISIBLE);
        }
        String myString = editDetail.getContactedPersonDesignation();

        ArrayAdapter myAdap = (ArrayAdapter) designationList.getAdapter();
        int spinnerPosition = myAdap.getPosition(myString);
        designationList.setSelection(spinnerPosition);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //    Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
        mDesignation = getResources().getStringArray(R.array.designation)[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void loadImageFromLocal(File file, ImageView imageView) {
        Picasso.get().load(file).fit().centerCrop()
                .placeholder(R.drawable.ic_photo_grey)
                .error(R.drawable.ic_photo_grey)
                .into(imageView);
        tvAdd.setText(R.string.update_photo);
        ivClose.setVisibility(View.VISIBLE);
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
                    contactPersonUrl = uri.toString();
                    File file = new File(AndroidUtil.getRealPathFromURI(getActivity(), uri));
                    localImageFiles.add(file);
                    ivContactPerson.setVisibility(View.VISIBLE);

                    loadImageFromLocal(file, ivContactPerson);
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
                    contactPersonUrl = uri.toString();
                    ivContactPerson.setVisibility(View.VISIBLE);
                    loadImageFromLocal(file, ivContactPerson);
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
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

    public boolean isValid() {
        if (etName.getText().toString().isEmpty() || etContact.getText().toString().isEmpty() || etReview.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "All fields are Mandatory", Toast.LENGTH_LONG).show();
            return false;
        } else if (etContact.getText().toString().length() < 10) {
            Toast.makeText(getContext(), "Invalid Phone number", Toast.LENGTH_LONG).show();
            return false;
//        } else if (localImageFiles == null || localImageFiles.size() < 1) {
//            Toast.makeText(getContext(), "image should not be empty", Toast.LENGTH_LONG).show();
//            return false;
        } else if (etAlternateContact.getText().toString().length() > 0 && etAlternateContact.getText().toString().length() < 10) {
            Toast.makeText(getContext(), "Invalid Alternate Phone number", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == ivAdd || v == tvAdd) {
            imagePickerUtils = new ImagePickerUtils();
            imagePickerUtils.setVideoDisabled(true);
            imagePickerUtils.showDialog(ContactPersonInfoFragment.this, true);
        } else if (v == btnContinue) {
            if (isValid()) {
                if (localImageFiles != null && localImageFiles.size() > 0) {
                    mImageFiles.add(localImageFiles.get(localImageFiles.size() - 1));
                    ((CreateVisitActivity) getActivity()).setFile(mImageFiles);
                }
                visitdetail = ((CreateVisitActivity) getActivity()).getVisitObject();
                visitdetail.setConatctedPersonContact(etContact.getText().toString());
                visitdetail.setAlternateContactNumber(etAlternateContact.getText().toString());
                visitdetail.setContactedPersonName(etName.getText().toString());
                visitdetail.setContactedPersonDesignation(mDesignation);
                visitdetail.setContactedPersonReview(etReview.getText().toString());
                visitdetail.setContactPersonPicUrl(contactPersonUrl);
                ((CreateVisitActivity) getActivity()).getNextPage(2);
            }
        } else if (v == ivClose) {
            if (localImageFiles != null)
                localImageFiles.clear();
            if (mImageFiles != null)
                mImageFiles.clear();
            contactPersonUrl = null;
            ivClose.setVisibility(View.INVISIBLE);
            ivContactPerson.setVisibility(View.INVISIBLE);
        }
    }
}
