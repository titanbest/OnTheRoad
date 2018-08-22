package com.sergey.ontheroad.utils

import android.content.Context
import com.sergey.data.executor.JobExecutor
import com.sergey.domain.interactor.UseCase
import com.sergey.domain.mapper.OutputMapper
import com.sergey.ontheroad.exception.ErrorMessageFactory
import com.sergey.ontheroad.viewmodel.BaseViewModel
import io.reactivex.Single
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class MappedFlow<
        TParamsType,
        TReturnType,
        TUiModel,
        TMapper : OutputMapper<TReturnType, TUiModel>,
        TUseCase : UseCase<TReturnType, TParamsType>
        > @Inject constructor() : IDisposable {

    @Inject lateinit var useCase: TUseCase
    @Inject lateinit var mapper: TMapper
    @Inject lateinit var context: Context
    @Inject lateinit var uiThread: UIThread
    @Inject lateinit var executor: JobExecutor

    fun run(params: TParamsType, viewModel: BaseViewModel? = null, callback: ((result: TUiModel) -> Unit)? = null) {
        viewModel?.loading?.value = true
        useCase.execute(object : DisposableObserver<TReturnType>() {

            override fun onNext(t: TReturnType) {
                Single.fromCallable {
                    mapper.transformFromDomain(t)
                }.subscribeOn(Schedulers.from(executor))
                        .observeOn(uiThread.scheduler)
                        .subscribe { it: TUiModel ->
                            callback?.invoke(it)
                            viewModel?.loading?.value = false
                        }
            }

            override fun onComplete() {
                viewModel?.error?.value = null
            }

            override fun onError(e: Throwable) {
                viewModel?.loading?.value = false
                viewModel?.error?.value = ErrorMessageFactory.create(context, e)
            }
        }, params)
    }

    override fun dispose() {
        useCase.dispose()
    }

    override fun clear() {
        useCase.clear()
    }
}

class Flow<TParamsType, TReturnType, TUseCase : UseCase<TReturnType, TParamsType>>
@Inject constructor() : MappedFlow<TParamsType, TReturnType, TReturnType, FakeMapper<TReturnType>, TUseCase>()

class FakeMapper<Item>
@Inject constructor() : OutputMapper<Item, Item> {

    override fun transformFromDomain(item: Item): Item {
        return item
    }
}