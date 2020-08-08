package com.example.foodapp.Presentor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapp.Module.DetailRecipe;
import com.example.foodapp.Module.FavDB;
import com.example.foodapp.R;

public class MyCustomAdaptor extends BaseAdapter {
    private String title;
    private int readyInMinutes;
    private int servings;
    private String sourceUrl;
    private String image;
    private DetailRecipe[] detailRecipes;
    private boolean access;
    private FavDB favDB;

    LayoutInflater mInflater;  // take the my-listview-detail layout,
    // "inflate" it with content and connect it with
    //the myListView

    public MyCustomAdaptor(Context c, DetailRecipe[] detailRecipes, FavDB favDB) {
        this.detailRecipes = detailRecipes;
        this.favDB = favDB;
        this.mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(detailRecipes==null||detailRecipes.length==0){
            access=false;
        }
        else{
            access=true;
        }
    }
    @Override
    public int getCount() {
        if(access){
            return detailRecipes.length;
        }
        return 10;
    }

    @Override
    public Object getItem(int position) {
        if(access){
            return detailRecipes[position];
        }
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
            v = mInflater.inflate(R.layout.search_result_template,null);
        }
        if(access){
            final DetailRecipe dr = detailRecipes[position];
            ImageView imageView = v.findViewById(R.id.recipeImageView);
            new ImageLoadTask(dr.getImage(), imageView).execute();

            TextView title = (TextView) v.findViewById(R.id.title);
            title.setText(dr.getTitle());

            TextView propertyTime = (TextView) v.findViewById(R.id.propertyTime);
            propertyTime.setText("Ready in "+ dr.getReadyInMinutes()+" mins");

            TextView sourceUrl = v.findViewById(R.id.sourceUrl);
            String url = dr.getSourceUrl();
            sourceUrl.setText(url.substring(7, url.indexOf(".com"))+".com");

            TextView propertyServing = (TextView) v.findViewById(R.id.propertyServing);
            propertyServing.setText(dr.getCalories()+" cals");
            TextView propertyPrice = v.findViewById(R.id.propertyPrice);
            propertyPrice.setText("$"+Math.round(dr.getPricePerServing())/100.0+" per serving");

            final Button bookmarkButton = v.findViewById(R.id.bookmarkButton);
            final View finalV = v;
//            bookmarkButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(dr.getFavStatus()==0){
//                        bookmarkButton.setBackground(finalV.getResources().getDrawable(R.drawable.ic_added_bookmark_24));
//                        dr.setFavStatus(1);
//
//                    }
//                    else{
//                        bookmarkButton.setBackground(finalV.getResources().getDrawable(R.drawable.book_mark));
//                        dr.setFavStatus(0);
//                    }
//
//
//                }
//            });
        }

        return v;
    }

    public void addToBookmarkWhenClicked(View view){
        Log.i("MyCustomAdaptor", "addToBookmarkWhenClicked: "+"adding to book mark");
    }


}

