package com.stashinvest.stashchallenge.view.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import com.stashinvest.stashchallenge.view.ui.fragment.MainFragment
import com.stashinvest.stashchallenge.view.ui.fragment.PopUpDialogFragment
import com.stashinvest.stashchallenge.viewmodel.BaseViewModel
import com.stashinvest.stashchallenge.viewmodel.ImageViewModel
import javax.inject.Inject

class ViewModelAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private val viewModels: MutableList<BaseViewModel<*>> = mutableListOf()
    private val viewTypeMap: SparseArrayCompat<BaseViewModel<*>> = SparseArrayCompat()


    fun setViewModels(viewModels: Collection<BaseViewModel<*>>) {
        this.viewModels.clear()
        viewTypeMap.clear()
        addAll(viewModels)
        notifyDataSetChanged()


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return viewTypeMap.get(viewType)!!.createViewHolder(parent)
    }


    // fragment <- ADAPTER -> rview
    //



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        viewModels[position].bindViewHolder(holder)
        Log.d("APP_DEBUG_IMG", viewModels.get(position).viewType.id.toString())

    }

    override fun getItemCount(): Int {
        return viewModels.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewModels[position].viewType.id
    }

    private fun addAll(viewModels: Collection<BaseViewModel<*>>?) {
        viewModels?.map { baseViewModel ->
            this.viewModels.add(baseViewModel)

            //If there are multiple items of the same type the index will just update
            viewTypeMap.put(baseViewModel.viewType.id, baseViewModel)
        }
    }

}




