package dev.tingh.admin.model;

import java.util.List;

public class StatusData {
    private String channel;
    private String type;
    private List<StatusItem> data;

    public String getChannel() {
        return channel;
    }

    public String getType() {
        return type;
    }

    public List<StatusItem> getData() {
        return data;
    }

    public static class StatusItem {
        private String connectionId;
        private String version;
        private String status;
        private String systemStatus;
        private String apiStatus;

        public String getConnectionId() {
            return connectionId;
        }

        public String getVersion() {
            return version;
        }

        public String getStatus() {
            return status;
        }

        public String getSystemStatus() {
            return systemStatus;
        }

        public String getApiStatus() {
            return apiStatus;
        }
    }
}