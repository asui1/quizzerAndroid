package com.asu1.quizzer

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.asu1.quizzer.model.VersionResponse
import com.asu1.quizzer.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import android.content.pm.PackageInfo
import com.asu1.quizzer.viewModels.MainViewModel
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var connectivityManager: ConnectivityManager

    @Mock
    private lateinit var networkCapabilities: NetworkCapabilities

    @Mock
    private lateinit var observer: Observer<Boolean>

    private lateinit var mainViewModel: MainViewModel

    @ExperimentalCoroutinesApi
    private val testDispatcher = StandardTestDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        whenever(application.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        mainViewModel = MainViewModel(application)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun checkInternetConnection_whenConnected_shouldPostTrue() {
        whenever(connectivityManager.activeNetwork).thenReturn(mock())
        whenever(connectivityManager.getNetworkCapabilities(any())).thenReturn(networkCapabilities)
        whenever(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)).thenReturn(true)

        mainViewModel.isInternetAvailable.observeForever(observer)
        mainViewModel.updateInternetConnection()

        verify(observer).onChanged(true)
    }

    @Test
    fun checkInternetConnection_whenNotConnected_shouldPostFalse() {
        whenever(connectivityManager.activeNetwork).thenReturn(null)

        mainViewModel.isInternetAvailable.observeForever(observer)
        mainViewModel.updateInternetConnection()

        verify(observer).onChanged(false)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkForUpdates_whenNewVersionAvailable_shouldPostTrue() = runTest {
        val versionResponse = VersionResponse("1.1.0")
        val response = Response.success(versionResponse)
        whenever(RetrofitInstance.api.getVersion()).thenReturn(response)

        val packageInfo = mock<PackageInfo> {
            on { versionName } doReturn "1.0.0"
        }
        whenever(application.packageManager.getPackageInfo(anyString(), anyInt())).thenReturn(packageInfo)

        mainViewModel.isUpdateAvailable.observeForever(observer)
        mainViewModel.updateIsUpdateAvailable()

        advanceUntilIdle()
        verify(observer).onChanged(true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkForUpdates_whenNoNewVersionAvailable_shouldPostFalse() = runTest {
        val versionResponse = VersionResponse("1.0.0")
        val response = Response.success(versionResponse)
        whenever(RetrofitInstance.api.getVersion()).thenReturn(response)

        val packageInfo = mock<PackageInfo> {
            on { versionName } doReturn "1.0.0"
        }
        whenever(application.packageManager.getPackageInfo(anyString(), anyInt())).thenReturn(packageInfo)

        mainViewModel.isUpdateAvailable.observeForever(observer)
        mainViewModel.updateIsUpdateAvailable()

        advanceUntilIdle()
        verify(observer).onChanged(false)
    }

    @Test
    fun compareVersions_whenFirstVersionIsGreater_shouldReturnTrue() {
        val result = mainViewModel.isUpdateNeeded("1.1.0", "1.0.0")
        Assert.assertTrue(result)
    }

    @Test
    fun compareVersions_whenFirstVersionIsLesser_shouldReturnFalse() {
        val result = mainViewModel.isUpdateNeeded("1.0.0", "1.1.0")
        Assert.assertFalse(result)
    }

    @Test
    fun compareVersions_whenVersionsAreEqual_shouldReturnFalse() {
        val result = mainViewModel.isUpdateNeeded("1.0.0", "1.0.0")
        Assert.assertFalse(result)
    }
}