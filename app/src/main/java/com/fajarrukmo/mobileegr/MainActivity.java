package com.fajarrukmo.mobileegr;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fajarrukmo.mobileegr.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

import static com.fajarrukmo.mobileegr.R.id.imageView;
import static com.fajarrukmo.mobileegr.R.id.no_telp;
import static com.fajarrukmo.mobileegr.R.id.pelanggaran;
import static com.fajarrukmo.mobileegr.R.id.t_lokasi;
import static com.fajarrukmo.mobileegr.R.id.t_pelanggaran;
import static com.fajarrukmo.mobileegr.R.id.t_status;
import static com.fajarrukmo.mobileegr.R.id.t_waktu;
import static com.fajarrukmo.mobileegr.R.id.textView2;
import static com.fajarrukmo.mobileegr.R.id.textView4;
import static com.fajarrukmo.mobileegr.R.id.textView5;
import static com.fajarrukmo.mobileegr.R.id.textView7;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    static final int REQUEST_IMAGE_CAPTURE = 12;
    static final int REQUEST_TAKE_PHOTO = 13;
    static final int REQUEST_PICK_PHOTO = 20;

    private static final int REQUEST_CAMERA = 111;
    private static final int SELECT_FILE = 200;

    final CharSequence[] items = {"Pilih dari Gallery", "Batal"};
//    final CharSequence[] items = {"Ambil Foto", "Pilih dari Gallery", "Batal"};

    ImageView mImageview;
    Uri picuri;
    EditText nama,npk,notelp,jabatan;
    String sstatus,swaktu,slokasipt,slokasiafd,spelanggaran;

    TextView t4;
    EditText enam;
    TextView t5;
    EditText enpk;
    TextView t7;
    EditText etel;
    TextView t2;
    EditText ejab;
    TextView tsta;
    TextView twak;
    TextView tpel;

    boolean is_action = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isStoragePermissionGranted();

        final Spinner status = (Spinner) findViewById(R.id.status);
        ArrayAdapter<CharSequence> status_adapter = ArrayAdapter.createFromResource(this,R.array.status_array, android.R.layout.simple_expandable_list_item_1);
        status_adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        status.setAdapter(status_adapter);

        final Spinner lokasi_pt = (Spinner) findViewById(R.id.lokasi_pt);
        ArrayAdapter<CharSequence> lokasi_adapter = ArrayAdapter.createFromResource(this,R.array.lokasi_pt_array, android.R.layout.simple_expandable_list_item_1);
        lokasi_adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        lokasi_pt.setAdapter(lokasi_adapter);

        final Spinner lokasi_afd = (Spinner) findViewById(R.id.lokasi_afd);
//        lokasi_afd.MODE_DIALOG;
        lokasi_adapter = ArrayAdapter.createFromResource(this, R.array.lokasi_afd_array, android.R.layout.simple_expandable_list_item_1);
//        ArrayAdapter<CharSequence> lokasi_adapter = ArrayAdapter.createFromResource(this,R.array.lokasi_afd_array, android.R.layout.simple_expandable_list_item_1);
        lokasi_adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        lokasi_afd.setAdapter(lokasi_adapter);


        final Spinner pelanggaran = (Spinner) findViewById(R.id.pelanggaran);
//        pelanggaran.MODE_DIALOG;
        ArrayAdapter<CharSequence> pelanggaran_adapter = ArrayAdapter.createFromResource(this,R.array.pelanggaran_array, android.R.layout.simple_expandable_list_item_1);
        pelanggaran_adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        pelanggaran.setAdapter(pelanggaran_adapter);


        final Spinner waktu = (Spinner) findViewById(R.id.waktu);
