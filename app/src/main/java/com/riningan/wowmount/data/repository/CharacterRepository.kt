package com.riningan.wowmount.data.repository

import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.data.repository.storage.local.CharacterLocalStorage
import com.riningan.wowmount.data.repository.storage.remote.CharacterRemoteStorage
import io.reactivex.Flowable


class CharacterRepository(localStorage: CharacterLocalStorage,
                          remoteStorage: CharacterRemoteStorage) : BaseRepository<Pair<Character?, List<Mount>>>(localStorage, remoteStorage) {
    override fun calculateHashCode(t: Pair<Character?, List<Mount>>): Any {
        var result = t.first?.hashCode() ?: 0
        for (mount in t.second) {
            result = result * 31 + mount.hashCode()
        }
        return result
    }


    fun getMountById(mountId: String): Flowable<Mount> = get()
            .map { (_, mounts) ->
                mounts.find { it.id == mountId }
                        ?: throw NullPointerException("No mount with id = $mountId")
            }
}