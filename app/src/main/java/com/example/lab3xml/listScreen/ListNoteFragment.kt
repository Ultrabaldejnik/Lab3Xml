package com.example.lab3xml.listScreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab3xml.MainActivity
import com.example.lab3xml.R
import com.example.lab3xml.adapter.NoteAdapter
import com.example.lab3xml.databinding.FragmentListNoteBinding
import com.example.lab3xml.databinding.FragmentLoginBinding


class ListNoteFragment : Fragment() {

    private var _binding: FragmentListNoteBinding? = null
    private val binding: FragmentListNoteBinding
        get() = _binding!!

    private lateinit var noteListAdapter: NoteAdapter

    private val vm: ListNoteViewModel by viewModels { ListNoteViewModel.factory }

    private var currentContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListNoteBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRV()
        initUI()
    }

    private fun initVM() {
        vm.listNote.observe(this) {
            noteListAdapter.submitList(it)
        }
        vm.toastMessage.observe(this){
            Toast.makeText(currentContext,"Пусто",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initUI() {
        with(binding) {
            btnAdd.setOnClickListener {
                val text = etText.text.toString()
                vm.addUI(text)
            }
            btnExit.setOnClickListener {
                (requireActivity() as MainActivity).exit()
            }
            userName.text = vm.userName
        }
    }

    private fun initRV() {
        noteListAdapter = NoteAdapter()
        with(binding.rvAdapter) {
            adapter = noteListAdapter
            layoutManager = LinearLayoutManager(currentContext, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        currentContext = null
    }

    companion object {
        const val TAG = "list_note_fragment"
    }
}