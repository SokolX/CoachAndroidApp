package pl.sokolx.coach.models;

import java.util.HashMap;
import java.util.Map;

public class MessageModel {
    String contentMessage;
    Long dateSend;
    String key;
    String keyFrom;
    String keyTo;
    String userUID;

    public MessageModel() {
    }

    public MessageModel(Long dateSend, String keyTo, String contentMessage, String key, String keyFrom, String userUID) {
        this.dateSend = dateSend;
        this.contentMessage = contentMessage;
        this.keyFrom = keyFrom;
        this.keyTo = keyTo;
        this.key = key;
        this.userUID = userUID;
    }

    public Long getDateSend() {
        return this.dateSend;
    }

    public String getContentMessage() {
        return this.contentMessage;
    }

    public String getKeyFrom() {
        return this.keyFrom;
    }

    public String getKeyTo() {
        return this.keyTo;
    }

    public String getKey() {
        return this.key;
    }

    public String getUserUID() {
        return this.userUID;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> resultMessage = new HashMap();
        resultMessage.put("dateSend", this.dateSend);
        resultMessage.put("contentMessage", this.contentMessage);
        resultMessage.put("keyFrom", this.keyFrom);
        resultMessage.put("keyTo", this.keyTo);
        resultMessage.put("key", this.key);
        resultMessage.put("userUID", this.userUID);
        return resultMessage;
    }
}
