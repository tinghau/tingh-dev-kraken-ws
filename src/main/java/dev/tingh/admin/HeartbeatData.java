package dev.tingh.admin;

import java.util.List;

public class HeartbeatData {
    private String channel;
    private String type;
    private List<HeartbeatItem> data;

    public String getChannel() {
        return channel;
    }

    public String getType() {
        return type;
    }

    public List<HeartbeatItem> getData() {
        return data;
    }

    public static class HeartbeatItem {
        private String timestamp;

        public String getTimestamp() {
            return timestamp;
        }
    }
}