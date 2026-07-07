package com.ashvinprajapati.soundwave.ui.library

import com.ashvinprajapati.soundwave.domain.model.PlaylistResponse

sealed class PlaylistAction {
    object Create : PlaylistAction()
    data class LongPress(val playlist: PlaylistResponse) : PlaylistAction()
}
