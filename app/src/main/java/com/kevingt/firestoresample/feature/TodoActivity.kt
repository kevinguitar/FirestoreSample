package com.kevingt.firestoresample.feature

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.kevingt.firestoresample.R
import com.kevingt.firestoresample.util.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_todo.*

class TodoActivity : AppCompatActivity(), TodoAdapter.Listener {

    private val adapter = TodoAdapter(this)
    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        viewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)

        rv_todo.layoutManager = LinearLayoutManager(this)
        rv_todo.adapter = adapter
        rv_todo.addItemDecoration(SpaceItemDecoration())

        fab_todo_add.setOnClickListener { viewModel.newThing() }

        viewModel.todoList.observe(this, Observer { list ->
            list?.sortedWith(compareByDescending { it.timestamp })
                ?.sortedBy { it.done }
                ?.apply {
                    adapter.updateList(this)
                }
        })

        viewModel.getTodoList()
    }

    override fun onThingDone(id: String) = viewModel.doneThing(id)

    override fun deleteThing(id: String) = viewModel.deleteThing(id)
}
