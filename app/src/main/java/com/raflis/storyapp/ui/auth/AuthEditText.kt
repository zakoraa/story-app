package com.raflis.storyapp.ui.auth

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.raflis.storyapp.R

class AuthEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var inputName: String = ""
    private var emptyInputMessage: String = ""
    private var minCharInputMessage: String = ""

    companion object {
        const val MIN_LENGTH = 8
    }

    init {
        init()
        onFocusListener()
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        when {
            s.isEmpty() -> {
                setError(emptyInputMessage, null)
            }

            inputName == context.getString(R.string.password) && s.length < MIN_LENGTH -> {
                setError(minCharInputMessage, null)
            }

            else -> {
                error = null
            }
        }
    }

    private fun init() {
        background = ContextCompat.getDrawable(context, R.drawable.edt_text_background)
        val horizontalPadding =
            context.resources.getDimensionPixelSize(R.dimen.edt_padding_horizontal)
        val drawablePadding = context.resources.getDimensionPixelSize(R.dimen.edt_icon_gap)

        setPadding(horizontalPadding, paddingTop, horizontalPadding, paddingBottom)
        compoundDrawablePadding = drawablePadding

        setHintTextColor(context.getColor(R.color.grey))
    }

    private fun onFocusListener() {
        setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setDrawableColor(R.color.primary)
            } else {
                setDrawableColor(R.color.grey)
            }
        }
    }

    private fun setDrawableColor(colorResId: Int) {
        emptyInputMessage = context.getString(R.string.error_empty_field, inputName)
        minCharInputMessage =
            context.getString(R.string.error_min_length_field, inputName, MIN_LENGTH)
        val color = ContextCompat.getColor(context, colorResId)

        val startDrawable = compoundDrawables[0]
        val endDrawable = compoundDrawables[2]

        startDrawable?.mutate()?.setTint(color)
        endDrawable?.mutate()?.setTint(color)

        setCompoundDrawablesWithIntrinsicBounds(
            startDrawable,
            compoundDrawables[1],
            endDrawable,
            compoundDrawables[3]
        )
    }

    fun setInputName(name: String) {
        inputName = name
    }
}
