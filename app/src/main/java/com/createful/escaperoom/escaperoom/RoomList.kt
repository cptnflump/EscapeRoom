package com.createful.escaperoom.escaperoom

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 *
 */
object RoomList {

    /**
     * An array of room items.
     */
    val ITEMS: ArrayList<Room> = ArrayList()

    fun populateRooms() {
        //rooms times MUST be given the format MM:SS
        //ROOM LIST
        addItem(Room("Scary Room", "03:00", arrayOf("aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa", "aaaa")))
        addItem(Room("Spooky Room", "01:00", arrayOf("aaaa", "bbbb", "cccc", "dddd")))
        addItem(Room("Rainbow Room", "05:00", arrayOf("aaaa", "aaaa", "aaaa", "aaaa")))
        addItem(Room("Roomy Room", "00:05", arrayOf("aaaa")))
        addItem(Room("test Room", "99:59", arrayOf("aaaa")))
        //ROOM LIST
    }


    /**
     * A map of Room items, by ID.
     */
    val ITEM_MAP: MutableMap<String, Room> = HashMap()

    private fun addItem(item: Room) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id!!, item)
    }
}

