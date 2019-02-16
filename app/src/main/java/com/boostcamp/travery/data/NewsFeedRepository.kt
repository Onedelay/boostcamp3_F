package com.boostcamp.travery.data;

import com.boostcamp.travery.data.model.UserAction
import com.boostcamp.travery.data.remote.NewsFeedRemoteDataSource
import com.boostcamp.travery.data.remote.model.MyResponse
import com.boostcamp.travery.data.remote.model.NewsFeed
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsFeedRepository private constructor(private val newsFeedDataSource: NewsFeedDataSource) : NewsFeedDataSource {
    private val mCachedFeed = ArrayList<NewsFeed>()

    companion object {
        const val FEED_LOAD_COUNT = 10
        @Volatile
        private var INSTANCE: NewsFeedRepository? = null

        @JvmStatic
        fun getInstance() = INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                            ?: NewsFeedRepository(NewsFeedRemoteDataSource.getInstance()).also {
                                INSTANCE = it
                            }
                }
    }


    override fun getFeedList(start: Int): Single<List<NewsFeed>> {
        return if (mCachedFeed.size - start > FEED_LOAD_COUNT) {
            Single.just(mCachedFeed.subList(start, start + FEED_LOAD_COUNT).toList())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

        } else {
            newsFeedDataSource.getFeedList(start)
                    .doOnSuccess { mCachedFeed.addAll(it) }
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun uploadFeed(userAction: UserAction, userId: String): Single<MyResponse> {
        return newsFeedDataSource.uploadFeed(userAction, userId).observeOn(AndroidSchedulers.mainThread())
    }
}
