package com.razzfazz.trophylist.Util;

public class ExperienceLogic {

    private static final int POINTS_BRONZE = 15;
    private static final int POINTS_SILVER = 30;
    private static final int POINTS_GOLD = 90;
    private static final int POINTS_PLAT = 180;

    private static int mLevel = 0;
    private static int mLevelProgress = 0;


    // calculates a games progress for a progress bar (returns value between 0 and 100)
    public static int calculateGameProgress(int[] amounts, int[] maximums) {
        return (int) Math.ceil((sumPoints(amounts) / (double) sumPoints(maximums)) * 100);
    }

    // summarizes the points of an array
    private static int sumPoints(int[] array) {
        int sum = 0;
        sum += (array[0] * POINTS_BRONZE);
        sum += (array[1] * POINTS_SILVER);
        sum += (array[2] * POINTS_GOLD);
        sum += (array[3] * POINTS_PLAT);
        return sum;
    }


    // calls overloaded method
    public static void calculateLevelprogress(int[] array) {
        calculateLevelProgress(sumPoints(array));
    }

    // calculates the level progress for a progress bar (returns value between 0 and 100)
    // and the users level
    // delivers the same values like the official PSN
    public static void calculateLevelProgress(int exp) {
        // when 1600 exp are reached you always need 8000 experience points to level up
        if(exp >= 16000) {
            calculateHighLevelProgress(exp);
        }
        if(exp < 200) {
            mLevel = 1;
            mLevelProgress = (int)Math.ceil((exp/200.0)*100);
            return;
        }
        if(exp < 600) {
            mLevel = 2;
            mLevelProgress = (int)Math.ceil((exp/600.0)*100);
            return;
        }
        if(exp < 1200) {
            mLevel = 3;
            mLevelProgress = (int)Math.ceil((exp/1200.0)*100);
            return;
        }
        if(exp < 2400) {
            mLevel = 4;
            mLevelProgress = (int)Math.ceil((exp/2400.0)*100);
            return;
        }
        if(exp < 4000) {
            mLevel = 5;
            mLevelProgress = (int)Math.ceil((exp/4000.0)*100);
            return;
        }
        if(exp < 6000) {
            mLevel = 6;
            mLevelProgress = (int)Math.ceil((exp/6000.0)*100);
            return;
        }
        if(exp < 8000) {
            mLevel = 7;
            mLevelProgress = (int)Math.ceil((exp/8000.0)*100);
            return;
        }
        if(exp < 10000) {
            mLevel = 8;
            mLevelProgress = (int)Math.ceil((exp/10000.0)*100);
            return;
        }
        if(exp < 12000) {
            mLevel = 9;
            mLevelProgress = (int)Math.ceil((exp/12000.0)*100);
            return;
        }
        if(exp < 14000) {
            mLevel = 10;
            mLevelProgress = (int)Math.ceil((exp/14000.0)*100);
            return;
        }
        if(exp < 16000) {
            mLevel = 11;
            mLevelProgress = (int)Math.ceil((exp/16000.0)*100);
            return;
        }

    }

    // calculates a high user level and level progress
    private static void calculateHighLevelProgress(int exp) {
        int minLevel = 12;
        int progress = exp-16000;
        minLevel += (progress/8000);
        mLevel = minLevel;
        mLevelProgress = (int)Math.ceil(((progress%8000)/8000.0)*100);
    }

    public static int getLevel() {
        return mLevel;
    }

    public static int getLevelProgress() {
        return mLevelProgress;
    }


}
