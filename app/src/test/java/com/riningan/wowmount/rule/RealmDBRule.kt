package com.riningan.wowmount.rule

import android.content.Context
import com.riningan.wowmount.data.db.model.CharacterEntity
import com.riningan.wowmount.data.db.model.MountEntity
import com.riningan.wowmount.data.repository.storage.remote.CharacterRemoteStorage
import com.riningan.wowmount.utils.isContain
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class RealmDBRule(private val mAppContext: Context) : TestRule {
    override fun apply(base: Statement, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            Realm.init(mAppContext)
            RealmConfiguration.Builder()
                    .inMemory()
                    .name("test-realm")
                    .build()
            Realm.getDefaultInstance().apply {
                beginTransaction()
                copyToRealm(CHARACTER_ENTITY)
                MOUNT_ENTITY_LIST.forEach { copyToRealm(it) }
                commitTransaction()
                close()
            }
            base.evaluate()
        }
    }


    fun getRealm(): Realm = Realm.getDefaultInstance()


    companion object {
        val CHARACTER_ENTITY = CharacterEntity().apply {
            name = "Квентис"
            realm = "Гордунни"
            level = 120
            thumbnail = "http://url"
            region = "en"
        }

        val MOUNT_ENTITY_1 = MountEntity().apply {
            id = CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_1)
            name = MountsResponseRule.MOUNT_1.name
            itemId = MountsResponseRule.MOUNT_1.itemId
            qualityId = MountsResponseRule.MOUNT_1.qualityId
            clientId = MountsResponseRule.MOUNT_1.qualityId
            icon = MountsResponseRule.MOUNT_1.icon
            isGround = MountsResponseRule.MOUNT_1.isGround
            isFlying = MountsResponseRule.MOUNT_1.isFlying
            isAquatic = MountsResponseRule.MOUNT_1.isAquatic
            isCollected = CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
        }

        val MOUNT_ENTITY_2 = MountEntity().apply {
            id = CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_2)
            name = MountsResponseRule.MOUNT_2.name
            itemId = MountsResponseRule.MOUNT_2.itemId
            qualityId = MountsResponseRule.MOUNT_2.qualityId
            clientId = MountsResponseRule.MOUNT_2.qualityId
            icon = MountsResponseRule.MOUNT_2.icon
            isGround = MountsResponseRule.MOUNT_2.isGround
            isFlying = MountsResponseRule.MOUNT_2.isFlying
            isAquatic = MountsResponseRule.MOUNT_2.isAquatic
            isCollected = CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
        }

        val MOUNT_ENTITY_3 = MountEntity().apply {
            id = CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_3)
            name = MountsResponseRule.MOUNT_3.name
            itemId = MountsResponseRule.MOUNT_3.itemId
            qualityId = MountsResponseRule.MOUNT_3.qualityId
            clientId = MountsResponseRule.MOUNT_3.qualityId
            icon = MountsResponseRule.MOUNT_3.icon
            isGround = MountsResponseRule.MOUNT_3.isGround
            isFlying = MountsResponseRule.MOUNT_3.isFlying
            isAquatic = MountsResponseRule.MOUNT_3.isAquatic
            isCollected = CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
        }

        val MOUNT_ENTITY_4 = MountEntity().apply {
            id = CharacterRemoteStorage.createId(MountsResponseRule.MOUNT_4)
            name = MountsResponseRule.MOUNT_4.name
            itemId = MountsResponseRule.MOUNT_4.itemId
            qualityId = MountsResponseRule.MOUNT_4.qualityId
            clientId = MountsResponseRule.MOUNT_4.qualityId
            icon = MountsResponseRule.MOUNT_4.icon
            isGround = MountsResponseRule.MOUNT_4.isGround
            isFlying = MountsResponseRule.MOUNT_4.isFlying
            isAquatic = MountsResponseRule.MOUNT_4.isAquatic
            isCollected = CharacterResponseRule.COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
        }

        val MOUNT_ENTITY_LIST = listOf(MOUNT_ENTITY_1, MOUNT_ENTITY_2, MOUNT_ENTITY_3, MOUNT_ENTITY_4)
    }
}