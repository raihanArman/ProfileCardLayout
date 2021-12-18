package com.raihanarman.profilecardlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.raihanarman.profilecardlayout.ui.theme.MyTheme
import com.raihanarman.profilecardlayout.ui.theme.lightGreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                UsersApplication()
            }
        }
    }
}

@Composable
fun UsersApplication(userProfiles: List<UserProfile> = userProfileList){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "users_list"){
        composable("users_list"){
            UserListScreen(userProfiles, navController)
        }
        composable(
            "users_details/{user_id}",
            arguments = listOf(
                navArgument("user_id"){
                    type = NavType.IntType
                }
            )
        ){navBackEntry ->
            UserProfileDetailsScreen(navBackEntry.arguments!!.getInt("user_id"), navController = navController)
        }
    }
}

@Composable
fun UserListScreen(userProfiles: List<UserProfile>, navController: NavController?) {
    Scaffold(topBar = {
        AppBar(
            title = "Users List",
            icon = Icons.Default.Home, {})
        }
    ) {

        Surface(
            modifier = Modifier.fillMaxSize()
        ){
            LazyColumn{
                items(userProfiles){userProfile ->
                    ProfileCard(userProfile = userProfile){
                        navController?.navigate("users_details/${userProfile.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun AppBar(title: String, icon: ImageVector, iconClickAction: () -> Unit){
    TopAppBar(
        navigationIcon = {Icon(icon,"",
            modifier = Modifier
                .padding(12.dp)
                .clickable(onClick = iconClickAction)
        )},
        title = {Text(title)}
    )
}

@Composable
fun ProfileCard(userProfile: UserProfile, clickAction: () -> Unit){
    Card(
        modifier = Modifier
            .padding(
                top = 8.dp,
                bottom = 8.dp,
                start = 16.dp,
                end = 16.dp
            )
            .fillMaxWidth()
            .wrapContentHeight(
                align = Alignment.Top
            )
            .clickable(
                onClick = clickAction
            ),
        elevation = 8.dp,
        backgroundColor = Color.White
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            ProfilePicture(userProfile.pictureUrl, userProfile.status, 72.dp)
            ProfileContent(userProfile.name, userProfile.status, Alignment.Start)
        }
    }
}

@Composable
fun ProfilePicture(pictureUrl: String, onlineStatus: Boolean, imageSize: Dp){
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = if(onlineStatus) MaterialTheme.colors.lightGreen
                    else Color.Red
        ),
        modifier = Modifier
            .padding(16.dp)
            .size(imageSize),
        elevation = 4.dp
    ){
        Image(
            painter = rememberImagePainter(
                data = pictureUrl,
                builder = {
                    transformations(CircleCropTransformation())
                }
            ),
            contentDescription = null,
            modifier = Modifier.size(72.dp)
        )
    }
}

@Composable
fun ProfileContent(username: String, onlineStatus: Boolean, alignment: Alignment.Horizontal){
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment
    ){
        CompositionLocalProvider(LocalContentAlpha provides (
                if(onlineStatus)
                    ContentAlpha.high
                else
                    ContentAlpha.medium
                )) {
            Text(
                username,
                style = MaterialTheme.typography.h5
            )
        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                if(onlineStatus)"Active now" else "Offline",
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun UserProfileDetailsScreen(userId: Int, navController: NavController?) {
    val userProfile = userProfileList.first{userProfile ->
        userId == userProfile.id
    }
    Scaffold(
        topBar = {
            AppBar("User Details", Icons.Default.ArrowBack){
                navController?.navigateUp() } }) {

        Surface(
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                ProfilePicture(userProfile.pictureUrl, userProfile.status, 240.dp)
                ProfileContent(userProfile.name, userProfile.status, Alignment.CenterHorizontally)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UserDetailPreview() {

    UserProfileDetailsScreen(userId = 0, null)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTheme{
        UserListScreen(userProfiles = userProfileList, null)
    }
}