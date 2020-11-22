package com.xtravels.models;

public enum PrivacyType {
    PUBLIC("Public"),PRIVATE("Private");

    public  final String name;
    PrivacyType(String name) {
        this.name=name;
    }

    public String getName() {
        return this.name;
    }
}
