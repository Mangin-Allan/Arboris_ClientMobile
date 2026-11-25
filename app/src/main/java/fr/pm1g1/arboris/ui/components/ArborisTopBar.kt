package fr.pm1g1.arboris.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.ui.theme.arborisPageBackgroundColor
import fr.pm1g1.arboris.ui.theme.arborisPageTitleColor

@Composable
fun ArborisTopBar(
    @StringRes titleRes: Int,
    showActionIcon: Boolean = false,
    onActionClick: (() -> Unit)? = null,
    @StringRes actionContentDescriptionRes: Int = R.string.auth_logout_button
) {
    val colorScheme = MaterialTheme.colorScheme

    androidx.compose.foundation.layout.Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(arborisPageBackgroundColor())
                .statusBarsPadding()
                .padding(top = 10.dp)
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 8.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(titleRes),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                color = arborisPageTitleColor(),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            if (showActionIcon && onActionClick != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_logout),
                        contentDescription = stringResource(actionContentDescriptionRes),
                        tint = colorScheme.onBackground.copy(alpha = 0.72f)
                    )
                }
            }
        }

        HorizontalDivider(color = colorScheme.outlineVariant.copy(alpha = 0.45f))
    }
}
