//CardBinder - a Magic: The Gathering collector's app.
//Copyright (C) 2024 Catalin Duta
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.(https://github.com/dutaci28/CardBinder?tab=GPL-3.0-1-ov-file#readme).
//If not, see <https://www.gnu.org/licenses/>.

package com.dutaci28.cardbinder.screens.content.search

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dutaci28.cardbinder.R
import com.dutaci28.cardbinder.model.MTGCard
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.io.IOException

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SharedTransitionScope.SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
    shouldFocus: Boolean,
    searchText: String = "",
    randomCardData: LazyPagingItems<MTGCard>,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val searchQuery by viewModel.searchQuery
    val context = LocalContext.current
    val searchedCards = viewModel.searchedCards.collectAsLazyPagingItems()
    val focusRequester = viewModel.focusRequester
    val isShowDialog = remember { mutableStateOf(false) }
    Log.d("CARDS", isShowDialog.value.toString())
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val activity = LocalContext.current as? Activity
    val recognizedText = remember { mutableStateOf<Text?>(null) }
    val outlinedBitmap =
        remember { mutableStateOf(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)) }
    if (searchText != "null" && searchText != "EMPTY_VALUE") {
        viewModel.updateSearchQuery(searchText)
        viewModel.searchCardsByName(searchText)
    }

    Scaffold(
        topBar = {
            if (activity != null) {
                SearchScreenTopBar(
                    text = searchQuery,
                    context = context,
                    onTextChange = { viewModel.updateSearchQuery(query = it) },
                    onSearchClicked = { viewModel.searchCardsByName(name = it) },
                    onClearClicked = { viewModel.clearSearchedCards() },
                    shouldFocus = shouldFocus,
                    focusRequester = focusRequester,
                    imageUri = imageUri,
                    imageBitmap = imageBitmap,
                    recognizedText = recognizedText,
                    outlinedBitmap = outlinedBitmap,
                    activity = activity,
                    isShowDialog = isShowDialog
                )
            }
        },
        content = { innerPadding ->
            if (isShowDialog.value)
                TextRecognitionScreen(
                    recognizedText = recognizedText,
                    outlinedBitmap = outlinedBitmap,
                    topPaddingModifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                    viewModel = viewModel,
                    isShowDialog = isShowDialog
                )
            else
                if (searchedCards.itemCount == 0) {
                    RandomCardWithBackground(
                        navController = navController,
                        randomCard = randomCardData,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                } else {
                    SearchedCardsList(
                        navController = navController,
                        items = searchedCards,
                        modifier = Modifier.padding(horizontal = 5.dp),
                        topPaddingModifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
        }
    )
}

@Composable
fun TextRecognitionScreen(
    recognizedText: MutableState<Text?>,
    outlinedBitmap: MutableState<Bitmap>,
    topPaddingModifier: Modifier,
    viewModel: SearchViewModel,
    isShowDialog: MutableState<Boolean>
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = topPaddingModifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 140.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            recognizedText.value?.let {
                Text(
                    text = "Tap the name of the card;",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "It will populate the search bar.",
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
                                    viewModel = viewModel,
                                    isShowDialog = isShowDialog
                                )
                            }
                        }
                    }
            )
        }
    }
}

@Composable
fun SearchScreenTopBar(
    text: String,
    context: Context,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onClearClicked: () -> Unit,
    shouldFocus: Boolean,
    focusRequester: FocusRequester,
    imageUri: MutableState<Uri?>,
    imageBitmap: MutableState<Bitmap?>,
    recognizedText: MutableState<Text?>,
    outlinedBitmap: MutableState<Bitmap>,
    activity: Activity,
    isShowDialog: MutableState<Boolean>
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        if (shouldFocus) LaunchedEffect(Unit) { focusRequester.requestFocus() }
        val takePictureLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                isShowDialog.value = true
                imageBitmap.value =
                    imageUri.value?.let {
                        getCorrectlyOrientedBitmap(
                            context.contentResolver,
                            it
                        )
                    }!!
                runTextRecognition(
                    bitmap = imageBitmap.value!!,
                    recognizedText = recognizedText,
                    resultingOutlinedBitmap = outlinedBitmap,
                )
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
                imageUri.value = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                takePictureLauncher.launch(imageUri.value!!)
            } else {
                Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(80.dp)
                .graphicsLayer {
                    shadowElevation = 50.dp.toPx()
                    shape = RoundedCornerShape(15.dp)
                    clip = true
                }
                .padding(10.dp)
                .background(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                )
                .focusRequester(focusRequester),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Gray,
                unfocusedTextColor = Color.Gray,
                cursorColor = Color.Gray,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Transparent,
                focusedSupportingTextColor = Color.Gray,
                unfocusedSupportingTextColor = Color.Gray
            ),
            value = text,
            singleLine = true,
            onValueChange = { onTextChange(it) },
            textStyle = TextStyle.Default.copy(fontSize = 20.sp),
            placeholder = {
                Text(
                    text = "Card lookup...",
                    color = Color.Gray,
                    style = TextStyle.Default.copy(fontSize = 20.sp)
                )
            },
            trailingIcon = {
                if (text.isNotEmpty())
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onTextChange("")
                            onClearClicked()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Empty Text Icon",
                            tint = Color.Gray
                        )
                    }
                else
                    IconButton(
                        onClick = {
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
                                    imageUri.value =
                                        FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            file
                                        )
                                    takePictureLauncher.launch(imageUri.value!!)
                                }

                                activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
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
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.camera),
                            contentDescription = "Empty Text Icon",
                            tint = Color.Gray
                        )
                    }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
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

fun searchTappedText(
    recognizedText: Text,
    offset: Offset,
    viewModel: SearchViewModel,
    isShowDialog: MutableState<Boolean>
) {
    for (block in recognizedText.textBlocks) {
        for (line in block.lines) {
            if (line.boundingBox != null && line.boundingBox!!.contains(
                    offset.x.toInt(),
                    offset.y.toInt()
                )
            ) {
                viewModel.updateSearchQuery(line.text)
                viewModel.searchCardsByName(line.text)
                isShowDialog.value = false
            }
        }
    }
}

fun drawOutlinesOnBitmap(bitmap: Bitmap, recognizedText: Text): Bitmap {
    val outlinedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(outlinedBitmap)
    val paint = Paint().apply {
        color = Color.Red.toArgb()
        style = Paint.Style.STROKE
        strokeWidth = 5f
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
