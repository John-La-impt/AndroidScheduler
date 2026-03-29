package com.trying.scheduler1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.trying.scheduler1.ui.theme.MyApplicationTheme
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.readJson
import org.jetbrains.kotlinx.dataframe.io.writeJson
import java.io.File
import java.io.IOException
import java.time.format.DateTimeFormatter


public class StartActivity : ComponentActivity() {

//    var taskArrayListBefore = arrayListOf<Task>()
//    ReadTask()
    var initialRead = true
    @SuppressLint("MutableCollectionMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    public fun Starter(name: String, navController: NavController?, context: Context?, modifier: Modifier) {
        // file for types
//    val file = File(context.filesDir,"types.txt")
//    try {
//        if (!file.exists()) {
//            if (file.createNewFile()) {
//                println("created types file")
//            } else {
//                println("create types file failed")
//            }
//        } else { // task file exists so read through it and add to typesArrayList
////        //
//        }
//    } catch (e: IOException) {
//
//    }


        var typeArrayList = ArrayList<TaskType>()
        // task types should be added to a txt file
        typeArrayList.add(TaskType("Not Done", 7))
        typeArrayList.add(TaskType("Overdue", 8))
        typeArrayList.add(TaskType("Deadline", 2)) // always add this 1 2nd
        typeArrayList.add(TaskType("No Deadline", 3)) // always add this 1 3rd
        typeArrayList.add(TaskType("Repeating", 4)) // always add this 1 4th
        typeArrayList.add(TaskType("No Repeating", 5)) // always add this 1 5th
        typeArrayList.add(TaskType("Done", 6))
        typeArrayList.add(TaskType("All tasks", 1)) // always add this 1 first
        // read from file
//    typeArrayList.add("type 1")
//    typeArrayList.add("type 2")
        // read from file
//        typeArrayList.add(TaskType("Add new type", 0))
        ReadType(context, typeArrayList)
        var currentTypeText by remember { mutableStateOf(typeArrayList[0]) }

        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        var expanded by remember { mutableStateOf(false) }
        var expanded2 by remember { mutableStateOf(false) }
        var expanded3 by remember { mutableStateOf(false) }

        // find the tasks - create the lists
//        val taskArrayList = ArrayList<Task>()
        var taskArrayList = remember { mutableStateListOf<Task>() }
//    for (i in 0 until 10) {
//        taskArrayList.add(SampleTask("Wake", true, "15-01-2026"))
//        taskArrayList.add(Task("Wake", "15-01-2026", "15:30", false))
//    }
        if (context != null && initialRead) {
//            ReadTask(context, taskArrayList)
            CheckOverdue(context, taskArrayList)
            initialRead = false
            FilterTask(context, typeArrayList[0].typeTag, typeArrayList[0].typeName, taskArrayList)
//            val iterator = taskArrayList.iterator()
//            while (iterator.hasNext()) {
//                val element = iterator.next()
//                if (element.done) {
//                    iterator.remove()
//                }
//            }
        }


        // create a task array List copy that will be used to show the tasks
//        var taskArrayListCopy = taskArrayList
//        var taskArrayListCopy = remember{ mutableStateListOf<Task>() }

        println("ran greeting!!!")

        var newTypeText by remember { mutableStateOf("") }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Box(
//                        modifier = Modifier
//                            .
                        ) {
                            Text(
                                text = currentTypeText.typeName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.clickable {
                                    expanded2 = !expanded2
                                }
                            )
                            DropdownMenu(
                                expanded = expanded2,
                                onDismissRequest = { expanded2 = false },
                                modifier = Modifier
                                    .size(width = 200.dp, height = 200.dp)
                            ) {
                                // the below needs to be a list
                                for (type in typeArrayList) {
                                    DropdownMenuItem(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        text = { Text(type.typeName) },
                                        onClick = {
                                            currentTypeText = type
//                                            println("click name: " + type.typeName)
//                                            println("click tag: " + type.typeTag)

                                            FilterTask(context, type.typeTag, type.typeName, taskArrayList)


                                            // check the tag - e.g. if typeTag == 0 -> go to create type activity
                                        }
                                    )
                                }
                                DropdownMenuItem(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    text = { Text("Add new type") },
                                    onClick = { // create a popup menu
                                        expanded3 = !expanded3
                                    }
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    DropdownMenu(
                                        expanded = expanded3,
                                        onDismissRequest = { expanded3 = false }
                                    ) {
                                        OutlinedTextField(
                                            value = newTypeText,
                                            onValueChange = { newTypeText = it },
                                            label = { Text("Type name") }
                                        )
                                        Button(onClick = {
//                                            CreateTask(taskNameValue, deadline, formattedDate, formattedTime, repeatCheck, chosenType, context)
//                                            context.startActivity(Intent(context, MainActivity::class.java))
                                            AddType(context, newTypeText)
                                            newTypeText = ""
                                            expanded3 = false
                                            ReadType(context, typeArrayList)
                                        }) {
                                            Text(text = "Create Task Type")
                                        }
                                    }
                                }
//                                typesArrayList.add(TaskType("Add new type", -1))
                            }
                        }

                    },

//                    navigationIcon = {
//                        IconButton(onClick = { /* do something */ }) {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                                contentDescription = "Localized description"
//                            )
//                        }
//                    },
                    actions = {
//                    IconButton(onClick = { onClick() }) {
//                        Icon(
//                            imageVector = Icons.Filled.Menu,
//                            contentDescription = "Localized description"
//                        )
//                    }
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Add new task") },
                                onClick = {
//                                    context.startActivity(
//                                        Intent(
//                                            context,
//                                            AddTaskActivity::class.java
//                                        )
//                                    )
                                    navController?.navigate(Screen.addTaskScreen.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Option 2") },
                                onClick = { /* Do something... */ }
                            )
                        }
                    },

