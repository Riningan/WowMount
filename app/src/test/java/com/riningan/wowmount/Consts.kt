package com.riningan.wowmount

import com.riningan.wowmount.data.db.model.CharacterEntity
import com.riningan.wowmount.data.db.model.MountEntity
import com.riningan.wowmount.data.network.model.CharacterMountsResponse
import com.riningan.wowmount.data.network.model.CharacterResponse
import com.riningan.wowmount.data.network.model.MountResponse
import com.riningan.wowmount.data.network.model.SpreadsheetCSVResponse
import com.riningan.wowmount.data.repository.model.Character
import com.riningan.wowmount.data.repository.model.Mount
import com.riningan.wowmount.data.repository.storage.remote.CharacterRemoteStorage
import com.riningan.wowmount.utils.isContain


const val NAME = "Квентис"
const val REALM = "Гордунни"
const val LEVEL = 120
const val THUMBNAIL = "http://url"
const val REGION = "en"

val MOUNT_RESPONSE_1 = MountResponse(
        "Test mount 1",
        1,
        1,
        1,
        1,
        "Test mount 1 Icon",
        true,
        true,
        true,
        true)

val MOUNT_RESPONSE_2 = MountResponse(
        "Test mount 2",
        2,
        2,
        2,
        2,
        "Test mount 2 Icon",
        true,
        false,
        true,
        true)

val MOUNT_RESPONSE_3 = MountResponse(
        "Test mount 3",
        3,
        3,
        3,
        3,
        "Test mount 3 Icon",
        false,
        true,
        true,
        true)

val MOUNT_RESPONSE_4 = MountResponse(
        "Test mount 4",
        4,
        4,
        4,
        4,
        "Test mount 4 Icon",
        true,
        false,
        false,
        false)

val MOUNT_RESPONSE_LIST = listOf(MOUNT_RESPONSE_1, MOUNT_RESPONSE_2, MOUNT_RESPONSE_3, MOUNT_RESPONSE_4)

val CHARACTER_COLLECTED_MOUNT_LIST = listOf(MOUNT_RESPONSE_1, MOUNT_RESPONSE_3, MOUNT_RESPONSE_4)

val CHARACTER_RESPONSE = CharacterResponse(
        0L,
        NAME,
        REALM,
        "battlegroup",
        0,
        0,
        0,
        LEVEL,
        0,
        THUMBNAIL,
        "calsClass",
        0,
        CharacterMountsResponse(CHARACTER_COLLECTED_MOUNT_LIST.size, MOUNT_RESPONSE_LIST.size - CHARACTER_COLLECTED_MOUNT_LIST.size, CHARACTER_COLLECTED_MOUNT_LIST),
        0)


val ROW_1 = SpreadsheetCSVResponse(
        MOUNT_RESPONSE_1.spellId,
        MOUNT_RESPONSE_1.creatureId,
        MOUNT_RESPONSE_1.itemId,
        101,
        MOUNT_RESPONSE_1.name
)

val ROW_2 = SpreadsheetCSVResponse(
        MOUNT_RESPONSE_2.spellId,
        MOUNT_RESPONSE_2.creatureId,
        MOUNT_RESPONSE_2.itemId,
        102,
        MOUNT_RESPONSE_2.name
)

val ROW_3 = SpreadsheetCSVResponse(
        MOUNT_RESPONSE_3.spellId,
        MOUNT_RESPONSE_3.creatureId,
        MOUNT_RESPONSE_3.itemId,
        103,
        MOUNT_RESPONSE_3.name
)

val ROW_LIST = listOf(ROW_1, ROW_2, ROW_3)


val MOUNT_ENTITY_1 = MountEntity().apply {
    id = CharacterRemoteStorage.createId(MOUNT_RESPONSE_1)
    name = MOUNT_RESPONSE_1.name
    itemId = MOUNT_RESPONSE_1.itemId
    qualityId = MOUNT_RESPONSE_1.qualityId
    clientId = MOUNT_RESPONSE_1.qualityId
    icon = MOUNT_RESPONSE_1.icon
    isGround = MOUNT_RESPONSE_1.isGround
    isFlying = MOUNT_RESPONSE_1.isFlying
    isAquatic = MOUNT_RESPONSE_1.isAquatic
    isCollected = CHARACTER_COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
}

