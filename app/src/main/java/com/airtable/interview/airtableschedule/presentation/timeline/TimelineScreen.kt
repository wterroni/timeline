package com.airtable.interview.airtableschedule.presentation.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airtable.interview.airtableschedule.domain.model.Event
import com.airtable.interview.airtableschedule.util.TimelineUtils
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selected by remember { mutableStateOf<Event?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Timeline", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            SimpleHorizontalTimeline(events = uiState.events, onEventClick = { selected = it })
        }
    }

    if (selected != null) {
        ModalBottomSheet(onDismissRequest = { selected = null }, sheetState = sheetState) {
            EventDetailsContent(event = selected!!)
        }
    }
}

@Composable
private fun SimpleHorizontalTimeline(
    events: List<Event>,
    onEventClick: (Event) -> Unit
) {
    if (events.isEmpty()) return
    val lanes = TimelineUtils.assignLanes(events)
    val all = lanes.flatten()
    val minDate = TimelineUtils.minDate(all)
    val maxDate = TimelineUtils.maxDate(all)
    val totalDays = TimelineUtils.daysInclusive(minDate, maxDate)

    var scale by remember { mutableFloatStateOf(1f) }
    val minScale = 0.5f
    val maxScale = 2.0f
    
    val transformableState = rememberTransformableState { zoomChange, _, _ ->
        scale = (scale * zoomChange).coerceIn(minScale, maxScale)
    }

    val dayWidth: Dp = 20.dp * scale
    val laneHeight: Dp = 56.dp
    val barHeight: Dp = 32.dp
    val totalWidth = dayWidth * totalDays
    val scaleHeight = 36.dp
    val scroll = rememberScrollState()

    Column(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .semantics {
                    contentDescription = "Zoom level is ${(scale * 100).toInt()} percent"
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Zoom: ${(scale * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.width(32.dp)) {
                Spacer(Modifier.height(scaleHeight))
                repeat(lanes.size) { idx ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(laneHeight)
                            .semantics {
                                contentDescription = "Lane ${idx + 1}"
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${idx + 1}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .transformable(state = transformableState)
                    .horizontalScroll(scroll)
                    .semantics {
                        contentDescription = "Timeline with ${events.size} events from ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(minDate)} to ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(maxDate)}. Pinch to zoom, scroll horizontally to navigate."
                    }
            ) {
                TimeScaleAdaptive(minDate, totalDays, dayWidth, scaleHeight, totalWidth)
                lanes.forEach { lane ->
                    LaneRow(
                        lane = lane.sortedBy { it.startDate },
                        minDate = minDate,
                        totalWidth = totalWidth,
                        laneHeight = laneHeight,
                        dayWidth = dayWidth,
                        barHeight = barHeight,
                        onEventClick = onEventClick
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeScaleAdaptive(
    start: Date,
    totalDays: Int,
    dayWidth: Dp,
    height: Dp,
    totalWidth: Dp,
    minLabelWidth: Dp = 48.dp
) {
    val df = SimpleDateFormat("MMM dd", Locale.getDefault())
    val step = max(1, ceil((minLabelWidth / dayWidth).toDouble()).toInt())
    Row(
        modifier = Modifier
            .width(totalWidth)
            .height(height),
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(totalDays) { i ->
            Box(
                modifier = Modifier
                    .width(dayWidth)
                    .height(height),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (i % step == 0) {
                    val d = Date(start.time + i * 86_400_000L)
                    Text(
                        df.format(d),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun LaneRow(
    lane: List<Event>,
    minDate: Date,
    totalWidth: Dp,
    laneHeight: Dp,
    dayWidth: Dp,
    barHeight: Dp,
    onEventClick: (Event) -> Unit
) {
    val outline = MaterialTheme.colorScheme.outlineVariant
    val gap = 6.dp

    Box(
        modifier = Modifier
            .width(totalWidth)
            .height(laneHeight)
            .drawBehind {
                val y = size.height / 2f
                drawLine(
                    color = outline,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        lane.forEachIndexed { idx, e ->
            val offsetDays = TimelineUtils.gapDays(minDate, e.startDate)
            val durationDays = TimelineUtils.daysInclusive(e.startDate, e.endDate)
            val x = dayWidth * offsetDays
            val w = dayWidth * durationDays

            val nextStartX = if (idx < lane.lastIndex)
                dayWidth * TimelineUtils.gapDays(minDate, lane[idx + 1].startDate)
            else
                totalWidth

            val chipMax = (nextStartX - x - gap).coerceAtLeast(w)

            EventBlock(
                event = e,
                width = w,
                height = barHeight,
                chipMaxWidth = chipMax,
                modifier = Modifier
                    .offset(x = x)
                    .align(Alignment.CenterStart),
                onClick = { onEventClick(e) }
            )
        }
    }
}

@Composable
private fun EventBlock(
    event: Event,
    width: Dp,
    height: Dp,
    chipMaxWidth: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val line = MaterialTheme.colorScheme.outlineVariant
    val pill = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)

    val textMeasurer = rememberTextMeasurer()
    val layout = textMeasurer.measure(event.name, style = MaterialTheme.typography.labelMedium)
    val padH = with(LocalDensity.current) { (8.dp * 2).toPx() }
    val chipDesired = with(LocalDensity.current) { (layout.size.width + padH).toDp() }
    val containerWidth = maxOf(width, chipDesired.coerceAtMost(chipMaxWidth))
    
    val dfFull = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val days = TimelineUtils.daysInclusive(event.startDate, event.endDate)
    val durationText = if (days == 1) "1 day" else "$days days"

    Box(
        modifier = modifier
            .width(containerWidth)
            .height(height)
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(3.dp)
                .clip(RoundedCornerShape(1.5.dp))
                .background(line)
        )
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = pill),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier.semantics {
                contentDescription = "${event.name}, from ${dfFull.format(event.startDate)} to ${dfFull.format(event.endDate)}, duration: $durationText. Tap for details."
            }
        ) {
            Box(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun EventDetailsContent(event: Event) {
    val dfFull = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val days = TimelineUtils.daysInclusive(event.startDate, event.endDate)
    Column(
        Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .semantics {
                contentDescription = "Event details for ${event.name}, from ${dfFull.format(event.startDate)} to ${dfFull.format(event.endDate)}, duration: ${if (days == 1) "1 day" else "$days days"}"
            },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(event.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(Modifier.weight(1f)) {
                Text("Start", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(dfFull.format(event.startDate), style = MaterialTheme.typography.bodyMedium)
            }
            Column(Modifier.weight(1f)) {
                Text("End", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(dfFull.format(event.endDate), style = MaterialTheme.typography.bodyMedium)
            }
        }
        Column {
            Text("Duration", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(if (days == 1) "1 day" else "$days days", style = MaterialTheme.typography.bodyMedium)
        }
    }
}