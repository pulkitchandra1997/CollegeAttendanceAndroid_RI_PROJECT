package project.internship.tcs.collegeattendance;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class StudentProfilePicUpload extends AppCompatActivity {

    Bitmap bitmap=null;

    boolean chk = true;

    Button SelectImageGallery, UploadImageServer;

    ImageView imageView;

    ProgressDialog progressDialog ;

    String ImageName = "image_name" ;

    String ImagePath = "image_path" ;

    String rollno="rollno";

    String userrollno;


    SharedPreferences sp;
    SharedPreferences.Editor se;

    String ServerUploadPath ="https://rentzhub.co.in/tcs_data/profilepicupload.php" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pic_upload);
        sp = getSharedPreferences("college_data", MODE_PRIVATE);
        se = sp.edit();

        Intent i=getIntent();

        userrollno=i.getStringExtra("rollno");

        imageView = (ImageView)findViewById(R.id.imageView);


        SelectImageGallery = (Button)findViewById(R.id.buttonSelect);

        UploadImageServer = (Button)findViewById(R.id.buttonUpload);

        SelectImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_PICK);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });


        UploadImageServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageUploadToServerFunction();

            }
        });
        if(sp.getString("pic",null)!=null) {
            if(sp.getString("pic",null).equalsIgnoreCase("1")){
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                File myImageFile = new File(directory, "profilepic.jpeg");
                Picasso.with(this).load(myImageFile).into(imageView);
            }
            else{
                imageView.setImageResource(R.mipmap.profilepic);
            }
        }
        else
            imageView.setImageResource(R.mipmap.profilepic);
    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void ImageUploadToServerFunction() {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStreamObject;

            byteArrayOutputStreamObject = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStreamObject);

            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

            final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();

                    progressDialog = ProgressDialog.show(StudentProfilePicUpload.this, "Image is Uploading", "Please Wait", false, false);
                }

                @Override
                protected void onPostExecute(String string1) {

                    super.onPostExecute(string1);

                    // Dismiss the progress dialog after done uploading.
                    progressDialog.dismiss();
                    if(string1.equalsIgnoreCase("success")){
                        AlertDialog builder = new AlertDialog.Builder(StudentProfilePicUpload.this).create();
                        builder.setIcon(R.mipmap.ic_launcher_round);
                        builder.setTitle(Html.fromHtml("<font color='#FF0000'>RentZHub</font>"));
                        builder.setMessage("Image Uploaded Successfully.");
                        builder.show();
                        saveToInternalStorage(bitmap);

                        se.putString("pic","1");
                        se.commit();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent(StudentProfilePicUpload.this,StudentMainActivity.class);
                                ActivityOptions options = ActivityOptions.makeCustomAnimation(StudentProfilePicUpload.this, R.anim.fade_in, R.anim.fade_out);
                                startActivity(intent, options.toBundle());
                                finish();
                            }
                        }, 3000);
                    }
                    else {
                        if(string1.contains("error1")){
                            AlertDialog builder = new AlertDialog.Builder(StudentProfilePicUpload.this).create();
                            builder.setIcon(R.mipmap.ic_launcher_round);
                            builder.setTitle(Html.fromHtml("<font color='#FF0000'>RentZHub</font>"));
                            builder.setMessage("Error in updating profile pic. Retry with small Image size.");
                            builder.show();
                        }
                        if(string1.contains("error2")){
                            AlertDialog builder = new AlertDialog.Builder(StudentProfilePicUpload.this).create();
                            builder.setIcon(R.mipmap.ic_launcher_round);
                            builder.setTitle(Html.fromHtml("<font color='#FF0000'>RentZHub</font>"));
                            builder.setMessage("Error in connection.");
                            builder.show();
                        }
                    }
                }

                @Override
                protected String doInBackground(Void... params) {

                    ImageProcessClass imageProcessClass = new ImageProcessClass();

                    HashMap<String, String> HashMapParams = new HashMap<String, String>();

                    HashMapParams.put(rollno, userrollno);

                    HashMapParams.put(ImagePath, ConvertImage);

                    String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                    return FinalData;
                }
            }
            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

            AsyncTaskUploadClassOBJ.execute();
        }
        else {
            AlertDialog builder = new AlertDialog.Builder(this).create();
            builder.setIcon(R.mipmap.ic_launcher_round);
            builder.setTitle(Html.fromHtml("<font color='#FF0000'>College Attendance Android App</font>"));
            builder.setMessage("Select an Image");
            builder.show();
        }
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null)
                    {
                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (chk==true)

                    chk = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profilepic.jpeg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}
