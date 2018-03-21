package com.bunny.groovy.view;

import android.support.v4.app.FragmentActivity;

import com.bunny.groovy.model.VenueModel;

import java.util.List;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public interface ISearchVenueList {
    FragmentActivity get();
    void setListView(List<VenueModel> list);
}
