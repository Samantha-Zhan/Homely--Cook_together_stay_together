package com.example.foodapp.Presentor;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Module.DetailRecipe;
import com.example.foodapp.Module.FavDB;
import com.example.foodapp.R;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    private ArrayList<DetailRecipe> favourites;
    private Context context;
    private FavDB favDB;

    public FavAdapter(ArrayList<DetailRecipe> favourites, Context context) {
        this.favourites = favourites;
        this.context = context;
        this.favDB = favDB;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavDB(context);
        //create table on first
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true); // default value true
        if(firstStart){
            createTableOnFirstStart();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item, parent, false);
        return new ViewHolder(view);
    }

    private void createTableOnFirstStart() {
        favDB.insertEmpty();
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.ViewHolder holder, int position) {
        final DetailRecipe dr = favourites.get(position);
        readCursorData(dr, holder);

    }

    private void readCursorData(DetailRecipe detailRecipe, ViewHolder viewHolder) {
        Cursor cursor = favDB.select_all_favorite_list();
        SQLiteDatabase db = favDB.getReadableDatabase();
        try{
            while(cursor.moveToNext()){
                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                detailRecipe.setFavStatus(item_fav_status);
                if(item_fav_status!=null&&item_fav_status.equals("1")){
                    viewHolder.bookmarkButton.setBackgroundResource(R.drawable.ic_added_bookmark_24);
                }
                else if(item_fav_status!=null&&item_fav_status.equals("0")){
                    viewHolder.bookmarkButton.setBackgroundResource(R.drawable.book_mark);
                }
            }
        }finally {
            if(cursor!=null&&cursor.isClosed()){
                cursor.close();
            }
            db.close();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button bookmarkButton;
        ImageView imageView;
        TextView title;
        TextView propertyTime;
        TextView sourceUrl;
        TextView propertyServing;
        TextView propertyPrice;
        public ViewHolder(@NonNull final View v) {
            super(v);
            final DetailRecipe dr = favourites.get(getAdapterPosition());
            imageView = v.findViewById(R.id.recipeImageView);
            new ImageLoadTask(dr.getImage(), imageView).execute();

            title = (TextView) v.findViewById(R.id.title);
            title.setText(dr.getTitle());

             propertyTime = (TextView) v.findViewById(R.id.propertyTime);
            propertyTime.setText("Ready in "+ dr.getReadyInMinutes()+" mins");

            sourceUrl = v.findViewById(R.id.sourceUrl);
            String url = dr.getSourceUrl();
            sourceUrl.setText(url.substring(7, url.indexOf(".com"))+".com");

            propertyServing = (TextView) v.findViewById(R.id.propertyServing);
            propertyServing.setText(dr.getCalories()+" cals");

            propertyPrice = v.findViewById(R.id.propertyPrice);
            propertyPrice.setText("$"+Math.round(dr.getPricePerServing())/100.0+" per serving");
            bookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dr.getFavStatus().equals("0")){
                        bookmarkButton.setBackground(itemView.getResources().getDrawable(R.drawable.ic_added_bookmark_24));
                        dr.setFavStatus("1");
                        favDB.insertIntoTheDatabase(dr.getTitle(), dr.getImage(), dr.getId()+"", dr.getFavStatus());
                    }
                    else{
                        bookmarkButton.setBackground(itemView.getResources().getDrawable(R.drawable.book_mark));
                        dr.setFavStatus("0");
                        favDB.remove_fav(dr.getId()+"");
                    }


                }
            });
        }
    }
}
