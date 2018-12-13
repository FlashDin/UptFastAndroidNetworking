package com.flashdin.fastandroidnetworking;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.flashdin.fastandroidnetworking.fast.TUsers;
import com.flashdin.fastandroidnetworking.fast.FastMethods;
import com.flashdin.fastandroidnetworking.fast.view.RecyclerViewFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class FormTest extends AppCompatActivity {

    private ImageView mImgUser;
    private ImageButton mBtnChooser;
    private TextView mTxtUsername, mTxtDate;
    private Button mBtnSimpan, mBtnUpdate, mBtnHapus, mBtnTampil;
    //Image request code
    private static final int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    private TUsers tUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_test);
        mImgUser = findViewById(R.id.imgUser);
        mBtnChooser = findViewById(R.id.btnChooser);
        mTxtUsername = findViewById(R.id.txtUsername);
        mTxtDate = findViewById(R.id.txtDate);
        mBtnSimpan = findViewById(R.id.btnSimpan);
        mBtnUpdate = findViewById(R.id.btnUpdate);
        mBtnHapus = findViewById(R.id.btnHapus);
        mBtnTampil = findViewById(R.id.btnTampil);
        mBtnChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), PICK_IMAGE_REQUEST);
            }
        });
        Bundle bundle = getIntent().getExtras();
        final String actFrom = bundle.getString("act");
        switch (actFrom) {
            case "nolibs":
                getSupportActionBar().setSubtitle("No Libs");
                break;
            case "retrofit":
                getSupportActionBar().setSubtitle("Retrofit");
                break;
            case "volley":
                getSupportActionBar().setSubtitle("Volley");
                break;
            case "fast":
                getSupportActionBar().setSubtitle("Fast Android Networking");
                break;
            case "ion":
                getSupportActionBar().setSubtitle("ION");
                break;
        }
        mBtnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (actFrom) {
                    case "fast":
                        if (putField()) {
                            new FastMethods(FormTest.this).saveData(tUsers, FormTest.this);
                        }
                        break;
                }
            }
        });

        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (actFrom) {
                    case "fast":
                        if (putField()) {
                            tUsers.setUserId(1);
                            new FastMethods(FormTest.this).saveData(tUsers, FormTest.this);
                        }
                        break;
                }
            }
        });

        mBtnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (actFrom) {
                    case "fast":
                        if (putField()) {
                            tUsers.setUserId(1);
                            new FastMethods(FormTest.this).deleteData(tUsers, FormTest.this);
                        }
                        break;
                }
            }
        });

        mBtnTampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (actFrom) {
                    case "fast":
                        RecyclerViewFragment frg = new RecyclerViewFragment();
                        FragmentManager manager = getFragmentManager();
                        FragmentTransaction transaction =
                                manager.beginTransaction();
                        transaction.setTransition(
                                FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        transaction.replace(R.id.frLayout, frg);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                }
            }
        });
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap decodeByte = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                decodeByte.compress(Bitmap.CompressFormat.PNG, 75, bos);
                mImgUser.setImageBitmap(decodeByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean putField() {
        boolean cancel = false;
        View focusView = null;
        tUsers = new TUsers();
        tUsers.setUserId(0);
        tUsers.setUserName(mTxtUsername.getText().toString());
        tUsers.setUserDate(new Date());
        Bitmap bitmap = ((BitmapDrawable) mImgUser.getDrawable()).getBitmap(); //from imageView
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] ar = bos.toByteArray();
        String mFoto = Base64.encodeToString(ar, Base64.DEFAULT);
        tUsers.setUserPhoto(mFoto);
        if (TextUtils.isEmpty(tUsers.getUserName())) {
            this.mTxtUsername.setError(getString(R.string.error_field_required));
            focusView = this.mTxtUsername;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

}
