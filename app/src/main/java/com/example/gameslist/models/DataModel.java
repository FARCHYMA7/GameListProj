package com.example.gameslist.models;

public class DataModel {

    private String title;
    private String imageGame;
    private String shortDescription;
    private String gameUrl;
    private String genre;
    private String platform;
    private String publisher;
    private String developer;
    private String releaseDate;

    public DataModel(String title, String imageGame, String shortDescription, String gameUrl, String genre, String platform, String publisher, String developer, String releaseDate) {
        this.title = title;
        this.imageGame = imageGame;
        this.shortDescription = shortDescription;
        this.gameUrl = gameUrl;
        this.genre = genre;
        this.platform = platform;
        this.publisher = publisher;
        this.developer = developer;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getImageGame() {
        return imageGame;
    }
    public String getShortDescription() {
        return shortDescription;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getReleaseDate() { return releaseDate; }


}
