package com.dutaci28.cardbinder.screens.content.scan

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dutaci28.cardbinder.screens.navigation.Routes
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File


@Composable
fun ScanScreen(
    viewModel: ScanViewModel = hiltViewModel(),
    navController: NavController
) {
    val auth = viewModel.auth
    val context = LocalContext.current
    val outlinedBitmap =
        remember { mutableStateOf(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "WIP", color = Color.Red)
            auth.currentUser?.email?.let { Text(text = it) }
            Button(onClick = {
                viewModel.signOut(
                    navController,
                    context
                )
            }) { Text(text = "Log Out") }
            TextRecognitionSection(outlinedBitmap = outlinedBitmap, navController = navController)
        }
    }
}

@Composable
fun TextRecognitionSection(outlinedBitmap: MutableState<Bitmap>, navController: NavController) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val activity = LocalContext.current as? Activity
    val recognizedText = remember { mutableStateOf<Text?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageBitmap = imageUri?.let { uri ->
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            }
            imageBitmap?.let { bitmap ->
                runTextRecognition(
                    bitmap = bitmap,
                    recognizedText = recognizedText,
                    resultingOutlinedBitmap = outlinedBitmap
                )
            }
        }
    }
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val file = File(context.cacheDir, "camera_photo.jpg").apply {
                createNewFile()
                deleteOnExit()
            }
            imageUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            takePictureLauncher.launch(imageUri!!)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    val file = File(
                        context.cacheDir,
                        "camera_photo.jpg"
                    ).apply {
                        createNewFile()
                        deleteOnExit()
                    }
                    imageUri =
                        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                    takePictureLauncher.launch(imageUri!!)
                }

                activity?.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) == true -> {
                    Toast.makeText(
                        context,
                        "Camera permission is needed to take pictures",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }) {
            Text("Scan a card")
        }
        recognizedText.value?.let {
            Text(
                text = "Tap the name of the card identified text boxes:",
                fontWeight = FontWeight.Bold
            )
        }
        Image(
            bitmap = outlinedBitmap.value.asImageBitmap(),
            contentDescription = "Image with text outlines",
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val scaleX = outlinedBitmap.value.width.toFloat() / size.width
                        val scaleY = outlinedBitmap.value.height.toFloat() / size.height
                        val adjustedOffset = Offset(
                            (offset.x * scaleX),
                            (offset.y * scaleY)
                        )
                        recognizedText.value?.let {
                            searchTappedText(
                                it,
                                adjustedOffset,
                                navController = navController
                            )
                        }
                    }
                }

        )

    }
}

fun searchTappedText(recognizedText: Text, offset: Offset, navController: NavController): String {
    for (block in recognizedText.textBlocks) {
        for (line in block.lines) {
            if (line.boundingBox != null && line.boundingBox!!.contains(
                    offset.x.toInt(),
                    offset.y.toInt()
                )
            ) {
                Log.d("CARDS", "Tapped ${line.text}")
                navController.navigate(route = "search/true/${line.text}") {
                    popUpTo(Routes.Search.defaultRoute) {
                        inclusive = true
                    }
                }
            }
        }
    }
    return ""
}

fun drawOutlinesOnBitmap(bitmap: Bitmap, recognizedText: Text): Bitmap {
    val outlinedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(outlinedBitmap)
    val paint = Paint().apply {
        color = Color.Red.toArgb()
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    for (block in recognizedText.textBlocks) {
        for (line in block.lines) {
            canvas.drawRect(line.boundingBox!!, paint)
        }
    }

    return outlinedBitmap
}

private fun processTextRecognitionResult(
    texts: Text,
    recognizedText: MutableState<Text?>,
    initialBitmap: Bitmap,
    resultingOutlinedBitmap: MutableState<Bitmap>
) {
    val blocks: List<Text.TextBlock> = texts.textBlocks
    if (blocks.isEmpty()) {
        Log.d("CARDS", "No text found")
        return
    }
    resultingOutlinedBitmap.value = drawOutlinesOnBitmap(initialBitmap, texts)
    recognizedText.value = texts
}

private fun runTextRecognition(
    bitmap: Bitmap,
    recognizedText: MutableState<Text?>,
    resultingOutlinedBitmap: MutableState<Bitmap>
) {
    val image = InputImage.fromBitmap(bitmap, 0)
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    recognizer.process(image)
        .addOnSuccessListener { texts ->
            processTextRecognitionResult(
                texts = texts,
                recognizedText = recognizedText,
                initialBitmap = bitmap,
                resultingOutlinedBitmap = resultingOutlinedBitmap
            )
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
        }
}
