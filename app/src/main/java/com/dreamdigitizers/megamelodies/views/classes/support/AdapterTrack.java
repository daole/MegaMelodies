package com.dreamdigitizers.megamelodies.views.classes.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSinger;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSong;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSong;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.Share;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MediaMetadataBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdapterTrack extends AdapterPlaylist {
    private PlaybackStateCompat mPlaybackState;
    private MediaMetadataCompat mMediaMetadata;

    public AdapterTrack(Context pContext) {
        this(pContext, new ArrayList<MediaBrowserCompat.MediaItem>());
    }

    public AdapterTrack(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems) {
        this(pContext, pMediaItems, null);
    }

    public AdapterTrack(Context pContext, List<MediaBrowserCompat.MediaItem> pMediaItems, AdapterMediaItem.IOnItemClickListener pListener) {
        super(pContext, pMediaItems, pListener);
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        View itemView = LayoutInflater.from(pParent.getContext()).inflate(R.layout.part__track, pParent, false);
        return new TrackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder pHolder, int pPosition) {
        TrackViewHolder trackViewHolder = (TrackViewHolder) pHolder;

        MediaBrowserCompat.MediaItem mediaItem = this.mMediaItems.get(pPosition);
        MediaDescriptionCompat mediaDescription = mediaItem.getDescription();
        Track track = (Track) mediaDescription.getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);

        Bitmap bitmap = mediaDescription.getIconBitmap();
        if (bitmap != null) {
            trackViewHolder.mImgMediaItem.setImageBitmap(bitmap);
        }
        trackViewHolder.mLblName.setText(track.getName());
        trackViewHolder.mLblArtist.setText(track.getArtist());
        trackViewHolder.mImgFavorite.setVisibility(track.isFavorite() ? View.VISIBLE : View.GONE);
        trackViewHolder.mMediaItem = mediaItem;

        Drawable drawable = null;
        if (this.mMediaMetadata != null) {
            Track currentTrack = (Track) this.mMediaMetadata.getBundle().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);
            if (currentTrack == null && Build.VERSION.SDK_INT >= 21) {
                currentTrack = Share.getCurrentTrack();
            }

            if (currentTrack != null && UtilsString.equals(track.getId(), currentTrack.getId())) {
                int state = PlaybackStateCompat.STATE_PAUSED;
                if (this.mPlaybackState != null) {
                    switch (this.mPlaybackState.getState()) {
                        case PlaybackStateCompat.STATE_PLAYING:
                            state = PlaybackStateCompat.STATE_PLAYING;
                            break;
                        default:
                            break;
                    }
                }

                AnimationDrawable animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(this.mContext, R.drawable.ic__equalizer);
                drawable = animationDrawable;
                trackViewHolder.mImgPlaybackStatus.setImageDrawable(animationDrawable);
                if (state == PlaybackStateCompat.STATE_PLAYING) {
                    animationDrawable.start();
                }
            }
        }
        if (drawable == null) {
            drawable = ContextCompat.getDrawable(this.mContext, R.drawable.ic__play);
            trackViewHolder.mImgPlaybackStatus.setImageDrawable(drawable);
        }
    }

    public void onPlaybackStateChanged(PlaybackStateCompat pPlaybackState) {
        this.mPlaybackState = pPlaybackState;
        this.notifyDataSetChanged();
    }

    public void onMetadataChanged(MediaMetadataCompat pMediaMetadata) {
        this.mMediaMetadata = pMediaMetadata;
        this.notifyDataSetChanged();
    }

    protected class TrackViewHolder extends AdapterPlaylist.PlaylistViewHolder {
        protected TextView mLblArtist;
        protected ImageView mImgPlaybackStatus;
        protected ImageView mImgFavorite;

        public TrackViewHolder(View pItemView) {
            super(pItemView);
            this.mLblArtist = (TextView) pItemView.findViewById(R.id.lblArtist);
            this.mImgPlaybackStatus = (ImageView) pItemView.findViewById(R.id.imgPlaybackStatus);
            this.mImgFavorite = (ImageView) pItemView.findViewById(R.id.imgFavorite);
        }

        @Override
        public void onCreateContextMenu(ContextMenu pContextMenu, View pView, ContextMenu.ContextMenuInfo pMenuInfo) {
            MenuInflater menuInflater = new MenuInflater(AdapterTrack.this.mContext);
            menuInflater.inflate(R.menu.menu__context_track_list, pContextMenu);

            MediaDescriptionCompat mediaDescription = this.mMediaItem.getDescription();
            Track track = (Track) mediaDescription.getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);

            MenuItem menuItem = pContextMenu.getItem(0);
            menuItem.setOnMenuItemClickListener(this);
            if (track.isFavorite()) {
                menuItem.setTitle(AdapterTrack.this.mContext.getString(R.string.context_menu_item__unfavorite));
            } else {
                menuItem.setTitle(AdapterTrack.this.mContext.getString(R.string.context_menu_item__favorite));
            }

            menuItem = pContextMenu.getItem(1);
            menuItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem pMenuItem) {
            int id = pMenuItem.getItemId();
            switch (id) {
                case R.id.context_menu_item__favorite:
                    if(AdapterTrack.this.mListener != null && AdapterTrack.this.mListener instanceof IOnItemClickListener) {
                        ((IOnItemClickListener) AdapterTrack.this.mListener).onFavoriteContextMenuItemClicked(this.mMediaItem);
                    }
                    return true;
                case R.id.context_menu_item__playlist:
                    if(AdapterTrack.this.mListener != null && AdapterTrack.this.mListener instanceof IOnItemClickListener) {
                        ((IOnItemClickListener) AdapterTrack.this.mListener).onPlaylistContextMenuItemClicked(this.mMediaItem);
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    public interface IOnItemClickListener extends AdapterMediaItem.IOnItemClickListener {
        void onFavoriteContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem);
        void onPlaylistContextMenuItemClicked(MediaBrowserCompat.MediaItem pMediaItem);
    }
}
