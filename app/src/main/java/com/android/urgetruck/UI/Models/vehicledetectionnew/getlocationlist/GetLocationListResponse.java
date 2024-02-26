package com.android.urgetruck.UI.Models.vehicledetectionnew.getlocationlist;

import java.util.List;

public class GetLocationListResponse {
        private List<Location> locations;
        private int status;
        private Object statusMessage;

        public GetLocationListResponse(List<Location> locations, int status, Object statusMessage) {
            this.locations = locations;
            this.status = status;
            this.statusMessage = statusMessage;
        }

        // Getter methods
        public List<Location> getLocations() {
            return locations;
        }

        public int getStatus() {
            return status;
        }

        public Object getStatusMessage() {
            return statusMessage;
        }

        // Setter methods (if needed)
        public void setLocations(List<Location> locations) {
            this.locations = locations;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setStatusMessage(Object statusMessage) {
            this.statusMessage = statusMessage;
        }

}
