package com.example.slideshowdummy;

import java.util.UUID;

public class ItemType {
    private final String id;
    private final Type type;
    private final String url;

    public ItemType(Type type, String url) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public enum Type { IMAGE, VIDEO }
}
