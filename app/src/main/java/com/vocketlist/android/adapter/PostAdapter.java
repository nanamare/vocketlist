package com.vocketlist.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.CommunityViewHolder;
import com.vocketlist.android.api.community.model.CommunityList;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;
import com.vocketlist.android.roboguice.log.Ln;

import java.util.List;

/**
 * 어댑터 : 커뮤니티 : 글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class PostAdapter extends BaseAdapter<CommunityList.CommunityData, CommunityViewHolder> {

    private RecyclerViewItemClickListener mListener;

    /**
     * 생성자
     * @param data
     */
    public PostAdapter(List<CommunityList.CommunityData> data, RecyclerViewItemClickListener listener) {
        super(data);
        mListener = listener;
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(CommunityViewHolder holder, int position) {
        Ln.d("onBindViewHolder : position = " + position);
        holder.bind(getItem(position));
    }
}