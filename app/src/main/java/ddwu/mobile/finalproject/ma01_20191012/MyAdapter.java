package ddwu.mobile.finalproject.ma01_20191012;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<BookDto> bookArrayList;
    private LayoutInflater layoutInflater;
    private ImageFileManager imageFileManager = null;

    public MyAdapter(Context context, int layout, ArrayList<BookDto> bookArrayList) {
        this.context = context;
        this.layout = layout;
        this.bookArrayList = bookArrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageFileManager = new ImageFileManager(context);
    }

    @Override
    public int getCount() {
        return bookArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookArrayList.get(position).get_id();
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        final int position = pos;
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tvTitle);
            viewHolder.tvAuthor = convertView.findViewById(R.id.tvAuthor);
            viewHolder.tvImage = convertView.findViewById(R.id.ivImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(String.valueOf(bookArrayList.get(position).getBookName()));
        viewHolder.tvAuthor.setText(bookArrayList.get(position).getBookAuthor());
        if (bookArrayList.get(position).getImageFileName() == null) {
            viewHolder.tvImage.setImageResource(R.mipmap.ic_launcher);
            Log.i("hyeonah", "파일 이름이 null");
        }
        else {
            Log.i("hyeonah", "여기도 돌아가나? " + bookArrayList.get(position).getImageFileName());
            viewHolder.tvImage.setImageBitmap(imageFileManager.getBitmapFromExternal(bookArrayList.get(position).getImageFileName()));}
        //viewHolder.tvImage.setImageResource(bookArrayList.get(position).getImage());
        return convertView;
    }
    static class ViewHolder {
        TextView tvName;
        TextView tvAuthor;
        //TextView tvRating;
        ImageView tvImage;
    }
}