                    scrollBehavior = scrollBehavior,
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp),
//                .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
//                var index = 0
                items(taskArrayList) { task ->
                    SampleTask(currentTypeText, taskArrayList, task, task.name, task.deadline, task.dlDate, task.dlTime, task.repeat, task.overdue, context)
//                    index++
                }
            }
        }
    }

    @Composable
    fun SampleTask(
        currentType: TaskType,
        taskArrayList: SnapshotStateList<Task>,
        task: Task,
        title: String,
        deadline: Boolean,
        dlDate: String,
        dlTime: String,
        repeating: Boolean,
        overdue: Boolean,
        context: Context?,
        modifier: Modifier = Modifier
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxSize(),
//        verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
//                .background(Color.LightGray)
//                .fillMaxSize()
                    .padding(10.dp)
                    .size(width = 250.dp, height = 100.dp),
//                .size(100.dp),
//            verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp
                )
                if (deadline) {
                    Text(
                        text = "Deadline date: $dlDate",
//                fontSize = 20.sp
                    )
                    Text(
                        text = "Deadline time: $dlTime",
//                fontSize = 20.sp
                    )
                    if (overdue) {
                        Text(
                            text = "OVERDUE!"
                        )
                    }
                } else {
                    Text(
                        text = "No deadline",
//                fontSize = 20.sp
                    )
                }
                Text(
                    text = "Repeating: $repeating"
//                modifier = Modifier
//                    .size()
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 20.dp)
            ) {
                IconButton(
                    onClick = { expanded = !expanded },
//                modifier = Modifier
//                    .align(Alignment.TopEnd)
//                    .padding(end = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }

                ) {
                    DropdownMenuItem(
                        text = {
                            if (!task.done) {
                                Text("Mark Done")
                            } else {
                                Text("Mark Undone")
                            }
                         },
                        onClick = {
                            taskArrayList.clear()
                            ReadTask(context, taskArrayList)
                            println("changing: " + task.name)

                            taskArrayList.toDataFrame().print()
                            println("_----_")
                            // get the task in the taskArrayList
                            for (taskIndex in taskArrayList) {
                                if (task.index == taskIndex.index) {
                                    taskIndex.done = !taskIndex.done
                                    println("changed: " + taskIndex.name + " " + taskIndex.done.toString())
                                    break
                                }
                            }

                            var toWrite = taskArrayList.toDataFrame()
                            toWrite.print()
                            toWrite.writeJson(context?.filesDir.toString() + "/tasks.json")
                            FilterTask(context, currentType.typeTag, currentType.typeName, taskArrayList)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Remove") },
                        onClick = { /* Do something... */ }
                    )
                }
            }
        }
    }


    fun ReadTask(context: Context?, taskArrayList: SnapshotStateList<Task>): Boolean {
        try {
            val file = DataFrame.readJson(context?.filesDir.toString() + "/tasks.json")

            // each column is an attribute, below are the column of each task
            var taskIndex: Int = -1
            var taskName: String = ""
            var deadline: Boolean = false
            var dlDate: String = ""
            var dlTime: String = ""
            var repeat: Boolean = false
            var taskType: String = ""
            var done: Boolean = false
            var overdue: Boolean = false

            var preTaskName = ""
            var preDlDate = ""
            var preDlTime = ""

            for (row in file) {
                taskIndex = row["index"] as Int
                taskName = row["name"] as String
                deadline = row["deadline"] as Boolean
                dlDate = row["dlDate"] as String
                dlTime = row["dlTime"] as String
                repeat = row["repeat"] as Boolean
                taskType = row["type"] as String
                done = row["done"] as Boolean
                overdue = row["overdue"] as Boolean
                taskArrayList.add(Task(taskIndex, taskName, deadline, dlDate, dlTime, repeat, taskType, done, overdue))
            }

            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    public fun ReadType(context: Context?, typesArrayList: ArrayList<TaskType>): Boolean {
        try {
            val file = File(context?.filesDir, "types.txt")
            var lines:List<String> = file.readLines()
//            var lineProgress = 4

            for (line in lines) {
                typesArrayList.add(TaskType(line, 0))
//                lineProgress++
            }


            for (type in typesArrayList) {
                println("typeL " + type.typeName + " " + type.typeTag)
            }

            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    public fun AddType(context: Context?, typeName: String) : Boolean {
        try {
            var addType = '\n' + typeName
            val file = File(context?.filesDir, "types.txt")
            file.appendText(addType)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    fun FilterTask(context: Context?, typeTag: Int, typeString: String, taskArrayList: SnapshotStateList<Task>) {
        // call to refresh the page
        // also the taskArrayList needs to be modified here?
        taskArrayList.clear()
        ReadTask(context, taskArrayList)
        val iterator = taskArrayList.iterator()

        if (typeTag == 1) { // all tasks
//            while (iterator.hasNext()) {
//                val element = iterator.next()
//                if (element.done) {
//                    iterator.remove()
//                }
//            }
        } else if (typeTag == 2) { // have deadline
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (!element.deadline || element.done) {
                    iterator.remove()
                }
            }

        } else if (typeTag == 3) { // no deadline
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (element.deadline || element.done) {
                    iterator.remove()
                }
            }

        } else if (typeTag == 4) { // repeating
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (!element.repeat || element.done) {
                    iterator.remove()
                }
            }
        } else if (typeTag == 5) { // no repeating
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (element.repeat || element.done) {
                    iterator.remove()
                }
            }
        } else if (typeTag == 6) { // done
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (!element.done) {
                    iterator.remove()
                }
            }
        } else if (typeTag == 7) { // not done
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (element.done) {
                    iterator.remove()
                }
            }
        } else if (typeTag == 8) {
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (!element.overdue) {
                    iterator.remove()
                }
            }
        } else { // typeTag == 0
            // compare the typeString
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (element.type != typeString || element.done) {
                    iterator.remove()
                }
            }
        }
    }

    fun CheckOverdue(context: Context?, taskArrayList: SnapshotStateList<Task>) {
        taskArrayList.clear()
        ReadTask(context, taskArrayList)
        var changed = false
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        for (task in taskArrayList) {
            if (task.deadline && !task.overdue) { // if the task have deadline and is not overdue
                // check the current time vs the task.dlDate
                var dateGot = java.time.LocalDate.parse(task.dlDate, dateFormatter)
                var currentDate = java.time.LocalDate.now()

                var compare = currentDate.compareTo(dateGot)
                if (compare > 0) { // current date is later than date got from task
                    task.overdue = true
                    changed = true
                }
            }
        }

        // write to the file
        if (changed) {
            var toWrite = taskArrayList.toDataFrame()
            toWrite.print()
            toWrite.writeJson(context?.filesDir.toString() + "/tasks.json")
        }
    }

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
//        val context = LocalContext.current

        Starter("Android", null,null, modifier = Modifier)
    }
}

}