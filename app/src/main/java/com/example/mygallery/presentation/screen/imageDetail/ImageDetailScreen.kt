package com.example.mygallery.presentation.screen.imageDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import android.content.Intent
import androidx.compose.material.icons.filled.Share
import androidx.core.content.FileProvider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailScreen(
    state: ImageDetailState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val image = state.image
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    fun shareImage() {
        if (image == null) return
        scope.launch(Dispatchers.IO) {
            try {
                val cachePath = File(context.cacheDir, "shared_images")
                cachePath.mkdirs()
                val newFile = File(cachePath, "shared_image.jpg")
                val contentUri = image.contentUri
                val inputStream = context.contentResolver.openInputStream(contentUri)
                val outputStream = FileOutputStream(newFile)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()

                val contentUriToShare = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    newFile
                )

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    setDataAndType(contentUriToShare, context.contentResolver.getType(contentUriToShare) ?: "image/*")
                    putExtra(Intent.EXTRA_STREAM, contentUriToShare)
                    type = "image/*" // Ensure type is set for ACTION_SEND
                }
                
                val chooser = Intent.createChooser(shareIntent, "Share Image")
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooser)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("사진 상세정보") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { shareImage() }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (image != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.Black)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(image.contentUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = image.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.align(Alignment.Center).fillMaxSize()
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "파일명: ${image.name}",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                val dateString = if (image.dateTaken != null) {
                    DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())
                        .format(Date(image.dateTaken))
                } else {
                    "Unknown"
                }
                
                Text(
                    text = "날짜: $dateString",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "경로: ${image.contentUri}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Image not found")
            }
        }
    }
}
