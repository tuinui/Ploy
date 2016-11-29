package com.nos.ploy.flow.ployee.home.content.availability.contract;

/**
 * Created by Saran on 22/11/2559.
 */

public class HeaderTimeAvailabilityVM implements AvailabilityViewModel {
    private String timeRange = "09:00 - 10:00";

    public HeaderTimeAvailabilityVM(String timeRange) {
        this.timeRange = timeRange;
    }


    public String getTimeRange(){
        return timeRange;
    }

    @Override
    public int getAvailibilityViewType() {
        return HEADER_TIME;
    }
}
