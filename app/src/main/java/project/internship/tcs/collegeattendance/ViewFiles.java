package project.internship.tcs.collegeattendance;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class ViewFiles extends Fragment {

    WebView webview;
    SharedPreferences sp;
    String url,course,branch,year,section;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewfiles, container, false);

        sp=getActivity().getSharedPreferences("college_data",MODE_PRIVATE);
        course=sp.getString("course",null);
        branch=sp.getString("branch",null);
        year=sp.getString("year",null);
        section=sp.getString("section",null);
        webview=view.findViewById(R.id.webview);

        WebSettings wb=webview.getSettings();
        url=URL.URL_view_files+"?course="+course+"&branch="+branch+"&year="+year+
                "&section="+section;
        webview.loadUrl(url);
        wb.setJavaScriptEnabled(true);
        wb.setLoadsImagesAutomatically(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                webview.loadUrl(url);
                /*Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/
            }
        });
        /*webview.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substring(url.lastIndexOf("/")+1));
                DownloadManager dm = (DownloadManager)getActivity().getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getActivity(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();

            }
        });*/
        return view;

    }
}
