package com.bunny.groovy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bunny.groovy.base.BaseListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 用户中心，适配器
 */

public class UserCenterAdapter extends FragmentStatePagerAdapter {
    private List<BaseListFragment> mFragments = new ArrayList<BaseListFragment>();
    private String[] title;

    public UserCenterAdapter(List<BaseListFragment> fragmentList, String[] titles, FragmentManager fm) {
        super(fm);
        if (fragmentList != null) {
            mFragments = fragmentList;
        }
        if (titles != null && titles.length >= mFragments.size())
            this.title = titles;
        else throw new IllegalArgumentException("fragment的标题长度小于页面数量");
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
