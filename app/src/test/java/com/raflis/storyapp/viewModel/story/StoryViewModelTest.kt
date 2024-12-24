package com.raflis.storyapp.viewModel.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.raflis.storyapp.data.ResultStatus
import com.raflis.storyapp.data.remote.entity.Story
import com.raflis.storyapp.data.remote.repository.StoryRepository
import com.raflis.storyapp.ui.home.StoryAdapter
import com.raflis.storyapp.utils.DataDummy
import com.raflis.storyapp.utils.MainDispatcherRule
import com.raflis.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `when get Stories Should Return Correct Data Size`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStory)

        val expectData = MutableLiveData<ResultStatus<PagingData<Story>>>()
        expectData.value = ResultStatus.Success(data)
        Mockito.`when`(storyRepository.getAllStories()).thenReturn(expectData)

        val storyViewModel = StoryViewModel(storyRepository)

        val actualResult: ResultStatus<PagingData<Story>> =
            storyViewModel.getAllStories().getOrAwaitValue()

        Assert.assertTrue(actualResult is ResultStatus.Success)

        val actualPagingData = (actualResult as ResultStatus.Success).data

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualPagingData)

        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
    }


    @Test
    fun `when get Stories Should Return First Data Correctly`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStory)

        val expectData = MutableLiveData<ResultStatus<PagingData<Story>>>()
        expectData.value = ResultStatus.Success(data)
        Mockito.`when`(storyRepository.getAllStories()).thenReturn(expectData)

        val storyViewModel = StoryViewModel(storyRepository)

        val actualResult: ResultStatus<PagingData<Story>> =
            storyViewModel.getAllStories().getOrAwaitValue()

        Assert.assertTrue(actualResult is ResultStatus.Success)

        val actualPagingData = (actualResult as ResultStatus.Success).data

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualPagingData)

        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when get Stories Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStory)

        val expectData = MutableLiveData<ResultStatus<PagingData<Story>>>()
        expectData.value = ResultStatus.Success(data)
        Mockito.`when`(storyRepository.getAllStories()).thenReturn(expectData)

        val storyViewModel = StoryViewModel(storyRepository)

        val actualResult: ResultStatus<PagingData<Story>> =
            storyViewModel.getAllStories().getOrAwaitValue()

        Assert.assertTrue(actualResult is ResultStatus.Success)

        val actualPagingData = (actualResult as ResultStatus.Success).data

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualPagingData)

        Assert.assertNotNull(differ.snapshot())
    }

    @Test
    fun `when Get Stories Empty Should Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())

        val expectData = MutableLiveData<ResultStatus<PagingData<Story>>>()
        expectData.value = ResultStatus.Success(data)
        Mockito.`when`(storyRepository.getAllStories()).thenReturn(expectData)

        val storyViewModel = StoryViewModel(storyRepository)

        val actualResult: ResultStatus<PagingData<Story>> =
            storyViewModel.getAllStories().getOrAwaitValue()

        Assert.assertTrue(actualResult is ResultStatus.Success)

        val actualPagingData = (actualResult as ResultStatus.Success).data

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualPagingData)

        Assert.assertEquals(0, differ.snapshot().size)
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
    }

    override fun onRemoved(position: Int, count: Int) {
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
    }

}

class StoryPagingSource : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return LoadResult.Page(emptyList(), prevKey = 0, nextKey = 1)
    }

    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }
}