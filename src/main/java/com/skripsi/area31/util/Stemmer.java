package com.skripsi.area31.util;

import jsastrawi.morphology.DefaultLemmatizer;
import jsastrawi.morphology.Lemmatizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class Stemmer {

    private Set<String> dictionary = new HashSet<>();

    public String steam(String word) throws IOException {
        if (dictionary.equals(new HashSet<>())) {
            //steam
            //https://github.com/jsastrawi/jsastrawi

            // Memuat file kata dasar dari distribusi JSastrawi
            InputStream in = Lemmatizer.class.getResourceAsStream("/root-words.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = br.readLine()) != null) {
                dictionary.add(line);
            }
        }
        Lemmatizer lemmatizer = new DefaultLemmatizer(dictionary);
        return lemmatizer.lemmatize(word);
    }
}
