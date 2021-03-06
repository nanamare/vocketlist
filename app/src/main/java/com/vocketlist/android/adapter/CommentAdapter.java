package com.vocketlist.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.CommentViewHolder;
import com.vocketlist.android.api.comment.model.CommentListModel;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;

import java.util.List;

/**
 * 어댑터 : 커뮤니티 : 댓글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class CommentAdapter extends BaseAdapter<CommentListModel.Comment, CommentViewHolder> {

    private RecyclerViewItemClickListener mListener;

    /**
     * 생성자
     * @param data
     */
    public CommentAdapter(List<CommentListModel.Comment> data, RecyclerViewItemClickListener mListener) {
        super(data);
        this.mListener = mListener;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
