package ddwu.mobile.finalproject.ma01_20191012;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    EditText etName;
    EditText etPhone;
    EditText etAddress;
    EditText etMemo;
    String placeId;
    String placeName;
    String placeNumber;
    String placeAddress;
    MyPlace mp;
    MyPlaceDBManager myPlaceDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        myPlaceDBManager = new MyPlaceDBManager(this);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etMemo = findViewById(R.id.etMemo);


        /*placeName = getIntent().getStringExtra("name");
        placeNumber = getIntent().getStringExtra("phone");
        placeAddress = getIntent().getStringExtra("address");*/
        mp = (MyPlace) getIntent().getSerializableExtra("myplace");
        placeId = mp.getPlaceId();
        placeName = mp.getName();
        placeNumber = mp.getNumber();
        placeAddress = mp.getAddress();

        etName.setText(placeName);
        etPhone.setText(placeNumber);
        etAddress.setText(placeAddress);



    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                Toast.makeText(this, "Save Place information", Toast.LENGTH_SHORT).show();
                mp.setName(etName.getText().toString());
                mp.setNumber(etPhone.getText().toString());
                mp.setAddress(etAddress.getText().toString());
                mp.setMemo(etMemo.getText().toString());
                boolean result = myPlaceDBManager.addNewPlace(mp);
                if (result) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("myplace", mp);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
                finish();
                break;
            case R.id.btnClose:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}
