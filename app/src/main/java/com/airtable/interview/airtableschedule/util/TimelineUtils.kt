package com.airtable.interview.airtableschedule.util

import com.airtable.interview.airtableschedule.domain.model.Event
import java.util.Calendar
import java.util.Date
import java.util.PriorityQueue
import kotlin.math.max

object TimelineUtils {

    fun assignLanes(events: List<Event>): List<List<Event>> {
        if (events.isEmpty()) return emptyList()

        val sorted = events.sortedWith(
            compareBy<Event> { it.startDate.time }.thenBy { it.endDate.time }
        )

        data class LaneSlot(var lastEnd: Date, val index: Int)

        val heap = PriorityQueue<LaneSlot>(compareBy { it.lastEnd.time })
        val lanes = mutableListOf<MutableList<Event>>()

        sorted.forEach { e ->
            val top = heap.peek()
            if (top != null && top.lastEnd.time < e.startDate.time) {
                heap.poll()
                lanes[top.index].add(e)
                top.lastEnd = e.endDate
                heap.offer(top)
            } else {
                val laneIndex = lanes.size
                lanes.add(mutableListOf(e))
                heap.offer(LaneSlot(e.endDate, laneIndex))
            }
        }
        return lanes
    }

    fun daysInclusive(start: Date, end: Date): Int {
        val s = cal(start); val e = cal(end)
        val diff = (e.timeInMillis - s.timeInMillis) / MILLIS_PER_DAY
        return diff.toInt() + 1
    }

    fun gapDays(from: Date, to: Date): Int {
        val d = daysInclusive(from, to) - 1
        return max(d, 0)
    }


    fun minDate(events: List<Event>): Date = events.minOf { it.startDate }
    fun maxDate(events: List<Event>): Date = events.maxOf { it.endDate }

    private const val MILLIS_PER_DAY = 86_400_000L

    private fun cal(d: Date) = Calendar.getInstance().apply { time = d; zero() }
    private fun Calendar.zero() {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}
