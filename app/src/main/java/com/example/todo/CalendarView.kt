package com.example.todo

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.util.Calendar
import kotlin.math.abs

class CalendarView : LinearLayout {

    constructor(context: Context) : super(context) {
        init(context)
        fillCalendarWithMonth(selectedYear, selectedMonth)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
        fillCalendarWithMonth(selectedYear, selectedMonth)
    }

    private val currentDay = getCurrentDay()
    private var selectedYear = getCurrentYear()
    private var selectedMonth = getCurrentMonth()
    private lateinit var context: Context
    private lateinit var currentDateView: TextView
    private lateinit var dateGrid: GridLayout
    private lateinit var daysOfMonth: MutableList<Int>
    fun getSelectedMonthAsStr(): String {
        return monthShortNamesList[selectedMonth - 1]
    }
    fun getSelectedYearAsStr(): String {
        return selectedYear.toString()
    }
    fun getSelectedMonthAsInt(): Int {
        return selectedMonth
    }
    fun getSelectedYearAsInt(): Int {
        return selectedYear
    }
    private val monthNamesList: Array<String> = resources.getStringArray(R.array.month_names)
    private val monthShortNamesList: Array<String> = resources.getStringArray(R.array.month_short_names)
    private var monthChangeListener: DataChangeListener? = null
    private val indexOfEndWeekDayNames = 6
    private var indexOfCurrentDay = 0
    private var calendarIsCollapsed = false
    fun setMonthChangeListener(listener: DataChangeListener) {
        monthChangeListener = listener
    }

