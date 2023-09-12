package codev.codility.jobapplication.services

import codev.codility.jobapplication.data.JobEntity
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApplicantServices {

    @GET("Job/getall")
    suspend fun getAllJobs(): ArrayList<JobEntity>

    @GET("Job/filter")
    suspend fun getFilterJobs(@Query("Keyword") keyword: String): ArrayList<JobEntity>

    @DELETE("Job/delete")
    suspend fun deleteJob(@Query("id") id: String)

    @PUT("Job/update")
    suspend fun updateJob(@Body body: RequestBody)

    @POST("Job/insert")
    suspend fun insertJob(@Body body: RequestBody)

    @POST("JobApplicant/applyjob/{jobId}")
    suspend fun applyJob(@Path("jobId") jobId: String, @Body body: RequestBody)
}