package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapp.Fragments.exploreFragment;
import com.example.foodapp.Module.DetailRecipe;
import com.example.foodapp.Presentor.ImageLoadTask;
import com.example.foodapp.Presentor.IngredientAdapter;
import com.example.foodapp.Presentor.InstructionAdapter;
import com.example.foodapp.Presentor.SearchPresentor;
import com.example.foodapp.Presentor.SearchResultAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowRecipeActivity extends AppCompatActivity {
    NonScrollListView listView;
    NonScrollListView ingredientList;
    String[] instruction;
    String[][] ingredients;
    long id;
    SearchPresentor searchPresentor;
    DetailRecipe[] detailRecipes;
    ImageView recipeImageView;
    Context context;
    TextView title;
    public static String TAG = ShowRecipeActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_after_clicked);
        ingredientList = findViewById(R.id.ingredientList);
        title = findViewById(R.id.title);
        searchPresentor = new SearchPresentor();
        context = this;
        recipeImageView = findViewById(R.id.recipeImageView);
        if(getIntent().hasExtra("DetailRecipe object")){
            id=getIntent().getLongExtra("DetailRecipe object", 0);
        }
        Long[] list = {id};
        String url=searchPresentor.generateRecipeInfoUrl(list);
        listView = findViewById(R.id.istructionList);
        getRecipeInfo(url);
    }

    public void getRecipeInfo(String url){
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client =  new OkHttpClient();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                searchPresentor.alertUserAboutError("There was an error, please try again.", ShowRecipeActivity.this);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    String searchResultJSONData = response.body().string();

                    if (response.isSuccessful()){
                        //Log.v(TAG, "the url is: "+searchUrl);
                         detailRecipes=searchPresentor.receiveRecipesInfo(searchResultJSONData, true);
                         final DetailRecipe detailRecipe=detailRecipes[0];
                        new ImageLoadTask(detailRecipe.getImage(), recipeImageView).execute();
                        Log.d(TAG, "onResponse: imageLink"+detailRecipe.getImage());
                         instruction = detailRecipe.getInstructions();
                         ingredients = detailRecipe.getIngredients();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView title = findViewById(R.id.title);
                                title.setText(detailRecipe.getTitle());
                                TextView summary = findViewById(R.id.summaryTextView);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    summary.setText(Html.fromHtml(detailRecipe.getSummary(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    summary.setText(Html.fromHtml(detailRecipe.getSummary()));
                                }
                                TextView propertyTime = findViewById(R.id.propertyTime);
                                propertyTime.setText("Ready in "+ detailRecipe.getReadyInMinutes()+" mins");

                                TextView sourceUrl = findViewById(R.id.sourceUrl);
                                String url = detailRecipe.getSourceUrl();
                                sourceUrl.setText(url.substring(8, url.indexOf(".com"))+".com");

                                TextView propertyServing = (TextView) findViewById(R.id.propertyServing);
                                propertyServing.setText(detailRecipe.getCalories()+" cals");
                                TextView propertyPrice = findViewById(R.id.propertyPrice);
                                propertyPrice.setText("$"+Math.round(detailRecipe.getPricePerServing())/100.0+" per serving");

                                final Button bookmarkButton = findViewById(R.id.bookmarkButton);
                                bookmarkButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(detailRecipe.getFavStatus().equals("0")) {
                                            detailRecipe.setFavStatus("1");
                                            bookmarkButton.setBackgroundResource(R.drawable.ic_added_bookmark_24);
                                        }
                                        else if(detailRecipe.getFavStatus().equals("1")) {
                                            bookmarkButton.setBackgroundResource(R.drawable.book_mark);
                                        }

                                    }
                                });
                                InstructionAdapter instructionAdapter = new InstructionAdapter(context, instruction);
                                listView.setAdapter(instructionAdapter);
                                IngredientAdapter ingredientAdapter = new IngredientAdapter(context, ingredients);
                                ingredientList.setAdapter(ingredientAdapter);

                            }
                        });



                    }
                    else{
                        //Log.v(TAG, "response failed!");
                        searchPresentor.alertUserAboutError("There was an error, please try again.", ShowRecipeActivity.this);
                    }
                } catch (IOException e) {
                    Log.v(TAG, "IOException caught: "+e);
                }
                catch (JSONException e){
                    Log.v(TAG, "JSONException caught: "+e);
                }
            }
        });
    }


}