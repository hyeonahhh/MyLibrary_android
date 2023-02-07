package ddwu.mobile.finalproject.ma01_20191012;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import noman.googleplaces.PlaceType;

public class LibraryActivity extends AppCompatActivity {
    //final int ADD_CODE = 100;
    final int UPDATE_CODE = 200;

    ArrayList<BookDto> bookArrayList = null;
    ListView listView;
    MyAdapter myAdapter;
    BookDBManager bookDBManager;
    private EditText editSearch;
    ArrayList<BookDto> list = null;


    int selectmenu = -1;    //select된 메뉴 int값으로 구분
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        bookDBManager = new BookDBManager(this);
        listView = findViewById(R.id.listView);
        editSearch = findViewById(R.id.searchText);
        bookArrayList = new ArrayList<BookDto>();
        selectmenu = 1;

        bookArrayList = bookDBManager.getAllBook();
        myAdapter = new MyAdapter(this, R.layout.listview_book, bookArrayList);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookDto book = bookArrayList.get(position);
                Intent intent = new Intent(LibraryActivity.this, UpdateActivity.class);
                intent.putExtra("book", book);
                startActivityForResult(intent, UPDATE_CODE);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
                builder.setTitle("도서 삭제")
                        .setMessage(bookArrayList.get(pos).getBookName() + "을(를) 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean result = bookDBManager.removeBook(bookArrayList.get(pos).get_id());
                                if (result) {
                                    Toast.makeText(LibraryActivity.this, "삭제 완료", Toast.LENGTH_SHORT);
                                    bookArrayList.clear();
                                    bookArrayList.addAll(bookDBManager.getAllBook());
                                    myAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(LibraryActivity.this, "삭제 실패", Toast.LENGTH_SHORT);
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();

                return true;
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editSearch.getText().toString();
                search(text);
            }
        });
    }

    public void search(String charText) {
        bookArrayList.clear();
        if (selectmenu == 1) {
            list = bookDBManager.getAllBook();
            if (charText.length() == 0) {
                bookArrayList.addAll(list);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getBookName().toLowerCase().contains(charText)) {
                        bookArrayList.add(list.get(i));
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        } else if (selectmenu == 2) {
            list = bookDBManager.getReadBook(Integer.toString(R.id.r2));
            if (charText.length() == 0) {
                bookArrayList.addAll(bookDBManager.getReadBook(Integer.toString(R.id.r2)));
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getBookName().toLowerCase().contains(charText)) {
                        bookArrayList.add(list.get(i));
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        } else if (selectmenu == 3) {
            list = bookDBManager.getReadBook(Integer.toString(R.id.r3));
            if (charText.length() == 0) {
                bookArrayList.addAll(bookDBManager.getReadBook(Integer.toString(R.id.r3)));
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getBookName().toLowerCase().contains(charText)) {
                        bookArrayList.add(list.get(i));
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        } else if (selectmenu == 4) {
            list = bookDBManager.getReadBook(Integer.toString(R.id.r1));
            if (charText.length() == 0) {
                bookArrayList.addAll(bookDBManager.getReadBook(Integer.toString(R.id.r1)));
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getBookName().toLowerCase().contains(charText)) {
                        bookArrayList.add(list.get(i));
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        }
        myAdapter.notifyDataSetChanged();
    }
    public void onClick(View v) {
        int idint = -1;
        switch (v.getId()) {
            case R.id.btn_Search:
                Intent searchIntent = new Intent(LibraryActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.b1:
                selectmenu = 1;
                bookArrayList.clear();
                bookArrayList.addAll(bookDBManager.getAllBook());
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.b2:
                selectmenu = 2;
                bookArrayList.clear();
                idint = R.id.r2;
                bookArrayList.addAll(bookDBManager.getReadBook(Integer.toString(idint)));
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.b3:
                selectmenu = 3;
                bookArrayList.clear();
                idint = R.id.r3;
                bookArrayList.addAll(bookDBManager.getReadBook(Integer.toString(idint)));
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.b4:
                selectmenu = 4;
                bookArrayList.clear();
                idint = R.id.r1;
                bookArrayList.addAll(bookDBManager.getReadBook(Integer.toString(idint)));
                myAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onResume() {
        int idint = -1;
        super.onResume();
        bookArrayList.clear();
        //bookArrayList.addAll(bookDBManager.getAllBook());
        if (selectmenu == 1) {
            bookArrayList.addAll(bookDBManager.getAllBook());
        }
        else if (selectmenu == 2) {
            idint = R.id.r2;
            bookArrayList.addAll(bookDBManager.getReadBook(Integer.toString(idint)));
        } else if (selectmenu == 3) {
            idint = R.id.r3;
            bookArrayList.addAll(bookDBManager.getReadBook(Integer.toString(idint)));
        } else if (selectmenu == 4) {
            idint = R.id.r1;
            bookArrayList.addAll(bookDBManager.getReadBook(Integer.toString(idint)));
        }
        myAdapter.notifyDataSetChanged();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == UPDATE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "책 정보 수정 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "책 정보 수정 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
