package project.internship.tcs.collegeattendance;


import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddMultipleStudents extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_FILE_REQUEST = 1;
    private String selectedFilePath;
    private String SERVER_URL = project.internship.tcs.collegeattendance.URL.URL_add_multiplestudents;
    com.beardedhen.androidbootstrap.BootstrapButton select,upload,cancel;
    TextView file;
    ProgressBar progress;
    LinearLayout base;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmultiplestudents);
        select=findViewById(R.id.select);
        cancel=findViewById(R.id.cancel);
        upload=findViewById(R.id.upload);
        select.setOnClickListener(this);
        cancel.setOnClickListener(this);
        upload.setOnClickListener(this);
        file=findViewById(R.id.file);
        progress=findViewById(R.id.progress);
        base=findViewById(R.id.base);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.cancel){
            Intent i=new Intent(AddMultipleStudents.this,TeacherMainActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(AddMultipleStudents.this, R.anim.fade_in, R.anim.fade_out);
            startActivity(i,options.toBundle());
        }
        if(v.getId()==R.id.select){

            //on attachment icon click
            showFileChooser();
        }
        if(v.getId()==R.id.upload){

            //on upload button Click
            if(selectedFilePath != null){
                dialog = ProgressDialog.show(AddMultipleStudents.this,"","Uploading File...",true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //creating new thread to handle Http Operations
                        uploadFile(selectedFilePath);
                    }
                }).start();
            }else{
                Toast.makeText(AddMultipleStudents.this,"Please choose a File First",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    if(selectedFilePath.substring(selectedFilePath.lastIndexOf(".")+1).equalsIgnoreCase("csv")||selectedFilePath.substring(selectedFilePath.lastIndexOf(".")+1).equalsIgnoreCase("xls")) {
                        upload.setEnabled(true);
                        file.setText(selectedFilePath.substring(selectedFilePath.lastIndexOf("/") + 1));
                    }
                    else{
                        upload.setEnabled(false);
                        file.setText("");
                        Snackbar snackbar = Snackbar
                                .make(base, "Select .CSV or .XLS File only", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }}
                else {
                    upload.setEnabled(false);
                    file.setText("");
                    Snackbar snackbar = Snackbar
                            .make(base, "Select File from Internal Storage only.", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }}
            }

    //android upload file to server
    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    file.setText("");
                    Snackbar snackbar=Snackbar.make(base,"Selected File does not exist.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();
                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            file.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://rentzhub.co.in/tcs_data/uploads/"+ fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddMultipleStudents.this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(AddMultipleStudents.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddMultipleStudents.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }
}