package com.riningan.wowmount.interactor

import com.riningan.wowmount.data.repository.CharacterRepository
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.utils.SchedulersProvider
import io.reactivex.Completable
import io.reactivex.Observable


class CharacterInteractor constructor(schedulersProvider: SchedulersProvider,
                                      private val mCharacterRepository: CharacterRepository) : BaseInteractor(schedulersProvider) {
    fun get(): Observable<Pair<Character, List<Mount>>> = mCharacterRepository
            .get()
            .map { (character, mounts) ->
                character?.let { Pair(it, mounts) }
                        ?: throw NullPointerException("Character is null")
            }
            .toObservable()
            .errorCast()
            .execution()

    fun update(): Observable<Pair<Character, List<Mount>>> = mCharacterRepository
            .update()
            .map { (character, mounts) ->
                character?.let { Pair(it, mounts) }
                        ?: throw NullPointerException("Character is null")
            }
            .toObservable()
            .errorCast()
            .execution()

    fun clear(): Completable = mCharacterRepository
            .clear()
            .execution()

    fun getMountById(mountId: String): Observable<Mount> = mCharacterRepository
            .getMountById(mountId)
            .toObservable()
            .errorCast()
            .execution()
}