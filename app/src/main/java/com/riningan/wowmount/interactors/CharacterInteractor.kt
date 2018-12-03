package com.riningan.wowmount.interactors

import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.data.repository.CharacterRepository
import io.reactivex.Observable
import io.reactivex.Scheduler


class CharacterInteractor constructor(executorThread: Scheduler, postExecutionThread: Scheduler,
                                      private val mCharacterRepository: CharacterRepository) : BaseInteractor(executorThread, postExecutionThread) {
    fun get(): Observable<Pair<Character, List<Mount>>> = mCharacterRepository
            .get()
            .map {(character, mounts)->
                character?.let {
                    Pair(it, mounts)
                } ?: throw NullPointerException("Character is null")
            }
            .execution()

    fun clear(): Observable<Boolean> = mCharacterRepository
            .clear()
            .execution()
}