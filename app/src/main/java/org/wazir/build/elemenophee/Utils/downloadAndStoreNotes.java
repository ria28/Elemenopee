package org.wazir.build.elemenophee.Utils;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

public class downloadAndStoreNotes {

    private static downloadAndStoreNotes downloadAndStoreNotes;

    Context context;


    private downloadAndStoreNotes() {
    }  //private constructor.

    public static downloadAndStoreNotes getInstance(Context context) {
        if (downloadAndStoreNotes == null) {
            downloadAndStoreNotes = new downloadAndStoreNotes();
            downloadAndStoreNotes.context = context;
        }

        return downloadAndStoreNotes;
    }


    public void openPDF(File outputFile, String link, String Title) {

        if (outputFile.exists()) {
            if (!isNetworkAvailable())
                Toast.makeText(context, "File will be downloded when network connection avialable..", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();
            downloadFile(link, Title);


        } else if (outputFile != null) {
            Toast.makeText(context, "File Opening", Toast.LENGTH_SHORT).show();

            Uri path;
            Intent pdf = new Intent(Intent.ACTION_VIEW);
            pdf.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            pdf.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                path = FileProvider.getUriForFile(context, "ibas.provider", outputFile);

                pdf.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                path = Uri.fromFile(outputFile);
            }

            pdf.setDataAndType(path, "application/pdf");
            try {
                context.startActivity(pdf);
            } catch (ActivityNotFoundException e) {
                e.getMessage();
            }
        }
    }


    private void downloadFile(String download_url, String Title) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(download_url));
        request.setTitle(Title)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                        | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String nameOfFile = URLUtil.guessFileName(download_url, null,
                MimeTypeMap.getFileExtensionFromUrl(download_url));
        request.setDestinationInExternalPublicDir("/Elemenophee/Notes", Title + ".pdf");

        DownloadManager manager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
