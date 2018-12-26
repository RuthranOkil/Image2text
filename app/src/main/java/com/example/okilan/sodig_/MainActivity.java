package com.example.okilan.sodig_;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    private static final int PICK_IMAGE_REQUEST =100;
    Uri selectedimage;
    Bitmap bitmap;
    TextRecognizer textRecognizer;
    Frame frame;
    SparseArray items;
    StringBuilder stringBuilder = new StringBuilder();
    TextView t1,t2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (resultCode){
            case PICK_IMAGE_REQUEST:
                if(resultCode == RESULT_OK)
                {
                    selectedimage = data.getData();
                    if(selectedimage != null)
                       imageView.setImageURI(selectedimage);
                }try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedimage);
                    textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                    frame = new Frame.Builder().setBitmap(bitmap).build();
                    items = textRecognizer.detect(frame);
                    for(int i =0;i<items.size();i++){
                        TextBlock item = (TextBlock)items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("/");
                        for (Text line :item.getComponents()){
                            Toast.makeText(this,line.getValue(),Toast.LENGTH_LONG).show();
                            Log.v("lines", line.getValue());
                            t1 = findViewById(R.id.textView);
                            t1.setText(line.getValue());
                            for (Text element :line.getComponents()){
                                Toast.makeText(this,"Word = "+element.getValue(),Toast.LENGTH_SHORT).show();
                                t2 = findViewById(R.id.textView2);
                                t2.setText(element.getValue());
                                Log.v("element", element.getValue());
                            }
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });
    }
}
