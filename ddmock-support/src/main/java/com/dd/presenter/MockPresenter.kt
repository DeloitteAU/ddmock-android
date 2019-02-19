package com.dd.presenter

import com.dd.DDMock
import com.dd.MockEntry
import com.dd.view.MockView
import kotlinx.coroutines.*

class MockPresenter {
    private var view: MockView? = null
    private var clickedMockEntry: MockEntry? = null

    fun onAttach(view: MockView) {
        this.view = view
    }

    @ExperimentalCoroutinesApi
    fun onResume() {
        CoroutineScope(Dispatchers.Main).launch {
            val job = async(Dispatchers.IO) {
                ArrayList(DDMock.getEntries().keys)
            }
            val keys = job.await()
            view?.showMockEntries(keys)
        }
    }

    fun onEntryClicked(key: String) {
        clickedMockEntry = DDMock.getEntry(key)
        view?.editMockEntry()
    }

    fun getEditMockEntry() = clickedMockEntry

    fun saveEditMockEntry(responseTime: String, statusCode: String, selectedFile: Int) {
        clickedMockEntry?.let { mockEntry ->
            mockEntry.responseTime = responseTime.toLong()
            mockEntry.statusCode = statusCode.toInt()
            mockEntry.selectedFile = selectedFile
        }
        clickedMockEntry = null
        view?.closeEditMockEntry()
    }

    fun onDetach() {
        view = null
    }
}