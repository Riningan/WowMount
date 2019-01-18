package com.riningan.wowmount.data.repository

import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.data.repository.storage.local.CharacterLocalStorage
import com.riningan.wowmount.data.repository.storage.remote.CharacterRemoteStorage
import com.riningan.wowmount.utils.Container
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


    fun getMountById(mountId: String): Flowable<Mount> {
        var onNextCount = 0
        return get()
                .map { (_, mounts) -> Container(mounts.find { it.id == mountId }) }
                .filter { it.value != null }
                .map { it.value!! }
                .doOnNext { onNextCount++ }
                .doOnComplete {
                    if (onNextCount == 0) {
                        throw NullPointerException("No mount with id = $mountId")
                    }
                }
    }
}