package com.dutaci28.cardbinder.util

class Constants {
    companion object {
        const val BASE_URL = "https://api.scryfall.com"
        const val MTG_CARD_TABLE = "mtg_card_table"
        const val LEGALITY_TABLE = "legality_table"
        const val MTG_CARD_REMOTE_KEYS_TABLE = "mtg_card_remote_keys_table"
        const val MTG_CARD_DATABASE = "mtg_card_database"
        const val ITEMS_PER_PAGE = 175 //default for scryfall
        const val NAV_ARGUMENT_CARD_ID = "id"
        const val NAV_ARGUMENT_SHOULD_FOCUS_SEARCH = "should_focus_search"
    }
}