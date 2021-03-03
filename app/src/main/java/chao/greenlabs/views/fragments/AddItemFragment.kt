package chao.greenlabs.views.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.*
import chao.greenlabs.viewmodels.AddItemViewModel
import chao.greenlabs.viewmodels.factories.AddItemVMFactory
import chao.greenlabs.views.adpaters.additem.AddCategoryAdapter
import chao.greenlabs.views.customedobjects.IntNumWatcher
import chao.greenlabs.views.customedobjects.OnImageTouchListener
import chao.greenlabs.views.customedobjects.SwipeCallback
import kotlinx.android.synthetic.main.fragment_add_item.*
import kotlinx.android.synthetic.main.fragment_add_item.ll_add
import kotlinx.android.synthetic.main.fragment_add_item.ll_back

class AddItemFragment : BaseFragment() {

    private lateinit var viewModel: AddItemViewModel
    private lateinit var onImageTouchListener: OnImageTouchListener
    private lateinit var adapter: AddCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewId(R.layout.fragment_add_item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel()
        setView()
        setListeners()
        setDefaultImage()
        registerObservers()
        viewModel.loadOptions()
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
        KeyboardUtils.showKeyboard(requireContext(), et_name)
    }

    private fun getViewModel() {
        val factory = AddItemVMFactory(
            Repository(requireContext())
        )
        viewModel = ViewModelProvider(requireActivity(), factory).get(AddItemViewModel::class.java)
    }

    private fun setView() {
        adapter = AddCategoryAdapter(viewModel)

        rv_options.layoutManager = LinearLayoutManager(requireContext())
        rv_options.setHasFixedSize(true)
        rv_options.adapter = adapter

        val itemTouchHelper =
            ItemTouchHelper(SwipeCallback { position ->
                onItemSwipedAction.invoke(position)
            })
        itemTouchHelper.attachToRecyclerView(rv_options)
    }

    private val onItemSwipedAction: ((position: Int) -> Unit) = { position ->
        val confirmAction: (() -> Unit) = {
            viewModel.removeCategory(position)
        }
        val cancelAction: (() -> Unit) = {
            adapter.notifyDataSetChanged()
        }
        DialogUtils.showDelete(requireContext(), confirmAction, cancelAction)
    }

    private fun setListeners() {
        ll_add_photo.setOnClickListener {
            showImagePicker()
        }

        ll_add.setOnClickListener {
            val name = et_name.text.toString()
            val price = et_price.text.toString()
            if (InputChecker.validItem(name, price)) {
                viewModel.onConfirmClicked(name, price, iv_image)
            } else {
                DialogUtils.showWrongFormat(requireContext())
            }
        }

        ll_back.setOnClickListener {
            KeyboardUtils.hideKeyboard(requireContext(), view)
            findNavController().popBackStack()
        }

        et_price.addTextChangedListener(
            IntNumWatcher(
                et_price
            )
        )

        onImageTouchListener = OnImageTouchListener(iv_image)
        iv_image.setOnTouchListener(onImageTouchListener)
    }

    private fun setDefaultImage() {
        if (!viewModel.getIsUpdateMode()) {
            BitmapUtils.loadDefault(requireContext(), iv_image)
        }
    }

    private fun registerObservers() {
        viewModel.getMessage().observe(viewLifecycleOwner, Observer { msg ->
            if (msg.isEmpty()) return@Observer
            if (viewModel.getIsUpdateMode()) {
                KeyboardUtils.hideKeyboard(requireContext(), view)
                findNavController().popBackStack()
                viewModel.clearUpdatedItem()
            } else {
                resetData()
            }
            ToastUtils.show(requireContext(), msg)
        })

        viewModel.getUpdatedItem().observe(viewLifecycleOwner, Observer { updatedItem ->
            if (updatedItem == null) return@Observer
            et_name.setText(updatedItem.name)
            et_price.setText(updatedItem.price)
            val bitmap = updatedItem.getImage()
            BitmapUtils.loadBitmap(requireContext(), bitmap, iv_image)
        })

        viewModel.getOptions().observe(viewLifecycleOwner, Observer { itemOptions ->
            adapter.setList(itemOptions)
        })
    }

    private fun resetData() {
        setDefaultImage()
        et_name.text.clear()
        et_price.text.clear()
        showKeyboard()
        onImageTouchListener.reset()
        iv_image.imageMatrix = Matrix()
    }

    private fun showImagePicker() {
        val cameraAction = {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(viewModel.getTmpPath()))
            openActivityForResult(takePicture)
            KeyboardUtils.hideKeyboard(requireContext(), view)
        }

        val galleryAction = {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            openActivityForResult(pickPhoto)
            KeyboardUtils.hideKeyboard(requireContext(), view)
        }

        DialogUtils.showPickImage(requireContext(), cameraAction, galleryAction)
    }

    private fun openActivityForResult(intent: Intent) {
        startForResult.launch(intent)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                BitmapUtils.getBitmapFromSource(result, requireContext(), viewModel, iv_image)
            }
        }

}