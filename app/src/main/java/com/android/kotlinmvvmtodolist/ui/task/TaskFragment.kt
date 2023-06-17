@file:Suppress("DEPRECATION")

package com.android.kotlinmvvmtodolist.ui.task

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kotlinmvvmtodolist.R
import com.android.kotlinmvvmtodolist.data.SettingDataStore
import com.android.kotlinmvvmtodolist.data.dataStore
import com.android.kotlinmvvmtodolist.databinding.FragmentTaskBinding
import com.android.kotlinmvvmtodolist.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var mAdapter: TaskAdapter
    private val layoutDataStore: SettingDataStore by lazy {
        SettingDataStore(requireContext().dataStore)
    }
    private var isLinearLayout = true


    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        mAdapter = TaskAdapter(TaskClickListener { taskEntry ->
            findNavController().navigate(TaskFragmentDirections.actionTaskFragmentToUpdateFragment(taskEntry))
        })

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getAllTasks.collect{ tasks ->
                    mAdapter.submitList(tasks)
                }
            }
        }

        binding.apply {
            recyclerView.adapter = mAdapter
            floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.action_taskFragment_to_addFragment)
            }
        }


        ItemTouchHelper(object  : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskEntry = mAdapter.currentList[position]
                viewModel.delete(taskEntry)

                Snackbar.make(binding.root, "Deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.insert(taskEntry)
                    }
                    show()
                }
            }
        }).attachToRecyclerView(binding.recyclerView)

        with(binding.recyclerView){
            addItemDecoration(DividerItemDecoration(context,
            RecyclerView.VERTICAL))
            setHasFixedSize(true)

        }

        setHasOptionsMenu(true)

        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        currentFocusedView.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_menu, menu)
        val menuItem = menu.findItem(R.id.action_switch_layout)
        setIcon(menuItem)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               if(newText != null){
                   runQuery(newText)
               }
                return true
            }
        })
    }

    fun runQuery(query: String){
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner) { tasks ->
            mAdapter.submitList(tasks)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_switch_layout) {
            lifecycleScope.launch {
                layoutDataStore.saveLayout(!isLinearLayout, requireContext())
            }
            return true
        }
        when(item.itemId){
            R.id.action_priority -> {
                lifecycleScope.launch{
                    repeatOnLifecycle(Lifecycle.State.STARTED){
                        viewModel.getAllPriorityTasks.collectLatest { tasks ->
                            mAdapter.submitList(tasks)
                        }
                    }
                }
            }
            R.id.action_delete_all -> deleteAllItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllItem() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete All")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes"){dialog, _ ->
                viewModel.deleteAll()
                dialog.dismiss()
            }.setNegativeButton("No"){dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner){
            isLinearLayout = it
            setLayout()
            activity?.invalidateOptionsMenu()
        }
        viewModel.scheduleUpdater(requireActivity().application)
    }

    private fun setLayout(){
        binding.recyclerView.layoutManager = if (isLinearLayout)
            LinearLayoutManager(context)
        else
            GridLayoutManager(context, 2)
    }
    private fun setIcon(menuItem: MenuItem) {
        val iconId = if (isLinearLayout)
            R.drawable.ic_grid_view
        else
            R.drawable.ic_view_list
        menuItem.icon = ContextCompat.getDrawable(requireContext(), iconId)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                MainActivity.PERMISSION_REQUEST_CODE
            )
        }
    }
}