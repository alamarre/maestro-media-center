package org.maestromedia.services.core.models;

import java.util.Collection;
import java.util.Map;

public class Playlist {
    Map<String,Collection<PlaylistEntry>> groups;

    public Map<String, Collection<PlaylistEntry>> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Collection<PlaylistEntry>> groups) {
        this.groups = groups;
    }
    
}
