package id.bhinneka.sholatkuy.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.bhinneka.sholatkuy.R
import kotlinx.android.synthetic.main.item_prayer_schedule.view.*
import java.text.SimpleDateFormat
import java.util.*

class AdapterPrayerSchedule(val context: Context, val listPrayTime: List<String>, val listPrayName:
List<String>) : RecyclerView.Adapter<AdapterPrayerSchedule.ViewHolderItemPrayerSchedule>(){

    private val TAG = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  AdapterPrayerSchedule
    .ViewHolderItemPrayerSchedule =
        ViewHolderItemPrayerSchedule(
                LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_prayer_schedule, null)
        )
    override fun onBindViewHolder(holder: ViewHolderItemPrayerSchedule, position: Int) {
        holder?.itemView?.let {
            val prayerName = listPrayName[position]
            val prayerTime = listPrayTime[position]
            val timeNow = SimpleDateFormat("HH:mm", Locale.US).format(Date())

            val timeHourNow = timeNow.split(":")[0].toInt()
            val timeMinuteNow = timeNow.split(":")[1].toInt()
            val timeHourPrayer = prayerTime.split(":")[0].toInt()
            val timeMinutePrayer = prayerTime.split(":")[1].toInt()
            var estimatedHour: Int
            estimatedHour = if (timeHourNow < timeHourPrayer){
                timeHourPrayer - timeHourNow
            } else {
                0
            }
            val estimatedMinute: Int
            estimatedMinute = if (timeMinuteNow < timeMinutePrayer && timeHourNow < timeHourPrayer){
                timeMinutePrayer - timeMinuteNow
            } else {
                if (estimatedHour == 0) {
                    0
                } else {
                    estimatedHour -= 1
                    60 - timeMinuteNow + timeMinutePrayer
                }
            }

            it.text_view_estimated_time_item_prayer_schedule.let{
                if (estimatedHour == 0 && estimatedMinute == 0) {
                    it.text = "Estimated time: Skip"
                } else {
                    it.text = "Estimated time: $estimatedHour $estimatedMinute Minutes"
                }
            }

            var hour = prayerTime.split(":")[0].toInt().let {
                if (it > 12) {
                    it - 12
                } else {
                    it
                }
            }

            val minute = prayerTime.split(":")[1].toInt()
            it.text_view_hour_item_prayer_schedule.text = hour.toString()
            it.text_view_minute_item_prayer_schedule.let{
                if (minute < 10){
                    it.text = ":0$minute"
                } else {
                    it.text = ":$minute"
                }
            }

            it.text_view_prayer_name_item_prayer_schedule.text = prayerName.capitalize()

            when (prayerName) {
                "shubuh" -> {
                    it.image_view_circle_item_prayer_schedule.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_circle_blue))
                    it.text_view_format_hour_item_prayer_schedule.text = context.getString(R.string.am)
                }

                "dzuhur" -> {
                    it.image_view_circle_item_prayer_schedule.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_circle_pink))
                    it.text_view_format_hour_item_prayer_schedule.text = context.getString(R.string.pm)
                }

                "ashr" -> {
                    it.image_view_circle_item_prayer_schedule.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_circle_green))
                    it.text_view_format_hour_item_prayer_schedule.text = context.getString(R.string.pm)
                }

                "maghrib" -> {
                    it.image_view_circle_item_prayer_schedule.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_circle_lime))
                    it.text_view_format_hour_item_prayer_schedule.text = context.getString(R.string.pm)
                }

                else -> {
                    /** do nothing */
                }
            }
        }
    }

    override fun getItemCount(): Int = listPrayName.size

   inner class ViewHolderItemPrayerSchedule(itemView: View?): RecyclerView.ViewHolder(itemView)

}