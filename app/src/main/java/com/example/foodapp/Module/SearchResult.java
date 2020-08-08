package com.example.foodapp.Module;

public class SearchResult {
    private Long id;

    private DetailRecipe detailRecipe;

    public SearchResult() {
    }

    public SearchResult(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public DetailRecipe getDetailRecipe(){
        return detailRecipe;
    }

    public void setDetailRecipe(DetailRecipe detailRecipe){
        this.detailRecipe = detailRecipe;
    }
}
