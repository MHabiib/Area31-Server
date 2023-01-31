package com.skripsi.area31.model.tesaurus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class Tesaurus {
    private String mainWord;
    private Set<String> synonyms;
}
