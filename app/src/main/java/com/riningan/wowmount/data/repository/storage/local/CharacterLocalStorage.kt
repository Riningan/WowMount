package com.riningan.wowmount.data.repository.storage.local

import com.riningan.wowmount.data.db.CharacterDBHelper
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import io.reactivex.Completable
import io.reactivex.Single


class CharacterLocalStorage(private val mCharacterDBHelper: CharacterDBHelper) : BaseLocalStorage<Pair<Character?, List<Mount>>>() {
    override fun get(): Single<Pair<Character?, List<Mount>>> = Single
            .just(Pair(mCharacterDBHelper.getCharacter(), mCharacterDBHelper.getMounts()))

    override fun set(cache: Pair<Character?, List<Mount>>): Completable = Completable
            .fromCallable {
                mCharacterDBHelper.apply {
                    if (cache.first == null) {
                        throw NullPointerException("Null character")
                    } else {
                        setCharacter(cache.first!!)
                    }
                    setMounts(cache.second)
                }
            }

    override fun clear(): Completable = Completable
            .fromCallable {
                mCharacterDBHelper.apply {
                    clearCharacter()
                    clearMounts()
                }
            }
}