package com.kevingt.firestoresample.feature

import android.animation.Animator
import android.support.constraint.ConstraintLayout
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.daimajia.swipe.SwipeLayout
import com.kevingt.firestoresample.R
import com.kevingt.firestoresample.model.Thing
import com.kevingt.firestoresample.util.setColor

class TodoAdapter(private val listener: Listener?) :
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private val todoList = mutableListOf<Thing>()

    fun updateList(newList: List<Thing>) {
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = todoList.size
            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return todoList[oldItemPosition].id == newList[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return (todoList[oldItemPosition].timestamp == newList[newItemPosition].timestamp) &&
                        (todoList[oldItemPosition].content == newList[newItemPosition].content) &&
                        (todoList[oldItemPosition].done == newList[newItemPosition].done) &&
                        (todoList[oldItemPosition].type == newList[newItemPosition].type)
            }
        }, true).apply {
            todoList.clear()
            todoList.addAll(newList)
            dispatchUpdatesTo(this@TodoAdapter)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_thing, parent, false)
        )

    override fun getItemCount(): Int = todoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            tvContent.text = todoList[position].content
            ivIcon.setImageResource(
                when (todoList[position].type) {
                    Thing.TYPE.NORMAL -> R.drawable.icon_normal
                    Thing.TYPE.FOOD -> R.drawable.icon_food
                    Thing.TYPE.MOVIE -> R.drawable.icon_movie
                    Thing.TYPE.TRAVEL -> R.drawable.icon_travel
                }
            )

            if (todoList[adapterPosition].done) {
                lottieCheckbox.isClickable = false
                vDeleteLine.visibility = View.VISIBLE
                clSurface.setColor(R.color.grey)
                lottieCheckbox.progress = 0.4f
            } else {
                lottieCheckbox.isClickable = true
                vDeleteLine.visibility = View.GONE
                clSurface.setColor(R.color.white)
                lottieCheckbox.progress = 0.2f
                lottieCheckbox.setMinAndMaxProgress(0.2f, 0.4f)
                lottieCheckbox.setOnClickListener {
                    (it as LottieAnimationView).playAnimation()
                }
                lottieCheckbox.addAnimatorListener(object : AnimatorListener() {
                    override fun onAnimationEnd(animation: Animator?) {
                        listener?.onThingDone(todoList[adapterPosition].id)
                    }
                })
            }

            // Bottom View
            slThing.showMode = SwipeLayout.ShowMode.LayDown
            rlDelete.setOnClickListener {
                slThing.close()
                listener?.deleteThing(todoList[adapterPosition].id)
            }
        }
    }

    private abstract class AnimatorListener : Animator.AnimatorListener {
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationStart(animation: Animator?) {}
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val clSurface: ConstraintLayout = v.findViewById(R.id.cl_thing_surface)
        val lottieCheckbox: LottieAnimationView = v.findViewById(R.id.lav_thing_done)
        val vDeleteLine: View = v.findViewById(R.id.v_thing_done_line)
        val tvContent: TextView = v.findViewById(R.id.tv_thing_content)
        val ivIcon: ImageView = v.findViewById(R.id.iv_thing_icon)
        val slThing: SwipeLayout = v.findViewById(R.id.sl_thing)
        val rlDelete: RelativeLayout = v.findViewById(R.id.rl_thing_delete)
    }

    interface Listener {
        fun onThingDone(id: String)
        fun deleteThing(id: String)
    }
}