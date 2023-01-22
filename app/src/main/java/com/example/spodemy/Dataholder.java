package com.example.spodemy;

public class Dataholder {
    String Name, Age, Gender, Height, Weight, BMI, Country, State, District, WaterIntake, FoodTracker;

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getBMI() {
        return BMI;
    }
    public void setBMI(String bmi) { BMI = bmi; }


    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getWaterIntake() {
        return WaterIntake;
    }

    public void setWaterIntake(String waterIntake) {
        WaterIntake = waterIntake;
    }

    public String getFoodTracker() {
        return FoodTracker;
    }

    public void setFoodTracker(String foodTracker) {
        FoodTracker = foodTracker;
    }

    public Dataholder(String name, String age, String height, String weight, String gender,String bmi, String country, String state, String district, String waterIntake, String foodTracker) {
        Name = name;
        Age = age;
        Height = height;
        Weight = weight;
        Gender = gender;
        BMI = bmi;
        Country = country;
        State = state;
        District = district;
        WaterIntake = waterIntake;
        FoodTracker = foodTracker;
    }

    public Dataholder(String name, String age,String gen){
        Name = name;
        Age = age;
        Gender = gen;
    }
}
