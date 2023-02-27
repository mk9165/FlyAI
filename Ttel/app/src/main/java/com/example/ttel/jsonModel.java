package com.example.ttel;

import java.io.Serializable;

public class jsonModel implements Serializable {
    private Ratio RATIO;
    private String KEYWORD;
    private String SUMMARY;
    private Info INFO;
    private int SAVE;

    public Ratio getRatio() {
        return RATIO;
    }
    public void setRatio(Ratio RATIO) {
        this.RATIO = RATIO;    }

    public Info getInfo() {
        return INFO;
    }
    public void setInfo(Info INFO) {
        this.INFO = INFO;    }


    public String getKeyword() {
        return KEYWORD;
    }
    public void setKeyword(String KEYWORD) {
        this.KEYWORD = KEYWORD;    }


    public String getSummary() {
        return SUMMARY;
    }
    public void setSummary(String SUMMARY) {
        this.SUMMARY = SUMMARY;    }

    public int getSave() {
        return SAVE;
    }
    public void setSave(int SAVE) {
        this.SAVE = SAVE;    }


    public static class Ratio implements Serializable {
        private double positive;
        private double angry;
        private double fear;
        private double sad;

        public double getPositive() {
            return positive;
        }
        public void setPositive(double positive) {
            this.positive = positive;    }

        public double getAngry() {
            return angry;
        }
        public void setAngry(double angry) {
            this.angry = angry;    }

        public double getFear() {
            return fear;
        }
        public void setFear(double fear) {
            this.fear = fear;    }

        public double getSad() {
            return sad;
        }
        public void setSad(double sad) {
            this.sad = sad;    }
    }

    public static class Info implements Serializable {
        private String NUMBER;
        private String DATE;
        private String TIME;
        private String DURATION;

        public String getNumber() {
            return NUMBER;
        }
        public void setNumber(String NUMBER) {
            this.NUMBER = NUMBER;    }

        public String getDate() {
            return DATE;
        }
        public void setDate(String DATE) {
            this.DATE = DATE;    }

        public String getTime() {
            return TIME;
        }
        public void setTime(String TIME) {
            this.TIME = TIME;    }

        public String getDuration() {
            return DURATION;
        }
        public void setDuration(String DURATION) {
            this.DURATION = DURATION;    }
    }

}