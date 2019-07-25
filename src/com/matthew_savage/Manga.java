package com.matthew_savage;

public class Manga {

    private int titleId;
    private String title;
    private String authors;
    private String status;
    private String summary;
    private String webAddress;
    private String genreTags;
    private int totalChapters;
    private int currentPage;
    private int lastChapterRead;
    private int lastChapterDownloaded;
    private int newChapters;
    private int favorite;

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getGenreTags() {
        return genreTags;
    }

    public void setGenreTags(String genreTags) {
        this.genreTags = genreTags;
    }

    public int getTotalChapters() {
        return totalChapters;
    }

    public void setTotalChapters(int totalChapters) {
        this.totalChapters = totalChapters;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastChapterRead() {
        return lastChapterRead;
    }

    public void setLastChapterRead(int lastChapterRead) {
        this.lastChapterRead = lastChapterRead;
    }

    public int getLastChapterDownloaded() {
        return lastChapterDownloaded;
    }

    public void setLastChapterDownloaded(int lastChapterDownloaded) {
        this.lastChapterDownloaded = lastChapterDownloaded;
    }

    public int getNewChapters() {
        return newChapters;
    }

    public void setNewChapters(int newChapters) {
        this.newChapters = newChapters;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public Manga(String title, String webAddress) {
        this.title = title;
        this.webAddress = webAddress;
    }

    public Manga(int titleId, String title, String summary) {
        this.titleId = titleId;
        this.title = title;
        this.summary = summary;
    }

    public Manga(int titleId, String title, String authors, String status, String summary, String webAddress, String genreTags, int totalChapters, int currentPage, int lastChapterRead, int lastChapterDownloaded, int newChapters, int favorite) {
        this.titleId = titleId;
        this.title = title;
        this.authors = authors;
        this.status = status;
        this.summary = summary;
        this.webAddress = webAddress;
        this.genreTags = genreTags;
        this.totalChapters = totalChapters;
        this.currentPage = currentPage;
        this.lastChapterRead = lastChapterRead;
        this.lastChapterDownloaded = lastChapterDownloaded;
        this.newChapters = newChapters;
        this.favorite = favorite;
    }
}