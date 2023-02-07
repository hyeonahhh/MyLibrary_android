package ddwu.mobile.finalproject.ma01_20191012;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyPlaceActivity extends AppCompatActivity {

    ArrayList<MyPlace> list;
    ListView listView;

    MyPlaceAdapter placeAdapter;
    final int UPDATE_CODE = 200;
    MyPlaceDBManager myPlaceDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myplace);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myPlaceDBManager = new MyPlaceDBManager(this);

        list = (ArrayList<MyPlace>) getIntent().getSerializableExtra("places");
        listView = findViewById(R.id.myPlaceList);

        placeAdapter = new MyPlaceAdapter(this, R.layout.place_adapter, list);
        listView.setAdapter(placeAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(MyPlaceActivity.this);
                builder.setTitle("장소 삭제")
                        .setMessage(list.get(pos).getName() + "을(를) 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean result = myPlaceDBManager.removePlace(list.get(pos).get_id());
                                if (result) {
                                    Toast.makeText(MyPlaceActivity.this, "삭제 완료", Toast.LENGTH_SHORT);
                                    list.clear();
                                    list.addAll(myPlaceDBManager.getAllPlace());
                                    placeAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MyPlaceActivity.this, "삭제 실패", Toast.LENGTH_SHORT);
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();

                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyPlace mp = list.get(position);
                Intent intent = new Intent(MyPlaceActivity.this, MyPlaceUpdateActivity.class);
                intent.putExtra("myplace", mp);
                startActivityForResult(intent, UPDATE_CODE);
            }
        });

    }
    protected void onResume() {
        super.onResume();
        list.clear();
        list.addAll(myPlaceDBManager.getAllPlace());
        placeAdapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) { super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "place 정보 수정 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "place 정보 수정 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
