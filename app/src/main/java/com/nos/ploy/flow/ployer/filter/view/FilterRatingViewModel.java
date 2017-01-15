package com.nos.ploy.flow.ployer.filter.view;

/**
 * Created by Saran on 14/1/2560.
 */

public class FilterRatingViewModel {


    private long rating;
    private boolean isChecked;

    public FilterRatingViewModel(long rating) {
        this.isChecked = false;
        this.rating = rating;
    }

    public boolean isChecked() {
        return isChecked;
    }


    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public long getRating() {
        return rating;
    }
}
