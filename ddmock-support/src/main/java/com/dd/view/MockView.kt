package com.dd.view

interface MockView {
    fun showMockEntries(mockEntries: ArrayList<String>)
    fun editMockEntry()
    fun closeEditMockEntry()
    fun showGenericError()
}