//        waktu.MODE_DIALOG;
        ArrayAdapter<CharSequence> waktu_adapter = ArrayAdapter.createFromResource(this,R.array.waktu_array, android.R.layout.simple_expandable_list_item_1);
        waktu_adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        waktu.setAdapter(waktu_adapter);


        nama = (EditText)findViewById(R.id.nama);
        npk = (EditText)findViewById(R.id.npk);
        notelp = (EditText)findViewById(no_telp);
        jabatan = (EditText)findViewById(R.id.jabatan);

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, status.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                sstatus = status.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        waktu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, waktu.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                swaktu = waktu.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        pelanggaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, pelanggaran.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                spelanggaran = pelanggaran.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        lokasi_pt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, lokasi.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                slokasipt = lokasi_pt.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        lokasi_afd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, lokasi.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                slokasiafd = lokasi_afd.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        t4 = (TextView) findViewById(R.id.textView4);
        enam = (EditText)findViewById(R.id.nama);
        t5 = (TextView) findViewById(R.id.textView5);
        enpk = (EditText)findViewById(R.id.npk);
        t7 = (TextView) findViewById(R.id.textView7);
        etel = (EditText)findViewById(R.id.no_telp);
        t2 = (TextView) findViewById(R.id.textView2);
        ejab = (EditText)findViewById(R.id.jabatan);
        tsta = (TextView) findViewById(R.id.t_status);
        twak = (TextView) findViewById(R.id.t_waktu);
        tpel = (TextView) findViewById(t_pelanggaran);


        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Log.d("Tes","tes mode condition");
                    t4.setText("Keterangan");
//                    enam.setVisibility(View.GONE);

                    t5.setVisibility(View.GONE);
                    enpk.setVisibility(View.GONE);
                    t7.setVisibility(View.GONE);
                    etel.setVisibility(View.GONE);
                    t2.setVisibility(View.GONE);
                    ejab.setVisibility(View.GONE);
                    tsta.setVisibility(View.GONE);
                    twak.setVisibility(View.GONE);
                    tpel.setVisibility(View.GONE);

                    status.setVisibility(View.GONE);
                    pelanggaran.setVisibility(View.GONE);
                    waktu.setVisibility(View.GONE);

                    is_action = false;
                } else {
                    // The toggle is disabled
                    Log.d("Tes","tes mode action");
                    t4.setText("Nama");
//                    enam.setVisibility(View.VISIBLE);

                    t5.setVisibility(View.VISIBLE);
                    enpk.setVisibility(View.VISIBLE);
                    t7.setVisibility(View.VISIBLE);
                    etel.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.VISIBLE);
                    ejab.setVisibility(View.VISIBLE);
                    tsta.setVisibility(View.VISIBLE);
                    twak.setVisibility(View.VISIBLE);
                    tpel.setVisibility(View.VISIBLE);

                    status.setVisibility(View.VISIBLE);
                    pelanggaran.setVisibility(View.VISIBLE);
                    waktu.setVisibility(View.VISIBLE);

                    is_action = true;
                }
            }
        });


    }

    public void change_mode(View view){
//        Log.d("Tes","tes mode");
    }
    public void ambil_gambar(View view){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());
        builder.setTitle("Tambah Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Ambil Foto")) {
//                    PROFILE_PIC_COUNT = 1;
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else if (items[item].equals("Pilih dari Gallery")) {
//                    PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_FILE);
                } else if (items[item].equals("Batal")) {
//                    PROFILE_PIC_COUNT = 0;
                    dialog.dismiss();
                }
                Log.d("number",String.valueOf(item));
            }
        });
        builder.show();


//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
    }

    public void pilih_gambar(View view){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 123);//one can be replaced with any action code
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("reqCode", Integer.toString(requestCode));
        Log.d("resCode", Integer.toString(resultCode));
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageview = (ImageView) findViewById(imageView);
            mImageview.setImageBitmap(imageBitmap);

            Bitmap bm = ((BitmapDrawable) mImageview.getDrawable()).getBitmap();
            String a = saveImage(bm,0);
            Log.d("camera save",a);
        }else
        if(requestCode==SELECT_FILE && resultCode == -1) {

//            if Build.VERSION.SDK_INT
            try {
                Uri pickedImage = data.getData();
                // Let's read picked image path using content resolver
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

                // At the end remember to close the cursor or you will end with the RuntimeException!
                cursor.close();

                Uri uri = data.getData();
//                Log.d("uri gambar", uri.toString());

                Log.i("Tag", "onActivityResult: file path : " + getRealPathFromURI(uri));
                Bitmap bMap = BitmapFactory.decodeFile(getRealPathFromURI(uri));

//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                //use the bitmap as you like

//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageview = (ImageView) findViewById(imageView);
//                mImageview.setImageBitmap(imageBitmap);

//                String b = saveImage(bMap,1);
//                Log.d("select image",b);
//                Uri.fromFile(new File(b));
//                mImageview.setImageURI(uri);

//                mImageview.setImageBitmap(decodeSampledBitmapFromResource(getResources()
//                        ,R.id.imageView,100,100));

                mImageview.setImageBitmap(getResizedBitmap(bMap,100,100));

                Image_path = getRealPathFromURI(uri);
            } catch (Exception e) {
                Log.e("hey",e.toString());
                alert();
            }
        }
        //        }else if(requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK){
