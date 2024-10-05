package com.example.busapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import com.example.busapp.viewmodels.TimetableViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ViewTimetables(
    navController: NavController,
    timetableViewModel: TimetableViewModel
) {
    val routes: List<List<String>> by timetableViewModel.routes.collectAsState()
    val tripsPerRoute: Map<String, MutableList<String>> by timetableViewModel.tripsPerRoute.collectAsState()
    val stopTimesPerRoute: Map<String, MutableList<Pair<String, String>>> by timetableViewModel.stopTimesPerTrip.collectAsState()
    val stopNamesPerRoute: Map<String, MutableList<String>> by timetableViewModel.stopNamesPerTrip.collectAsState()
    println("timetable screen")
    println(stopNamesPerRoute)
    var expanded by remember { mutableStateOf(false) }
    var selectedRouteName by remember { mutableStateOf("")}
    var selectedRouteId by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("") }
    var headerList by remember { mutableStateOf(mutableListOf<String>()) }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Timetables", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.size(20.dp))
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                ) {
                    TextField(
                        value = selectedRouteName,
                        placeholder = { Text("Select Bus Service") },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        routes.forEach { route ->
                            DropdownMenuItem(
                                text = { Text(text = route[1] + " - " + route[2]) },
                                onClick = {
                                    selectedRouteName = route[1] + " - " + route[2]
                                    selectedRouteId = route[0]
                                    //TODO: Get weekday trip_id when file has been uploaded
                                    headerList = stopNamesPerRoute[route[0]]!!
                                    expanded = false
                                })
                        }
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))

                Button(
                    onClick = {}
                ) {
                    Text("View Other Direction", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.size(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = { selectedDay = "Weekday" }
                    ) {
                        Text("Weekday")
                    }
                    Button(
                        onClick = { selectedDay = "Saturday" }
                    ) {
                        Text("Saturday")
                    }
                    Button(
                        onClick = { selectedDay = "Sunday" }
                    ) {
                        Text("Sunday")
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))

            }
        }

        //From routes.txt: Get route_id, route_short_name, route_long_name
        //  - display route_short_name/route_long_name at the top of page
        //From trips.csv: Get trip_id. direction_id
        // - use direction_id to know which direction bus is, use in View Other Direction button
        //From stop_times.csv: For trip_id get all stop_id's, arrival_time/departure_time
        //  - display the arrival_time/departure_time in the table
        //From stops.txt: For stop_id get stop_name
        // - display the stop_name as the column headers

        //TODO: Get the data from the stop_times/trips/stops files
        //val headerList = stopNamesPerRoute[selectedRouteId]
        val headerList2 = listOf("Eastgate Mall (Buckleys Rd)", "St Martins Shops", "Princess Margaret Hospital", "Barrington Mall (Barrington St)", "Westfield Riccarton", "Burnside High School", "Northlands Platform B", "The Palms(North Parade)", "Eastgate Mall (Buckleys Road)")
        val dataList = listOf("12:30pm","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a")

        val columnHeaderModifier = Modifier
            .border(1.dp, Color.Black)
            .wrapContentSize()
            .padding(4.dp)
        val dataModifier = Modifier
            .border(1.dp, Color.Black)
            .wrapContentSize()
            .padding(4.dp)

        //TODO: Change value in here to num stops in route
        val numColumns = 9
        item {
            LazyRow {
                item {
                    val size = headerList.size
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(numColumns),
                        //Will need to change how height is set up later
                        modifier = Modifier
                            .width((numColumns * 128).dp)
                            .height(((size) * 30).dp),
                    ) {
                        items(headerList) {
                            Text(it, columnHeaderModifier, fontSize = 10.sp)
                        }
                        items(dataList) { Text(it, dataModifier, fontSize = 10.sp) }
                    }
                }
            }
        }
    }
}
