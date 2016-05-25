package com.dreamdigitizers.megamelodies.views.classes.services.support;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.dreamdigitizers.androiddatafetchingapisclient.models.MusicNct;
import com.dreamdigitizers.androiddatafetchingapisclient.models.MusicZing;

public class MetadataBuilder {
    public static final String BUNDLE_KEY__TRACK = "track";

    public static MediaMetadataCompat build(MusicNct pMusicNct) {
        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, pMusicNct.getId())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, pMusicNct.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, pMusicNct.getCreator())
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, pMusicNct.getAvatar())
                .build();
        mediaMetadata.getBundle().putSerializable(MetadataBuilder.BUNDLE_KEY__TRACK, pMusicNct);
        return mediaMetadata;
    }

    public static MediaMetadataCompat build(MusicZing pMusicZing) {
        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, pMusicZing.getId())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, pMusicZing.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, pMusicZing.getPerformer())
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, pMusicZing.getBackImage())
                .build();
        mediaMetadata.getBundle().putSerializable(MetadataBuilder.BUNDLE_KEY__TRACK, pMusicZing);
        return mediaMetadata;
    }

    public static MediaDescriptionCompat build(MediaMetadataCompat pMediaMetadata) {
        Bundle bundle = new Bundle();
        Bundle mediaMetadataBundle = pMediaMetadata.getBundle();
        if (mediaMetadataBundle.containsKey(MetadataBuilder.BUNDLE_KEY__TRACK)) {
            bundle.putSerializable(MetadataBuilder.BUNDLE_KEY__TRACK, mediaMetadataBundle.getSerializable(MetadataBuilder.BUNDLE_KEY__TRACK));
        }
        MediaDescriptionCompat.Builder mediaDescriptionBuilder = new MediaDescriptionCompat.Builder();
        mediaDescriptionBuilder.setMediaId(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID));
        mediaDescriptionBuilder.setTitle(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        mediaDescriptionBuilder.setSubtitle(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_AUTHOR));
        mediaDescriptionBuilder.setDescription(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION));
        String artUri = pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI);
        mediaDescriptionBuilder.setIconUri(artUri == null ? null : Uri.parse(artUri));
        mediaDescriptionBuilder.setIconBitmap(pMediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON));
        mediaDescriptionBuilder.setExtras(bundle);
        return mediaDescriptionBuilder.build();
    }
}
