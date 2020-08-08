package com.example.foodapp.Presentor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.foodapp.AlertDialogFragment;
import com.example.foodapp.MainActivity;
import com.example.foodapp.Module.DetailRecipe;
import com.example.foodapp.Module.SearchEntry;
import com.example.foodapp.Module.SearchResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchPresentor {
    private SearchEntry searchEntry;

    private static final String API_KEY = "a096573eeebb4d13b96d1fb1a735705c";
            //"139d07386a4a4333b578a8128391291a";
            //"52fdc867da7e4939b0c25e9944757909";
            //"a096573eeebb4d13b96d1fb1a735705c";

    public SearchPresentor(){

    }

    public SearchPresentor(SearchEntry searchEntry){
        this.searchEntry = searchEntry;
    }


    //collect the ids of result recipe, used to then generate data needed to present the result
    public Long[] receiveSearchResults(String jsonData) throws JSONException {
        JSONObject jo = new JSONObject(jsonData);
        JSONArray results = jo.getJSONArray("results");
        int numOfResults = results.length();
        Long[] urls = new Long[numOfResults];
        JSONObject jb = null;
        for(int i=0; i<numOfResults; i++){
            jb = results.getJSONObject(i);
            Long rId = jb.getLong("id");
            urls[i]=jb.getLong("id");
            //Log.d(TAG, "getSearchResult: "+sr.getTitle());
        }
        return urls;
    }
    public String generateRecipeInfoUrl(Long[] ids){
        String url = "https://api.spoonacular.com/recipes/informationBulk?apiKey="+API_KEY+"&ids=";
        for(int i =0; i<ids.length;i++){
            Long id = ids[i];
            if(id!=0L){
                if(i==ids.length-1){
                    url= url+id;
                }
                else{
                    url = url+id+",";
                }
            }
            else{
                break;
            }

        }
        return url;
    }


    public DetailRecipe[] receiveRecipesInfo(String jsonData, boolean flag) throws JSONException{
        JSONArray jsonArray = new JSONArray(jsonData);
        DetailRecipe[] detailRecipes = new DetailRecipe[jsonArray.length()];
        for(int i=0; i<jsonArray.length();i++){
            JSONObject jo = jsonArray.getJSONObject(i);
           detailRecipes[i] = extractInfo(jo, flag);
        }
        return detailRecipes;
    }

    // extractInfo from recipe JSON according to how much info is needed
    public DetailRecipe extractInfo(JSONObject jo, boolean flag) throws  JSONException{
        DetailRecipe detailRecipe = null;
        String summary = jo.getString("summary");
        String processedSummary = summary.substring(0,summary.indexOf(" calories</b>"));
        String calories = processedSummary.substring(processedSummary.lastIndexOf("<b>")+3);
        if(flag){
            JSONArray steps = jo.getJSONArray("analyzedInstructions").getJSONObject(0).getJSONArray("steps");
            String[] instructions = new String[steps.length()];
            for(int j = 0; j<instructions.length; j++){
                instructions[j] = steps.getJSONObject(j).getString("step");
                Log.d("SearchPresentor", "extractInfo: "+instructions);
            }
            JSONArray ingreds = jo.getJSONArray("extendedIngredients");
            String[][] ingredients = new String[ingreds.length()][2];
            for(int i =0; i<ingredients.length;i++){
                ingredients[i][0] = ingreds.getJSONObject(i).getString("name");
                ingredients[i][1] = new DecimalFormat("##.##").format(ingreds.getJSONObject(i).getDouble("amount"))+" "+ingreds.getJSONObject(i).getString("unit");
            }
            if(!summary.isEmpty()&&summary.indexOf(" Try")!=-1){
                    summary = summary.substring(0, summary.indexOf(" Try"));
            }
            detailRecipe = new DetailRecipe(jo.getLong("id"), jo.getString("title"),
                    jo.getInt("readyInMinutes"), jo.getInt("servings"), jo.getString("sourceUrl"),
                    jo.getInt("healthScore"), jo.getDouble("pricePerServing"), jo.getString("image"),
                    instructions, Integer.parseInt(calories), summary, ingredients);
        }
        else{
            detailRecipe = new DetailRecipe(jo.getLong("id"), jo.getString("title"),
                    jo.getInt("readyInMinutes"), jo.getString("sourceUrl"),
                    jo.getInt("healthScore"), jo.getDouble("pricePerServing"), jo.getString("image"),Integer.parseInt(calories));
        }
        return detailRecipe;
    }


    public void alertUserAboutError(String message, Context context) {
        //visually alert the user in the interface using Dialogs(a pop up)
        AlertDialogFragment dialog = new AlertDialogFragment(message);
        dialog.show(((FragmentActivity)(Activity)context).getSupportFragmentManager(), "error_dialog");
    }
}
