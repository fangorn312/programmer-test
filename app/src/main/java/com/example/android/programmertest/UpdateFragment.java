package com.example.android.programmertest;


import android.Manifest;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    private static final int WRITE_REQUEST_CODE = 300;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String url;
//    private EditText editTextUrl;

    public UpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update, container, false);

//        editTextUrl = view.findViewById(R.id.editTextUrl);

        Button downloadButton = view.findViewById(R.id.buttonDownload);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if SD card is present or not
//                if (CheckForSDCard.isSDCardPresent()) {

                //check if app has permission to write to the external storage.
                if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Get the URL entered
                    url = "https://raw.githubusercontent.com/fangorn312/epi1-16/master/%D0%9B%D1%8B%D1%87%D0%B5%D0%BD%D0%BA%D0%BE/%D0%9B%D0%B0%D0%B11/lab1.m";
//                        url = editTextUrl.getText().toString();
                    new DownloadFile().execute(url);

                } else {
                    //If permission is not present request for the same.
                    EasyPermissions.requestPermissions(getActivity(), getString(R.string.write_file), WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }


//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "SD Card not found", Toast.LENGTH_LONG).show();

//                }
            }

        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //Download the file once permission is granted
        url = "https://raw.githubusercontent.com/fangorn312/epi1-16/master/%D0%9B%D1%8B%D1%87%D0%B5%D0%BD%D0%BA%D0%BE/%D0%9B%D0%B0%D0%B11/lab1.m";
//        url = editTextUrl.getText().toString();
        new DownloadFile().execute(url);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(getActivity());
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = getActivity().getFilesDir().getAbsolutePath() + File.separator;
//                folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getActivity().getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }
}
