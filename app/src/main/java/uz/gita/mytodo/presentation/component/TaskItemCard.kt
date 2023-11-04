package uz.gita.mytodo.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uz.gita.mytodo.data.model.TaskData
import uz.gita.mytodo.util.getCalendar
import uz.gita.mytodo.util.getColorFromCalendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItemCard(
    task: TaskData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onCheckboxChange: (Boolean) -> Unit,
    onRemove: () -> Unit,
    onEdit: () -> Unit,
    theme: Boolean = isSystemInDarkTheme()
) {
    var show by remember { mutableStateOf(true) }

    var dismissState = rememberDismissState(
        positionalThreshold = { _ -> 150.dp.toPx() },
        confirmValueChange = {
            when (it) {
                DismissValue.DismissedToStart -> {
                    onEdit()
                    false
                }

                DismissValue.DismissedToEnd -> {
//                    delete = true
                    onRemove()
                    true
                }

                else -> false
            }
        }
    )

    var color by remember {
        mutableStateOf(getColorFromCalendar(getCalendar(task.timer), theme))
    }

//    LaunchedEffect(key1 = dismissState.progress, block = {
//        color = color.copy(alpha = dismissState.progress)
//    })

    AnimatedVisibility(
        show, exit = fadeOut(spring()),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                TaskContent(
                    task = task,
                    modifier = modifier,
                    onClick = onClick,
                    color = color,
                    onCheckboxChange = onCheckboxChange
                )
            },

        )
    }

}

@Composable
fun TaskContent(
    task: TaskData,
    modifier: Modifier,
    color: Color,
    onClick: () -> Unit,
    onCheckboxChange: (Boolean) -> Unit
) {

    // checkbox state
    var status by remember { mutableStateOf(task.isCompleted) }
    var color = if (status) Color(0xFF00887B) else color.copy(alpha = 0.8f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Card(colors = CardDefaults.cardColors(
            containerColor = color,
//            contentColor = Color(ColorUtils.blendARGB(color.toArgb(), Color.Black.toArgb(), 0.8f))
            contentColor = Color.White
        )) {

//            Text(text = if (task.notified) "Notified" else "Not notified", modifier = Modifier.fillMaxWidth().padding(top = 16.dp, end = 8.dp), textAlign = TextAlign.End)

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color)
                    .clickable { onClick() }
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                EisenCheckBox(
                    value = status,
                    onValueChanged = {
                        status = it
                        onCheckboxChange(status)
                    }
                )

                Spacer(modifier = modifier.width(12.dp))
                Column(
                    modifier = modifier
                        .align(Alignment.CenterVertically),
                ) {
                    Text(
                        text = task.title,
                        style = if (status) {
                            TextStyle(
                                textDecoration = TextDecoration.LineThrough,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                color = Color.Gray
                            )
                        } else {
                            TextStyle(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
//                                color = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = modifier.height(12.dp))

                    Text(
                        text = task.body,
                        style = if (status) {
                            TextStyle(
                                textDecoration = TextDecoration.LineThrough,
                                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                color = Color.Gray
                            )

                        } else {
                            TextStyle(
                                fontSize = MaterialTheme.typography.labelMedium.fontSize,
//                                color = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        modifier = Modifier.wrapContentHeight(),
                        fontWeight = FontWeight.Normal
                    )


                    Text(
                        text = task.timer,
                        style = if (status) {
                            TextStyle(
                                textDecoration = TextDecoration.LineThrough,
                                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                color = Color.Gray
                            )
                        } else {
                            TextStyle(
                                fontSize = MaterialTheme.typography.labelSmall.fontSize,
//                                color = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                }

            }
        }

    }
}

@Composable
fun EisenCheckBox(value: Boolean, onValueChanged: (Boolean) -> Unit) {
    Checkbox(
        checked = value,
        onCheckedChange = {
            onValueChanged(it)
        },
        colors = CheckboxDefaults.colors(
            uncheckedColor = MaterialTheme.colorScheme.surface,
            checkedColor = MaterialTheme.colorScheme.surface,
            checkmarkColor = MaterialTheme.colorScheme.tertiary
        )
    )
}
