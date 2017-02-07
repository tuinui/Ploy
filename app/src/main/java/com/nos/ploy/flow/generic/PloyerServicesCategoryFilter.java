package com.nos.ploy.flow.generic;

import android.widget.Filter;

import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.flow.ployer.service.view.PloyerCategoryRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 8/2/2560.
 */

public class PloyerServicesCategoryFilter extends Filter {

    private List<PloyerServicesGson.Data> contactList;
    private List<PloyerServicesGson.Data> filteredContactList;
    private PloyerCategoryRecyclerAdapter adapter;

    public PloyerServicesCategoryFilter(List<PloyerServicesGson.Data> contactList, PloyerCategoryRecyclerAdapter adapter) {
        this.adapter = adapter;
        this.contactList = contactList;
        this.filteredContactList = new ArrayList();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredContactList.clear();
        final FilterResults results = new FilterResults();

        //here you need to add proper items do filteredContactList
        for (final PloyerServicesGson.Data item : contactList) {
            if (item.getServiceName().toLowerCase().trim().contains(constraint)) {
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
