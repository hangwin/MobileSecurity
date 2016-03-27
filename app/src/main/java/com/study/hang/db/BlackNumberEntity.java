package com.study.hang.db;

/**
 * Created by hang on 16/3/27.
 */
public class BlackNumberEntity {
    private String number;
    private String mode;

    public BlackNumberEntity() {
    }

    public BlackNumberEntity(String mode, String number) {
        this.mode = mode;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
