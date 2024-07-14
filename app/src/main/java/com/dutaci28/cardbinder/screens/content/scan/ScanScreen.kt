package com.dutaci28.cardbinder.screens.content.scan

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dutaci28.cardbinder.screens.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.io.IOException


@Composable
fun ScanScreen(
    viewModel: ScanViewModel = hiltViewModel(),
    navController: NavController
) {
    val auth = viewModel.auth
    val context = LocalContext.current
    val outlinedBitmap =
        remember { mutableStateOf(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)) }
    val scrollState = rememberScrollState()
    val isShowDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 140.dp)
    )
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { isShowDialog.value = true }) { Text(text = "Log Out") }
            if (isShowDialog.value) ShowAccountDialog(
                isShowDialog = isShowDialog,
                viewModel = viewModel,
                auth = auth,
                navController = navController,
                context = context
            )
            TextRecognitionSection(
                outlinedBitmap = outlinedBitmap,
                navController = navController
            )
        }
    }
}

@Composable
fun ShowAccountDialog(
    isShowDialog: MutableState<Boolean>,
    viewModel: ScanViewModel,
    auth: FirebaseAuth,
    navController: NavController,
    context: Context
) {
    AlertDialog(
        title = {
            Text(text = "Account")
        },
        text = {
            auth.currentUser?.email?.let { Text(text = "Signed in as: $it") }
        },
        onDismissRequest = {
            isShowDialog.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.signOut(
                        navController,
                        context
                    )
                }
            ) {
                Text("Log Out")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    isShowDialog.value = false
                }
            ) {
                Text("Dismiss")
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun TextRecognitionSection(
    outlinedBitmap: MutableState<Bitmap>,
    navController: NavController
) {
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
                getCorrectlyOrientedBitmap(context.contentResolver, uri)
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
                text = "Tap the box with name of the card:",
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
                        Log.d("CARDS", "Tapped adjusted ${adjustedOffset.x} ${adjustedOffset.y}")
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

fun getBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    val inputStream = contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(inputStream)
}

fun getCorrectlyOrientedBitmap(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    val bitmap = getBitmapFromUri(contentResolver, uri)
    if (bitmap != null) {
        val orientation = getExifOrientation(contentResolver, uri)
        return correctBitmapOrientation(bitmap, orientation)
    }
    return null
}

fun correctBitmapOrientation(bitmap: Bitmap, orientation: Int): Bitmap {
    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
        else -> return bitmap
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun getExifOrientation(contentResolver: ContentResolver, uri: Uri): Int {
    var orientation = ExifInterface.ORIENTATION_UNDEFINED
    try {
        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream != null) {
            val exif = ExifInterface(inputStream)
            orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            inputStream.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return orientation
}

fun searchTappedText(recognizedText: Text, offset: Offset, navController: NavController): String {
    for (block in recognizedText.textBlocks) {
        for (line in block.lines) {
            Log.d(
                "CARDS",
                "Line coords ${line.boundingBox?.top} ${line.boundingBox?.right} ${line.boundingBox?.bottom} ${line.boundingBox?.left}"
            )
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
