package com.dreamdigitizers.megamelodies.views.classes.support;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.models.Playlist;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MediaMetadataBuilder;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPlaylist extends AdapterMediaItem<AdapterPlaylist.PlaylistViewHolder> {
    public AdapterPlaylist(Context pContext) {
        this(pContext, new ArrayList<MediaBrowserCompat.MediaItem>());
    }

    public AdapterPlaylist(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this(pContext, pMediaItems, null);
    }

    public AdapterPlaylist(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems, AdapterMediaItem.IOnItemClickListener pListener) {
        super(pContext, pMediaItems, pListener);
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        View itemView = LayoutInflater.from(pParent.getContext()).inflate(R.layout.part__playlist, pParent, false);
        return new PlaylistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder pHolder, int pPosition) {
        MediaBrowserCompat.MediaItem mediaItem = this.mMediaItems.get(pPosition);
        MediaDescriptionCompat mediaDescription = mediaItem.getDescription();
        Playlist playlist = (Playlist) mediaDescription.getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__PLAYLIST);
        Bitmap bitmap = mediaDescription.getIconBitmap();
        if (bitmap != null) {
            pHolder.mImgMediaItem.setImageBitmap(bitmap);
        }

        pHolder.mLblName.setText(playlist.getName());

        int tracksSize = playlist.getTracks().size();
        String tracksNum = pHolder.mLblTracksNum.getContext().getResources().getQuantityString(R.plurals.label__tracks_num, tracksSize, tracksSize);
        pHolder.mLblTracksNum.setText(tracksNum);

        pHolder.mMediaItem = mediaItem;
    }

    protected class PlaylistViewHolder extends AdapterMediaItem.MediaItemViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        protected CircleImageView mImgMediaItem;
        protected TextView mLblName;
        protected TextView mLblTracksNum;
        protected ImageButton mBtnContextMenu;

        public PlaylistViewHolder(View pItemView) {
            super(pItemView);
            this.mImgMediaItem = (CircleImageView) pItemView.findViewById(R.id.imgMediaItem);
            this.mLblName = (TextView) pItemView.findViewById(R.id.lblName);
            this.mLblTracksNum = (TextView) pItemView.findViewById(R.id.lblTracksNum);
            this.mBtnContextMenu = (ImageButton) pItemView.findViewById(R.id.btnContextMenu);
            this.mBtnContextMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View pView) {
                    PlaylistViewHolder.this.contextMenuButtonClicked();
                }
            });
            this.mBtnContextMenu.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu pContextMenu, View pView, ContextMenu.ContextMenuInfo pMenuInfo) {
            MenuInflater menuInflater = new MenuInflater(AdapterPlaylist.this.mContext);
            menuInflater.inflate(R.menu.menu__context_playlist_list, pContextMenu);

            MenuItem menuItem = pContextMenu.getItem(0);
            menuItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem pMenuItem) {
            int id = pMenuItem.getItemId();
            switch (id) {
                case R.id.context_menu_item__delete:
                    if(AdapterPlaylist.this.mListener != null && AdapterPlaylist.this.mListener instanceof IOnItemClickListener) {
                        ((IOnItemClickListener) AdapterPlaylist.this.mListener).onDeleteContextMenuItemClicked(this.mMediaItem);
                    }
                    return true;
                default:
                    return false;
            }
        }

        protected void contextMenuButtonClicked() {
            ((Activity) AdapterPlaylist.this.mContext).openContextMenu(this.mBtnContextMenu);
        }
    }

    public interface IOnItemClickListener extends AdapterMediaItem.IOnItemClickListener {
        void onDeleteContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem);
    }
}
