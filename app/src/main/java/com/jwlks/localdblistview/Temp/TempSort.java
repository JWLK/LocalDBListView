package com.jwlks.localdblistview.Temp;

import java.util.Comparator;

public class TempSort {

    static Comparator<TempListViewModel> idAsc = new Comparator<TempListViewModel>() {
        @Override
        public int compare(TempListViewModel item1, TempListViewModel item2) {
            int ret ;

            if (item1.getId() < item2.getId())
                ret = -1 ;
            else if (item1.getId() == item2.getId())
                ret = 0 ;
            else
                ret = 1 ;

            return ret ;

            // 위의 코드를 간단히 만드는 방법.
            // return (item1.getNo() - item2.getNo()) ;
        }
    } ;

    static Comparator<TempListViewModel> idDesc = new Comparator<TempListViewModel>() {
        @Override
        public int compare(TempListViewModel item1, TempListViewModel item2) {
            int ret = 0 ;

            if (item1.getId() < item2.getId())
                ret = 1 ;
            else if (item1.getId() == item2.getId())
                ret = 0 ;
            else
                ret = -1 ;

            return ret ;

            // 위의 코드를 간단히 만드는 방법.
            // return (item2.getNo() - item1.getNo()) ;
        }
    };



}