//            if (resultCode == RESULT_OK) {
//                try {
//                    final Uri imageUri = data.getData();
//                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                    mImageview.setImageBitmap(selectedImage);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
////                    Toast.makeText("Something went wrong", Toast.LENGTH_LONG).show();
//                }
//
//            }else {
////                Toast.makeText("You haven't picked Image",Toast.LENGTH_LONG).show();
//            }
//        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED
                            &&
                            checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED

                    ) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {
                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    public void alert(){
        Context context = getApplicationContext();
        CharSequence text = "Coba gambar resolusi lebih rendah";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();

        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private String saveImage(Bitmap finalBitmap,Integer c) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        Log.d("root",root +" Root value in saveImage Function");
        File myDir = new File(root + "/EGR");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String iname = "EGR-" + n + ".jpg";
        File file = new File(myDir, iname);
        if (file.exists()) {
            file.delete();
        }else{
            Log.d("filenya","tidak ada");
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            if(c==1) {
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }else if(c==0){
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            }
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("error disini","sini lo");
        }

        try {
            MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);

                        }
                    });
//            Image_path = (root+"/EGR/"+iname);
            Uri urix=Uri.fromFile(file);
//            Uri.parse()
            Image_path = getRealPathFromURI(urix);

            File[] files = myDir.listFiles();
            int numberOfImages=files.length;
            Log.d("total",urix.toString());
            Log.d("filenya",Image_path.toString());

        }catch (Exception e){
            Log.d("erorr",e.toString());
            Log.d("perhatian","gak ada memory eksternal");
        }
        return file.getAbsolutePath().toString();

    }


    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    String mCurrentPhotoPath,Image_path;

    public void kirim(View view){
        try {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);

            try {
                Uri uri = Uri.parse(Image_path);
                sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                sendIntent.setType("image/jpeg");
            }catch (Exception e){
                Log.d("linda",e.toString());
                Toast.makeText(this, "Pilih Foto Dahulu", Toast.LENGTH_SHORT).show();
            }

            String wa_text = "";

            if(is_action) {
                wa_text = "Laporan Pelanggaran Unsafe Action\n";

                wa_text += "\nNama       :\n - " + nama.getText().toString();
                wa_text += "\nNPK        :\n - " + npk.getText().toString();
                wa_text += "\nNo telp    :\n - " + notelp.getText().toString();
                wa_text += "\nJabatan    :\n - " + jabatan.getText().toString();

                wa_text += "\nStatus     :\n - " + sstatus;
                wa_text += "\nWaktu      :\n - " + swaktu;
                wa_text += "\nPelanggaran:\n - " + spelanggaran;
                wa_text += "\nLokasi     :\n - " + slokasiafd + " - " + slokasipt;
            }else{
                wa_text = "Laporan Unsafe Condition\n";

                wa_text += "\nKeterangan :\n - " + nama.getText().toString();
                wa_text += "\nLokasi     :\n - " + slokasiafd + " - " + slokasipt;
            }
            sendIntent.putExtra(Intent.EXTRA_TEXT, wa_text);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(sendIntent);
            } catch (android.content.ActivityNotFoundException ex) {
//                Toast ToastHelper;
//                ToastHelper.MakeShortText("Whatsapp have not been installed.");
            }

        }catch (Exception e){
            Log.d("excellent",e.toString());
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
