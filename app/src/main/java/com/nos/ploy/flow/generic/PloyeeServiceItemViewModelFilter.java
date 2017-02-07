package com.nos.ploy.flow.generic;

import android.widget.Filter;

import com.nos.ploy.flow.ployee.home.content.service.list.PloyeeHomeRecyclerAdapter;
import com.nos.ploy.flow.ployee.home.content.service.list.viewmodel.PloyeeServiceItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 8/2/2560.
 */

public class PloyeeServiceItemViewModelFilter extends Filter {

    private List<PloyeeServiceItemViewModel> contactList;
    private List<PloyeeServiceItemViewModel> filteredContactList;
    private PloyeeHomeRecyclerAdapter adapter;

    public PloyeeServiceItemViewModelFilter(List<PloyeeServiceItemViewModel> contactList, PloyeeHomeRecyclerAdapter adapter) {
        this.adapter = adapter;
        this.contactList = contactList;
        this.filteredContactList = new ArrayList();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredContactList.clear();
        final FilterResults results = new FilterResults();

        //here you need to add proper items do filteredContactList
        for (final PloyeeServiceItemViewModel item : contactList) {
            if (item.getText().toLowerCase().trim().contains(constraint)) {
                filteredContactList.add(item);
            }
        }

        results.values = filteredContactList;
        results.count = filteredContactList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setFilteredData(filteredContactList);
        adapter.notifyDataSetChanged();
    }
}
