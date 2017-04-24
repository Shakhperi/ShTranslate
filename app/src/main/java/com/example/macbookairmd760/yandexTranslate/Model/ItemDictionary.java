package com.example.macbookairmd760.yandexTranslate.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookairmd760 on 23.04.17.
 */
public class ItemDictionary {
    @SerializedName("text")
    private String value;

    @SerializedName("pos")
    private String partOfSpeech;

    @SerializedName("ts")
    private String transcription;

    @SerializedName("tr")
    private List<ItemDictionary> translateList;

    @SerializedName("syn")
    private List<ItemDictionary> synonymList;

    @SerializedName("mean")
    private List<ItemDictionary> meaningList;

    @SerializedName("ex")
    private List<ItemDictionary> exampleList;

    public String getValue() {
        if (value == null) {
            value = "";
        }
        return value;
    }

    public String getPartOfSpeech() {
        if (partOfSpeech == null) {
            partOfSpeech = "";
        }
        return partOfSpeech;
    }

    public String getTranscription() {
        if (transcription == null) {
            transcription = "";
        }
        return transcription;
    }

    public List<ItemDictionary> getTranslateList() {
        if (translateList == null) {
            return new ArrayList<>();
        }
        return translateList;
    }

    public List<ItemDictionary> getSynonymList() {
        if (synonymList == null) {
            synonymList = new ArrayList<>();
        }
        return synonymList;
    }

    public List<ItemDictionary> getMeaningList() {
        if (meaningList == null) {
            meaningList = new ArrayList<>();
        }
        return meaningList;
    }

    public List<ItemDictionary> getExampleList() {
        if (exampleList == null) {
            exampleList = new ArrayList<>();
        }
        return exampleList;
    }
}
