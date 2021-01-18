package chao.greenlabs.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.BitmapUtils
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.utils.ToastUtils
import chao.greenlabs.viewmodels.factories.AddItemVMFactory
import chao.greenlabs.viewmodels.AddItemViewModel
import kotlinx.android.synthetic.main.fragment_add_item.*


class AddItemFragment : Fragment() {

    private lateinit var viewModel: AddItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel()
        setListeners()
        setDefaultImage()
        registerObservers()
    }

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearUpdatedItem()
    }

    private fun showKeyboard() {
        et_name.requestFocus()
        val imm: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun getViewModel() {
        val factory = AddItemVMFactory(
            Repository(requireContext())
        )
        viewModel = ViewModelProvider(requireActivity(), factory).get(AddItemViewModel::class.java)
    }

    private fun setListeners() {
        iv_image.setOnClickListener {
            showImagePicker()
        }

        bt_confirm.setOnClickListener {
            viewModel.onConfirmClicked(et_name.text.toString(), et_price.text.toString(), iv_image)
        }
    }

    private fun setDefaultImage() {
        val bitmap =
            AppCompatResources.getDrawable(requireContext(), R.mipmap.ic_launcher)?.toBitmap()
        iv_image.setImageBitmap(bitmap)
    }

    private fun registerObservers() {
        viewModel.getMessage().observe(viewLifecycleOwner, Observer { msg ->
            if (msg.isEmpty()) return@Observer
            if (viewModel.getIsUpdateMode()) {
                findNavController().popBackStack()
                viewModel.clearUpdatedItem()
            } else {
                resetData()
            }
            ToastUtils.show(requireContext(), msg)
        })

        viewModel.getUpdatedItem().observe(viewLifecycleOwner, Observer { updatedItem ->
            if (updatedItem == null) return@Observer
            val bitmap = viewModel.getImage(updatedItem.name, updatedItem.price)
            et_name.setText(updatedItem.name)
            et_price.setText(updatedItem.price)
            iv_image.setImageBitmap(bitmap)
        })
    }

    private fun resetData() {
        setDefaultImage()
        et_name.text.clear()
        et_price.text.clear()
        showKeyboard()
    }

    private fun showImagePicker() {
        val cameraAction = {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(viewModel.getTmpPath()))
            openActivityForResult(takePicture)
            hideKeyboard()
        }

        val galleryAction = {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            openActivityForResult(pickPhoto)
            hideKeyboard()
        }

        DialogUtils.showPickImage(requireContext(), cameraAction, galleryAction)
    }

    private fun openActivityForResult(intent: Intent) {
        startForResult.launch(intent)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                BitmapUtils.loadBitmap(result, requireContext(), viewModel, iv_image)
                //..iv_image.setImageBitmap(image)
            }
        }
}