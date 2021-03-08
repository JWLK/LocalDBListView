package com.jwlks.localdblistview;

import java.util.Comparator;

public class TempSort implements Comparator<TempListViewModel> {

    private int sortType;

    public TempSort(int sortType) {
        this.sortType = sortType;
    }

    @Override
    public int compare(TempListViewModel model_0, TempListViewModel model_1) {
        switch (sortType){
            case 0:
                return Integer.compare(model_0.getId(), model_1.getId());
            case 1:
                return Integer.compare(model_0.getId(), model_1.getId())*(-1);
            default:
                return 0;
        }
    }

}

