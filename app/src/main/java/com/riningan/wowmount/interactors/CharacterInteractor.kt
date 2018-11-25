package com.riningan.wowmount.interactors

import com.riningan.wowmount.data.CharacterRepository
import com.riningan.wowmount.data.LocalPreferences
import com.riningan.wowmount.data.MountsRepository
import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CharacterInteractor constructor(private val mLocalPreferences: LocalPreferences,
                                      private val mMountsRepository: MountsRepository,
                                      private val mCharacterRepository: CharacterRepository) {
    fun get(server: String, realmName: String, characterName: String): Observable<Character> = mMountsRepository
            .update(server, realmName, characterName)
            .flatMap {  }
            .doOnNext {
                mLocalPreferences.apply {
                    setServer(server)
                    setRealmName(realmName)
                    setCharacterName(characterName)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}