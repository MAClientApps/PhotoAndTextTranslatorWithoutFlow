package com.texttrans.translator.app_data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;


import com.texttrans.translator.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.theartofdev.edmodo.cropper.CropImageView;

import com.texttrans.translator.Glob;

public class CropedActivity extends AppCompatActivity implements View.OnClickListener {
    public static CropImageView cropImagesView;
    public static Bitmap croppedImage;
    LinearLayout iv_done;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_cropping);

        cropImagesView = (CropImageView) findViewById(R.id.cropImageView);
        this.iv_done = (LinearLayout) findViewById(R.id.iv_done);

        Glide.with((FragmentActivity) this).asBitmap().load(Glob.photoUri).into(new SimpleTarget<Bitmap>() {
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {

                CropedActivity.cropImagesView.setImageBitmap(bitmap);
            }

            public void onLoadFailed(Drawable drawable) {

                super.onLoadFailed(drawable);
            }
        });

    }


    public void onClick(View view) {

        switch (view.getId ()) {
            case R.id.iv_back:
                onBackPressed ();
                return;
            case R.id.iv_done:
                Bitmap croppedImage2 = cropImagesView.getCroppedImage ();
                croppedImage = croppedImage2;
                runTextRec (croppedImage2);
                return;
            case R.id.lytLeftRotate:
                cropImagesView.rotateImage (-90);
                return;
            default:
                return;
        }
    }

    private void runTextRec(Bitmap bitmap) {
        FirebaseVision.getInstance().getOnDeviceTextRecognizer().processImage(FirebaseVisionImage.fromBitmap(bitmap)).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                if (firebaseVisionText.getText().length() <= 0) {
                    Toast.makeText(CropedActivity.this.getApplicationContext(), "No text Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(CropedActivity.this, Text_activity.class);
                intent.putExtra("text", firebaseVisionText.getText());
                CropedActivity.this.startActivity(intent);
                CropedActivity.this.finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception exc) {
                Toast.makeText(CropedActivity.this.getApplicationContext(), "Unable to fetch text from image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

}
