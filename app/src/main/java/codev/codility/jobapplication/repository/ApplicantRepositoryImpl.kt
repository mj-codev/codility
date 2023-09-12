package codev.codility.jobapplication.repository

import codev.codility.jobapplication.data.ApplicantEntity
import codev.codility.jobapplication.data.JobEntity
import codev.codility.jobapplication.services.ApplicantServices
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import kotlin.collections.ArrayList

class ApplicantRepositoryImpl constructor(private val applicantServices: ApplicantServices) : ApplicantRepository() {

    override suspend fun getAllJobs(): ArrayList<JobEntity> {
        return applicantServices.getAllJobs()
    }

    override suspend fun getFilterJobs(keyword: String): ArrayList<JobEntity> {
        return applicantServices.getFilterJobs(keyword)
    }

    override suspend fun deleteJob(id: String) {
        return applicantServices.deleteJob(id)
    }

    override suspend fun updateJob(jobEntity: JobEntity) {
        val hashMap = HashMap<String, Any?>()
        hashMap["id"] = jobEntity.id
        hashMap["noOfOpenings"] = jobEntity.noOfOpenings
        hashMap["title"] = jobEntity.title
        hashMap["description"] = jobEntity.description
        hashMap["industry"] = jobEntity.industry

        return applicantServices.updateJob(RequestBody.create(
            "application/json;charset=UTF-8".toMediaTypeOrNull(),
            Gson().toJson(hashMap)
        ))
    }

    override suspend fun insertJob(jobEntity: JobEntity) {
        val hashMap = HashMap<String, Any?>()
        hashMap["noOfOpenings"] = jobEntity.noOfOpenings
        hashMap["title"] = jobEntity.title
        hashMap["description"] = jobEntity.description
        hashMap["industry"] = jobEntity.industry

        return applicantServices.insertJob(RequestBody.create(
            "application/json;charset=UTF-8".toMediaTypeOrNull(),
            Gson().toJson(hashMap)
        ))
    }

    override suspend fun applyJob(applicantEntity: ApplicantEntity) {
        val hashMap = HashMap<String, Any?>()
        hashMap["fullName"] = applicantEntity.fullName
        hashMap["emailAddress"] = applicantEntity.emailAddress

        return applicantServices.applyJob(applicantEntity.id, RequestBody.create(
            "application/json;charset=UTF-8".toMediaTypeOrNull(),
            Gson().toJson(hashMap)
        ))
    }
}