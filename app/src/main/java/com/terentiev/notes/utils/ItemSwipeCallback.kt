package com.terentiev.notes.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.terentiev.notes.R
import com.terentiev.notes.ui.NoteListAdapter

class ItemSwipeCallback(
    private var adapter: NoteListAdapter,
    private var context: Context,
    private var isArchive: Boolean
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    private var icon: Drawable =
            ContextCompat.getDrawable(context, R.drawable.ic_archive_white_36dp)!!

    private var background: ColorDrawable =
        ColorDrawable(ContextCompat.getColor(context, R.color.colorAccentLight))

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition;
        if (!isArchive) {
            adapter.deleteItem(position)
        } else {
            if (direction == ItemTouchHelper.RIGHT) {
                adapter.restoreItem(position)
            } else if (direction == ItemTouchHelper.LEFT) {
                adapter.deleteItem(position)
            }
        }

    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView: View = viewHolder.itemView
        val backgroundCornerOffset = 0 //so background is behind the rounded corners of itemView

        when (isArchive) {
            false -> {
                val iconMargin: Int = icon.intrinsicWidth / 2;
                val iconTop: Int =
                    itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                val iconBottom = iconTop + icon.intrinsicHeight

                when {
                    dX > 0 -> { // Swiping to the right
                        icon = ContextCompat.getDrawable(context, R.drawable.ic_archive_white_36dp)!!
                        background = ColorDrawable(ContextCompat.getColor(context, R.color.colorAccentLight))
                        val iconLeft: Int = itemView.left + iconMargin
                        val iconRight: Int = itemView.left + iconMargin + icon.intrinsicWidth
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        background.setBounds(
                            itemView.left, itemView.top,
                            itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
                        )
                    }
                    dX < 0 -> { // Swiping to the left
                        icon = ContextCompat.getDrawable(context, R.drawable.ic_archive_white_36dp)!!
                        background = ColorDrawable(ContextCompat.getColor(context, R.color.colorAccentLight))
                        val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
                        val iconRight: Int = itemView.right - iconMargin
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        background.setBounds(
                            itemView.right + dX.toInt() - backgroundCornerOffset,
                            itemView.top, itemView.right, itemView.bottom
                        )
                    }
                    else -> { // view is unSwiped
                        background.setBounds(0, 0, 0, 0)
                    }
                }

                background.draw(c)
                icon.draw(c)
            }
            true -> {
                when {
                    dX > 0 -> { // Swiping to the right (unarchive)
                        icon = ContextCompat.getDrawable(context, R.drawable.ic_inarchive_white_36dp)!!
                        background = ColorDrawable(ContextCompat.getColor(context, R.color.colorAccentLight))
                        val iconMargin: Int = icon.intrinsicWidth / 2;
                        val iconTop: Int =
                            itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                        val iconBottom = iconTop + icon.intrinsicHeight
                        val iconLeft: Int = itemView.left + iconMargin
                        val iconRight: Int =
                            itemView.left + iconMargin + icon.intrinsicWidth
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        background.setBounds(
                            itemView.left, itemView.top,
                            itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
                        )
                    }
                    dX < 0 -> { // Swiping to the left (delete)
                        icon = ContextCompat.getDrawable(context, R.drawable.ic_delete_sweep_white_36dp)!!
                        background = ColorDrawable(ContextCompat.getColor(context, R.color.colorDanger))
                        val iconMargin: Int = icon.intrinsicWidth / 2;
                        val iconTop: Int =
                            itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                        val iconBottom = iconTop + icon.intrinsicHeight
                        val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
                        val iconRight: Int = itemView.right - iconMargin
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        background.setBounds(
                            itemView.right + dX.toInt() - backgroundCornerOffset,
                            itemView.top, itemView.right, itemView.bottom
                        )
                    }
                    else -> { // view is unSwiped
                        background.setBounds(0, 0, 0, 0)
                    }
                }
                background.draw(c)
                icon.draw(c)
            }
        }
    }
}