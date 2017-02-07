package com.nos.ploy.flow.generic;

import android.widget.Filter;

import com.nos.ploy.api.ployer.model.ProviderUserListGson;
import com.nos.ploy.flow.ployer.person.list.view.PloyerPersonListRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 8/2/2560.
 */

public class UserServiceFilter extends Filter {

    private List<ProviderUserListGson.Data.UserService> contactList;
    private List<ProviderUserListGson.Data.UserService> filteredContactList;
    private PloyerPersonListRecyclerAdapter adapter;

    public UserServiceFilter(List<ProviderUserListGson.Data.UserService> contactList, PloyerPersonListRecyclerAdapter adapter) {
        this.adapter = adapter;
        this.contactList = contactList;
        this.filteredContactList = new ArrayList();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredContactList.clear();
        final FilterResults results = new FilterResults();

        //here you need to add proper items do filteredContactList
        for (final ProviderUserListGson.Data.UserService item : contactList) {
            String name = item.getFirstName().toLowerCase()+item.getLastName().toLowerCase();
            if (name.trim().contains(constraint)) {
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
