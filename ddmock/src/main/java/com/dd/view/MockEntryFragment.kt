package com.dd.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dd.R
import com.dd.presenter.MockPresenter
import kotlinx.android.synthetic.main.fragment_mock_entry.*

class MockEntryFragment : Fragment(), TextWatcher {
    private var presenter: MockPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mock_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            // Set status code auto complete text view
            val httpCodes = resources.getStringArray(R.array.http_code)
            val statusCodeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, httpCodes)
            statusCode.setAdapter(statusCodeAdapter)

            presenter?.getEditMockEntry()?.let { mockEntry ->
                pathName.text = mockEntry.path
                responseTime.setText(mockEntry.responseTime.toString(), TextView.BufferType.EDITABLE)

                // Status code setup
                val currentStatusCode = mockEntry.statusCode.toString()
                for (index in 0 until httpCodes.size) {
                    val httpCode = httpCodes[index]
                    if (httpCode.split(" ")[0] == currentStatusCode) {
                        statusCode.setText(httpCode, TextView.BufferType.EDITABLE)
                    }
                }
                statusCode.addTextChangedListener(this@MockEntryFragment)
                statusCode.setOnItemClickListener { _, _, _, _ ->
                    // Enable save button as user selected status code from dropdown
                    save.isEnabled = true
                }

                // Populate the list of mock files
                mockEntry.files.let { mockFiles ->
                    for (i in 0 until mockFiles.size) {
                        val segments = mockFiles[i].split("/")
                        val fileName = segments[segments.lastIndex]
                        val radioButton = RadioButton(context)
                        radioButton.text = fileName
                        radioButton.id = i
                        files.addView(radioButton)
                    }
                }

                // Set currently selected mock file
                files.check(mockEntry.selectedFile)
            }

            // Save button listener
            save.setOnClickListener {
                hideSoftKeyboard()
                presenter?.saveEditMockEntry(
                        responseTime.text.toString(),
                        statusCode.text.toString().split(" ")[0],
                        files.checkedRadioButtonId
                )
            }
        }
    }

    private fun hideSoftKeyboard() {
        activity?.run {
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            currentFocus?.run {
                imm.hideSoftInputFromWindow(windowToken, 0)
            } ?: run {
                imm.hideSoftInputFromWindow(View(this).windowToken, 0)
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
        // User edited status code field and did not choose from dropdown,
        // so resetting index value and disabling save button
        save.isEnabled = false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter = (activity as? MockActivity)?.presenter
    }

    override fun onDetach() {
        presenter = null
        super.onDetach()
    }

}