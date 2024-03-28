package com.hnu.heshequ.home.ui

/**
 * A simple interface to represent a fragment which only has a list view(any view that can display a list, like ListView,
 * RecyclerView, LinearLayout, etc.).
 */
interface IListFragment {
    fun isFirstItemVisible(): Boolean
}