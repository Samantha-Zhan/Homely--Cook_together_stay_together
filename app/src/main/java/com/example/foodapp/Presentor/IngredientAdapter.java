package com.example.foodapp.Presentor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.foodapp.R;

public class IngredientAdapter extends BaseAdapter {
    private String[][] ingredients;
    LayoutInflater mInflater;  // take the my-listview-detail layout,
    Context context;
    // "inflate" it with content and connect it with
    //the myListView

    public IngredientAdapter(Context c, String[][] ingredients) {
        this.context = c;
        this.ingredients = ingredients;
        this.mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return ingredients.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(convertView == null){
            v = mInflater.inflate(R.layout.ingredient_sample,null);
        }
        TextView ingredientName = v.findViewById(R.id.ingredientName);
        TextView amount  = v.findViewById(R.id.amount);
        String name = ingredients[position][0];
        String gram = ingredients[position][1];
        if(name.isEmpty()|| name == null){
            ingredientName.setText(" ");
        }
        else {
            ingredientName.setText(name);
        }
        if(gram.isEmpty()||gram==null){
            amount.setText(" ");
        }
        else{
            amount.setText(gram);
        }
        return v;
    }



}

