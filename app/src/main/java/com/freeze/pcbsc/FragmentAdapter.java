package com.freeze.pcbsc;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter  implements BuildsFilter.searchBuildsButton{

    BuildsFragment buildsFragment;
    BuildTableFragment buildTableFragment;
    BuildsFilter buildsFilter;
    public FragmentAdapter(FragmentManager fm, BuildsFragment buildsFragment) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.buildsFragment = buildsFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                buildTableFragment = new BuildTableFragment();
                return buildTableFragment;
            case 1:
                buildsFilter = new BuildsFilter();
                buildsFilter.setOnSearchBuildsButton(this);
                return buildsFilter;
            default:
                return null;
        }
    }

    @Override
    public void searchButtonPressed() {
        buildsFragment.switchTab(0);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 : return "List";
            case 1 : return "Filters";
            default: return "Something wrong";
        }
    }
}