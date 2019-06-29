package com.wysiwyg.mountcak.ui.mountgallery

class MountGalleryPresenter(private val view: MountGalleryView) {

    fun getPhotos(photos: List<String?>) {
        view.showPhotos(photos)
    }
}