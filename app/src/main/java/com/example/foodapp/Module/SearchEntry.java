package com.example.foodapp.Module;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SearchEntry {
    private String query;
    private String[] cuisin ;
    // One or more (comma separated) of the following: african, chinese, japanese, korean,
    // vietnamese, thai, indian, british, irish, french, italian, mexican, spanish, middle eastern,
    // jewish, american, cajun, southern, greek, german, nordic, eastern european, caribbean, or
    // latin american.
    private String[] diet;
    //Possible values are: pescetarian, lacto vegetarian, ovo vegetarian, vegan, and vegetarian.
    private String[] intolerances;
    //recipes must not have ingredients that could cause problems for people with one of the given
    // tolerances. Possible values are: dairy, egg, gluten, peanut, sesame, seafood, shellfish, soy,
    // sulfite, tree nut, and wheat .
    private int numResults;
    //The number of results to return (between 0 and 100).
    private String[] type;
    //The type of the recipes. One of the following: main course, side dish, dessert, appetizer,
    // salad, bread, breakfast, soup, beverage, sauce, or drink.
    private static final String API_KEY = "a096573eeebb4d13b96d1fb1a735705c";


    public SearchEntry(String query, String[] cuisin, String[] diet, String[] intolerances, int numResults, String[] type) {
        this.query = query;
        this.cuisin = cuisin;
        this.diet = diet;
        this.intolerances = intolerances;
        this.numResults = numResults;
        this.type = type;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String[] getCuisin() {
        return cuisin;
    }

    public void setCuisin(String[] cuisin) {
        this.cuisin = cuisin;
    }

    public String[] getDiet() {
        return diet;
    }

    public void setDiet(String[] diet) {
        this.diet = diet;
    }

    public String[] getIntolerances() {
        return intolerances;
    }

    public void setIntolerances(String[] intolerances) {
        this.intolerances = intolerances;
    }

    public int getNumResults() {
        return numResults;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    //get search results based on query and filters applied
    public String generateUrl() {
        final String FIX_DIVIDER = "%252C%20";
        final String SPACE_PLACEHOLDER="%20";
        String searchUrl="https://api.spoonacular.com/recipes/search?apiKey="+API_KEY;
        searchUrl = searchUrl+"&maxReadyTime=50";
        if(cuisin.length!=0){
            searchUrl+="&cuisin=";
            for(int i=0; i<cuisin.length;i++){
                if(i!=cuisin.length-1){
                    searchUrl=searchUrl+cuisin[i]+FIX_DIVIDER;
                }
                else{
                    searchUrl=searchUrl+cuisin[i];
                }
            }
        }
        if(diet.length!=0){
            searchUrl+="&diet=";
            for(int i=0; i<diet.length;i++){
                if(i!=diet.length-1){
                    searchUrl=searchUrl+diet[i]+FIX_DIVIDER;
                }
                else{
                    searchUrl=searchUrl+diet[i];
                }
            }
        }
        if(intolerances.length!=0){
            searchUrl+="&intolerances=";
            for(int i=0; i<intolerances.length;i++){
                if(i!=intolerances.length-1){
                    searchUrl=searchUrl+intolerances[i]+FIX_DIVIDER;
                }
                else{
                    searchUrl=searchUrl+intolerances[i];
                }
            }
        }

        searchUrl=searchUrl+"&number="+numResults;
        if(type.length!=0){
            searchUrl+="&type=";
            for(int i=0; i<type.length;i++){
                if(i!=type.length-1){
                    searchUrl=searchUrl+type[i].replace(" ", SPACE_PLACEHOLDER)+FIX_DIVIDER;
                }
                else{
                    searchUrl=searchUrl+type[i].replace(" ", SPACE_PLACEHOLDER);
                }
            }
        }

        //https://api.spoonacular.com/recipes/search?apiKey=a096573eeebb4d13b96d1fb1a735705c&cuisin=Chinese&diet=Vegetarian&intolerances=egg&number=10&type=main%20course&query=tofu
        searchUrl=searchUrl+"&query="+encodeValue(query);
        return searchUrl;
    }

    //convert query String to url string
    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
