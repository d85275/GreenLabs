package chao.greenlabs.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import chao.greenlabs.utils.*
import chao.greenlabs.viewmodels.AddItemViewModel
import chao.greenlabs.viewmodels.factories.AddItemVMFactory
import kotlinx.android.synthetic.main.fragment_add_item.*
import kotlinx.android.synthetic.main.fragment_add_item.ll_back
import kotlinx.android.synthetic.main.fragment_add_item.tv_title


private const val MIN_ZOOM: Float = 1f
private const val MAX_ZOOM: Float = 1f

class AddItemFragment : Fragment(), View.OnTouchListener {
    // image scale
    // These matrices will be used to scale points of the image
    private var matrix: Matrix? = Matrix()
    private var savedMatrix: Matrix = Matrix()

    // The 3 states (events) which the user is trying to perform
    private val NONE = 0
    private val DRAG = 1
    private val ZOOM = 2
    private var mode = NONE

    // these PointF objects are used to record the point(s) the user is touching
    private var start = PointF()
    private var mid = PointF()
    private var oldDist = 1f
    // image scale

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
        //initView()
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

    private fun setListeners() {
        ll_add_photo.setOnClickListener {
            showImagePicker()
        }

        bt_confirm.setOnClickListener {
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

        et_price.addTextChangedListener(textWatcher)

        //iv_image.setOnTouchListener(this)
    }

    private fun setDefaultImage() {
        val bitmap =
            AppCompatResources.getDrawable(requireContext(), R.mipmap.ic_launcher)?.toBitmap()
        iv_image.setImageBitmap(bitmap)
        //setImageCenter(bitmap)
    }

    // todo: still needs to find out how to set the image to the centre
    private fun setImageCenter(bitmap: Bitmap?) {
        if (bitmap == null) return

        val bHeight = bitmap.height
        val bWidth = bitmap.width

        Log.e("123", "bHeight: $bHeight")
        Log.e("123", "bWidth: $bWidth")

        iv_image.measure(
            View.MeasureSpec.makeMeasureSpec(cl_parent.width, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(cl_parent.height, View.MeasureSpec.UNSPECIFIED)
        )

        val xOffset = bWidth / 2
        val yOffset = bHeight / 2

        Log.e("123", "image height: ${iv_image.measuredWidth}")
        Log.e("123", "image width: ${iv_image.measuredHeight}")

        val matrix: Matrix = iv_image.imageMatrix
        matrix.postTranslate(xOffset.toFloat(), yOffset.toFloat())
        iv_image.imageMatrix = matrix
        iv_image.invalidate()
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
            val bitmap = viewModel.getImage(updatedItem.name, updatedItem.price)
            et_name.setText(updatedItem.name)
            et_price.setText(updatedItem.price)
            iv_image.setImageBitmap(bitmap)
        })
    }

    private fun initView() {
        if (!viewModel.getIsUpdateMode()) return
        ll_delete.visibility = View.VISIBLE
        ll_delete.setOnClickListener {
            DialogUtils.showDelete(requireContext()) { viewModel.deleteUpdatedData() }
        }
        tv_title.text = getString(R.string.edit_item)
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
                BitmapUtils.loadBitmap(result, requireContext(), viewModel, iv_image)
            }
        }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            var text = s.toString()
            if (text.contains(".")) {
                text = text.replace(".", "")
                et_price.setText(text)
                et_price.setSelection(et_price.text.length)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        iv_image.scaleType = ImageView.ScaleType.MATRIX
        when (event!!.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                Log.e("123", "ACTION_DOWN")
                savedMatrix.set(matrix)
                start[event.x] = event.y
                mode = DRAG
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                Log.e("123", "ACTION_POINTER_UP")
                mode = NONE
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                Log.e("123", "ACTION_POINTER_DOWN")
                oldDist = spacing(event)
                if (oldDist > 5f) {
                    savedMatrix.set(matrix)
                    midPoint(mid, event)
                    mode = ZOOM
                }
            }
            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                matrix!!.set(savedMatrix)
                matrix!!.postTranslate(
                    event.x - start.x,
                    event.y - start.y
                ) // create the transformation in the matrix  of points
            } else if (mode == ZOOM) {
                // pinch zooming
                val newDist = spacing(event)
                if (newDist > 5f) {
                    matrix!!.set(savedMatrix)
                    val scale = newDist / oldDist // setting the scaling of the
                    // matrix...if scale > 1 means
                    // zoom in...if scale < 1 means
                    // zoom out
                    Log.e("123", "scale: $scale")
                    matrix!!.postScale(scale, scale, mid.x, mid.y)
                }
            }
        }
        Log.e("123", "mode: $mode")
        iv_image.imageMatrix = matrix // display the transformation on screen

        return true // indicate event was handled
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return kotlin.math.sqrt(x * x + y * y)
    }


    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[x / 2] = y / 2
    }
}