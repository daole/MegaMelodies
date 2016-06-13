package com.dreamdigitizers.megamelodies.views.classes.services.support;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.MusicNct;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.Singer;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.MusicZing;

import java.util.List;

public class MetadataBuilder {
    public static final String BUNDLE_KEY__TRACK = "track";

    public static MediaMetadataCompat build(com.dreamdigitizers.androiddatafetchingapisclient.models.nct.Song pSong) {
        String id = pSong.getId();
        String name = pSong.getName();
        String singerName = null;
        List<Singer> singers = pSong.getSingers();
        if (!singers.isEmpty()) {
            singerName = singers.get(0).getName();
        }

        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, name)
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, singerName)
                .build();
        mediaMetadata.getBundle().putSerializable(MetadataBuilder.BUNDLE_KEY__TRACK, pSong);

        return mediaMetadata;
    }

    public static MediaMetadataCompat build(com.dreamdigitizers.androiddatafetchingapisclient.models.zing.Song pSong) {
        String id = pSong.getId();
        String name = pSong.getName();
        String artist = pSong.getArtist();

        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, name)
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, artist)
                .build();
        mediaMetadata.getBundle().putSerializable(MetadataBuilder.BUNDLE_KEY__TRACK, pSong);

        return mediaMetadata;
    }

    public static MediaMetadataCompat build(MusicNct pMusicNct) {
        String id = pMusicNct.getId();
        String title = pMusicNct.getTitle();
        String creator = pMusicNct.getCreator();
        String avatar = pMusicNct.getAvatar();

        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, creator)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, avatar)
                .build();
        mediaMetadata.getBundle().putSerializable(MetadataBuilder.BUNDLE_KEY__TRACK, pMusicNct);

        return mediaMetadata;
    }

    public static MediaMetadataCompat build(MusicZing pMusicZing) {
        String id = pMusicZing.getId();
        String title = pMusicZing.getTitle();
        String performer = pMusicZing.getPerformer();
        String backImage = pMusicZing.getBackImage();

        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, performer)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, backImage)
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

        String artUri = pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI);
        MediaDescriptionCompat.Builder mediaDescriptionBuilder = new MediaDescriptionCompat.Builder();
        mediaDescriptionBuilder.setMediaId(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID));
        mediaDescriptionBuilder.setTitle(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        mediaDescriptionBuilder.setSubtitle(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_AUTHOR));
        mediaDescriptionBuilder.setDescription(pMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION));
        mediaDescriptionBuilder.setIconUri(artUri == null ? null : Uri.parse(artUri));
        mediaDescriptionBuilder.setIconBitmap(pMediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON));
        mediaDescriptionBuilder.setExtras(bundle);

        MediaDescriptionCompat mediaDescription = mediaDescriptionBuilder.build();
        return mediaDescription;
    }
}
