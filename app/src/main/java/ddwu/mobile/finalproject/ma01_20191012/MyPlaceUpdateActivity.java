package ddwu.mobile.finalproject.ma01_20191012;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyPlaceUpdateActivity extends AppCompatActivity {
    MyPlace mp;
    TextView name;
    TextView number;
    TextView address;
    TextView memo;
    MyPlaceDBManager myPlaceDBManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myplaceupdate);
        mp = (MyPlace) getIntent().getSerializableExtra("myplace");

        name = findViewById(R.id.mpupName);
        number = findViewById(R.id.mpupPhone);
        address = findViewById(R.id.mpupAddress);
        memo = findViewById(R.id.mpupMemo);

        name.setText(mp.getName());
        number.setText(mp.getNumber());
        address.setText(mp.getAddress());
        memo.setText(mp.getMemo());

        myPlaceDBManager = new MyPlaceDBManager(this);

    }
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnUpdate:
                if (name.getText() == null || number.getText() == null || address.getText() == null ||
                        name.getText().toString().equals("") || number.getText().toString().equals("") || address.getText().toString().equals("")) {
                    Toast.makeText(this, "필수 정보를 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("place update", "필수 정보 입력 안함");
                    setResult(RESULT_CANCELED);
                }
                else {
                    mp.setName(name.getText().toString());
                    mp.setNumber(number.getText().toString());
                    mp.setAddress(address.getText().toString());
                    mp.setMemo(memo.getText().toString());


                    if (myPlaceDBManager.modifyPlace(mp)) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("myplace", mp);
                        setResult(RESULT_OK, resultIntent);
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                    finish();
                }
                break;
            case R.id.btnUpdatecancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btnShare:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = "Place Name : " + mp.getName()
                        + "\nPlace Number : " + mp.getNumber()
                        + "\nPlace Address : " + mp.getAddress();
                intent.putExtra(Intent.EXTRA_TEXT, text);
                Intent chooser = Intent.createChooser(intent, "SHARE!!");
                startActivity(chooser);
                break;
            case R.id.callButton:
                Intent callintent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mp.getNumber()));
                startActivity(callintent);
                break;
        }

    }
}
