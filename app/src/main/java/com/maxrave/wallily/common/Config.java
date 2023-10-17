package com.maxrave.wallily.common;

import com.maxrave.wallily.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {

    public static final String BASE_URL = "https://pixabay.com/" ;
    public static final String API_KEY = "38906081-54bee281fad3c5780116e20ec" ;

    public static ArrayList<String> listKeyword = new ArrayList<>(Arrays.asList("" ,"backgrounds", "fashion", "nature", "science", "education", "feelings", "health", "people", "religion", "places", "animals", "industry", "computer", "food", "sports", "transportation", "travel", "buildings", "business", "music"));
    public static ArrayList<Integer> listChipId = new ArrayList<>(Arrays.asList(R.id.chipAll, R.id.chipBackgrounds, R.id.chipFashion, R.id.chipNature, R.id.chipScience, R.id.chipEducation, R.id.chipFeelings, R.id.chipHealth, R.id.chipPeople, R.id.chipReligion, R.id.chipPlaces, R.id.chipAnimals, R.id.chipIndustry, R.id.chipComputer, R.id.chipFood, R.id.chipSports, R.id.chipTransportation, R.id.chipTravel, R.id.chipBuildings, R.id.chipBusiness, R.id.chipMusic));

//    public ArrayList<String> listCatalog = new ArrayList<String>("backgrounds", "fashion", "nature", "science", "education", "feelings", "health", "people", "religion", "places", "animals", "industry", "computer", "food", "sports", "transportation", "travel", "buildings", "business", "music");
}
