package com.riningan.wowmount.domain.interactor

import com.riningan.wowmount.data.repository.CharacterRepository
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.domain.SchedulersProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


class CharacterInteractor constructor(schedulersProvider: SchedulersProvider, private val mCharacterRepository: CharacterRepository) : BaseInteractor(schedulersProvider) {
    fun get(): Flowable<Pair<Character, List<Mount>>> = mCharacterRepository
            .get()
            .map { (character, mounts) ->
                character?.let { Pair(it, mounts) }
                        ?: throw NullPointerException("Character is null")
            }
            .execution()
            .errorCast()

    fun update(): Single<Pair<Character, List<Mount>>> = mCharacterRepository
            .update()
            .map { (character, mounts) ->
                character?.let { Pair(it, mounts) }
                        ?: throw NullPointerException("Character is null")
            }
            .execution()
            .errorCast()

    fun clear(): Completable = mCharacterRepository
            .clear()
            .execution()
            .errorCast()

    fun getMountById(mountId: String): Flowable<Mount> = mCharacterRepository
            .getMountById(mountId)
            .execution()
            .errorCast()
}