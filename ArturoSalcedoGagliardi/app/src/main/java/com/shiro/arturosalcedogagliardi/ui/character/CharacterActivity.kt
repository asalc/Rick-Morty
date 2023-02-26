package com.shiro.arturosalcedogagliardi.ui.character

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shiro.arturosalcedogagliardi.R
import com.shiro.arturosalcedogagliardi.databinding.ActivityCharacterBinding
import com.shiro.arturosalcedogagliardi.helpers.Constants
import com.shiro.arturosalcedogagliardi.helpers.extensions.capitalize
import com.shiro.arturosalcedogagliardi.helpers.extensions.showDoubleDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCharacterBinding
    private lateinit var viewModel: CharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCharacterBinding.inflate(layoutInflater, null, false)
        viewModel = ViewModelProvider(this)[CharacterViewModel::class.java]

        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setContentView(binding.root)

        initObservers()

        viewModel.checkIntent(intent.extras)
    }

    fun closeActivity() {
        setResult(
            RESULT_CANCELED,
            Intent()
        )
        finish()
    }

    private fun initObservers() {
        viewModel.character.observe(this) { character ->
            Glide.with(this)
                .load(character.image)
                .error(ContextCompat.getDrawable(this, R.drawable.logo))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(com.bumptech.glide.R.anim.abc_fade_in))
                .into(binding.imageUser)

            binding.imageGender.setImageResource(
                when (character.gender?.capitalize()) {
                    Constants.MALE -> R.drawable.ic_gender_male
                    Constants.FEMALE -> R.drawable.ic_gender_female
                    else -> R.drawable.ic_gender_undefined
                }
            )

            binding.textStatus.setTextColor(
                ContextCompat.getColor(
                    this,
                    when (character.status?.capitalize()) {
                        Constants.ALIVE -> R.color.green
                        Constants.DEAD -> R.color.red
                        else -> R.color.black
                    }
                )
            )

            character.name?.capitalize()?.let { binding.editTextName.setText(it) }
            character.origin?.capitalize()?.let { binding.editTextOrigin.setText(it) }
            character.location?.capitalize()?.let { binding.editTextLocation.setText(it) }
        }

        viewModel.characterError.observe(this) {
            showDoubleDialog(
                mapOf(
                    Constants.DIALOG_TITLE to getString(R.string.error_title),
                    Constants.DIALOG_DESCRIPTION to getString(it)
                )
            ) {
                finish()
            }
        }

        viewModel.characterUpdateSuccessful.observe(this) {
            if (it)
                showDoubleDialog(
                    mapOf(
                        Constants.DIALOG_TITLE to getString(R.string.success),
                        Constants.DIALOG_DESCRIPTION to getString(R.string.changes_saved_successfully)
                    )
                ) {
                    setResult(
                        RESULT_OK,
                        Intent().putExtra(Constants.CHARACTER, viewModel.character.value)
                    )
                    finish()
                }
        }
    }
}