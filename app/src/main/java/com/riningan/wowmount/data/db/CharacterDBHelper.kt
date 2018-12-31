package com.riningan.wowmount.data.db

import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount


interface CharacterDBHelper {
    fun getCharacter(): Character?
    fun getMounts(): List<Mount>

    fun setCharacter(character: Character)
    fun setMounts(mounts: List<Mount>)

    fun clearCharacter()
    fun clearMounts()
}