package uz.gita.mytodo.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.hilt.getViewModel
import uz.gita.mytodo.data.model.TaskData
import uz.gita.mytodo.presentation.component.TaskItemCard
import uz.gita.mytodo.util.formatCalendar
import uz.gita.mytodo.util.navigator.MyScreen
import uz.gita.mytodo.worker.cancelWorkRequest
import java.util.Calendar

class HomeScreen : MyScreen() {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<HomeViewModel>()
        val uiState = viewModel.uiState.collectAsState().value

        HomeContent(uiState = uiState, viewModel::eventDispatcher)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeContent(
        uiState: HomeContract.UiState,
        eventDispatcher: (HomeContract.Intent) -> Unit
    ) {
        val context = LocalContext.current
        val listTypeState = arrayListOf("All Tasks", "Completed", "Uncompleted")
        val listBetweenState = arrayListOf("Day", "Week", "Month", "Year")
        listBetweenState.reverse()
        var expanded by remember { mutableStateOf(false) }
        var expanded2 by remember { mutableStateOf(false) }
        var selectedTask by remember { mutableStateOf(TaskData(0, "", "", "")) }
        var selectedText by remember { mutableStateOf(listTypeState[0]) }
        var selectedText2 by remember { mutableStateOf(listBetweenState[0]) }

        when (selectedText) {
            "All Tasks" -> {
                eventDispatcher.invoke(HomeContract.Intent.GetTasksList(0, selectedText2))
            }

            "Completed" -> {
                eventDispatcher.invoke(HomeContract.Intent.GetTasksList(1, selectedText2))
            }

            "Uncompleted" -> {
                eventDispatcher.invoke(HomeContract.Intent.GetTasksList(2, selectedText2))
            }
        }
        Column(modifier = Modifier.background(Color.Black)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color(0xFF00AC6D)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded, onExpandedChange = {
                        expanded = !expanded
                    }, modifier = Modifier
                        .width(200.dp)
                        .padding(start = 32.dp)
                ) {
                    TextField(
                        value = selectedText,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            if (expanded) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onTertiary
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onTertiary
                                )
                            }
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .align(CenterVertically),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onTertiary,
                            unfocusedTextColor = MaterialTheme.colorScheme.onTertiary
                        )
                    )
                    ExposedDropdownMenu(expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        listTypeState.forEach {
                            DropdownMenuItem(text = { Text(text = it)}, onClick = {
                                selectedText = it
                                expanded = false
                                eventDispatcher.invoke(
                                    HomeContract.Intent.GetTasksList(
                                        listTypeState.indexOf(
                                            selectedText
                                        ),
                                        selectedText2
                                    )
                                )
                            })
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expanded2, onExpandedChange = {
                        expanded2 = !expanded2
                    }, modifier = Modifier
                        .width(200.dp)
                        .padding(end = 32.dp)
                ) {
                    TextField(
                        value = selectedText2,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            if (expanded2) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onTertiary
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onTertiary
                                )
                            }
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .align(CenterVertically),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onTertiary,
                            unfocusedTextColor = MaterialTheme.colorScheme.onTertiary
                        )
                    )
                    ExposedDropdownMenu(expanded = expanded2,
                        onDismissRequest = { expanded2 = false }) {
                        listBetweenState.forEach {
                            DropdownMenuItem(text = { Text(text = it) }, onClick = {
                                selectedText2 = it
                                expanded2 = false
                                eventDispatcher.invoke(
                                    HomeContract.Intent.GetTasksList(
                                        listBetweenState.indexOf(
                                            selectedText2
                                        ),
                                        selectedText2
                                    )
                                )
                            })
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 12.dp),
            ) {
                itemsIndexed(
                    items = uiState.list,
                    key = { _, item -> item.hashCode() }
                ) { _, task ->
                    TaskItemCard(
                        task = task,
                        onClick = {
                            selectedTask = task
                        },
                        onCheckboxChange = {
                            task.isCompleted = it
                            eventDispatcher.invoke(HomeContract.Intent.UpdateTask(task))
                        },
                        onRemove = {
                            context.cancelWorkRequest(task)
                            eventDispatcher.invoke(HomeContract.Intent.DeleteTask(task))
                        },
                        onEdit = {
                            eventDispatcher.invoke(HomeContract.Intent.OpenAddTaskScreen(task))
                        }
                    )
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = {
                    val taskData = TaskData(
                        title = "",
                        body = "",
                        timer = formatCalendar(Calendar.getInstance()),
                        id = 0L
                    )
                    eventDispatcher.invoke(HomeContract.Intent.OpenAddTaskScreen(taskData))
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 36.dp, end = 36.dp)
                    .size(72.dp)
                ,
                containerColor = MaterialTheme.colorScheme.tertiary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task",
                    modifier = Modifier.size(42.dp),
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }
}