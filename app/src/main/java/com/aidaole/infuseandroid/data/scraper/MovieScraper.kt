package com.aidaole.infuseandroid.data.scraper

import com.aidaole.infuseandroid.data.api.TmdbApi
import com.aidaole.infuseandroid.data.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieScraper @Inject constructor(
    private val tmdbApi: TmdbApi
) {
    suspend fun scrapeMovie(file: File): Movie? = withContext(Dispatchers.IO) {
        try {
            // 从文件名中提取可能的电影名称
            val fileName = file.nameWithoutExtension
            val cleanFileName = cleanFileName(fileName)
            
            // 搜索电影
            val searchResult = tmdbApi.searchMovies(cleanFileName)
            
            // 获取第一个匹配结果
            searchResult.results.firstOrNull()?.let { movie ->
                // 获取详细信息
                tmdbApi.getMovieDetails(movie.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun cleanFileName(fileName: String): String {
        // 移除常见的质量标记和年份
        return fileName
            .replace(Regex("\\[.*?\\]"), "") // 移除方括号内容
            .replace(Regex("\\(.*?\\)"), "") // 移除圆括号内容
            .replace(Regex("\\d{4}"), "") // 移除年份
            .replace(Regex("[._\\-]"), " ") // 替换分隔符为空格
            .replace(Regex("\\s+"), " ") // 合并多个空格
            .trim()
    }
} 