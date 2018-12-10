package com.riningan.wowmount.interactors

import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.data.repository.CharacterRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler


class CharacterInteractor constructor(executorThread: Scheduler, postExecutionThread: Scheduler,
                                      private val mCharacterRepository: CharacterRepository) : BaseInteractor(executorThread, postExecutionThread) {
    fun get(): Observable<Pair<Character, List<Mount>>> = mCharacterRepository
            .get()
            .map { (character, mounts) ->
                character?.let { Pair(it, mounts) }
                        ?: throw NullPointerException("Character is null")
            }
            .toObservable()
            .execution()

    fun update(): Observable<Pair<Character, List<Mount>>> = mCharacterRepository
            .update()
            .map { (character, mounts) ->
                character?.let { Pair(it, mounts) }
                        ?: throw NullPointerException("Character is null")
            }
            .toObservable()
            .execution()

    fun getMountByItemId(mountId: String): Observable<Mount> = mCharacterRepository
            .getMountById(mountId)
            .toObservable()
            .execution()

    fun clear(): Completable = mCharacterRepository
            .clear()
            .execution()
}