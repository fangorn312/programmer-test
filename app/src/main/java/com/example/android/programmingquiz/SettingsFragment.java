package com.example.android.programmingquiz;


import android.Manifest;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements EasyPermissions.PermissionCallbacks, OnBackPressedListener {

    private static final int WRITE_REQUEST_CODE = 100;
    private static final String TAG = SettingsFragment.class.getSimpleName();
    private static final String URL_TO_DB = "https://github.com/fangorn312/programming-quiz/raw/master/database/myDB.sqlite";
    private static final String URL_TO_INFO = "https://raw.githubusercontent.com/fangorn312/programming-quiz/master/database/dataBaseInfo.txt";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onBackPressed() {
        MainActivity.instance().changeView(IFragments.MENU);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

//    updateButton = view.findViewById(R.id.update_btn);
//
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MainActivity.instance().changeView(IFragments.UPDATE);
//            }
//        });
//        editTextUrl = view.findViewById(R.id.editTextUrl);

        Button aboutButton = view.findViewById(R.id.about_btn);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.instance().changeView(IFragments.ABOUT);
            }
        });

        Button downloadButton = view.findViewById(R.id.update_btn);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if SD card is present or not
//                if (CheckForSDCard.isSDCardPresent()) {

                //check if app has permission to write to the external storage.
                if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET)) {
                    //Get the URL entered
//                        URL_TO_DB = editTextUrl.getText().toString();
                    new InternetCheck(getActivity()).execute();
                    //new DownloadFile(getActivity()).execute(URL_TO_INFO, URL_TO_DB);

                } else {
                    //If permission is not present request for the same.
                    EasyPermissions.requestPermissions(getActivity(), getString(R.string.write_file), WRITE_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET);

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
//        URL_TO_DB = editTextUrl.getText().toString();
        new InternetCheck(getActivity()).execute();
//        new DownloadFile(getActivity()).execute(URL_TO_INFO, URL_TO_DB);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }


    static class InternetCheck extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<FragmentActivity> contextRef;

        InternetCheck(FragmentActivity activity) {
            contextRef = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("InternetCheck", "Started");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Socket sock = new Socket();
                sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
                sock.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean internet) {
            FragmentActivity context = contextRef.get();
            if (context==null) return;
            if (internet) new DownloadFile(context).execute(URL_TO_INFO, URL_TO_DB);
            else Toast.makeText(context.getApplicationContext(),
                    "Включите интернет", Toast.LENGTH_LONG).show();

        }
    }


    /**
     * Async Task to download file from URL
     */
    private static class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;

        private WeakReference<FragmentActivity> contextRef;

        DownloadFile(FragmentActivity activity) {
            contextRef = new WeakReference<>(activity);
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.instance().mDBHelper.close();
            FragmentActivity context = contextRef.get();
            if (context == null) return;
            this.progressDialog = new ProgressDialog(context);
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
//                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                //Extract file name from URL
                fileName = "DB_INFO.txt";

                //External directory path to save file
                folder = MainActivity.instance().mDBHelper.getDbPath();

//                folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";

                //Create androiddeft folder if it does not exist

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder+ fileName);

                byte data[] = new byte[1024];

                long total = 0;

                publishProgress("" + 0);
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called

                    //Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                BufferedReader br = new BufferedReader(new FileReader(new File(folder + fileName)));

                String db_info;
                if ((db_info = br.readLine()) == null) db_info = "";

                String oldInfo = MainActivity.instance().mDBHelper.loadFromCache();
                if (oldInfo.equals(db_info)) return "Нет обновлений";


                //-----------------------------//
                //******************************
                url = new URL(f_url[1]);
                connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                input = new BufferedInputStream(url.openStream(), 8192);

                //Extract file name from URL
                fileName = DBHelper.DB_NAME;
                //folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+File.separator;
                //External directory path to save file
//                folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";

                //Create androiddeft folder if it does not exist

                // Output stream to write file
                output = new FileOutputStream(folder + fileName, false);


                String outFileName = folder+fileName;
                output = new FileOutputStream(outFileName, false);
                byte[] buffer = new byte[4096];
                total =0;
                while ((count = input.read(buffer)) > 0) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));
                    output.write(buffer, 0, count);
                }
                output.flush();
                output.close();
                input.close();

//                byte data2[] = new byte[1024];
//
//                total = 0;
//
//                while ((count = input.read(data2)) != -1) {
//                    total += count;
//                    // publishing the progress....
//                    // After this onProgressUpdate will be called
//                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
//                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));
//
//                    // writing data to file
//                    output.write(data, 0, count);
//                }
//
//                // flushing output
//                output.flush();
//
//                // closing streams
//                output.close();
//                input.close();

                MainActivity.instance().mDBHelper.putToCache(db_info);
                return "Версия базы : " + db_info;

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
//            MainActivity.instance().mDBHelper.copyDataBaseFromDownload();
            //            MainActivity.instance().mDBHelper.createDataBaseForced();
            //MainActivity.instance().mDBHelper.createDataBaseForced();
            MainActivity.instance().mDBHelper.openDataBaseForced();
            FragmentActivity context = contextRef.get();
            if (context != null)
                // Display File path after downloading
                Toast.makeText(context.getApplicationContext(),
                        message, Toast.LENGTH_LONG).show();
        }
    }

}
