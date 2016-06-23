package com.dreamdigitizers.megamelodies.views.classes.support;

import android.graphics.Bitmap;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsString;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.models.Playlist;
import com.dreamdigitizers.megamelodies.models.Track;
import com.dreamdigitizers.megamelodies.views.classes.services.support.MediaMetadataBuilder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPlaylistDialog extends RecyclerView.Adapter<AdapterPlaylistDialog.DialogPlaylistViewHolder> {
    private Track mTrack;
    private MediaBrowserCompat.MediaItem mMediaItemTrack;
    private List<MediaBrowserCompat.MediaItem> mPlaylists;

    private IOnAddRemoveButtonClickListener mListener;

    public AdapterPlaylistDialog(MediaBrowserCompat.MediaItem pTrack, List<MediaBrowserCompat.MediaItem> pPlaylists, IOnAddRemoveButtonClickListener pListener) {
        this.mMediaItemTrack = pTrack;
        this.mTrack = (Track) this.mMediaItemTrack.getDescription().getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__TRACK);
        this.mPlaylists = pPlaylists;
        this.mListener = pListener;
    }

    @Override
    public DialogPlaylistViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        View itemView = LayoutInflater.from(pParent.getContext()).inflate(R.layout.part__playlist_dialog, pParent, false);
        return new DialogPlaylistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DialogPlaylistViewHolder pHolder, int pPosition) {
        MediaBrowserCompat.MediaItem mediaItem = this.mPlaylists.get(pPosition);
        MediaDescriptionCompat mediaDescription = mediaItem.getDescription();
        Playlist playlist = (Playlist) mediaDescription.getExtras().getSerializable(MediaMetadataBuilder.BUNDLE_KEY__PLAYLIST);
        Bitmap bitmap = mediaDescription.getIconBitmap();
        if (bitmap != null) {
            pHolder.mImgMediaItem.setImageBitmap(bitmap);
        }

        pHolder.mLblName.setText(playlist.getName());

        String id = this.mTrack.getId();
        boolean isAdded = false;
        for (Track track : playlist.getTracks()) {
            if (UtilsString.equals(id, track.getId())) {
                isAdded = true;
                break;
            }
        }
        if (isAdded) {
            pHolder.mBtnAddToRemoveFromPlaylist.setText(R.string.btn__remove);
        } else {
            pHolder.mBtnAddToRemoveFromPlaylist.setText(R.string.btn__add);
        }

        pHolder.mIsAdded = isAdded;
        pHolder.mPlaylist = mediaItem;
    }

    @Override
    public int getItemCount() {
        return this.mPlaylists.size();
    }

    public void setOnAddRemoveButtonClickListener(IOnAddRemoveButtonClickListener pListener) {
        this.mListener = pListener;
    }

    public void onAddToRemoveFromPlaylistResult() {
        this.notifyDataSetChanged();
    }

    public class DialogPlaylistViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mImgMediaItem;
        private TextView mLblName;
        private Button mBtnAddToRemoveFromPlaylist;
        private boolean mIsAdded;
        private MediaBrowserCompat.MediaItem mPlaylist;

        public DialogPlaylistViewHolder(View pItemView) {
            super(pItemView);
            this.mImgMediaItem = (CircleImageView) pItemView.findViewById(R.id.imgMediaItem);
            this.mLblName = (TextView) pItemView.findViewById(R.id.lblName);
            this.mBtnAddToRemoveFromPlaylist = (Button) pItemView.findViewById(R.id.btnAddToRemoveFromPlaylist);
            this.mBtnAddToRemoveFromPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View pView) {
                    DialogPlaylistViewHolder.this.buttonAddToRemoveFromPlaylistClicked();
                }
            });
        }

        private void buttonAddToRemoveFromPlaylistClicked() {
            if (AdapterPlaylistDialog.this.mListener != null) {
                AdapterPlaylistDialog.this.mListener.onAddRemoveButtonClicked(AdapterPlaylistDialog.this.mMediaItemTrack, this.mPlaylist, !this.mIsAdded);
            }
        }
    }

    public interface IOnAddRemoveButtonClickListener {
        void onAddRemoveButtonClicked(MediaBrowserCompat.MediaItem pTrack, MediaBrowserCompat.MediaItem pPlaylist, boolean pIsAdd);
    }
}
