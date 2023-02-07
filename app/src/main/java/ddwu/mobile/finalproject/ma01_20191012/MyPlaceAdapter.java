package ddwu.mobile.finalproject.ma01_20191012;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyPlaceAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<MyPlace> placeArrayList;
    private LayoutInflater layoutInflater;
    String type;

    public MyPlaceAdapter(Context context, int layout, ArrayList<MyPlace> placeArrayList) {
        this.context = context;
        this.layout = layout;
        this.placeArrayList = placeArrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return placeArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return placeArrayList.get(position).get_id();
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        final int position = pos;
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvAddress = convertView.findViewById(R.id.tvAuthor);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(String.valueOf(placeArrayList.get(position).getName()));
        viewHolder.tvAddress.setText(placeArrayList.get(position).getAddress());

        //viewHolder.tvImage.setImageResource(bookArrayList.get(position).getImage());
        return convertView;
    }
    static class ViewHolder {
        TextView tvName;
        TextView tvAddress;
    }
}
