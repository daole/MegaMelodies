package com.dreamdigitizers.megamelodies.views.classes.services.support;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctMusic;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSinger;
import com.dreamdigitizers.androiddatafetchingapisclient.models.nct.NctSong;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingMusic;
import com.dreamdigitizers.androiddatafetchingapisclient.models.zing.ZingSong;
import com.dreamdigitizers.megamelodies.models.Track;

import java.util.List;

public class MetadataBuilder {
    public static final String BUNDLE_KEY__TRACK = "track";

    public static MediaMetadataCompat build(NctSong pNctSong) {
        Track track = new Track();
        track.setOriginalTrack(pNctSong);

        String id = pNctSong.getId();
        String name = pNctSong.getName();
        String singerName = null;
        List<NctSinger> nctSingers = pNctSong.getSingers();
        if (!nctSingers.isEmpty()) {
            singerName = nctSingers.get(0).getName();
        }

        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, name)
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, singerName)
                .build();
        mediaMetadata.getBundle().putSerializable(MetadataBuilder.BUNDLE_KEY__TRACK, track);

        return mediaMetadata;
    }

    public static MediaMetadataCompat build(ZingSong pZingSong) {
        Track track = new Track();
        track.setOriginalTrack(pZingSong);

        String id = pZingSong.getId();
        String name = pZingSong.getName();
        String artist = pZingSong.getArtist();

        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, name)
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, artist)
                .build();
        mediaMetadata.getBundle().putSerializable(MetadataBuilder.BUNDLE_KEY__TRACK, track);

        return mediaMetadata;
    }

    public static MediaMetadataCompat build(NctMusic pNctMusic) {
        Track track = new Track();
        track.setOriginalTrack(pNctMusic);

        String id = pNctMusic.getId();
        String title = pNctMusic.getTitle();
        String creator = pNctMusic.getCreator();
        String avatar = pNctMusic.getAvatar();

        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, creator)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, avatar)
                .build();
        mediaMetadata.getBundle().putSerializable(MetadataBuilder.BUNDLE_KEY__TRACK, track);

        return mediaMetadata;
    }

    public static MediaMetadataCompat build(ZingMusic pZingMusic) {
        Track track = new Track();
        track.setOriginalTrack(pZingMusic);

        String id = pZingMusic.getId();
        String title = pZingMusic.getTitle();
        String performer = pZingMusic.getPerformer();
        String backImage = pZingMusic.getBackImage();

        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, performer)
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, backImage)
                .build();
        mediaMetadata.getBundle().putSerializable(MetadataBuilder.BUNDLE_KEY__TRACK, track);

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
