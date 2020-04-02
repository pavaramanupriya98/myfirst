package com.moraspirit.moraspiritapp.menuitem;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

public class MenuItem {
    private String title;
    private Class<?> clasz;
    @DrawableRes
    private int bgRes;
    @ColorInt
    private int textColor;

    public MenuItem(String title, Class<?> clasz,@DrawableRes int bgRes,@ColorInt int textColor) {
        this.title = title;
        this.clasz = clasz;
        this.bgRes = bgRes;
        this.textColor = textColor;
    }

    public String getTitle() {
        return title;
    }

    public Class<?> getClasz() {
        return clasz;
    }

    public int getBgRes() {
        return bgRes;
    }

    public int getTextColor() {
        return textColor;
    }
}
