package com.android.urgetruck.UI.Models;

import java.util.List;

public class DataModelTrackVehicle {

    private List<JobMilestone> jobMilestones;
    private List<MilestoneActionsTracking> trackingList;
    private boolean isExpandable;

    public DataModelTrackVehicle(List<JobMilestone> jobMilestones,List<MilestoneActionsTracking> trackingList ) {
        this.jobMilestones = jobMilestones;
        this.trackingList = trackingList;
        isExpandable = false;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public List<MilestoneActionsTracking> getNestedList() {
        return trackingList;
    }
    public List<JobMilestone> getItemText() {
        return jobMilestones;
    }


    public boolean isExpandable() {
        return isExpandable;
    }
}
