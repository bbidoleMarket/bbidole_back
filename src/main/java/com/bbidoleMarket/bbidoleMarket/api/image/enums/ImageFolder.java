package com.bbidoleMarket.bbidoleMarket.api.image.enums;

public enum ImageFolder {
    PROFILE("profile/"),
    LIST("list/");

    private final String path;

    ImageFolder(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}