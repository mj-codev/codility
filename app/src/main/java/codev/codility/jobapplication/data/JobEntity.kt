package codev.codility.jobapplication.data

data class JobEntity (
        val id: String,
        var noOfOpenings: Int,
        var title: String,
        var description: String,
        var industry: Int,
        )