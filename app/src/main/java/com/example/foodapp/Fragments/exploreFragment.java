package com.example.foodapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.foodapp.Module.DetailRecipe;
import com.example.foodapp.Module.SearchEntry;
import com.example.foodapp.Presentor.SearchPresentor;
import com.example.foodapp.Presentor.SearchResultAdapter;
import com.example.foodapp.R;
import com.example.foodapp.ShowRecipeActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link exploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class exploreFragment extends Fragment implements SearchResultAdapter.OnNoteListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DetailRecipe[] detailRecipes;
    private SearchPresentor searchPresentor;
    private RecyclerView resultsRecyclerView;
    private static final String API_KEY = "a096573eeebb4d13b96d1fb1a735705c";
    public static final String TAG = exploreFragment.class.getSimpleName();
    private EditText editText;
    private TextView textView;
    private SearchResultAdapter itemAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public exploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment exploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static exploreFragment newInstance(String param1, String param2) {
        exploreFragment fragment = new exploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        resultsRecyclerView = v.findViewById(R.id.result_list_view);
        // Get input text
        textView = v.findViewById(R.id.errorTextView);
        editText = v.findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.i(TAG, "onCreateView: inputText is "+editText.getText().toString());
                    SearchEntry searchEntry = new SearchEntry(editText.getText().toString(), new String[0], new String[0], new String[0], 10, new String[0]);
                    searchPresentor = new SearchPresentor(searchEntry);
                    getSearchResult(searchEntry);
                    return true;
                }
                return false;
            }
        });

        return v;
    }

    public void getSearchResult(SearchEntry searchEntry){
        String searchUrl=searchEntry.generateUrl();
        Request request = new Request.Builder().url(searchUrl).build();
        OkHttpClient client =  new OkHttpClient();
        okhttp3.Call call = client.newCall(request);
        Log.d(TAG, "getSearchResult: url is "+searchUrl);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                searchPresentor.alertUserAboutError("There was an error, please try again.", getContext());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    String searchResultJSONData = response.body().string();

                    if (response.isSuccessful()){
                        //Log.v(TAG, "the url is: "+searchUrl);
                        Long[] resultRecipeIds = searchPresentor.receiveSearchResults(searchResultJSONData);
                        if(resultRecipeIds.length==0){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Context c = getActivity();
                                    textView.setText("Sorry, there is no result for your entry!");

                                }
                            });
                        }
                        else{
                            String recipeBundleUrl = searchPresentor.generateRecipeInfoUrl(resultRecipeIds);
                            getRecipesInfo(recipeBundleUrl, false, resultsRecyclerView);
                        }
                         // only need basic info since it only used to show results


                    }
                    else{
                        //Log.v(TAG, "response failed!");
                        searchPresentor.alertUserAboutError("There was an error, please try again.", getContext());
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

    //fill the "detailRecipes" array according to the ids of the search result
    private void getRecipesInfo(String resultUrl, final boolean flag, final RecyclerView recyclerView){
        Request request = new Request.Builder().url(resultUrl).build();
        OkHttpClient client =  new OkHttpClient();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //searchPresentor.alertUserAboutError("There was an error, please try again.", exploreFragment.this);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String JSONData = response.body().string();

                    if (response.isSuccessful()){
                        detailRecipes = searchPresentor.receiveRecipesInfo(JSONData, flag);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Context c = getActivity();
                                itemAdapter = new SearchResultAdapter(detailRecipes, c, exploreFragment.this);
                                Log.d(TAG, "run: my adapter!");
                                recyclerView.setAdapter(itemAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            }
                        });
                    }
                    else{
                        //Log.v(TAG, "response failed!");
                        //searchPresentor.alertUserAboutError("There was an error, please try again.", MainActivity.this);
                    }
                }
                catch (IOException e) {
                    Log.v(TAG, "IOException caught: "+e);
                }
                catch (JSONException e){
                    Log.v(TAG, "JSONException caught: "+e);
                }



            }
        });

    }

    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: "+position);
        Intent intent = new Intent(getActivity(), ShowRecipeActivity.class);
      intent.putExtra("DetailRecipe object", detailRecipes[position].getId());
        getContext().startActivity(intent);
    }
}