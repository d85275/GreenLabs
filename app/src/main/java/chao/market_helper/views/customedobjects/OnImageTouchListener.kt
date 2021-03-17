package chao.market_helper.views.customedobjects

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

class OnImageTouchListener(private val imageView: ImageView) : View.OnTouchListener {
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


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        imageView.scaleType = ImageView.ScaleType.MATRIX
        when (event!!.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                savedMatrix.set(matrix)
                start[event.x] = event.y
                mode = DRAG
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
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
                    matrix!!.postScale(scale, scale, mid.x, mid.y)
                }
            }
        }
        imageView.imageMatrix = matrix // display the transformation on screen

        return true
    }

    fun reset() {
        matrix = Matrix()
        savedMatrix = Matrix()
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