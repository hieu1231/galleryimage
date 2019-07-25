package hieusenpaj.com.task6.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import hieusenpaj.com.task6.adapter.GirdViewFolderAdapter;
import hieusenpaj.com.task6.object.Model_images;
import hieusenpaj.com.task6.R;

public class MainActivity extends AppCompatActivity {
//    ArrayList<ImageDataModel> allImages = new ArrayList<ImageDataModel>();
    ArrayList<Model_images> al_images = new ArrayList<>();
    GridView gridView;
    ProgressDialog progressDialog;
    boolean boolean_folder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handlePermiss();
        gridView = findViewById(R.id.gv);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DislayActivity.class);
                intent.putExtra("value",i);
//                String string =al_images.get(i).str_folder;
//                intent.putExtra("String",string);


                startActivity(intent);

            }
        });
    }
    private void handlePermiss() {
        String[] perms = new String[]{
                "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(perms, 3);
        }

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 3/*200*/:
                if (grantResults[0] == 0) {

                    new MyAsyncTask().execute();

                }

                return;
            default:
                return;
        }
    }
//    public  ArrayList<ImageDataModel> gettAllImages(Activity activity) {
//
//        //Remove older images to avoid copying same image twice
//
//        allImages.clear();
//        Uri uri;
//        Cursor cursor;
//        int column_index_data, column_index_folder_name;
//
//        String absolutePathOfImage = null, imageName;
//
//        //get all images from external storage
//
//        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//        String[] projection = { MediaStore.MediaColumns.DATA,
//                MediaStore.Images.Media.DISPLAY_NAME };
//
//        cursor = activity.getContentResolver().query(uri, projection, null,
//                null, null);
//
//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//
//        column_index_folder_name = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
//
//        while (cursor.moveToNext()) {
//
//            absolutePathOfImage = cursor.getString(column_index_data);
//
//            imageName = cursor.getString(column_index_folder_name);
//
//            allImages.add(new ImageDataModel(imageName, absolutePathOfImage));
//
//        }
//
//        // Get all Internal storage images
//
//        uri = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;
//
//        cursor = activity.getContentResolver().query(uri, projection, null,
//                null, null);
//
//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//
//        column_index_folder_name = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
//
//        while (cursor.moveToNext()) {
//
//            absolutePathOfImage = cursor.getString(column_index_data);
//
//            imageName = cursor.getString(column_index_folder_name);
//
//            allImages.add(new ImageDataModel(imageName, absolutePathOfImage));
//        }
//
//        return allImages;
//    }

    public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            al_images = getImage();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//
            // Hide ProgressDialog here
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            GirdViewFolderAdapter girdViewAdapter = new GirdViewFolderAdapter(getApplicationContext(),R.layout.list_item,al_images);
            gridView.setAdapter(girdViewAdapter);

        }

        @Override
        protected void onPreExecute() {
            // Show ProgressDialog here
//
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0].intValue());
        }
    }

    public ArrayList<Model_images> getImage() {
        al_images.clear();
        final int a=0;

        int int_position =0;
        Uri uri;
        final Cursor cursor;
        final int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));
//
            for (int i = 0; i < al_images.size(); i++) {
                if (al_images.get(i).getStr_folder().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;

                    int_position = i;

                    break;
                } else {
                    boolean_folder = false;
                }
            }

            if (boolean_folder) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(al_images.get(int_position).getAl_imagepath());
                al_path.add(absolutePathOfImage);

                al_images.get(int_position).setAl_imagepath(al_path);


            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                Model_images obj_model = new Model_images();
                obj_model.setStr_folder(cursor.getString(column_index_folder_name));
                obj_model.setAl_imagepath(al_path);

                al_images.add(obj_model);

            }


        }
        for (int i = 0; i < al_images.size(); i++) {
            Log.e("FOLDER", al_images.get(i).getStr_folder());
            for (int j = 0; j < al_images.get(i).getAl_imagepath().size(); j++) {
                Log.e("FILE", al_images.get(i).getAl_imagepath().get(j));
            }
        }




        return al_images;
    }
}

