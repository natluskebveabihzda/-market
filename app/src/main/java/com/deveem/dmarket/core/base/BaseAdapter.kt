package com.deveem.dmarket.core.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, VB : ViewBinding>(
  private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : RecyclerView.Adapter<BaseAdapter<T, VB>.ViewHolder>() {
  
  private var dataList: MutableList<T> = mutableListOf()
  
  fun setData(newDataList: List<T>) {
    val diffCallback = BaseDiffCallback(dataList, newDataList)
    val diffResult = DiffUtil.calculateDiff(diffCallback)
    dataList.clear()
    dataList.addAll(newDataList)
    diffResult.dispatchUpdatesTo(this)
  }
  
  fun removeItem(position: Int) {
    dataList.removeAt(position)
    notifyItemRemoved(position)
    notifyItemRangeChanged(position, itemCount)
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }
  
  override fun getItemCount(): Int = dataList.size
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(dataList[position])
  }
  
  abstract fun bindData(model: T, binding: VB, adapterPosition: Int, itemView: View)
  
  inner class ViewHolder(private val binding: VB) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: T) = bindData(data, binding, absoluteAdapterPosition, itemView)
  }
  
  private class BaseDiffCallback<T>(private val oldList: List<T>, private val newList: List<T>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition] == newList[newItemPosition]
    
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition] == newList[newItemPosition]
  }
}
