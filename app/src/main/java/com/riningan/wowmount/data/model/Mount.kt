package com.riningan.wowmount.data.model

import com.riningan.wowmount.network.model.MountResponse
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Mount constructor(mountResponse: MountResponse = MountResponse.EMPTY): RealmObject() {
    @PrimaryKey
    private var mItemId: Int = mountResponse.itemId
    private var mName: String = mountResponse.name
    private var mQualityId: Int = mountResponse.qualityId
    private var mIcon: String = mountResponse.icon
    private var mIsGround: Boolean = mountResponse.isGround
    private var mIsFlying: Boolean = mountResponse.isFlying
    private var mIsAquatic: Boolean = mountResponse.isAquatic
    private var mIsCollected: Boolean = false
}