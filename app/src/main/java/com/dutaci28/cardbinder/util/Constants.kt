//CardBinder - a Magic: The Gathering collector's app.
//Copyright (C) 2024 Catalin Duta
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.(https://github.com/dutaci28/CardBinder?tab=GPL-3.0-1-ov-file#readme).
//If not, see <https://www.gnu.org/licenses/>.


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
        const val NAV_ARGUMENT_SEARCH_TEXT = "search_text"
    }
}