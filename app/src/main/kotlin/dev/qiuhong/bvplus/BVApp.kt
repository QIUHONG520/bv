package dev.qiuhong.bvplus

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import de.schnettler.datastore.manager.DataStoreManager
import dev.qiuhong.bvplus.dao.AppDatabase
import dev.qiuhong.bvplus.repository.UserRepository
import dev.qiuhong.bvplus.repository.VideoInfoRepository
import dev.qiuhong.bvplus.viewmodel.LoginViewModel
import dev.qiuhong.bvplus.viewmodel.PlayerViewModel
import dev.qiuhong.bvplus.viewmodel.TagViewModel
import dev.qiuhong.bvplus.viewmodel.search.SearchResultViewModel
import dev.qiuhong.bvplus.viewmodel.UserViewModel
import dev.qiuhong.bvplus.viewmodel.home.AnimeViewModel
import dev.qiuhong.bvplus.viewmodel.home.DynamicViewModel
import dev.qiuhong.bvplus.viewmodel.home.PopularViewModel
import dev.qiuhong.bvplus.viewmodel.home.RcmdViewModel
import dev.qiuhong.bvplus.viewmodel.search.SearchInputViewModel
import dev.qiuhong.bvplus.viewmodel.user.FavoriteViewModel
import dev.qiuhong.bvplus.viewmodel.user.FollowViewModel
import dev.qiuhong.bvplus.viewmodel.user.FollowingSeasonViewModel
import dev.qiuhong.bvplus.viewmodel.user.HistoryViewModel
import dev.qiuhong.bvplus.viewmodel.user.UpInfoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class BVApp : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var dataStoreManager: DataStoreManager
        lateinit var koinApplication: KoinApplication
        lateinit var firebaseAnalytics: FirebaseAnalytics

        fun getAppDatabase(context: Context = this.context) = AppDatabase.getDatabase(context)
    }

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        dataStoreManager = DataStoreManager(applicationContext.dataStore)
        koinApplication = startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@BVApp)
            modules(appModule)
        }
        firebaseAnalytics = Firebase.analytics
    }
}

val appModule = module {
    single { UserRepository() }
    single { VideoInfoRepository() }
    viewModel { DynamicViewModel(get()) }
    viewModel { RcmdViewModel() }
    viewModel { PopularViewModel() }
    viewModel { LoginViewModel(get()) }
    viewModel { PlayerViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { FavoriteViewModel() }
    viewModel { UpInfoViewModel() }
    viewModel { FollowViewModel() }
    viewModel { SearchInputViewModel() }
    viewModel { SearchResultViewModel() }
    viewModel { AnimeViewModel() }
    viewModel { FollowingSeasonViewModel() }
    viewModel { TagViewModel() }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")