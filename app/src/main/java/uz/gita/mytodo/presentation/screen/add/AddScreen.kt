package uz.gita.mytodo.presentation.screen.add

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import uz.gita.mytodo.R
import uz.gita.mytodo.data.model.TaskData
import uz.gita.mytodo.util.formatCalendar
import uz.gita.mytodo.util.getCalendar
import uz.gita.mytodo.util.navigator.MyScreen
import uz.gita.mytodo.worker.cancelWorkRequest
import uz.gita.mytodo.worker.createWorkRequest
import java.text.SimpleDateFormat
import java.time.*
import java.util.*
import kotlin.math.abs

class AddScreen(private val taskData: TaskData) : MyScreen() {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        val viewModel = getViewModel<AddViewModel>()
        AddTaskContent(viewModel::eventDispatcher)
    }
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddTaskContent(
        eventDispatcher: (AddContract.Intent) -> Unit
    ) {
        val editable = taskData.id != 0L

        val calendar by remember {
            mutableStateOf(getCalendar(taskData.timer))
        }

        val dateDialogState = rememberMaterialDialogState()
        val timeDialogState = rememberMaterialDialogState()


        val mCalendar = Calendar.getInstance()

        val context = LocalContext.current
        var title by remember { mutableStateOf(taskData.title) }
        var body by remember { mutableStateOf(taskData.body) }


        var timeTextField by remember { mutableStateOf(SimpleDateFormat("HH:mm").format(calendar.time)) }
        var dateTextField by remember { mutableStateOf(SimpleDateFormat("dd MMM yyyy").format(Date(System.currentTimeMillis()))) }

        val calendarState = rememberUseCaseState()
        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(monthSelection = true, yearSelection = true),
            selection = CalendarSelection.Date { newDate ->
                calendar.set(Calendar.DAY_OF_MONTH, newDate.dayOfMonth)
                calendar.set(Calendar.MONTH, newDate.monthValue-1)
                calendar.set(Calendar.YEAR, newDate.year)
                dateTextField = SimpleDateFormat("dd MMM yyyy").format(calendar.time)
            })

        val clockState = rememberUseCaseState()
        ClockDialog(
            state = clockState,
            config = ClockConfig(boundary = LocalTime.of(0, 0)..LocalTime.of(23, 59)),
            selection = ClockSelection.HoursMinutes { hours, minutes ->
                calendar.set(Calendar.HOUR_OF_DAY, hours)
                calendar.set(Calendar.MINUTE, minutes)
                timeTextField = SimpleDateFormat("HH:mm").format(calendar.time)
            })

        Column(modifier = Modifier.background(Color.Black)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color(0xFF1D1B1B))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .width(56.dp)
                        .height(56.dp)
                        .clickable {
                            eventDispatcher.invoke(AddContract.Intent.Back)
                        }
                        .clip(RoundedCornerShape(16.dp))
                        .padding(16.dp)
                    ,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
                Text(text = "Add ToDo Tasks",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 50.dp)
                    ,
                    color = Color(0xFF6BDA98),
                    style = TextStyle(
                        fontSize = 24.sp
                    )
                )

            }

            Image(
                painter = painterResource(id = R.drawable.checking),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(50.dp),
            )
            TextField(
                modifier = Modifier
                    .background(Color.Black) // Set the background color to black
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .border(1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(16.dp)),
                value = title,
                onValueChange = { newtext ->
                    title = newtext
                },
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                maxLines = 2,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Notifications, contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },
                label = {
                    Text(text = "Title:") // Set label text color to black
                }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .border(1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(16.dp))
                    .wrapContentHeight(),
                value = body,
                onValueChange = { newtext ->
                    body = newtext
                },
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit, contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },
                label = {
                    Text(text = "Description:", color = MaterialTheme.colorScheme.tertiary)
                },
                maxLines = 5,
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .border(1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(16.dp))
                    .wrapContentHeight(),
                value = dateTextField,
                onValueChange = {

                },

                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                interactionSource = remember { MutableInteractionSource() }
                    .also {interactionSource->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Press) {
                                    // works like onClick
                                    calendarState.show()
                                }
                            }
                        }
                    },
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary
                ),
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            calendarState.show()
                        },
                        imageVector = Icons.Default.DateRange, contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },
                label = {
                    Text(text = "Date:", color = MaterialTheme.colorScheme.tertiary)
                },
                readOnly = true
            )

            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .border(1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(16.dp))
                    .wrapContentHeight()
                    .clickable {
                        clockState.show()
                    },
                value = timeTextField,
                onValueChange = {

                },
                interactionSource = remember { MutableInteractionSource() }
                    .also {interactionSource->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Press) {
                                    // works like onClick
                                    clockState.show()
                                }
                            }
                        }
                    },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.secondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange, contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },
                label = {
                    Text(text = "Time:", color = MaterialTheme.colorScheme.tertiary)
                },
                readOnly = true
            )


            MaterialDialog(
                dialogState = dateDialogState,
                buttons = {
                    positiveButton(text = " OK") {}
                    negativeButton(text = "Cancel") {}
                }
            ) {
                datepicker(
                    initialDate = LocalDate.now(),
                    title = "Pick a date"
                ) { newDate ->
                    calendar.set(Calendar.DAY_OF_MONTH, newDate.dayOfMonth)
                    calendar.set(Calendar.MONTH, newDate.monthValue)
                    calendar.set(Calendar.YEAR, newDate.year)
                }
            }
            MaterialDialog(
                dialogState = timeDialogState,
                buttons = {
                    positiveButton(text = " OK") {}
                    negativeButton(text = "Cancel") {}
                }
            ) {
                timepicker(
                    initialTime = LocalTime.now(),
                    title = "Pick a time"
                ) { newTime ->
                    calendar.set(Calendar.HOUR_OF_DAY, newTime.hour)
                    calendar.set(Calendar.MINUTE, newTime.minute)

                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val task = TaskData(
                        title = title,
                        body = body,
                        timer = formatCalendar(calendar),
                        id = if (taskData.id != 0L) {
                            taskData.id
                        } else {
                            abs(mCalendar.timeInMillis)
                        }
                    )
                    if (editable) {
                        context.cancelWorkRequest(taskData)
                    }
                    if(!title.isEmpty() && !body.isEmpty()){
                        eventDispatcher.invoke(AddContract.Intent.AddTask(task)).run {
                            context.createWorkRequest(task)

                        }
                    }

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF00AC6D))
            ) {
                Text(text = "Save Task", color = Color.White, fontSize = 25.sp)
            }
        }
    }
}
