package com.vocketlist.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.VolunteerSearchViewHolder;
import com.vocketlist.android.dto.Volunteer;

import java.util.List;

/**
 * 어댑터 : 봉사활동 : 검색
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class VolunteerSearchAdapter extends BaseAdapter<VolunteerSearchViewHolder> {

    /**
     * 생성자
     * @param data
     */
    public VolunteerSearchAdapter(List<Volunteer.Data> data) {
        super(data);
    }

    @Override
    public VolunteerSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VolunteerSearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_volunteer_search, parent, false));
    }
}