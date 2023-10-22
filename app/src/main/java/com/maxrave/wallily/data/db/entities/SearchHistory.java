package com.maxrave.wallily.data.db.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "search_history")
public class SearchHistory {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String searchHistory;
    public SearchHistory(@NonNull String searchHistory) {
        this.searchHistory = searchHistory;
    }

    public String getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(String searchHistory) {
        this.searchHistory = searchHistory;
    }
}
