package com.dd.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dd.R
import com.dd.adapter.MockAdapter
import com.dd.presenter.MockPresenter
import kotlinx.android.synthetic.main.activity_mock.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MockActivity : AppCompatActivity(), MockView, MockAdapter.Callback {
    private var adapter: MockAdapter? = null
    val presenter = MockPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock)

        mockList.layoutManager = LinearLayoutManager(this)
        adapter = MockAdapter(this)
        mockList.adapter = adapter
        presenter.onAttach(this)
    }

    @ExperimentalCoroutinesApi
    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun showMockEntries(mockEntries: ArrayList<String>) {
        adapter?.items = mockEntries
    }

    override fun onEntryClicked(key: String) {
        presenter.onEntryClicked(key)
    }

    override fun editMockEntry() {
        val tag = MockEntryFragment::class.java.simpleName
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, com.dd.view.MockEntryFragment(), tag)
                .addToBackStack(tag)
                .commitAllowingStateLoss()
        container.visibility = View.VISIBLE
    }

    override fun closeEditMockEntry() {
        container.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.container) != null) {
            closeEditMockEntry()
        }
        super.onBackPressed()
    }

    override fun showGenericError() {
        Toast.makeText(this, R.string.mock_error, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}