package com.riningan.wowmount.data.db

import com.riningan.wowmount.data.db.model.CharacterEntity
import com.riningan.wowmount.data.db.model.MountEntity
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import io.realm.Realm


class DBHelper : CharacterDBHelper {
    override fun getCharacter() = Realm.getDefaultInstance().run {
        val entity = where(CharacterEntity::class.java).findFirst()
        close()
        if (entity == null) {
            null
        } else {
            Character(
                    entity.name,
                    entity.realm,
                    entity.level,
                    entity.thumbnail,
                    entity.region)
        }
    }

    override fun getMounts() = Realm.getDefaultInstance().run {
        val mounts = where(MountEntity::class.java).findAll().toList()
        close()
        mounts.map {
            Mount(
                    it.id,
                    it.name,
                    it.itemId,
                    it.qualityId,
                    it.icon,
                    it.isGround,
                    it.isFlying,
                    it.isAquatic,
                    it.isCollected)
        }
    }

    override fun setCharacter(character: Character) {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            delete(CharacterEntity::class.java)
            copyToRealm(CharacterEntity().apply {
                name = character.name
                realm = character.realm
                level = character.level
                thumbnail = character.thumbnail
                region = character.region
            })
            commitTransaction()
            close()
        }
    }

    override fun setMounts(mounts: List<Mount>) {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            delete(MountEntity::class.java)
            mounts.map {
                MountEntity().apply {
                    id = it.id
                    name = it.name
                    itemId = it.itemId
                    qualityId = it.qualityId
                    icon = it.icon
                    isGround = it.isGround
                    isFlying = it.isFlying
                    isAquatic = it.isAquatic
                    isCollected = it.isCollected
                }
            }.forEach { copyToRealm(it) }
            commitTransaction()
            close()
        }
    }

    override fun clearCharacter() {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            delete(CharacterEntity::class.java)
            commitTransaction()
            close()
        }
    }

    override fun clearMounts() {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            delete(MountEntity::class.java)
            commitTransaction()
            close()
        }
    }
}