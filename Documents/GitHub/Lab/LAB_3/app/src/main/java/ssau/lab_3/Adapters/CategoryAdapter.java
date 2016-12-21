package ssau.lab_3.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ssau.lab_3.DBModel.Category;
import ssau.lab_3.R;


public class CategoryAdapter extends ArrayAdapter<Category> {
    Context context;
    Category[] categories;

    public CategoryAdapter(Context context, Category[] categories) {
        super(context, R.layout.category_list_item, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.category_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.tvCategory);
        textView.setText(categories[position].getName());
        rowView.setTag(categories[position]);
        return rowView;
    }
}