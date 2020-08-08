package com.example.foodapp.Module;

public class DetailRecipe {
        private long id;
        private String title;
        private int readyInMinutes;
        private int searvings;
        private String sourceUrl;
        private int healthScore;
        private double pricePerServing;
        private String image;
        private String[] instructions;
        private int calories;
        private String favStatus = "0";
        private String label = "";
        private String summary;
        private String[][] ingredients;

    public DetailRecipe() {
    }
    //for keyRecipe info(less info displayed)
    public DetailRecipe(long id, String title, int readyInMinutes, String sourceUrl, int healthScore, double pricePerServing, String image, int calories) {
        this.id = id;
        this.title = title;
        this.readyInMinutes = readyInMinutes;
        this.sourceUrl = sourceUrl;
        this.healthScore = healthScore;
        this.pricePerServing = pricePerServing;
        this.image = image;
        this.calories = calories;
    }
    //for detail reicpe generation, full info
    public DetailRecipe(long id, String title, int readyInMinutes, int searvings, String sourceUrl, int healthScore, double pricePerServing, String image, String[] instructions, int calories, String summary, String[][] ingredients) {
        this.id = id;
        this.title = title;
        this.readyInMinutes = readyInMinutes;
        this.searvings = searvings;
        this.sourceUrl = sourceUrl;
        this.healthScore = healthScore;
        this.pricePerServing = pricePerServing;
        this.image = image;
        this.instructions = instructions;
        this.calories = calories;
        this.summary = summary;
        this.ingredients = ingredients;
    }

    public String[][] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[][] ingredients) {
        this.ingredients = ingredients;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCalories(){
        return calories;
    }
    public void setCalories(int calories){
        this.calories = calories;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public int getSearvings() {
        return searvings;
    }

    public void setSearvings(int searvings) {
        this.searvings = searvings;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(int healthScore) {
        this.healthScore = healthScore;
    }

    public double getPricePerServing() {
        return pricePerServing;
    }

    public void setPricePerServing(double pricePerServing) {
        this.pricePerServing = pricePerServing;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
    }
}
