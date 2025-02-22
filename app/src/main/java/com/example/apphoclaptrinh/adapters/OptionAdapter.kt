package com.example.apphoclaptrinh.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoclaptrinh.R
import com.example.apphoclaptrinh.models.ExerciseOption

class OptionAdapter(
    private val options: List<ExerciseOption>,
    private val onOptionSelected: (ExerciseOption) -> Unit,
    private val correctAnswerId: String // Truyền correct_answer từ bài tập
) : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    private var selectedOption: ExerciseOption? = null
    private var isAnswerChecked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_option, parent, false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option)

        // Nếu đã chọn đáp án thì thay đổi màu nền của câu trả lời
        // Kiểm tra xem đáp án đã được chọn chưa
        if (isAnswerChecked) {
            if (option.id.toString() == correctAnswerId) { // So sánh ID dưới dạng String
                holder.itemView.setBackgroundColor(Color.GREEN) // Đáp án đúng
            } else if (option == selectedOption) {
                holder.itemView.setBackgroundColor(Color.RED) // Đáp án sai
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE) // Các đáp án khác
            }
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE) // Trạng thái ban đầu
        }
    }

    override fun getItemCount(): Int = options.size

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val radioButton: RadioButton = itemView.findViewById(R.id.radio_button)
        private val optionText: TextView = itemView.findViewById(R.id.option_text)

        fun bind(option: ExerciseOption) {
            optionText.text = option.text

            // Lắng nghe sự kiện người dùng chọn đáp án
            radioButton.setOnClickListener {
                selectedOption = option
                isAnswerChecked = true
                onOptionSelected(option)
                notifyDataSetChanged() // Cập nhật lại giao diện
            }
        }
    }
}
