package com.example.cardbinder.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun SearchWidget(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

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
                ),
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
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onTextChange("")
                    }
                ) {
                    if (text.isNotEmpty())
                        Icon(
                            imageVector = Icons.Default.Close,
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

@Composable
@Preview
fun SearchWidgetPreview() {
    SearchWidget(
        text = "Search",
        onTextChange = {},
        onSearchClicked = {},
        onCloseClicked = {}
    )
}