package com.example.stepcounterpoc.ui.healthconnect

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.metadata.DataOrigin
import androidx.health.connect.client.records.ActivitySession
import androidx.health.connect.client.records.ActivitySession.ActivityType
import androidx.health.connect.client.records.Distance
import androidx.health.connect.client.records.HeartRateSeries
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.Steps
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ChangesTokenRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.ViewModel
import java.time.Period
import kotlin.reflect.KClass

class HealthConnectViewModel : ViewModel() {

    suspend fun getChangesToken(
        client: HealthConnectClient,
        recordTypes: Set<KClass<out Record>>,
    ) {
        val response =
            client.getChangesToken(
                ChangesTokenRequest(
                    recordTypes = recordTypes,
                )
            )
    }

    //Ensure the following:
    //
    //Your app is updating from changes much more regularly than this to avoid stale tokens.
    //Your app can handle the case where the token is no longer valid, and has a fallback mechanism for obtaining the necessary data.
    //Request changes after obtaining the changes token:
    suspend fun getChanges(
        client: HealthConnectClient,
        changesToken: String,
    ) {
        val response = client.getChanges(changesToken)
        response.changes.forEach {
            // Process each change
        }
    }

    suspend fun insertSteps(healthConnectClient: HealthConnectClient) {
//        val records = listOf(
//            Steps(
//                count = 120,
//                startTime = START_TIME,
//                endTime = END_TIME,
//                startZoneOffset = START_ZONE_OFFSET,
//                endZoneOffset = END_ZONE_OFFSET,
//            )
//        )
//        val response = healthConnectClient.insertRecords(records)
    }

    //Reading data with Health Connect is restricted to applications running in the foreground.
    // This restriction is in place to further strengthen user privacy. It notifies and assures
    // users that Health Connect does not have background read access to their data and that data
    // is only read and accessed in the foreground.
    //
    //For situations in which being interrupted is expected, such as displaying a reading in your
    // application, read directly from Health Connect to your client application.
    //
    //For situations in which you may prefer not to be interrupted, such as reading a range of data
    // from Health Connect then writing and uploading it elsewhere, use a ForegroundService,
    // rather than an Activity where it can easily be dismissed.
    suspend fun readDataRange(client: HealthConnectClient) {
//        val response =
//            client.readRecords(
//                ReadRecordsRequest(
//                    recordType = Steps::class,
//                    timeRangeFilter = TimeRangeFilter.between(START_TIME, END_TIME),
//                )
//            )
//        response.records.forEach {
//            // Process each entry
//        }
    }

    //    The following code example shows how to delete step data by its UID. The Record unique identifier can be found in metadata.uid.
    suspend fun deleteStepDataByUid(client: HealthConnectClient, uid: String) {
        client.deleteRecords(
            Steps::class,
            uidsList = listOf(uid),
            clientIdsList = emptyList()
        )
    }

    //    The following code example shows how to delete data of a specific data type in a time range.
    suspend fun deleteDataByRange(client: HealthConnectClient) {
//        client.deleteRecords(
//            Steps::class,
//            timeRangeFilter = TimeRangeFilter.between(START_TIME, END_TIME)
//        )
    }

    //    The following example shows you how to aggregate data for a data type.
    suspend fun aggregateDistance(client: HealthConnectClient): Double? {
//        val request = AggregateRequest(
//            metrics = setOf(Distance.DISTANCE_TOTAL),
//            timeRangeFilter = TimeRangeFilter.between(START_TIME, END_TIME)
//        )
//        val response = client.aggregate(request)
        // The result may be null if no data is available to aggregate.
//        return response.getMetric(Distance.DISTANCE_TOTAL)
        return null
    }

    //    Statistical aggregation will compute the minimum, maximum, or average values.
    suspend fun aggregateHROverTime(client: HealthConnectClient): Long? {
//        val aggregate = client.aggregate(
//            AggregateRequest(
//                setOf(HeartRateSeries.BPM_MAX),
//                timeRangeFilter = TimeRangeFilter.between(START_TIME, END_TIME),
//                dataOriginFilter = listOf(
//                    DataOrigin("androidx.health.connect.client.sample")
//                )
//            )
//        )
        // The result may be null if no data is available to aggregate.
//        return aggregate.getMetric(HeartRateSeries.BPM_MAX)
        return null
    }

    //    The following shows an example of aggregating steps into monthly buckets:
    suspend fun aggregateIntoMonths(healthConnectClient: HealthConnectClient) {
//        val aggregateGroupByPeriodRequest = if (VERSION.SDK_INT >= VERSION_CODES.O) {
//            AggregateGroupByPeriodRequest(
//                metrics = setOf(Steps.COUNT_TOTAL),
//                timeRangeFilter = TimeRangeFilter.between(START_TIME, END_TIME),
//                timeRangeSlicer = Period.ofMonths(1)
//            )
//        } else {
//            TODO("VERSION.SDK_INT < O")
//        }
//        val response = healthConnectClient.aggregateGroupByPeriod(
//            aggregateGroupByPeriodRequest
//        )
//        response.forEach { period ->
//            val totalSteps = period.result.getMetric(Steps.COUNT_TOTAL) ?: 0L
//        }
    }
    //To aggregate by Duration, the approach follows the same pattern as previously,
// but instead uses the aggregateGroupByDuration(request: AggregateGroupByDurationRequest) method.

//    The following code sample demonstrates how to build an insertion request that includes a session:
    suspend fun writeActivitySession(healthConnectClient: HealthConnectClient) {
//        healthConnectClient.insertRecords(
//            listOf(
//                ActivitySession(
//                    startTime = START_TIME,
//                    startZoneOffset = START_ZONE_OFFSET,
//                    endTime = END_TIME,
//                    endZoneOffset = END_ZONE_OFFSET,
//                    activityType = ActivityType.RUNNING,
//                    title = "My Run"
//                ),
//                Distance(
//                    startTime = START_TIME,
//                    startZoneOffset = START_ZONE_OFFSET,
//                    endTime = END_TIME,
//                    endZoneOffset = END_ZONE_OFFSET,
//                    distanceMeters = 5000.0
//                ),
//                // ... other records
//            )
//        )
    }

    //Here’s an example of how to read activity sessions:
    suspend fun readActivitySessions(
        client:HealthConnectClient
    ): List<ActivitySession> {
//        val request = ReadRecordsRequest(
//            recordType = ActivitySession::class,
//            timeRangeFilter = TimeRangeFilter.between(START_TIME, END_TIME)
//        )
//        val response = client.readRecords(request)
//        return response.records
        return listOf()
    }
}