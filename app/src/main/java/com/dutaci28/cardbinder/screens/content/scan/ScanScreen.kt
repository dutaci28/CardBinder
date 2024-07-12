package com.dutaci28.cardbinder.screens.content.scan

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
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

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "WIP", color = Color.Red)
            auth.currentUser?.email?.let { Text(text = it) }
            Button(onClick = {
                viewModel.signOut(navController, context)
            }) {
                Text(text = "Log Out")
            }
            TextRecognitionSection(navController)
        }
    }
}

@Composable
fun TextRecognitionSection(navController: NavController) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val activity = LocalContext.current as? Activity
    val resultingTextList = remember { mutableStateListOf<String>() }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageBitmap = imageUri?.let { uri ->
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val originalBitmap = BitmapFactory.decodeStream(stream)
                    val ei = ExifInterface(stream)
                    val orientation = ei.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED
                    )
                    val rotatedBitmap: Bitmap = when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(originalBitmap, 90f)

                        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(originalBitmap, 180f)

                        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(originalBitmap, 270f)

                        ExifInterface.ORIENTATION_NORMAL -> originalBitmap
                        else -> originalBitmap
                    }
                    rotatedBitmap
                }
            }
            imageBitmap?.let { bitmap ->
                runTextRecognition(
                    bitmap = bitmap,
                    resultingTextList = resultingTextList
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
            Text("Take Picture")
        }

        Text(text = "Tap the correct name:", fontWeight = FontWeight.Bold)
        resultingTextList.forEach {
            Text(text = it, modifier = Modifier.clickable {
                navController.navigate(route = "search/true/$it") {
                    popUpTo(Routes.Register.route) {
                        inclusive = true
                    }
                }
            })
        }
    }
}


private fun processTextRecognitionResult(
    texts: Text,
    resultingTextList: SnapshotStateList<String>
) {
    val blocks: List<Text.TextBlock> = texts.textBlocks
    if (blocks.isEmpty()) {
        Log.d("CARDS", "No text found")
        return
    }
    for (i in blocks.indices) {
        val lines: List<Text.Line> = blocks[i].lines
        val extractedTextLine: StringBuilder = StringBuilder("")
        var extractedTextLineBoundingBox = Rect(0, 0, 0, 0)
        for (j in lines.indices) {
            val elements: List<Text.Element> = lines[j].elements
            for (k in elements.indices) {
                extractedTextLine.append(elements[k].text + " ")
                extractedTextLineBoundingBox = elements[k].boundingBox!!
            }
        }
        if (extractedTextLine.toString().length < 40)
            resultingTextList.add(extractedTextLine.toString())
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height,
        matrix, true
    )
}

private fun runTextRecognition(
    bitmap: Bitmap,
    resultingTextList: SnapshotStateList<String>
) {
    val image = InputImage.fromBitmap(bitmap, 0)
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    recognizer.process(image)
        .addOnSuccessListener { texts ->
            processTextRecognitionResult(
                texts = texts,
                resultingTextList = resultingTextList
            )
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
        }
}
