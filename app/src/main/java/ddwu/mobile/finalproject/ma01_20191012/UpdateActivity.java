package ddwu.mobile.finalproject.ma01_20191012;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {
    BookDto book;
    TextView name;
    TextView author;
    TextView publisher;
    EditText memo;
    ImageView image;
    TextView description;
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
        setContentView(R.layout.activity_update);
        book = (BookDto) getIntent().getSerializableExtra("book");
        imageFileManager = new ImageFileManager(this);

        name = (TextView)findViewById(R.id.updateTitle);
        author = (TextView)findViewById(R.id.updateAuthor);
        publisher = (TextView)findViewById(R.id.updatePublisher);
        //bookPrice = findViewById(R.id.text04);
        //bookRating = findViewById(R.id.rating);
        image = findViewById(R.id.updateImage);
        memo = findViewById(R.id.updateMemo);
        description = findViewById(R.id.updateDescription);
        cate = findViewById(R.id.updateCate);
        mImageView = (ImageView) findViewById(R.id.imageView2);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
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
        memo.setText(book.getBookMemo());
        description.setText(book.getBookDescription());
        //cate.check(R.id.r4);
        //book.setBookCate(R.id.r4);
        if (book.getBookCate() == R.id.r1)
            cate.check(R.id.rr1);
        if (book.getBookCate() == R.id.r2)
            cate.check(R.id.rr2);
        if (book.getBookCate() == R.id.r3)
            cate.check(R.id.rr3);
        if (book.getBookCate() == R.id.r4)
            cate.check(R.id.rr4);


        cate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId ) {
                if (checkedId == R.id.rr1) {
                    book.setBookCate(R.id.r1);
                } else if (checkedId == R.id.rr2) {
                    book.setBookCate(R.id.r2);
                } else if (checkedId == R.id.rr3) {
                    book.setBookCate(R.id.r3);
                } else if (checkedId == R.id.rr4) {
                    book.setBookCate(R.id.r4);
                }
            }
        });

        if (book.getImageFileName() == null) {
            image.setImageResource(R.mipmap.ic_launcher);
            Log.i("hyeonah", "?????? ????????? null");
        }
        else {
            Log.i("hyeonah", "????????? ????????????? " + book.getImageFileName());
            image.setImageBitmap(imageFileManager.getBitmapFromExternal(book.getImageFileName()));}

        if (book.getPlusimageFileName() == null) {
            mImageView.setImageResource(R.mipmap.ic_launcher);
            Log.i("hyeonah", "??????2 ????????? null");
        }
        else {
            Log.i("hyeonah", "????????? ????????????? " + book.getPlusimageFileName());
            if (book.getPlusType() == 1) {
                mImageView.setImageBitmap(imageFileManager.getBitmapFromExternal(book.getPlusimageFileName()));
            } else if (book.getPlusType() == 2) {
                Bitmap bitmap = BitmapFactory.decodeFile(book.getPlusimageFileName());//????????? ?????? ??????????????? ??????
                mImageView.setImageBitmap(bitmap);
            }
        }

        bookDBManager = new BookDBManager(this);

    }

    /*?????? ?????? ?????? ??????*/
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
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
                Log.i("hyeonah", "original image name : " + book.getPlusimageFileName());
                Log.i("hyeonah", mCurrentPhotoPath);
                book.setPlusimageFileName(photoFile.getName());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    /*?????? ?????? ????????? ???????????? ?????? ?????? ??????*/
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
            case R.id.btn_Update:
                if (name.getText() == null || author.getText() == null || publisher.getText() == null ||
                        name.getText().toString().equals("") || author.getText().toString().equals("") || publisher.getText().toString().equals("")) {
                    Toast.makeText(this, "?????? ????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "?????? ?????? ?????? ??????");
                    setResult(RESULT_CANCELED);
                }
                else {
                    book.setBookName(name.getText().toString());
                    book.setBookAuthor(author.getText().toString());
                    book.setBookPublisher(publisher.getText().toString());
                    book.setBookMemo(memo.getText().toString());
                    book.setBookDescription(description.getText().toString());
                    //book.setPrice(Integer.parseInt(bookPrice.getText().toString()));
                    //book.setRating(bookRating.getRating());


                    if (bookDBManager.modifyBook(book)) {
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
            case R.id.btn_share:
                book.setBookMemo(memo.getText().toString());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = "Book Name : " + book.getBookName()
                        + "\nBook Author : " + book.getBookAuthor()
                        + "\n??? ?????? ???????????????.";
                intent.putExtra(Intent.EXTRA_TEXT, text);
                Intent chooser = Intent.createChooser(intent, "SHARE!!");
                startActivity(chooser);
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

        String imagePath = getRealPathFromURI(imgUri); // path ??????
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//????????? ?????? ??????????????? ??????
        mImageView.setImageBitmap(bitmap);//????????? ?????? ????????? ??????
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


    /*????????? ????????? ImageView?????? ????????? ??? ?????? ????????? ??????*/
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
        Log.i("hyeonah", "Bitmap name : " + mCurrentPhotoPath);
        mImageView.setImageBitmap(bitmap);
    }
}
