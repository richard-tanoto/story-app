package com.richard.storyapp.core.ui

import android.app.Dialog
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.richard.storyapp.R
import com.richard.storyapp.core.util.StoryPermission
import com.richard.storyapp.databinding.LayoutLoadingBinding

abstract class BaseFragment : Fragment() {

    private lateinit var dialog: Dialog

    fun showLoading(isLoading: Boolean, onDismissListener: () -> Unit = {}) {
        if (::dialog.isInitialized.not()) {
            dialog = Dialog(requireContext(), R.style.Dialog_Loading)
            val dialogBinding = LayoutLoadingBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.apply {
                setLayout(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                )
            }
            dialog.setOnDismissListener {
                onDismissListener()
            }
        }
        if (isLoading) dialog.show() else dialog.hide()
    }

    fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun permissionGranted(permission: StoryPermission): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission.value
        ) == PackageManager.PERMISSION_GRANTED

}