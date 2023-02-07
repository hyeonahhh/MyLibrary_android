package ddwu.mobile.finalproject.ma01_20191012;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    BookDto book;
    TextView name;
    TextView author;
    TextView publisher;
    EditText memo;
    TextView descripton;
    ImageView image;
    BookDBManager bookDBManager;
    ImageView mImageView;
    private ImageFileManager imageFileManager = null;
    final static String TAG = "Toast";
    RadioGroup cate;

    private String mCurrentPhotoPath;
    private static final int REQUEST_TAKE_PHOTO = 200;
    private static final int REQUEST_GALLERY_IMAGE = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        book = (BookDto) getIntent().getSerializableExtra("book");
        imageFileManager = new ImageFileManager(this);

        name = (TextView)findViewById(R.id.addTitle);
        author = (TextView)findViewById(R.id.addAuthor);
        publisher = (TextView)findViewById(R.id.addPublisher);
        //bookPrice = findViewById(R.id.text04);
        //bookRating = findViewById(R.id.rating);
        image = findViewById(R.id.addImage);
        memo = findViewById(R.id.addMemo);
        cate = findViewById(R.id.addCate);
        descripton = (TextView)findViewById(R.id.addDescription);
        descripton.setMovementMethod(ScrollingMovementMethod.getInstance());
        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setTitle("notification")
                        .setMessage("Please select")
                        .setIcon(R.mipmap.ic_launcher)
                        .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dispatchTakePictureIntent();
                            }
                        })
                        .setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
                            }
                        })
                        .setPositiveButton("Cancel", null)
                        .setCancelable(true);
                Dialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

            }
        });


        name.setText(book.getBookName());
        author.setText(book.getBookAuthor());
        publisher.setText(book.getBookPublisher());
        descripton.setText(book.getBookDescription());
        cate.check(R.id.r4);
        book.setBookCate(R.id.r4);


        Log.i("hyeonah", "AddActivity");

        cate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId ) {
                if (checkedId == R.id.r1) {
                    book.setBookCate(R.id.r1);
                } else if (checkedId == R.id.r2) {
                    book.setBookCate(R.id.r2);
                } else if (checkedId == R.id.r3) {
                    book.setBookCate(R.id.r3);
                } else if (checkedId == R.id.r4) {
                    book.setBookCate(R.id.r4);
                }
            }
        });

        if (book.getImageFileName() == null) {
            image.setImageResource(R.mipmap.ic_launcher);
            Log.i("hyeonah", "파일 이름이 null");
        }
        else {
            Log.i("hyeonah", "여기도 돌아가나? " + book.getImageFileName());
            image.setImageBitmap(imageFileManager.getBitmapFromExternal(book.getImageFileName()));}

        bookDBManager = new BookDBManager(this);

        PermissionListener permissionlistener = new PermissionListener() {

            @Override
            public void onPermissionGranted() {

                Toast.makeText(AddActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                Toast.makeText(AddActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

            }

        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\nPlease turn on permissions at [Setting] > [Permission] ")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


        //앨범선택, 사진촬영, 취소 다이얼로그 생성


    }

    /*원본 사진 파일 저장*/
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Log.i("hyeonah", "000000000");
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "ddwu.mobile.finalproject.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                Log.i("hyeonah", photoFile.getName());
                book.setPlusimageFileName(photoFile.getName());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    /*현재 시간 정보를 사용하여 파일 정보 생성*/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("hyeonah", mCurrentPhotoPath);
        return image;
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_Add:
                if (name.getText() == null || author.getText() == null || publisher.getText() == null ||
                        name.getText().toString().equals("") || author.getText().toString().equals("") || publisher.getText().toString().equals("")) {
                    Toast.makeText(this, "필수 정보를 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "필수 정보 입력 안함");
                    //setResult(RESULT_CANCELED);
                }
                else {
                    book.setBookName(name.getText().toString());
                    book.setBookAuthor(author.getText().toString());
                    book.setBookPublisher(publisher.getText().toString());
                    book.setBookMemo(memo.getText().toString());
                    book.setBookDescription(descripton.getText().toString());
                    //book.setPrice(Integer.parseInt(bookPrice.getText().toString()));
                    //book.setRating(bookRating.getRating());



                    if (bookDBManager.addNewBook(book)) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("book", book);
                        setResult(RESULT_OK, resultIntent);
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                    finish();
                }
                break;
            case R.id.btn_Updatecancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.i("hyeonah", mCurrentPhotoPath);
            book.setPlusType(1);
            setPic();
        } else if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK) {
            book.setPlusType(2);
            sendPicture(data.getData());
        }
    }

    private void sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        mImageView.setImageBitmap(bitmap);//이미지 뷰에 비트맵 넣기
        book.setPlusimageFileName(imagePath);
    }
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    /*사진의 크기를 ImageView에서 표시할 수 있는 크기로 변경*/
    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }
}
