package com.dreamdigitizers.megamelodies.views.classes.support;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class AdapterMediaItem<V extends AdapterMediaItem.MediaItemViewHolder> extends RecyclerView.Adapter<V> {
    protected Context mContext;
    protected List<MediaBrowserCompat.MediaItem> mMediaItems;

    protected IOnItemClickListener mListener;

    public AdapterMediaItem(Context pContext) {
        this(pContext, new ArrayList<MediaBrowserCompat.MediaItem>());
    }

    public AdapterMediaItem(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this(pContext, pMediaItems, null);
    }

    public AdapterMediaItem(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems, IOnItemClickListener pListener) {
        this.mContext = pContext;
        this.mMediaItems = pMediaItems;
        this.mListener = pListener;
    }

    @Override
    public int getItemCount() {
        return this.mMediaItems.size();
    }

    public void setOnItemClickListener(IOnItemClickListener pListener) {
        this.mListener = pListener;
    }

    public void clearMediaItems() {
        this.mMediaItems.clear();
        this.notifyDataSetChanged();
    }

    public void addMediaItems(List<MediaBrowserCompat.MediaItem> pMediaItems, boolean pIsAddToTop) {
        for (MediaBrowserCompat.MediaItem mediaItem : pMediaItems) {
            if (pIsAddToTop) {
                this.mMediaItems.add(0, mediaItem);
                this.notifyItemInserted(0);
            } else {
                int index = this.mMediaItems.size();
                this.mMediaItems.add(mediaItem);
                this.notifyItemInserted(index);
            }
        }
    }

    public void removeMediaItem(MediaBrowserCompat.MediaItem pMediaItem) {
        int index = this.mMediaItems.indexOf(pMediaItem);
        this.mMediaItems.remove(pMediaItem);
        this.notifyItemRemoved(index);
    }

    protected abstract class MediaItemViewHolder extends RecyclerView.ViewHolder {
        protected MediaBrowserCompat.MediaItem mMediaItem;

        public MediaItemViewHolder(View pItemView) {
            super(pItemView);

            pItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View pView) {
                    MediaItemViewHolder.this.clicked();
                }
            });
        }

        protected void clicked() {
            if(AdapterMediaItem.this.mListener != null) {
                AdapterMediaItem.this.mListener.onItemClicked(this.mMediaItem);
            }
        }
    }

    public interface IOnItemClickListener {
        void onItemClicked(MediaBrowserCompat.MediaItem pMediaItem);
    }
}
