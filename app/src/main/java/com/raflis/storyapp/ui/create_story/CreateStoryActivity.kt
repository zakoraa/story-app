package com.raflis.storyapp.ui.create_story

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raflis.storyapp.R
import com.raflis.storyapp.data.ResultStatus
import com.raflis.storyapp.databinding.ActivityCreateStoryBinding
import com.raflis.storyapp.ui.home.HomeActivity
import com.raflis.storyapp.utils.FileConverterUtil.getImageUri
import com.raflis.storyapp.utils.FileConverterUtil.reduceFileImage
import com.raflis.storyapp.utils.FileConverterUtil.uriToFile
import com.raflis.storyapp.viewModel.ViewModelFactory
import com.raflis.storyapp.viewModel.story.StoryViewModel
import java.io.File

class CreateStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStoryBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: StoryViewModel by viewModels {
        factory
    }
    private var currentImageUri: Uri? = null
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initAction()
    }

    private fun initAction() {
        with(binding) {
            btnCamera.setOnClickListener {
                startCamera()
            }
            btnGallery.setOnClickListener {
                startGallery()
            }
            btnCreateStory.setOnClickListener {
                createStory()
            }
            ibBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun createStory() {
        with(binding) {
            val description = edtDesc.text.toString()
            if (file != null) {
                viewModel.createStory(file!!, description)
                    .observe(this@CreateStoryActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is ResultStatus.Loading -> {
                                    progressBar.visibility = View.VISIBLE
                                    btnCreateStory.visibility = View.GONE
                                    btnLoading.visibility = View.VISIBLE
                                    btnGallery.isClickable = false
                                    btnCamera.isClickable = false
                                }

                                is ResultStatus.Success -> {
                                    resetDisplay()
                                    showToast(getString(R.string.create_story_success))

                                    val intent =
                                        Intent(this@CreateStoryActivity, HomeActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finishAffinity()
                                }

                                is ResultStatus.Error -> {
                                    resetDisplay()
                                    showToast(getString(R.string.error_something_wrong))
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun resetDisplay() {
        with(binding) {
            progressBar.visibility = View.GONE
            btnCreateStory.visibility = View.VISIBLE
            btnLoading.visibility = View.GONE
            btnGallery.isClickable = true
            btnCamera.isClickable = true
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            currentImageUri.let {
                if (it != null) {
                    file = uriToFile(it, this).reduceFileImage()
                }
            }
            showImage()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri.let {
                if (it != null) {
                    file = uriToFile(it, this).reduceFileImage()
                }
            }
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivPreview.setImageURI(it)
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}