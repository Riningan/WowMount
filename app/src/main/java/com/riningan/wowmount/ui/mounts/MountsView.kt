package com.riningan.wowmount.ui.mounts

import com.riningan.wowmount.data.model.Character
import com.riningan.wowmount.data.model.Mount
import com.riningan.wowmount.ui.base.BaseView


interface MountsView : BaseView {
    fun setCharacter(character: Character)
    fun setMounts(mounts: List<Mount>)
}