    fun removeMonthChangeListener() {
        monthChangeListener = null
    }
    fun notifyMonthChanged(month: String, year: String) {
        monthChangeListener?.onDataChanged(month, year)
    }
    private fun init(context: Context) {
        this.context = context
        val rootView = inflate(context, R.layout.calendar_view, null)
        dateGrid = rootView.findViewById(R.id.daysField)
        setupCalendarOnSwipe()
        currentDateView = rootView.findViewById(R.id.currentDate)
        this.addView(rootView)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupCalendarOnSwipe() {
        dateGrid.setOnTouchListener(object : OnTouchListener {
            val gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onFling(
                        e1: MotionEvent,
                        e2: MotionEvent,
                        velocityX: Float,
                        velocityY: Float
                    ): Boolean {
                        val deltaY = e2.y - e1.y
                        val minMoving = 3
                        val minSwipeSpeed = 3

                        // для свайпа вниз и вверх
                        if (abs(deltaY) > minMoving && abs(velocityY) > minSwipeSpeed) {
                            dateGrid.removeViews(7, 42)
                            if (deltaY > 0) {
                                // Свайп вниз
                                if (selectedMonth == 1) {
                                    selectedYear -= 1
                                    selectedMonth = 12
                                } else {
                                    selectedMonth -= 1
                                }
                            } else {
                                // Свайп вверх
                                if (selectedMonth == 12) {
                                    selectedYear += 1
                                    selectedMonth = 1
                                } else {
                                    selectedMonth += 1
                                }
                            }
                            notifyMonthChanged(monthShortNamesList[selectedMonth-1], selectedYear.toString())
                            fillCalendarWithMonth(selectedYear, selectedMonth)
                        }
                        return super.onFling(e1, e2, velocityX, velocityY)
                    }
                })

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(event)
                return true
            }
        })
    }

    private fun setDayStyles(day: TextView, dayNum: Int, params: GridLayout.LayoutParams) {
        day.text = dayNum.toString()
        day.textAlignment = TEXT_ALIGNMENT_CENTER
        day.gravity = Gravity.CENTER
        /*        day.setPadding(40, 30, 40, 30)*/
        day.setPadding(30, 30, 30, 30)
        day.paint.isFakeBoldText = true
        day.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)

        params.setGravity(Gravity.CENTER)
        params.setMargins(10, 0, 10, 0)
    }

    @SuppressLint("SetTextI18n")
    fun fillCalendarWithMonth(year: Int, month: Int) {
        currentDateView.text = "${monthNamesList[month - 1]} $year"
        currentDateView.setTextColor(ContextCompat.getColor(context, R.color.white))

        if (calendarIsCollapsed) {
            dateGrid.removeViews(7, 7)
            calendarIsCollapsed = false
        }

        // Первый диапазон дней из предыдущего месяца, поэтому изначальный цвет чисел серый
        var isGray = true
        // Первые 7 ячеек заняты названиями дней недели
        var indexOfEndDayPrevMonth = indexOfEndWeekDayNames
        var indexOfFirstDayNextMonth = 0
        for (typeOfDays in 1..3) {
            daysOfMonth = computeDaysInCalendar(year, month, typeOfDays)
            if (typeOfDays == 1) {
                indexOfEndDayPrevMonth += daysOfMonth.size
            }
            if (typeOfDays == 2) {
                indexOfFirstDayNextMonth = indexOfEndDayPrevMonth + daysOfMonth.size + 1
            }
            if (isGray) {
                daysOfMonth.forEach { dayNum ->
                    val day = TextView(context)
                    val params = GridLayout.LayoutParams()

                    setDayStyles(day, dayNum, params)
                    day.setTextColor(ContextCompat.getColor(context, R.color.gray))

                    dateGrid.addView(day, params)
                }
                isGray = false
            } else {
                daysOfMonth.forEach { dayNum ->
                    val day = TextView(context)
                    val params = GridLayout.LayoutParams()

                    setDayStyles(day, dayNum, params)
                    day.setTextColor(ContextCompat.getColor(context, R.color.white))

                    dateGrid.addView(day, params)
                }
                isGray = true
            }
        }
        var i = 0
        var day: TextView
        while (i < 49) {
            if (i in (indexOfEndDayPrevMonth + 1) until indexOfFirstDayNextMonth) {
                day = dateGrid.getChildAt(i) as TextView
                day.setTextColor(ContextCompat.getColor(context, R.color.red))
                dateGrid.removeViewAt(i)
                dateGrid.addView(day, i)
            }
            i += 7
        }

        if (selectedMonth == getCurrentMonth()
            && selectedYear == getCurrentYear()
        ) {
            indexOfCurrentDay = indexOfEndDayPrevMonth + currentDay
            // Устанавливаем бэкграунд для сегодняшнего дня
            day = dateGrid.getChildAt(indexOfCurrentDay) as TextView
            day.setBackgroundResource(R.drawable.bg_circle)
            dateGrid.removeViewAt(indexOfCurrentDay)
            dateGrid.addView(day, indexOfCurrentDay)
        }
    }

    fun fillCalendarWithWeek() {
        var indexOfFirstDayOfCurrWeek = 0
        if (selectedMonth == getCurrentMonth() && selectedYear == getCurrentYear()) {
            when(indexOfCurrentDay / 7) {
                (0) -> indexOfFirstDayOfCurrWeek = 0
                (1) -> indexOfFirstDayOfCurrWeek = 7
                (2) -> indexOfFirstDayOfCurrWeek = 2*7
                (3) -> indexOfFirstDayOfCurrWeek = 3*7
                (4) -> indexOfFirstDayOfCurrWeek = 4*7
                (5) -> indexOfFirstDayOfCurrWeek = 5*7
            }
            if (indexOfFirstDayOfCurrWeek != 7) {
                var indexOfDayInShortCalendar = 7
                for (position: Int in indexOfFirstDayOfCurrWeek .. indexOfFirstDayOfCurrWeek+6) {
                    val day = dateGrid.getChildAt(position) as TextView
                    dateGrid.removeViewAt(position)
                    dateGrid.removeViewAt(indexOfDayInShortCalendar)
                    dateGrid.addView(day, indexOfDayInShortCalendar)
                    dateGrid.addView(TextView(context), position)
                    indexOfDayInShortCalendar++
                }
            }
        }
        dateGrid.removeViews(14, 35)
        calendarIsCollapsed = true
    }

    interface DataChangeListener {
        fun onDataChanged(selectedMonth: String, selectedYear: String)
    }

    companion object {
        private fun getCurrentDay(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.DAY_OF_MONTH)
        }

        private fun getCurrentMonth(): Int {
            val calendar = Calendar.getInstance()
            // Месяцы в Calendar начинаются с 0, поэтому добавляем 1
            return calendar.get(Calendar.MONTH) + 1
        }

        private fun getCurrentYear(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.YEAR)
        }
        private fun isLeapYear(year: Int): Boolean =
            year % 4 == 0 && year % 100 != 0 || year % 400 == 0

        private fun computeWeekNum(year: Int, month: Int, date: Int = 1): Int {
            val isMonth1Or2 = month == 1 || month == 2
            val month1 = if (isMonth1Or2) month + 12 else month
            val year1 = if (isMonth1Or2) year - 1 else year
            val weekNum =
                (date + 2 * month1 + 3 * (month1 + 1) / 5 + year1 - year1 / 100 + year1 / 4 + year1 / 400) % 7
            return weekNum + 1
        }

        private fun computeMonthDayCount(year: Int, month: Int): Int {
            when (month) {
                2 -> return if (isLeapYear(year)) 29 else 28
                1, 3, 5, 7, 8, 10, 12 -> return 31
                4, 6, 9, 11 -> return 30
            }
            return -1
        }

        fun computeDaysInCalendar(year: Int, month: Int, typeOfDays: Int): MutableList<Int> {
            val dates: MutableList<Int> = mutableListOf()
            // Вычисляем, сколько дней в текущем месяце
            val monthDayCount =
                computeMonthDayCount(year, month)
            val preMonthDayNum = computeWeekNum(
                year,
                month
            ) // Получаем дату на следующей неделе Затем вычисляем количество дней в последнем месяце, которое будет отображаться в дате.
            val preMonthDayCount = computeMonthDayCount(
                if (month == 1) year - 1 else year,
                if (month == 1) 12 else month - 1
            )
            // Вычисляем количество дней для отображения в следующем месяце
            val nextMonthDayNum =
                42 - preMonthDayNum - monthDayCount

            when (typeOfDays) {
                // Дни предыдущего месяца
                1 -> IntRange(
                    0,
                    preMonthDayNum - 1
                ).forEach { dates.add(preMonthDayCount - (preMonthDayNum - 1 - it)) }
                // Дни текущего месяца
                2 -> IntRange(
                    0,
                    monthDayCount - 1
                ).forEach { dates.add(it + 1) }
                // Дни следующего месяца
                3 -> IntRange(
                    0,
                    nextMonthDayNum - 1
                ).forEach { dates.add(it + 1) }

                else -> dates.add(0)
            }

            return dates
        }
    }
}