package com.texttrans.translator.app_data;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;


import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.texttrans.translator.BuildConfig;

import com.texttrans.translator.R;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.texttrans.translator.app_data.utility.AdsUtil;
import com.theartofdev.edmodo.cropper.CropImage;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HomingActivity extends AppCompatActivity implements MaxRewardedAdListener {
    LinearLayout adsize;
    String imageFilePath;
    File photoFile;
    private Bitmap mImageBitmap;
    public void onCreate(Bundle bundle) {
        super.onCreate (bundle);
        getWindow ().setFlags (1024, 1024);
        setContentView (R.layout.activity_home);
        FirebaseApp.initializeApp (this);

        findViewById (R.id.imgLangTranslator).setOnClickListener (new View.OnClickListener () {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    HomingActivity.this.askforPermission ("1");
                } else {

                    HomingActivity.this.goTranslate ();

                }
            }
        });
        findViewById (R.id.imgHistory).setOnClickListener (new View.OnClickListener () {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    HomingActivity.this.askforPermission (ExifInterface.GPS_MEASUREMENT_2D);
                } else {

                    HomingActivity.this.goHistory ();

                }
            }
        });

        findViewById (R.id.imageCamTranslate).setOnClickListener (new View.OnClickListener () {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    HomingActivity.this.askforPermission ("5");
                } else {
                    HomingActivity.this.openCameraFilterIntent ();

                }

            }
        });
        findViewById (R.id.imgGlryTranslate).setOnClickListener (new View.OnClickListener () {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    HomingActivity.this.askforPermission ("6");
                } else {
                    HomingActivity.this.goRead ();
                }
            }
        });

        findViewById (R.id.imagePolicy).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                try {
                    String url = "" + getResources ().getString (R.string.strPolicy);
                    Intent i = new Intent (Intent.ACTION_VIEW);
                    i.setData (Uri.parse (url));
                    startActivity (i);

                } catch (Exception e) {

                }

            }
        });

        findViewById (R.id.imageShareApp).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                Intent shareIntent = new Intent ("android.intent.action.SEND");
                shareIntent.setType ("text/plain");
                shareIntent.putExtra ("android.intent.extra.SUBJECT", "Now " + getString (R.string.app_name) + " Available on Google Playstore please download it on share it");
                shareIntent.putExtra ("android.intent.extra.TEXT", " - https://play.google.com/store/apps/details?id=" + getPackageName () + " \n\n");
                startActivity (Intent.createChooser (shareIntent, "Share Via"));

            }
        });
        if (AdsUtil.showTAds) {
            try {
                AdsUtil.initialiseTextAds (this);
                AdsUtil.showTextMoviesAds (findViewById (R.id.adViewMaxSoccer));
                AdsUtil.setInstanceMaxTextRewardedAd (this);
                AdsUtil.sMaxTextRewardedAd.setListener (this);
            } catch (Exception e) {
            }
        }

    }

    public void goTranslate() {
        startActivity (new Intent (HomingActivity.this, Text_activity.class));
    }


    public void goHistory() {
        startActivity (new Intent (HomingActivity.this, HistorifyActivity.class));

                }

    public void goRead() {

        startActivityForResult (new Intent ("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 101);
    }
    public void askforPermission(final String str) {
        Dexter.withActivity (this)
                .withPermissions ("android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.CAMERA")
                .withListener (new MultiplePermissionsListener () {
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (!multiplePermissionsReport.areAllPermissionsGranted ()) {
                            return;
                        }
                        if (str.equals ("1")) {
                            HomingActivity.this.goTranslate ();
                        } else if (str.equals (ExifInterface.GPS_MEASUREMENT_2D)) {
                            HomingActivity.this.goHistory ();
                        } else if (str.equals (ExifInterface.GPS_MEASUREMENT_3D)) {
                            //HomeActivity.this.goVideoChat ();
                        } else if (str.equals ("4")) {

                        } else if (str.equals ("5")) {
                            HomingActivity.this.openCameraFilterIntent ();
                        } else if (str.equals ("6")) {
                            HomingActivity.this.goRead ();
                        }
                    }

                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list,
                                                                   PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest ();
                    }
                }).check ();
    }

    public void openCameraFilterIntent() {


                try {

                    Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                            BuildConfig.APPLICATION_ID + ".provider", createImaginaryFile ());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(intent, 100);

                } catch(Exception ex) {
                    ex.printStackTrace();
                }

    }
    private String cameraFilingPath;
    File cameraImage;
    private File createImaginaryFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );


        cameraFilingPath = image.getAbsolutePath();
        return image;
    }




    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult (i, i2, intent);
        if (i == 203) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult (intent);
            if (i2 == -1) {
                try {
                    runTextRec (MediaStore.Images.Media.getBitmap (getContentResolver (), activityResult.getUri ()));
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            } else if (i2 == 204) {
                Toast.makeText (getApplicationContext (), "Error", Toast.LENGTH_SHORT).show ();
            }
        }
        if (i == 100) {

            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap (this.getContentResolver (), Uri.parse (imageFilePath));

            } catch (Exception e) {
                e.printStackTrace ();
            }

            File file = new File (this.cameraFilingPath);
            Uri fromFile = Uri.fromFile (file);

            if (file.length () > 0) {
                CropImage.activity (fromFile).start (this);
            }
        } else if (i == 101) {
            try {
                String[] strArr = {"_data"};
                Cursor query = getContentResolver ().query (intent.getData (), strArr, (String) null, (String[]) null, (String) null);
                query.moveToFirst ();
                @SuppressLint("Range") String string = query.getString (query.getColumnIndex (strArr[0]));
                query.close ();
                File file2 = new File (string);
                Uri fromFile2 = Uri.fromFile (file2);
                if (file2.length () > 0) {
                    CropImage.activity (fromFile2).start (this);
                }
            } catch (Exception e2) {
                e2.printStackTrace ();
            }
        }
    }


    private void runTextRec(Bitmap bitmap) {
        FirebaseVision.getInstance ().getOnDeviceTextRecognizer ().processImage (FirebaseVisionImage.fromBitmap (bitmap)).addOnSuccessListener (new OnSuccessListener<FirebaseVisionText> () {
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                if (firebaseVisionText.getText ().length () <= 0) {
                    Toast.makeText (HomingActivity.this.getApplicationContext (), "No text Found", Toast.LENGTH_LONG).show ();
                    return;
                }
                Intent intent = new Intent (HomingActivity.this, Text_activity.class);
                intent.putExtra ("text", firebaseVisionText.getText ());
                HomingActivity.this.startActivity (intent);
            }
        }).addOnFailureListener (new OnFailureListener () {
            public void onFailure(Exception exc) {
                Toast.makeText (HomingActivity.this.getApplicationContext (), "Unable to fetch text from image", Toast.LENGTH_LONG).show ();
            }
        });
    }

    public void onBackPressed() {
        openExitAppDialog ();
    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo ();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting ();
    }

    private void openExitAppDialog() {
        final Dialog dialog = new Dialog (this, R.style.ExitDialogStyle);
        dialog.requestWindowFeature (1);
        dialog.setContentView (R.layout.dialog_application_exit);
        dialog.setCanceledOnTouchOutside (true);
        dialog.findViewById (R.id.canceled).setOnClickListener (new View.OnClickListener () {
            public void onClick(View view) {
                dialog.dismiss ();
            }
        });
        dialog.findViewById (R.id.exit).setOnClickListener (new View.OnClickListener () {
            public void onClick(View view) {
                dialog.dismiss ();
                HomingActivity.this.finish ();
            }
        });
        dialog.show ();
    }

    @Override
    public void onRewardedVideoStarted(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {

    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {

    }

    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }





}
