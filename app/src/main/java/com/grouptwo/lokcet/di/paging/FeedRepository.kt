package com.grouptwo.lokcet.di.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.grouptwo.lokcet.data.model.Feed
import com.grouptwo.lokcet.di.service.AccountService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val pagingConfig: PagingConfig,
    private val firestore: FirebaseFirestore,
    private val accountService: AccountService
) {
    fun getFeeds(friendIds: List<String>): Flow<PagingData<Feed>> {
        return Pager(config = pagingConfig, pagingSourceFactory = {
            FeedPagingSource(
                firestore = firestore, friendIds = friendIds, accountService = accountService
            )
        }).flow
    }
}