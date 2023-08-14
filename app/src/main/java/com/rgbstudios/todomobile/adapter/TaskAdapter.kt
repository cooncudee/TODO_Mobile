package com.rgbstudios.todomobile.adapter

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rgbstudios.todomobile.R
import com.rgbstudios.todomobile.databinding.ItemTaskBinding
import com.rgbstudios.todomobile.model.TaskDataFromFirebase
import com.rgbstudios.todomobile.model.TaskViewModel

class TaskAdapter(
    private val tasks: List<TaskDataFromFirebase>,
    private val viewModel: TaskViewModel
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.taskTitleTextView.text = task.title
        holder.binding.taskDescriptionTextView.text = task.description

        holder.binding.apply {
            taskDateTime.visibility = View.VISIBLE // TODO work on picking up the time
            taskTitleTextView.text = task.title
            taskDescriptionTextView.text = task.description

            // Check if the task is completed, and apply the strikethrough effect and change icon if true
            if (task.taskCompleted) {
                taskTitleTextView.paintFlags =
                    taskTitleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                taskTitleTextView.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        com.google.android.material.R.color.material_on_surface_disabled
                    )
                )
                taskDateTime.visibility = View.GONE
                taskDescriptionTextView.visibility = View.GONE
                markCompletedImageView.setImageResource(R.drawable.checkboxd)
                markCompletedImageView.setColorFilter(
                    ContextCompat.getColor(
                        root.context,
                        com.google.android.material.R.color.design_default_color_primary
                    )
                )
                starIconImageView.visibility = View.GONE
            } else {
                taskTitleTextView.paintFlags =
                    taskTitleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                markCompletedImageView.setImageResource(R.drawable.check)
            }

            if (taskDescriptionTextView.text.isEmpty()) {
                taskDescriptionTextView.visibility = View.GONE
            }

            taskDetailsLayout.setOnClickListener {
                // Set the selected task data in the ViewModel
                viewModel.setSelectedTaskData(task)

                // Navigate to the EditTaskFragment using the Navigation Component
                root.findNavController()
                    .navigate(R.id.action_homeFragment_to_editTaskFragment)
            }

            markCompletedImageView.setOnClickListener {
                val newTaskCompleted = !task.taskCompleted

                updateTaskData(task.taskId, task.title, task.description, newTaskCompleted)
            }
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
    private fun updateTaskData(id: String, title: String, description: String, completed: Boolean) {

        // Call the ViewModel's method to update the task
        viewModel.updateTask(id, title, description, completed) { isSuccessful ->
            if (isSuccessful) {
                // Handle success

            } else {
                // Handle failure

            }
        }
    }


}