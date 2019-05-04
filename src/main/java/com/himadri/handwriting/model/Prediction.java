package com.himadri.handwriting.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Prediction {
    private String method;
    private int prediction;
    private double confidence;
    private boolean preferred;
}
