package com.enigmatix.deprocrastinator.ui.aboutme

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.enigmatix.deprocrastinator.*
import com.enigmatix.deprocrastinator.database.DbXP
import com.enigmatix.deprocrastinator.databinding.AboutMeFragmentBinding
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import kotlin.math.abs


class AboutMeFragment : Fragment() {
    private lateinit var binding: AboutMeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideKeyboard(requireActivity())
        binding = AboutMeFragmentBinding.inflate(inflater)
        UserInfoManipulator.nameLiveData.observe(viewLifecycleOwner){
            binding.name.setText(it)
        }
        binding.setName.setOnClickListener {
            UserInfoManipulator.setName(requireActivity(), binding.name.text.toString())
        }
        ExperienceManipulator.getCurrentXP(requireContext()).observe(viewLifecycleOwner){
            var xp = 0L
            if (it != null)
                xp = it.xp
            binding.xpTextView.text = requireContext().getString(R.string.xp_format).format(
                xp / 100,
                100 - xp % 100
            )
        }
        ExperienceManipulator.getAllXP(requireContext()).observe(viewLifecycleOwner) { dbXp ->
            if (dbXp == null || dbXp.isEmpty()) {
                binding.chart.visibility = LineChartView.GONE
                binding.completeTextView.visibility = TextView.VISIBLE
                return@observe
            }
            binding.chart.visibility = LineChartView.VISIBLE
            binding.completeTextView.visibility = TextView.GONE
            val values = mutableListOf<PointValue>()
            val xpList = mutableListOf(DbXP(Date(dbXp.reversed()[0].day.time-TimeUnit.HOURS.toMillis(24)), 0))
            for (xp in dbXp.reversed()) {
                while (abs(xpList.last().day.time - xp.day.time) > TimeUnit.HOURS.toMillis(47)) {
                    val now = xpList.last()
                    xpList.add(DbXP(Date(now.day.time + TimeUnit.HOURS.toMillis(24)), now.xp))
                }
                xpList.add(xp)
            }
            val list = xpList.reversed().stream().limit(14).collect(Collectors.toList()).reversed()
            for ((pos, xp) in list.withIndex()) {
                values.add(PointValue(pos.toFloat(), xp.xp.toFloat()))
            }
            val lines = listOf(Line(values).setColor(Color.BLACK))
            val data = LineChartData()
            data.lines = lines
            val yAxisValues = mutableListOf(list[0].xp, list[list.lastIndex].xp).map {
                AxisValue(it.toFloat())
            }
            var pos = 0
            val xAxisValues = mutableListOf(prettyTimeString(list[0].day, true), prettyTimeString(list[list.lastIndex].day, true)).map {
                val x = AxisValue(pos.toFloat())
                pos = list.lastIndex
                x.setLabel(it)
                x
            }
            val yAxis = Axis()
            val xAxis = Axis()
            yAxis.values = yAxisValues
            yAxis.name = "Experience"
            xAxis.values = xAxisValues
            xAxis.name = "Day"
            data.axisYLeft = yAxis
            data.axisXBottom = xAxis

            binding.chart.lineChartData = data
        }
        return binding.root
    }
}