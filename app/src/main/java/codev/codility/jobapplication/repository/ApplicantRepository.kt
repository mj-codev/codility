package codev.codility.jobapplication.repository

import codev.codility.jobapplication.data.ApplicantEntity
import codev.codility.jobapplication.data.JobEntity

abstract class ApplicantRepository {
    abstract suspend fun getAllJobs() : ArrayList<JobEntity>
    abstract suspend fun getFilterJobs(keyword: String) : ArrayList<JobEntity>
    abstract suspend fun deleteJob(id: String)
    abstract suspend fun updateJob(jobEntity: JobEntity)
    abstract suspend fun insertJob(jobEntity: JobEntity)
    abstract suspend fun applyJob(applicantEntity: ApplicantEntity)
}