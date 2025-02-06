package com.example.myfisloanlibone

import android.content.Context
import android.content.Intent
import android.widget.Toast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

object LoanLib {
    fun launchFirstScreen(context: Context) {
        Toast.makeText(context, "Launching First Screen", Toast.LENGTH_SHORT).show()

        val intent = Intent(context, FirstScreenActivity::class.java)
        context.startActivity(intent)
    }
    @Composable
    fun ImagePreview(
        image: Painter = painterResource(id = R.drawable.image1),
        modifier: Modifier = Modifier,
        description: String = "Testing",
        contentDescription: String = "",
        onImageClick: () -> Unit = {}
    ) {
        Box(
            modifier = modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(10.dp))
                .shadow(15.dp, RoundedCornerShape(15.dp))
                .clickable { onImageClick() }
        ) {
            Image(
                painter = image,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black
                            )
                        )
                    )
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
        }
    }
}