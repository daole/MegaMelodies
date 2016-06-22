package com.dreamdigitizers.megamelodies.views.classes.services.support;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;

import com.dreamdigitizers.androidbaselibrary.views.classes.services.ServiceMediaBrowser;
import com.dreamdigitizers.androidbaselibrary.views.classes.services.support.MediaPlayerNotificationReceiver;
import com.dreamdigitizers.megamelodies.R;
import com.dreamdigitizers.megamelodies.views.classes.activities.ActivityMain;

import java.util.ArrayList;
import java.util.List;

public class PlaybackNotificationReceiver extends MediaPlayerNotificationReceiver {
    private static final int REQUEST_CODE = 0;

    public PlaybackNotificationReceiver(ServiceMediaBrowser pService) {
        super(pService);
    }

    @Override
    protected Notification createNotification() {
        if (this.mMediaMetadata == null || this.mPlaybackState == null) {
            return null;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.mService);
        int skipToPreviousButtonPosition = -1;
        int playPauseButtonPosition = 0;
        int skipToNextButtonPosition = -1;

        if ((this.mPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0) {
            notificationBuilder.addAction(R.drawable.ic__skip_to_previous, this.mService.getString(R.string.action__skip_to_previous), this.getSkipToPreviousPendingIntent());
            skipToPreviousButtonPosition = playPauseButtonPosition;
            playPauseButtonPosition++;
        }

        if(this.mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            notificationBuilder.addAction(R.drawable.ic__pause, this.mService.getString(R.string.action__pause), this.getPausePendingIntent());
        } else {
            notificationBuilder.addAction(R.drawable.ic__play, this.mService.getString(R.string.action__play), this.getPlayPendingIntent());
        }

        if ((this.mPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
            notificationBuilder.addAction(R.drawable.ic__skip_to_next, this.mService.getString(R.string.action__skip_to_next), this.getSkipToNextPendingIntent());
            skipToNextButtonPosition = playPauseButtonPosition + 1;
        }

        List<Integer> buttonPositions = new ArrayList<>();
        if (skipToPreviousButtonPosition >= 0) {
            buttonPositions.add(skipToPreviousButtonPosition);
        }
        buttonPositions.add(playPauseButtonPosition);
        if (skipToNextButtonPosition >= 0) {
            buttonPositions.add(skipToNextButtonPosition);
        }
        int[] buttons = new int[buttonPositions.size()];
        for(int i = 0; i < buttonPositions.size(); i++) {
            buttons[i] = buttonPositions.get(i);
        }

        MediaDescriptionCompat mediaDescription = this.mMediaMetadata.getDescription();
        String contentTitle = this.mMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE);
        String contentText = this.mMediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_AUTHOR);

        Bitmap art = mediaDescription.getIconBitmap();
        if (art == null) {
            art = BitmapFactory.decodeResource(mService.getResources(), R.drawable.ic__megamelodies_logo);
        }

        notificationBuilder
                .setStyle(new NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(buttons)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(this.getStopPendingIntent())
                        .setMediaSession(this.mSessionToken))
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentIntent(this.createContentIntent(mediaDescription))
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic__megamelodies_notification)
                .setLargeIcon(art);

        this.setNotificationPlaybackState(notificationBuilder);

        return notificationBuilder.build();
    }

    private PendingIntent createContentIntent(MediaDescriptionCompat pMediaDescription) {
        Intent intent = new Intent(this.mService, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (pMediaDescription != null) {
            intent.putExtra(ActivityMain.EXTRA__CURRENT_MEDIA_DESCRIPTION, pMediaDescription);
        }
        return PendingIntent.getActivity(this.mService, PlaybackNotificationReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void setNotificationPlaybackState(NotificationCompat.Builder pNotificationBuilder) {
        if (this.mPlaybackState == null || !this.mStarted) {
            this.mService.stopForeground(true);
            return;
        }
        if (this.mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING && this.mPlaybackState.getPosition() >= 0) {
            pNotificationBuilder.setWhen(System.currentTimeMillis() - this.mPlaybackState.getPosition())
                    .setShowWhen(true)
                    .setUsesChronometer(true);
        } else {
            pNotificationBuilder.setWhen(0)
                    .setShowWhen(false)
                    .setUsesChronometer(false);
        }
    }
}
