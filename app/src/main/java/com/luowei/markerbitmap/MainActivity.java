package com.luowei.markerbitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BitmapLoad(((ImageView) findViewById(R.id.imageView1))).execute(50);
        new BitmapLoad(((ImageView) findViewById(R.id.imageView2))).execute(100);
        new BitmapLoad(((ImageView) findViewById(R.id.imageView3))).execute(150);

    }


    class BitmapLoad extends AsyncTask<Integer, Void, Bitmap> {
        public BitmapLoad(ImageView imageView) {
            this.imageView = imageView;
        }

        ImageView imageView;

        @Override
        protected Bitmap doInBackground(Integer... params) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
            Bitmap marker = MarkerUtils.getMarker(bitmap, params[0]);
            return marker;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);

        }
    }


}
