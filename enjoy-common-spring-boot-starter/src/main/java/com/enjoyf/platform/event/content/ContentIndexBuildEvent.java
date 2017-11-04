package com.enjoyf.platform.event.content;

import com.enjoyf.platform.event.Event;

/**
 * Created by zhimingli on 2017/5/31.
 */

public class ContentIndexBuildEvent extends Event {

    public ContentIndexBuildEvent() {
        super(ContentEventType.CONTENT_INDEX_BUILD, ContentEventConstants.bindKey);
    }
}
