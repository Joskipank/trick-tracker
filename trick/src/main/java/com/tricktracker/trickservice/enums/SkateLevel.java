package com.tricktracker.trickservice.enums;

public enum SkateLevel {
    BEGINNER("Новичок"),
    INTERMEDIATE("Опытный"),
    PRO("Профи");

    private final String ruTitle;

    SkateLevel(String ruTitle){
        this.ruTitle = ruTitle;
    }

    public String getRuTitle(){
        return ruTitle;
    }
}
