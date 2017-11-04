package com.enjoyf.platform.event.content;

import com.enjoyf.platform.event.EventType;

/**
 * Created by ericliu on 2017/5/10.
 */
public class ContentEventType extends EventType {

    public static final ContentEventType CONTENT_INSERT = new ContentEventType("content.insert");

    public static final ContentEventType CONTENT_INDEX_BUILD = new ContentEventType("content.index.build");
    public static final ContentEventType CONTENT_INDEX_DELETE = new ContentEventType("content.index.build");


    public ContentEventType(String type) {
        super(type);
    }

    public ContentEventType() {
        super();
    }
}
