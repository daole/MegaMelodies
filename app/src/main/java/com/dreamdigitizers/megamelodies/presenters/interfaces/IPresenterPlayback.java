package com.dreamdigitizers.megamelodies.presenters.interfaces;

import com.dreamdigitizers.androidbaselibrary.utilities.UtilsDialog;
import com.dreamdigitizers.megamelodies.models.Playlist;
import com.dreamdigitizers.megamelodies.models.Track;

import java.util.List;

public interface IPresenterPlayback extends IPresenterRx {
    void nctSearch(final UtilsDialog.IRetryAction pRetryAction, String pQ);
    void zingSearch(final UtilsDialog.IRetryAction pRetryAction, String pType, int pNum, String pQuery);
    void nctFetch(final UtilsDialog.IRetryAction pRetryAction, String pUrl, String pKeyword);
    void zingFetch(final UtilsDialog.IRetryAction pRetryAction, String pName, String pArtist, String pId);
    List<Track> retrieveFavoriteTracks();
    List<Playlist> retrieveAllPlaylists();
    void checkFavoriteTrack(Track pTrack);
    void favorite(Track pTrack);
    void unfavorite(Track pTrack);
    Playlist createPlaylist(Track pTrack, String pPlaylistName);
    void deletePlaylist(Playlist pPlaylist);
    void addToPlaylist(Track pTrack, Playlist pPlaylist);
    void removeFromPlaylist(Track pTrack, Playlist pPlaylist);
}
