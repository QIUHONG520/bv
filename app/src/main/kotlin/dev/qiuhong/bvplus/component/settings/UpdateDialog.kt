package dev.qiuhong.bvplus.component.settings

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import androidx.tv.material3.Text
import dev.qiuhong.bvplus.BuildConfig
import dev.qiuhong.bvplus.network.AppCenterApi
import dev.qiuhong.bvplus.network.PackageInfo
import io.ktor.client.content.ProgressListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

private const val OWNER_NAME = "aaa1115910-gmail.com"
private const val APP_NAME = "bv"
private const val GROUP_NAME = "public"
private const val GROUP_ID = "9259f371-d475-4088-b9fe-e5adfac1b563"

@Composable
fun UpdateDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    onHideDialog: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var updateStatus by remember { mutableStateOf(UpdateStatus.UpdatingInfo) }

    var bytesSentTotal: Long by remember { mutableStateOf(0) }
    var contentLength: Long by remember { mutableStateOf(0) }
    var targetProgress by remember { mutableStateOf(0f) }
    val progress by animateFloatAsState(targetValue = targetProgress)

    var latestPackageId by remember { mutableStateOf(0) }
    var packageInfo: PackageInfo? by remember { mutableStateOf(null) }

    val checkUpdate: () -> Unit = {
        updateStatus = UpdateStatus.UpdatingInfo

        scope.launch(Dispatchers.Default) {
            runCatching {

                val availablePackageList = AppCenterApi.getPackageList(
                    ownerName = OWNER_NAME,
                    appName = APP_NAME,
                    distributionGroupName = GROUP_NAME
                )
                val latestPackage = availablePackageList.first()
                if (latestPackage.version.toInt() <= BuildConfig.VERSION_CODE) {
                    updateStatus = UpdateStatus.NoAvailableUpdate
                    return@launch
                } else {
                    latestPackageId = latestPackage.id
                }

                packageInfo = AppCenterApi.getPackageInfo(
                    ownerName = OWNER_NAME,
                    appName = APP_NAME,
                    distributionGroupName = GROUP_NAME,
                    releaseId = latestPackageId
                )
            }.onFailure {
                updateStatus = UpdateStatus.CheckError
            }.onSuccess {
                updateStatus = UpdateStatus.Ready
            }
        }
    }

    val sendInstallAnalytics: () -> Unit = {
        scope.launch(Dispatchers.Default) {
            runCatching {
                AppCenterApi.sendInstallAnalytics(
                    ownerName = OWNER_NAME,
                    appName = APP_NAME,
                    distributionGroupId = GROUP_ID,
                    releaseId = latestPackageId
                )
            }
        }
    }

    val installUpdate: (File) -> Unit = { file ->
        updateStatus = UpdateStatus.Installing
        runCatching {
            val uri =
                FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                setDataAndType(uri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        }.onFailure {
            updateStatus = UpdateStatus.InstallError
        }
    }

    val startUpdate: () -> Unit = {
        updateStatus = UpdateStatus.Downloading
        scope.launch(Dispatchers.IO) {
            val tempFilename = "${UUID.randomUUID()}.apk"
            val tempFile = File(context.cacheDir, tempFilename)
            tempFile.createNewFile()
            runCatching {
                AppCenterApi.downloadFile(
                    packageInfo!!.downloadUrl,
                    tempFile,
                    object : ProgressListener {
                        override suspend fun invoke(downloaded: Long, total: Long) {
                            bytesSentTotal = downloaded
                            contentLength = total
                            targetProgress =
                                runCatching { bytesSentTotal.toFloat() / contentLength }
                                    .getOrDefault(0f)
                        }
                    })
                if (!BuildConfig.DEBUG) sendInstallAnalytics()
                if (show) installUpdate(tempFile)
            }.onFailure {
                updateStatus = UpdateStatus.DownloadError
            }
        }
    }

    LaunchedEffect(Unit) {
        checkUpdate()
    }

    LaunchedEffect(show) {
        if (show) {
            checkUpdate()
        } else {
            updateStatus = UpdateStatus.UpdatingInfo
        }
    }

    if (show) {
        AlertDialog(
            modifier = modifier
                .width(400.dp)
                .animateContentSize(),
            onDismissRequest = { onHideDialog() },
            title = {
                Text(
                    text = when (updateStatus) {
                        UpdateStatus.UpdatingInfo -> "获取更新信息中"
                        UpdateStatus.Ready -> packageInfo!!.shortVersion
                        UpdateStatus.Downloading -> "下载中"
                        UpdateStatus.Installing -> "安装中"
                        UpdateStatus.NoAvailableUpdate -> "无可用更新"
                        UpdateStatus.CheckError -> "检查更新失败"
                        UpdateStatus.DownloadError -> "下载失败"
                        UpdateStatus.InstallError -> "安装失败"
                    }
                )
            },
            text = {
                when (updateStatus) {
                    UpdateStatus.UpdatingInfo -> {
                        Text(text = "检查更新中...")
                    }

                    UpdateStatus.Ready -> {
                        Text(text = packageInfo?.releaseNotes ?: "")
                    }

                    UpdateStatus.Downloading -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = progress,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(text = "$bytesSentTotal/$contentLength")
                            }
                        }
                    }

                    UpdateStatus.Installing -> {
                        Text(text = "请坐和放宽")
                    }

                    UpdateStatus.DownloadError -> {
                        Text(text = "下载失败")
                    }

                    UpdateStatus.InstallError -> {
                        Text(text = "安装失败")
                    }

                    UpdateStatus.CheckError -> {
                        Text(text = "获取更新信息失败")
                    }

                    UpdateStatus.NoAvailableUpdate -> {
                        Text(text = "无可用更新")
                    }
                }
            },
            confirmButton = {
                when (updateStatus) {
                    UpdateStatus.UpdatingInfo, UpdateStatus.NoAvailableUpdate, UpdateStatus.Downloading, UpdateStatus.Installing -> {}

                    UpdateStatus.Ready -> {
                        TextButton(onClick = startUpdate) {
                            Text(text = "立即更新")
                        }
                    }

                    UpdateStatus.InstallError, UpdateStatus.DownloadError, UpdateStatus.CheckError -> {
                        TextButton(onClick = checkUpdate) {
                            Text(text = "再试一次")
                        }
                    }
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !(updateStatus == UpdateStatus.Downloading || updateStatus == UpdateStatus.Installing),
                    onClick = { onHideDialog() }
                ) {
                    Text(
                        text = when (updateStatus) {
                            UpdateStatus.UpdatingInfo -> "我点错了"
                            UpdateStatus.Ready -> "打死不更"
                            UpdateStatus.NoAvailableUpdate -> "走了走了"
                            UpdateStatus.CheckError, UpdateStatus.DownloadError, UpdateStatus.InstallError -> "算了算了"
                            UpdateStatus.Downloading, UpdateStatus.Installing -> "你已经无路可逃！"
                        }
                    )
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }
}

enum class UpdateStatus {
    UpdatingInfo, Ready, Downloading, Installing,
    NoAvailableUpdate, CheckError, DownloadError, InstallError
}