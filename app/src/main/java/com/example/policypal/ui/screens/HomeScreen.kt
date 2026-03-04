package com.example.policypal.ui.screens

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.policypal.data.LeadEntity
import com.example.policypal.data.LeadViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

@Composable
fun HomeScreen(vm: LeadViewModel) {
    val leads = vm.leads.collectAsStateWithLifecycle().value

    val totalLeads = leads.size
    val newLeads = leads.count { it.status == "Active" }   // keep as your existing logic
    val activeLeads = leads.count { it.status == "Active" }
    val closedLeads = leads.count { it.status == "Closed" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("GOOD AFTERNOON", style = MaterialTheme.typography.labelSmall)
        Text(
            "PolicyPal",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF0B7A75)
        )
        Text("LIC Agent Command Center")

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("TOTAL LEADS", totalLeads.toString(), Modifier.weight(1f))
            StatCard("NEW LEADS", newLeads.toString(), Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("SCHEDULED", "12", Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("ACTIVE", activeLeads.toString(), Modifier.weight(1f))
            StatCard("CLOSED", closedLeads.toString(), Modifier.weight(1f))
        }

        Spacer(Modifier.height(20.dp))

        LeadChartCard(leads)
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium)
            Text(
                "+12% vs last month",
                color = Color(0xFF1FAE5B),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun LeadChartCard(leads: List<LeadEntity>) {
    val activeCount = leads.count { it.status == "Active" }
    val closedCount = leads.count { it.status == "Closed" }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Lead Status", style = MaterialTheme.typography.titleMedium)
            Text("Active vs Closed", style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(16.dp))

            AndroidView(
                factory = { context ->
                    BarChart(context).apply {
                        val entries = listOf(
                            BarEntry(1f, activeCount.toFloat()),
                            BarEntry(2f, closedCount.toFloat())
                        )

                        val dataSet = BarDataSet(entries, "").apply {
                            color = AndroidColor.parseColor("#0B7A75")
                            valueTextSize = 12f
                        }

                        data = BarData(dataSet).apply { barWidth = 0.6f }

                        description.isEnabled = false
                        legend.isEnabled = false
                        axisRight.isEnabled = false

                        axisLeft.apply {
                            axisMinimum = 0f
                            setDrawGridLines(false)
                            setDrawAxisLine(false)
                        }

                        xAxis.apply {
                            setDrawGridLines(false)
                            setDrawAxisLine(false)
                            setDrawLabels(true)
                            granularity = 1f
                            position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                            valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                                override fun getFormattedValue(value: Float): String {
                                    return when (value.toInt()) {
                                        1 -> "Active"
                                        2 -> "Closed"
                                        else -> ""
                                    }
                                }
                            }
                        }

                        setScaleEnabled(false)
                        setPinchZoom(false)
                        animateY(600)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}