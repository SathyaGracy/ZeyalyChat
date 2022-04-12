package com.zeyalychat.com.bean;

public class NavigationBean {

    private String title;
    private boolean isSelf;
    private boolean isSelected;

    public NavigationBean() {
    }

    public NavigationBean(String title, boolean isSelf, boolean isSelected) {
        this.title = title;
        this.isSelf = isSelf;
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
