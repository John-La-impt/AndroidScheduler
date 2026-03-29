package com.trying.scheduler1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trying.scheduler1.ui.theme.MyApplicationTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.jetbrains.kotlinx.dataframe.AnyFrame
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.append
import org.jetbrains.kotlinx.dataframe.api.count
import org.jetbrains.kotlinx.dataframe.io.readJson
import org.jetbrains.kotlinx.dataframe.io.writeJson
import java.io.File
import java.io.IOException
import kotlin.math.exp


public class AddTaskActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
//    @SuppressLint("SimpleDateFormat")
    @Composable
    public fun TaskAdder(context: Context, navController: NavController?, modifier: Modifier) {

        var datePicked by remember { mutableStateOf(LocalDate.now()) }
        var timePicked by remember { mutableStateOf(LocalTime.now()) }

        val formattedDate by remember {
            derivedStateOf {
                DateTimeFormatter
                    .ofPattern("dd/MM/yyyy")
                    .format(datePicked)
            }
        }

        val formattedTime by remember {
            derivedStateOf {
                DateTimeFormatter
                    .ofPattern("HH:mm")
                    .format(timePicked)
            }
        }

        val dateDialogState = rememberMaterialDialogState()
        val timeDialogState = rememberMaterialDialogState()


        var taskNameValue by remember { mutableStateOf("") }

        var deadline by remember { mutableStateOf(false) }

        var repeatCheck by remember { mutableStateOf(false) }


        // getting types
        var typesArrayList = arrayListOf<TaskType>()
        try {
            val file = File(context.filesDir, "types.txt")
            var lines:List<String> = file.readLines()
            for (line in lines) {
                typesArrayList.add(TaskType(line, 0))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var expanded by remember{ mutableStateOf(false) }
        var chosenType by remember { mutableStateOf("No Type") }


        IconButton(
            modifier = Modifier
                .padding(top = 10.dp),
            onClick = { context.startActivity(Intent(context, MainActivity::class.java)) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Localized description"
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Task name field
            OutlinedTextField(
                value = taskNameValue,
                onValueChange = { taskNameValue = it },
                label = { Text("Name") }
            )
            if (taskNameValue.isEmpty()) {
                Text(text = "Task Name: None")
            } else {
                Text(text = "Task Name: $taskNameValue")
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Pick deadline
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Deadline:")
                Spacer(modifier = Modifier.width(10.dp))
                Switch(
                    checked = deadline,
                    onCheckedChange = {
                        deadline = it
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (deadline) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Pick deadline date
                    Button(onClick = {
                        dateDialogState.show()
                    }) {
                        Text(text = "Pick deadline date")
                    }
                    Text(text = formattedDate)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pick deadline time
                    Button(onClick = {
                        timeDialogState.show()
                    }) {
                        Text(text = "Pick deadline time")
                    }
                    Text(text = formattedTime)

                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                Box() {}
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Repeat:")
                Spacer(modifier = Modifier.width(10.dp))
                Switch(
                    checked = repeatCheck,
                    onCheckedChange = {
                        repeatCheck = it
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox (
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = chosenType,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
//                    DropdownMenuItem(
//                        text = { Text("Option 1") },
//                        onClick = { /* Do something... */ }
//                    )
                    DropdownMenuItem(
                        text = { Text("No Type") },
                        onClick = { chosenType = "No Type" }
                    )
                    for (type in typesArrayList) {
                        DropdownMenuItem(
                            text = { Text(type.typeName) },
                            onClick = { chosenType = type.typeName }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                CreateTask(taskNameValue, deadline, formattedDate, formattedTime, repeatCheck, chosenType, context)
                context.startActivity(Intent(context, MainActivity::class.java))
            }) {
                Text(text = "Create Task")
            }

        }

        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = "Ok")
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date",
            ) {
                datePicked = it
            }
        }

        MaterialDialog(
            dialogState = timeDialogState,
            buttons = {
                positiveButton(text = "Ok")
                negativeButton(text = "Cancel")
            }
        ) {
            timepicker(
                initialTime = LocalTime.now(),
                title = "Pick a time",
            ) {
                timePicked = it
            }
        }
    }

    fun CreateTask(
        taskName: String,
        deadline: Boolean,
        dlDate: String,
        dlTime: String,
        repeating: Boolean,
        taskType: String,
        context: Context
    ): Boolean {
        try {

            // read the file and turn it into dataframe (anyframe)
            var file = DataFrame.readJson(context.filesDir.toString() + "/tasks.json")

            // get the count of file
            var fileCount = file.count()

            // create the object
//            val toAppend = Task(taskName, deadline, dlDate, dlTime, repeating)
//            println("new task: " + toAppend)
            // append the object to a new row in the dataframe
            var type = ""
            if (taskType != "No Type") {
                type = taskType
            }
            file = file.append(fileCount, taskName, deadline, dlDate, dlTime, repeating, type, false, false)

            // printing the dataframe
            for (row in file) {
                println(row)
            }

            // write to file
            file.writeJson(context.filesDir.toString() + "/tasks.json")
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            println("Create task failed")
            return false
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview2() {
        MyApplicationTheme {
//        Greeting2("Android")
            TaskAdder(LocalContext.current, null, Modifier)
        }
    }
}