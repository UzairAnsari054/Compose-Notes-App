package com.example.composenotesapp1.components.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composenotesapp1.R
import com.example.composenotesapp1.commons.TestTags

@Composable
fun TextDialog(
    modifier: Modifier = Modifier,
    isOpened: Boolean,
    onDismissCallBack: () -> Unit,
    onConfirmCallBack: (() -> Unit)? = null,
    title: String = stringResource(id = R.string.warning_title),
    description: String = stringResource(id = R.string.delete_confirmation_text)
) {
    if (isOpened) {
        AlertDialog(
            onDismissRequest = { onDismissCallBack() },
            title = {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                if (onConfirmCallBack != null) {
                    TextButton(
                        onClick = { onConfirmCallBack() },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.semantics { testTag = TestTags.CONFIRM_BUTTON }
                    ) {
                        Text(
                            text = stringResource(id = R.string.confirm),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissCallBack() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.semantics { testTag = TestTags.DISMISS_BUTTON }
                ) {
                    Text(
                        text = stringResource(id = R.string.dismiss),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
        )
    }
}