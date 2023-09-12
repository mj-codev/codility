package codev.codility.jobapplication.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import codev.codility.jobapplication.data.ApplicantEntity
import codev.codility.jobapplication.data.JobEntity
import codev.codility.jobapplication.ui.theme.JobApplicationTheme
import codev.codility.jobapplication.viewmodel.ApplicantViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.android.ext.android.inject


class MainActivity : ComponentActivity() {
    private val applicantViewModel: ApplicantViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JobApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "JobListScreen") {
                        composable("JobListScreen") {
                            JobListScreen(
                                navController = navController,
                                applicantViewModel = applicantViewModel
                            )
                        }
                        composable("ApplyJobScreen") {
                            ApplyJobScreen(
                                navController = navController,
                                applicantViewModel = applicantViewModel
                            )
                        }
                        composable("InsertUpdateJobScreen") {
                            // Creating gson object
                            val gson: Gson = GsonBuilder().create()
                            /* Extracting the user object json from the route */
                            val jobJson = applicantViewModel.jobArg.value
                            // Convert json string to the User data class object
                            val jobEntity = gson.fromJson(jobJson, JobEntity::class.java)
                            InsertUpdateJobScreen(
                                navController = navController,
                                applicantViewModel = applicantViewModel,
                                jobEntity = jobEntity
                            )
                           applicantViewModel.jobArg.value = ""
                        }
                    }
                }
            }
        }
        applicantViewModel.getAllJob()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen(navController: NavController, applicantViewModel: ApplicantViewModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        var keyword by remember { mutableStateOf("") }
        Row(modifier = Modifier.padding(all = 0.dp)) {
            TextField(
                value = keyword,
                onValueChange = {
                    keyword = it
                    applicantViewModel.getFilterJobs(it)
                },
                singleLine = true,
                label = { Text("Search Keyword") },
                modifier = Modifier.weight(1f, true).padding(all = 10.dp)
            )
            FloatingActionButton(
                onClick = {
                    navController.navigate("InsertUpdateJobScreen")
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(all = 10.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Job")
            }
        }
        JobRow(navController, applicantViewModel)
    }
}

@Composable
fun JobRow(navController: NavController, applicantViewModel: ApplicantViewModel) {
    val jobList = applicantViewModel.jobEntityList.value
    jobList.forEach { job ->
        Card(modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxWidth()) {
            Column(modifier = Modifier.padding(all = 10.dp)) {
                Text(
                    "No Of Openings: " + job.noOfOpenings.toString(),
                    color = Color.Gray,
                    modifier = Modifier.padding(0.dp)
                )
                Text("Title: " + job.title, color = Color.Gray, modifier = Modifier.padding(0.dp))
                Text(
                    "Description: " + job.description,
                    color = Color.Gray,
                    modifier = Modifier.padding(0.dp)
                )
                Text(
                    "Industry: " + job.industry.toString(),
                    color = Color.Gray,
                    modifier = Modifier.padding(0.dp)
                )
            }
            Row(modifier = Modifier.padding(all = 0.dp)) {
                Button(onClick =
                {
                    applicantViewModel.jobIdArg.value = job.id
                    navController.navigate("ApplyJobScreen")
                }, modifier = Modifier.padding(5.dp)) {
                    Text(text = "Apply")
                }
                Button(onClick =
                {
                    val gson: Gson = GsonBuilder().create()
                    val jobJson = gson.toJson(job).toString()
                    applicantViewModel.jobArg.value = jobJson
                    navController.navigate("InsertUpdateJobScreen")
                }, colors = ButtonDefaults.buttonColors(Color.Gray), modifier = Modifier.padding(5.dp)) {
                    Text(text = "Update")
                }
                Button(onClick =
                {
                    applicantViewModel.deleteJob(job.id)
                }, colors = ButtonDefaults.buttonColors(Color.Red), modifier = Modifier.padding(5.dp)) {
                    Text(text = "Delete")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyJobScreen(navController: NavController, applicantViewModel: ApplicantViewModel) {
    Card(modifier = Modifier
        .padding(all = 10.dp)
        .fillMaxWidth()
        .wrapContentHeight()) {
        Column(modifier = Modifier.padding(all = 10.dp)) {
            var firstname by remember { mutableStateOf("") }
            var lastname by remember { mutableStateOf("") }
            var emailAddress by remember { mutableStateOf("") }

            TextField(
                value = firstname,
                onValueChange = {
                    firstname = it
                },
                singleLine = true,
                label = { Text("FirstName") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = lastname,
                onValueChange = {
                    lastname = it
                },
                label = { Text("LastName") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = emailAddress,
                onValueChange = {
                    emailAddress = it
                },
                singleLine = true,
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick =
            {
                ApplicantEntity(
                    id = applicantViewModel.jobIdArg.value,
                    fullName = "$firstname $lastname",
                    emailAddress = emailAddress
                ).let {
                    applicantViewModel.jobIdArg.value = ""
                    applicantViewModel.applyJob(it)
                    navController.popBackStack()
                }
            }, modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()) {
                Text(text = "Save")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertUpdateJobScreen(navController: NavController, applicantViewModel: ApplicantViewModel, jobEntity: JobEntity?) {
    Card(modifier = Modifier
        .padding(all = 10.dp)
        .fillMaxWidth()
        .wrapContentHeight()) {
        Column(modifier = Modifier.padding(all = 10.dp)) {
            var id by remember { mutableStateOf("") }
            var noOfOpenings by remember { mutableStateOf("") }
            var title by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var industry by remember { mutableStateOf("") }

            jobEntity?.let {
                id = it.id
                noOfOpenings = it.noOfOpenings.toString()
                title = it.title
                description = it.description
                industry = it.industry.toString()
            }

            TextField(
                value = noOfOpenings,
                onValueChange = {
                    noOfOpenings = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("No of Openings") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = title,
                onValueChange = {
                    title = it
                },
                singleLine = true,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = description,
                onValueChange = {
                    description = it
                },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = industry,
                onValueChange = {
                    industry = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Industry") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick =
            {
                JobEntity(
                    id,
                    noOfOpenings.toInt(),
                    title,
                    description,
                    industry.toInt()
                ).let {
                    applicantViewModel.insertUpdateJob(it)
                    navController.popBackStack()
                }
            }, modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()) {
                Text(text = "Save")
            }
        }
    }
}