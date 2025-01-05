package com.mvpsales.github.ui.newsdetail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mvpsales.github.api.response.ArticleNewsApiResponse
import com.mvpsales.github.api.response.ArticleSourceNewsApiResponse
import com.mvpsales.github.api.response.formatPublishedDate
import com.mvpsales.github.ui.newslist.NewsContent

@Composable
fun NewsDetailScreen(article: ArticleNewsApiResponse) {
    Column(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth().height(208.dp),
            model = article.urlToImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = BrushPainter(
                Brush.linearGradient(
                    listOf(
                        Color(color = 0xFFFF0000),
                        Color(color = 0xFFDDDDDD),
                    )
                )
            )
        )
        Text(
            article.title,
            modifier = Modifier.padding(all = 8.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            article.description,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            article.content,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            article.author ?: "Unknown author",
            modifier = Modifier
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            "Published at ${article.formatPublishedDate("dd MMM yyyy")}",
            modifier = Modifier
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelMedium
        )
        val uriHandler = LocalUriHandler.current
        Button(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            content = {
                Text("Read more on ${article.source.name}")
            },
            onClick = {
                uriHandler.openUri(article.url)
            }
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun NewsDetailPreview() {
    NewsDetailScreen(
        ArticleNewsApiResponse(
            author = "shrutishekar@gmail.com (Shruti Shekar)",
            title = "Android Central's Best of 2024: Apps and Services",
            description = "Here are all the winners for Best Apps and Services for 2024!",
            url = "https://www.androidcentral.com/apps-software/android-central-best-of-2024-apps-services",
            urlToImage = "https://cdn.mos.cms.futurecdn.net/kWGZ6wr2t9dDGdmZW7pLEP-1200-80.jpg",
            publishedAt = "2025-01-01T13:00:00Z",
            content = "There have been some stellar apps and services that were released this year and I can wholeheartedly agree with every single one of the winners on this list. \r\nI am a bit biased here, but I am a huge… [+4354 chars]",
            source = ArticleSourceNewsApiResponse(
                name = "Android Central",
                id = null
            )
        )
    )
}