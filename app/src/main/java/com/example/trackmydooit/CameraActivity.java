//package com.example.trackmydooit;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//import android.content.ClipData;
//import android.content.ClipboardManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Base64;
//import android.util.Log;
//import android.util.SparseArray;
//import android.view.SurfaceHolder;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.mlkit.common.MlKitException;
//import com.google.mlkit.vision.interfaces.Detector;
//import com.google.mlkit.vision.text.Text;
//import com.google.mlkit.vision.text.TextRecognition;
//import com.google.mlkit.vision.text.TextRecognizer;
//import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.mlkit.vision.common.InputImage;
//import com.google.mlkit.vision.text.Text;
//import com.google.mlkit.vision.text.TextRecognition;
//import com.google.mlkit.vision.text.TextRecognizer;
//import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//public class CameraActivity extends AppCompatActivity {
//
//    Button BTCapture, BTCopyText;
//    TextView textView_data;
//    Bitmap bitmap;
//    private static final int REQUEST_CAMERA_CODE = 100;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//
//        BTCapture = findViewById(R.id.BTCapture);
//        BTCopyText = findViewById(R.id.BTCopyText);
//        textView_data = findViewById(R.id.text_data);
//
//        if(ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{
//                    Manifest.permission.CAMERA
//            }, REQUEST_CAMERA_CODE);
//        }
//
//        BTCapture.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(CameraActivity.this);
//
//            }
//        });
//
//        BTCopyText.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                String scannedText = textView_data.getText().toString();
//                copyToClipBoard(scannedText);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
//        super.onActivityResult(requestCode,resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK){
//                Uri resultUri = result.getUri();
//                try{
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
//                    getTextFromImage(bitmap);
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    }
//
//
//    private void getTextFromImage(Bitmap bitmap){
//        InputImage image = InputImage.fromBitmap(bitmap, 0);
//        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
//        Task<Text>  result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
//            @Override
//            public void onSuccess(Text text) {
//
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                e.printStackTrace();
//            }
//        });
////                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
////                SparseArray<Text.TextBlock> textBlockSparseArray = recognizer.image;
////
////                StringBuilder stringBuilder = new StringBuilder();
////                for(int i = 0; i<textBlockSparseArray.size(); i++){
////                    Text.TextBlock textBlock = textBlockSparseArray.valueAt(i);
////                    stringBuilder.append(textBlock.getText());
////                    stringBuilder.append("\n");
////                }
//            textView_data.setText(result.toString());
//            BTCapture.setText("Retake");
//            BTCopyText.setVisibility(View.VISIBLE);
//        }
//
//
//    private void copyToClipBoard(String text){
//        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData clip = ClipData.newPlainText("Copied data", text);
//        clipboardManager.setPrimaryClip(clip);
//        Toast.makeText(CameraActivity.this,"Copied to clipboard", Toast.LENGTH_SHORT).show();
//
//    }
//
//}