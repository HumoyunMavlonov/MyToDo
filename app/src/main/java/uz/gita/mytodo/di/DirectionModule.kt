package uz.gita.mytodo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.mytodo.presentation.screen.add.AddDirection
import uz.gita.mytodo.presentation.screen.add.AddDirectionImpl
import uz.gita.mytodo.presentation.screen.home.HomeDirection
import uz.gita.mytodo.presentation.screen.home.HomeDirectionImpl

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionModule {

    @Binds
    fun bindHomeDirection(impl: HomeDirectionImpl): HomeDirection

    @Binds
    fun bindAddDirection(impl: AddDirectionImpl): AddDirection

}