val MOUNT_ENTITY_2 = MountEntity().apply {
    id = CharacterRemoteStorage.createId(MOUNT_RESPONSE_2)
    name = MOUNT_RESPONSE_2.name
    itemId = MOUNT_RESPONSE_2.itemId
    qualityId = MOUNT_RESPONSE_2.qualityId
    clientId = MOUNT_RESPONSE_2.qualityId
    icon = MOUNT_RESPONSE_2.icon
    isGround = MOUNT_RESPONSE_2.isGround
    isFlying = MOUNT_RESPONSE_2.isFlying
    isAquatic = MOUNT_RESPONSE_2.isAquatic
    isCollected = CHARACTER_COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
}

val MOUNT_ENTITY_3 = MountEntity().apply {
    id = CharacterRemoteStorage.createId(MOUNT_RESPONSE_3)
    name = MOUNT_RESPONSE_3.name
    itemId = MOUNT_RESPONSE_3.itemId
    qualityId = MOUNT_RESPONSE_3.qualityId
    clientId = MOUNT_RESPONSE_3.qualityId
    icon = MOUNT_RESPONSE_3.icon
    isGround = MOUNT_RESPONSE_3.isGround
    isFlying = MOUNT_RESPONSE_3.isFlying
    isAquatic = MOUNT_RESPONSE_3.isAquatic
    isCollected = CHARACTER_COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
}

val MOUNT_ENTITY_4 = MountEntity().apply {
    id = CharacterRemoteStorage.createId(MOUNT_RESPONSE_4)
    name = MOUNT_RESPONSE_4.name
    itemId = MOUNT_RESPONSE_4.itemId
    qualityId = MOUNT_RESPONSE_4.qualityId
    clientId = MOUNT_RESPONSE_4.qualityId
    icon = MOUNT_RESPONSE_4.icon
    isGround = MOUNT_RESPONSE_4.isGround
    isFlying = MOUNT_RESPONSE_4.isFlying
    isAquatic = MOUNT_RESPONSE_4.isAquatic
    isCollected = CHARACTER_COLLECTED_MOUNT_LIST.isContain { it.itemId == itemId }
}

val MOUNT_ENTITY_LIST = listOf(MOUNT_ENTITY_1, MOUNT_ENTITY_2, MOUNT_ENTITY_3, MOUNT_ENTITY_4)

val CHARACTER_ENTITY = CharacterEntity().apply {
    name = NAME
    realm = REALM
    level = LEVEL
    thumbnail = THUMBNAIL
    region = REGION
}


val MOUNT_1 = Mount(
        MOUNT_ENTITY_1.id,
        MOUNT_ENTITY_1.name,
        MOUNT_ENTITY_1.itemId,
        MOUNT_ENTITY_1.qualityId,
        MOUNT_ENTITY_1.clientId,
        MOUNT_ENTITY_1.icon,
        MOUNT_ENTITY_1.isGround,
        MOUNT_ENTITY_1.isFlying,
        MOUNT_ENTITY_1.isAquatic,
        MOUNT_ENTITY_1.isCollected)

val MOUNT_2 = Mount(
        MOUNT_ENTITY_2.id,
        MOUNT_ENTITY_2.name,
        MOUNT_ENTITY_2.itemId,
        MOUNT_ENTITY_2.qualityId,
        MOUNT_ENTITY_2.clientId,
        MOUNT_ENTITY_2.icon,
        MOUNT_ENTITY_2.isGround,
        MOUNT_ENTITY_2.isFlying,
        MOUNT_ENTITY_2.isAquatic,
        MOUNT_ENTITY_2.isCollected)

val MOUNT_3 = Mount(
        MOUNT_ENTITY_3.id,
        MOUNT_ENTITY_3.name,
        MOUNT_ENTITY_3.itemId,
        MOUNT_ENTITY_3.qualityId,
        MOUNT_ENTITY_3.clientId,
        MOUNT_ENTITY_3.icon,
        MOUNT_ENTITY_3.isGround,
        MOUNT_ENTITY_3.isFlying,
        MOUNT_ENTITY_3.isAquatic,
        MOUNT_ENTITY_3.isCollected)

val MOUNT_4 = Mount(
        MOUNT_ENTITY_4.id,
        MOUNT_ENTITY_4.name,
        MOUNT_ENTITY_4.itemId,
        MOUNT_ENTITY_4.qualityId,
        MOUNT_ENTITY_4.clientId,
        MOUNT_ENTITY_4.icon,
        MOUNT_ENTITY_4.isGround,
        MOUNT_ENTITY_4.isFlying,
        MOUNT_ENTITY_4.isAquatic,
        MOUNT_ENTITY_4.isCollected)

val MOUNT_LIST = listOf(MOUNT_1, MOUNT_2, MOUNT_3, MOUNT_4)

val CHARACTER = Character(
        NAME,
        REALM,
        LEVEL,
        THUMBNAIL,
        REGION)