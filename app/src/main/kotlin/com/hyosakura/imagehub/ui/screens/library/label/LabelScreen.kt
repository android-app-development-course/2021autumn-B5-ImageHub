package com.hyosakura.imagehub.ui.screens.library.label

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.toDateTime
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.viewmodel.TagManageViewModel
import com.hyosakura.imagehub.viewmodel.TagManageViewModelFactory
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.stream.Collectors
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelScreen(
    repository: DataRepository,
    navController: NavHostController,
) {
    val viewModel: TagManageViewModel =
        TagManageViewModelFactory(repository).create(TagManageViewModel::class.java)

    var isEditMode by remember { mutableStateOf(false) }
    var isAddMode by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text("标签列表") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "标签列表"
                        )
                    }
                }
            )
        }
    ) {
        Column(Modifier.fillMaxSize()) {
            viewModel.allTags.observeAsState().value?.let { entityList ->
                val map = entityList.stream().collect(Collectors.groupingBy {
                    it.addTime!!.toDateTime().toLocalDate()
                })
                val iterator = map.iterator()
                while (iterator.hasNext()) {
                    val entry = iterator.next()
                    val date = entry.key
                    val list = entry.value

                    var oldLabelName = ""
                    // 展示tag
                    for (label in list) {

                        LabelItem(
                            label.name!!,
                            label.star,
                            onEditClick = {
                                isEditMode = true
                                oldLabelName = label.name!!
                            },
                            onLabelClick = { navController.navigate("LabelImage/${label.tagId}") },
                            onStarClick = {
                                label.star = if (label.star == 0) 1 else 0
                            },
                            modifier = Modifier.swipeToDismiss {
                                viewModel.deleteTag(label)
                            }
                        )

                        if (isEditMode) {
                            var editText by remember { mutableStateOf(oldLabelName) }
                            AlertDialog(
                                onDismissRequest = { isEditMode = false },
                                title = { Text(text = "编辑标签") },
                                text = {
                                    OutlinedTextField(
                                        value = editText, onValueChange = { editText = it },
                                        singleLine = true,
                                        textStyle = MaterialTheme.typography.titleMedium,
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            textColor = MaterialTheme.colorScheme.primary,
                                            cursorColor = MaterialTheme.colorScheme.inversePrimary,
                                            focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            isEditMode = false
                                            label.name = editText
                                            viewModel.updateTag(label)
                                        }
                                    ) { Text("更改标签") }
                                },
                                dismissButton = {
                                    TextButton(onClick = { isEditMode = false }) { Text("取消") }
                                }
                            )
                        }
                    }
                }
            }

            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val fab = createRef()
                ExtendedFloatingActionButton(
                    onClick = {
                        isAddMode = !isAddMode
                    },
                    icon = { Icon(Icons.Filled.Add, "添加标签") },
                    text = { Text(text = "添加标签") },
                    modifier = Modifier.constrainAs(fab) {

                        bottom.linkTo(parent.bottom, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                    }
                )
            }

            if (isAddMode) {
                var editText by remember { mutableStateOf("") }
                AlertDialog(
                    onDismissRequest = { isAddMode = false },
                    title = { Text(text = "添加标签") },
                    text = {
                        OutlinedTextField(
                            value = editText,
                            onValueChange = { editText = it },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.titleMedium,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.inversePrimary,
                                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    confirmButton = {
                        viewModel.insertTag(TagEntity(name = editText, addTime = System.currentTimeMillis()))
                        TextButton(onClick = { isAddMode = false }) { Text("添加标签") }
                    },
                    dismissButton = {
                        TextButton(onClick = { isAddMode = false }) { Text("取消") }
                    })
            }
        }
    }
}

@Composable
private fun LabelItem(
    label: String,
    initStar: Int,
    onStarClick: () -> Unit,
    onEditClick: () -> Unit,
    onLabelClick: () -> Unit,
    modifier: Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        var isStar by remember { mutableStateOf(initStar != 0) }

        TextButton(
            onClick = onStarClick.also { isStar = !isStar },
            modifier = Modifier.size(60.dp)
        ) {
            if (isStar) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_label_24),
                    contentDescription = null
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_label_24),
                    contentDescription = null
                )
            }
        }

        TextButton(
            onClick = onLabelClick,
            modifier = Modifier
                .height(60.dp)
        ) {
            Row {
                Text(label, style = MaterialTheme.typography.titleMedium)
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth())

        TextButton(
            onClick = onEditClick, modifier = Modifier
                .requiredSize(60.dp)
                .offset((-30).dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = null
            )
        }
    }
}

private fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    val offsetX = remember { Animatable(0f) } // Add this line
    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)
        // Wrap in a coroutine scope to use suspend functions for touch events and animation.
        coroutineScope {
            while (true) {
                // Wait for a touch down event.
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                offsetX.stop()
                // Prepare for drag events and record velocity of a fling.
                val velocityTracker = VelocityTracker()
                // Wait for drag events.
                awaitPointerEventScope {
                    horizontalDrag(pointerId) { change ->
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch {
                            offsetX.snapTo(horizontalDragOffset)
                        }
                        // Record the velocity of the drag.
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        // Consume the gesture event, not passed to external
                        change.consumePositionChange()
                    }
                }                // Dragging finished. Calculate the velocity of the fling.
                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    if (targetOffsetX.absoluteValue <= size.width) {
                        // Not enough velocity; Slide back.
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // Enough velocity to slide away the element to the edge.
                        offsetX.animateDecay(velocity, decay)
                        // The element was swiped away.
                        onDismissed()
                    }
                }
            }
        }
    }.offset {
        IntOffset(offsetX.value.roundToInt(), 0)
    }
}