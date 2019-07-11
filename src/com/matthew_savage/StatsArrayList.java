package com.matthew_savage;

public class StatsArrayList {
    private int titlesTotal;
    private int titlesReading;
    private int titlesFinished;
    private int pagesRead;
    private int favorites;
    private int blacklisted;
    private int dailyPages;
    private String genreOne;
    private String genreTwo;
    private String genreThree;

    public int getTitlesTotal() {
        return titlesTotal;
    }

    public void setTitlesTotal(int titlesTotal) {
        this.titlesTotal = titlesTotal;
    }

    public int getTitlesReading() {
        return titlesReading;
    }

    public void setTitlesReading(int titlesReading) {
        this.titlesReading = titlesReading;
    }

    public int getTitlesFinished() {
        return titlesFinished;
    }

    public void setTitlesFinished(int titlesFinished) {
        this.titlesFinished = titlesFinished;
    }

    public int getPagesRead() {
        return pagesRead;
    }

    public void setPagesRead(int pagesRead) {
        this.pagesRead = pagesRead;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(int blacklisted) {
        this.blacklisted = blacklisted;
    }

    public int getDailyPages() {
        return dailyPages;
    }

    public void setDailyPages(int dailyPages) {
        this.dailyPages = dailyPages;
    }

    public String getGenreOne() {
        return genreOne;
    }

    public void setGenreOne(String genreOne) {
        this.genreOne = genreOne;
    }

    public String getGenreTwo() {
        return genreTwo;
    }

    public void setGenreTwo(String genreTwo) {
        this.genreTwo = genreTwo;
    }

    public String getGenreThree() {
        return genreThree;
    }

    public void setGenreThree(String genreThree) {
        this.genreThree = genreThree;
    }

    public StatsArrayList(int titlesTotal, int titlesReading, int titlesFinished, int pagesRead, int favorites, int blasklisted, int dailyPages, String genreOne, String genreTwo, String genreThree) {
        this.titlesTotal = titlesTotal;
        this.titlesReading = titlesReading;
        this.titlesFinished = titlesFinished;
        this.pagesRead = pagesRead;
        this.favorites = favorites;
        this.blacklisted = blasklisted;
        this.dailyPages = dailyPages;
        this.genreOne = genreOne;
        this.genreTwo = genreTwo;
        this.genreThree = genreThree;
    }
}
