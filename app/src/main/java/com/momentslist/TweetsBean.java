package com.momentslist;

import com.alibaba.fastjson.annotation.JSONField;
import com.widgets.recycleviewwrap.BaseRecyclerItem;

import java.util.List;

/**
 * Created by liuyuwei on 2016/9/17.
 */
public class TweetsBean implements BaseRecyclerItem {

    @JSONField(name = "sender")
    private Sender sender;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "images")
    private List<imageurl> images;
    @JSONField(name = "comments")
    private List<Comments> comments;
    @JSONField(name = "error")
    private String error;

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<imageurl> getImages() {
        return images;
    }

    public void setImages(List<imageurl> images) {
        this.images = images;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class Sender {
        @JSONField(name = "username")
        private String username;
        @JSONField(name = "nick")
        private String nick;
        @JSONField(name = "avatar")
        private String avatar;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class imageurl {
        @JSONField(name = "url")
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Comments {
        @JSONField(name = "content")
        private String content;
        @JSONField(name = "sender")
        private Sender sender;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Sender getSender() {
            return sender;
        }

        public void setSender(Sender sender) {
            this.sender = sender;
        }
    }

    @Override
    public int getDataType() {
        return 0;
    }
}
