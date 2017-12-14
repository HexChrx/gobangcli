package model;

import java.util.HashMap;

public class MessageModel {

    private int type;

    private int errno;

    private HashMap<String, String> content;

    public MessageModel() {
        content = new HashMap<>();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HashMap<String, String> getContent() {
        return content;
    }

    public void setContent(HashMap<String, String> content) {
        this.content = content;
    }

    public String get(String key) {
        return content.get(key);
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }
}
