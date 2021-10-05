package com.siang.androidportfolio;

import java.util.List;

public class NotificationDetail {
    private String title;
    private String body;
    private String clickAction;
    private String extraText;
    private String channelId;
    private String style;
    private String largeIconName;
    private List<String> actionList;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setClickAction(String clickAction) {
        this.clickAction = clickAction;
    }

    public void setExtraText(String extraText) {
        this.extraText = extraText;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setLargeIconName(String largeIconName) {
        this.largeIconName = largeIconName;
    }

    public void setActionList(List<String> actionList) {
        this.actionList = actionList;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getClickAction() {
        return clickAction;
    }

    public String getExtraText() {
        return extraText;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getStyle() {
        return style;
    }

    public String getLargeIconName() {
        return largeIconName;
    }

    public List<String> getActionList() {
        return actionList;
    }
}
