package com.bunny.groovy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.model.MusicBean;

import java.util.ArrayList;

/**
 * 音乐列表适配器
 * Created by Administrator on 2017/12/13.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MHodler> {

    private ArrayList<MusicBean> listData;

    public MusicAdapter(ArrayList<MusicBean> list) {
        this.listData = list;
    }

    @Override
    public MHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_layout, parent, false);
        return new MHodler(inflate);
    }

    @Override
    public void onBindViewHolder(MHodler holder, final int position) {
        MusicBean map = listData.get(position);
        holder.tvName.setText(String.valueOf(map.getTitle()));
        holder.tvSize.setText(map.getLength() + "M");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });
    }

    private OnItemClickListener mItemClickListener;

    public void setItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int positon);
    }

    @Override
    public int getItemCount() {
        if (listData != null) return listData.size();
        return 0;
    }

    public class MHodler extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvSize;

        public MHodler(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_music_name);
            tvSize = itemView.findViewById(R.id.item_music_size);
        }
    }
}
