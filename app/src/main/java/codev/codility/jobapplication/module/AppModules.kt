package codev.codility.jobapplication.module

import codev.codility.jobapplication.network.RetrofitHelper
import codev.codility.jobapplication.repository.ApplicantRepository
import codev.codility.jobapplication.repository.ApplicantRepositoryImpl
import codev.codility.jobapplication.services.ApplicantServices
import codev.codility.jobapplication.viewmodel.ApplicantViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ApplicantViewModel(get()) }
}

val serviceModule = module {
    single { Gson() }
    single { createApi(ApplicantServices::class.java) }
}

val repositoryModule = module {
    single { ApplicantRepositoryImpl(get()) } bind ApplicantRepository::class
}

inline fun <reified T> createApi(clazz: Class<T>): T {
    return RetrofitHelper.createApi(clazz)
}

val appModule = arrayListOf(
    serviceModule,
    repositoryModule,
    viewModelModule
)