package com.onandoff.onandoff_android.data.api.notification

import com.onandoff.onandoff_android.data.model.FeedData
import com.onandoff.onandoff_android.data.model.FeedResponse
import com.onandoff.onandoff_android.data.model.NotificationData
import com.onandoff.onandoff_android.data.model.NotificationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH

interface NotificationInterface {
    @PATCH("/alarms/notice")
    fun updateNoticeAlarmResponse(
        @Body body: NotificationData
    ): Call<NotificationResponse>

    @PATCH("/alarms/like")
    fun updateLikeAlarmResponse(
        @Body body: NotificationData
    ): Call<NotificationResponse>

    @PATCH("/alarms/following")
    fun updateFollowAlarmResponse(
        @Body body: NotificationData
    ): Call<NotificationResponse>
}