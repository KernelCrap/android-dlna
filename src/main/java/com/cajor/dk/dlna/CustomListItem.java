package com.cajor.dk.dlna;

public class CustomListItem {

    private int icon;
    private String title;
    private String description;
    private String description2;
    private String iconUrl;
    private Boolean hideIcon;

    protected CustomListItem(int icon) {
        this(icon, null, false, null, null, null);
    }

    public CustomListItem(int icon, String title, String description) {
        this(icon, null, false, title, description, null);
    }

    public CustomListItem(int icon, String iconUrl, Boolean hideIcon, String title,
                          String description, String description2) {
        this.icon = icon;
        this.iconUrl = iconUrl;
        this.hideIcon = hideIcon;
        this.title = title;
        this.description = description;
        this.description2 = description2;
    }

    public String getId() {
        return "";
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Boolean getHideIcon() {
        return this.hideIcon;
    }

    public void setHideIcon(Boolean hideIcon) {
        this.hideIcon = hideIcon;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription2() {
        return this.description2;
    }

    public void setDescription2(String description) {
        this.description2 = description;
    }
}
