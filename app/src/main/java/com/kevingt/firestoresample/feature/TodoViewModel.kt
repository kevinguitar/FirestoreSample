package com.kevingt.firestoresample.feature

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.kevingt.firestoresample.model.Thing
import com.kevingt.firestoresample.util.default
import com.kevingt.firestoresample.util.toThing

class TodoViewModel : ViewModel() {
    companion object {
        private const val TABLE_THINGS = "Things"
        private const val FIELD_DONE = "done"
        private const val FIELD_TIMESTAMP = "timestamp"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val compositeListener = mutableListOf<ListenerRegistration>()
    private var fakeIndex = 0
    private val fakeList = arrayListOf(
        Thing(content = "聲林之王 星期五21:00首播", type = Thing.TYPE.NORMAL),
        Thing(content = "打去兵役課催兵單QQ", type = Thing.TYPE.NORMAL),
        Thing(content = "宜蘭花蓮輕旅行", type = Thing.TYPE.TRAVEL),
        Thing(content = "香港自由行", type = Thing.TYPE.TRAVEL),
        Thing(content = "肉多多火鍋", type = Thing.TYPE.FOOD),
        Thing(content = "響食天堂下午茶", type = Thing.TYPE.FOOD),
        Thing(content = "一個巨星的誕生", type = Thing.TYPE.MOVIE),
        Thing(content = "比悲傷更悲傷的故事", type = Thing.TYPE.MOVIE)
    ).shuffled()

    var todoList = MutableLiveData<MutableList<Thing>>().default(mutableListOf())

    fun newThing() {
        firestore
            .collection(TABLE_THINGS)
            .add(fakeList[fakeIndex].also {
                it.timestamp = System.currentTimeMillis()
            })
        fakeIndex = (fakeIndex + 1).rem(fakeList.size)
    }

    fun doneThing(id: String) {
        firestore
            .collection(TABLE_THINGS)
            .document(id)
            .update(
                FIELD_DONE, true,
                FIELD_TIMESTAMP, System.currentTimeMillis()
            )
    }

    fun deleteThing(id: String) {
        firestore
            .collection(TABLE_THINGS)
            .document(id)
            .delete()
    }

    fun getTodoList() {
        firestore
            .collection(TABLE_THINGS)
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.documentChanges?.forEach { doc ->
                    when (doc.type) {
                        DocumentChange.Type.ADDED -> todoList.value?.add(doc.toThing())
                        DocumentChange.Type.REMOVED -> todoList.value?.remove(doc.toThing())
                        DocumentChange.Type.MODIFIED -> {
                            doc.toThing().apply {
                                val modifiedItem = todoList.value?.find { it.id == this.id }
                                if (modifiedItem != null) {
                                    todoList.value?.remove(modifiedItem)
                                    todoList.value?.add(this)
                                }
                            }
                        }
                    }
                    todoList.postValue(todoList.value)
                }
            }.also { compositeListener.add(it) }
    }

    override fun onCleared() {
        compositeListener.forEach { it.remove() }
        super.onCleared()
    }

}