package com.example.timer.screens.trainingcomposer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.timer.R
import com.example.timer.internal.TrainingComposerEvent
import com.example.timer.model.Exercise
import com.example.timer.topappbar.TimerTopAppBar
import com.example.timer.ui.theme.SunriseYellow
import kotlinx.coroutines.launch


val availableIcons = listOf(
    R.drawable.add_24px,
    R.drawable.duration_divider,
    R.drawable.pause_circle_24px,
    R.drawable.play_circle_24px,
    R.drawable.remove_24px,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupTrainingParamsScreen(navController: NavController, viewModel: TrainingComposerViewModel) {


    val screenWidth = LocalConfiguration.current.screenWidthDp
    val textFieldScrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val trainingTitle = remember {
        mutableStateOf("")
    }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        availableIcons.size
    }
    val pickedIcon = viewModel.pickedTrainingIcon.collectAsState()
    Scaffold(topBar = {
        TimerTopAppBar(
            navIcon = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
            }
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                Text(text = "Pick icon")
                Surface(
                    shape = RoundedCornerShape(10),
                    shadowElevation = 2.dp
                ) {
                    HorizontalPager(
                        modifier = Modifier
                            .height((screenWidth / 3).dp)
                            .clip(RoundedCornerShape(10)),
                        state = pagerState,
                        pageSize = PageSize.Fixed((screenWidth / 3).dp)
                    ) { page ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(((screenWidth / 3) * 0.2).dp)
                                .clip(RoundedCornerShape(10))
                                .background(
                                    if (pickedIcon.value == page) SunriseYellow
                                    else Color.Transparent
                                )
                                .clickable {
                                    viewModel.pickedTrainingIcon.value = page
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = availableIcons[page]
                                ),
                                contentDescription = "Go back",
                                modifier = Modifier.fillMaxSize(0.8f)
                            )
                        }

                    }
                }
                /*                LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    val availablePages = with(availableIcons.size / 3) {
                                        if ((availableIcons.size % 3) != 0) {
                                            this + 1
                                        } else
                                            this
                                    }
                                    items(availablePages) { pageNumber ->
                                        val realPage = pageNumber + 1
                                        val pageMaxCount = realPage * 3
                                        val pageContent = if (pageNumber != availablePages)
                                            pageMaxCount - 3..pageMaxCount
                                        else {
                                            0 .. pageMaxCount
                                        }
                                        RadioButton(
                                            selected = pageContent.contains(pagerState.currentPage),
                                            onClick = {
                                                scope.launch {
                                                    pagerState.scrollToPage(pageNumber * 3)
                                                }
                                            })
                                    }
                                }*/
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Training name"
                )
                Surface(
                    shape = RoundedCornerShape(
                        10
                    ),
                    shadowElevation = 2.dp
                ) {
                    TextField(
                        modifier = Modifier
                            .width(screenWidth.dp)
                            .horizontalScroll(textFieldScrollState),
                        value = trainingTitle.value,
                        onValueChange = { newText ->
                            trainingTitle.value = newText
                            scope.launch {
                                textFieldScrollState.scrollTo(textFieldScrollState.maxValue)
                            }
                        },
                        placeholder = {
                            Text(text = "Default name")
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        maxLines = 1,
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f),
                shape = RoundedCornerShape(10, 10, 0, 0),
                onClick = {
//                    TODO("Check if name is in database already")
                    viewModel.trainingName = trainingTitle.value
                    viewModel.createdExercise = Exercise(
                        name = "",
                        duration = 0L,
                        isRest = false,
                        parentId = viewModel.parentId
                    )
                    viewModel.sendUiEvent(TrainingComposerEvent.PickExercisesScreen)
                }) {
                Text(text = "Next")
            }
        }
    }
}
