package codev.codility.jobapplication.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import codev.codility.jobapplication.data.ApplicantEntity
import codev.codility.jobapplication.data.JobEntity
import codev.codility.jobapplication.repository.ApplicantRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ApplicantViewModel (private val repository: ApplicantRepository) : ViewModel() {

    @SuppressLint("MutableCollectionMutableState")
    var jobEntityList: MutableState<ArrayList<JobEntity>> = mutableStateOf(ArrayList())
    var jobIdArg: MutableState<String> = mutableStateOf(String())
    var jobArg: MutableState<String> = mutableStateOf(String())
    var jobErrorArg: MutableState<String> = mutableStateOf(String())

    fun getAllJob() {
        CoroutineScope(Dispatchers.Main).launch {
            jobEntityList.value = repository.getAllJobs()
        }
    }

    fun getFilterJobs(keyword: String) {
        CoroutineScope(Dispatchers.Main).launch {
            jobEntityList.value = repository.getFilterJobs(keyword)
        }
    }

    fun deleteJob(id: String) {
        CoroutineScope(Dispatchers.Main).launch {
            repository.deleteJob(id)
            jobEntityList.value = repository.getAllJobs()
        }
    }

    fun insertUpdateJob(jobEntity: JobEntity) {
        CoroutineScope(Dispatchers.Main).launch {
            if (jobEntity.id.isNotEmpty() || jobEntity.id.isNotBlank()) {
                repository.updateJob(jobEntity)
            } else {
                repository.insertJob(jobEntity)
            }
            jobEntityList.value = repository.getAllJobs()
        }
    }

    fun applyJob(applicantEntity: ApplicantEntity) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                repository.applyJob(applicantEntity)
            } catch (e: Exception) {
                jobErrorArg.value = "You have previously applied for this job."
                e.printStackTrace()
            }
        }
    }
}