package org.maestromedia.tv.index;

import java.util.Collection;

public class TitleInfo {
    
    String title;
    Collection<String> tags;
    String titleType = "movie";
    String path;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    public String getType() {
        return titleType;
    }

    public void setType(String type) {
        this.titleType = type;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
