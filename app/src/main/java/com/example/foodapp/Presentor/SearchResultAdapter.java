package com.example.foodapp.Presentor;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Module.DetailRecipe;
import com.example.foodapp.Module.FavDB;
import com.example.foodapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;


public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private DetailRecipe[] detailRecipes;
    private Context context;
    //private FavDB favDB;
    private OnNoteListener onNoteListener;

    public SearchResultAdapter(DetailRecipe[] detailRecipes, Context context, OnNoteListener onNoteListener) {
        this.onNoteListener = onNoteListener;
        this.detailRecipes = detailRecipes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        favDB = new FavDB(context);
//        //create table on first
//        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
//        boolean firstStart = prefs.getBoolean("firstStart", true);
//        if (firstStart) {
//            createTableOnFirstStart();
//        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_template, parent, false);

        return new ViewHolder(view, onNoteListener);
    }



    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {
        final DetailRecipe detailRecipe = detailRecipes[position];
        new ImageLoadTask(detailRecipe.getImage(), holder.imageView).execute();
        holder.titleTextView.setText(detailRecipe.getTitle());
        holder.propertyTime.setText("Ready in "+ detailRecipe.getReadyInMinutes()+" mins");
        String url = detailRecipe.getSourceUrl();
        if(url.indexOf(".com")<0){
            holder.sourceUrl.setText("N/A");
        }
        else {
            holder.sourceUrl.setText(url.substring(8, url.indexOf(".com"))+".com");
        }
        holder.propertyServing.setText(detailRecipe.getCalories()+" cals");
        holder.propertyPrice.setText("$"+Math.round(detailRecipe.getPricePerServing())/100.0+" per serving");
    }



    @Override
    public int getItemCount() {
        return detailRecipes.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView titleTextView;
        Button favBtn;
        TextView propertyServing;
        TextView sourceUrl;
        TextView propertyTime;
        TextView propertyPrice;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View v, OnNoteListener onNoteListener) {
            super(v);

            imageView = v.findViewById(R.id.recipeImageView);
            titleTextView = v.findViewById(R.id.title);
            favBtn = v.findViewById(R.id.bookmarkButton);
            //likeCountTextView = itemView.findViewById(R.id.likeCountTextView);
            propertyServing = (TextView) v.findViewById(R.id.propertyServing);
            sourceUrl = v.findViewById(R.id.sourceUrl);
            propertyTime = (TextView) v.findViewById(R.id.propertyTime);
            propertyPrice = v.findViewById(R.id.propertyPrice);
            this.onNoteListener = onNoteListener;
            //add to fav btn
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    DetailRecipe detailRecipe = detailRecipes[position];
                    if(detailRecipe.getFavStatus().equals("0")) {
                        detailRecipe.setFavStatus("1");
                        favBtn.setBackgroundResource(R.drawable.ic_added_bookmark_24);
                    }
                    else if(detailRecipe.getFavStatus().equals("1")) {
                        favBtn.setBackgroundResource(R.drawable.book_mark);
                    }

//                    likeClick(detailRecipe, favBtn);
                }
            });
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface  OnNoteListener{
        void onNoteClick(int position);
    }

//    private void createTableOnFirstStart() {
//        favDB.insertEmpty();
//
//        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean("firstStart", false);
//        editor.apply();
//    }

//    private void readCursorData(DetailRecipe detailRecipe, ViewHolder viewHolder) {
//        Cursor cursor = favDB.read_all_data(detailRecipe.getId()+"");
//        SQLiteDatabase db = favDB.getReadableDatabase();
//        try {
//            while (cursor.moveToNext()) {
//                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
//                detailRecipe.setFavStatus(item_fav_status);
//
//                //check fav status
//                if (item_fav_status != null && item_fav_status.equals("1")) {
//                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_added_bookmark_24);
//                } else if (item_fav_status != null && item_fav_status.equals("0")) {
//                    viewHolder.favBtn.setBackgroundResource(R.drawable.book_mark);
//                }
//            }
//        } finally {
//            if (cursor != null && cursor.isClosed())
//                cursor.close();
//            db.close();
//        }
//
//    }

//    // like click
//    private void likeClick (DetailRecipe detailRecipe, Button favBtn) {
//        DatabaseReference refLike = FirebaseDatabase.getInstance().getReference().child("likes");
//        final DatabaseReference upvotesRefLike = refLike.child(detailRecipe.getId()+"");
//
//        if (detailRecipe.getFavStatus().equals("0")) {
//
//            detailRecipe.setFavStatus("1");
//            favDB.insertIntoTheDatabase(detailRecipe.getTitle(), detailRecipe.getImage(),
//                    detailRecipe.getId()+"", detailRecipe.getFavStatus());
//            favBtn.setBackgroundResource(R.drawable.ic_added_bookmark_24);
//            favBtn.setSelected(true);
//
//            upvotesRefLike.runTransaction(new Transaction.Handler() {
//                @NonNull
//                @Override
//                public Transaction.Result doTransaction(@NonNull final MutableData mutableData) {
//                    try {
//                        Integer currentValue = mutableData.getValue(Integer.class);
//                        if (currentValue == null) {
//                            mutableData.setValue(1);
//                        } else {
//                            mutableData.setValue(currentValue + 1);
//                        }
//                    } catch (Exception e) {
//                        throw e;
//                    }
//                    return Transaction.success(mutableData);
//                }
//
//                @Override
//                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//                    System.out.println("Transaction completed");
//                }
//            });
//
//
//
//        } else if (detailRecipe.getFavStatus().equals("1")) {
//            detailRecipe.setFavStatus("0");
//            favDB.remove_fav(detailRecipe.getId()+"");
//            favBtn.setBackgroundResource(R.drawable.book_mark);
//            favBtn.setSelected(false);
//
//            upvotesRefLike.runTransaction(new Transaction.Handler() {
//                @NonNull
//                @Override
//                public Transaction.Result doTransaction(@NonNull final MutableData mutableData) {
//                    try {
//                        Integer currentValue = mutableData.getValue(Integer.class);
//                        if (currentValue == null) {
//                            mutableData.setValue(1);
//                        } else {
//                            mutableData.setValue(currentValue - 1);
//                        }
//                    } catch (Exception e) {
//                        throw e;
//                    }
//                    return Transaction.success(mutableData);
//                }
//
//                @Override
//                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//                    System.out.println("Transaction completed");
//                }
//            });
//        }




}
