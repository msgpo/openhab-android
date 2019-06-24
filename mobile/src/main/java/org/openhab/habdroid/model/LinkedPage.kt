/*
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.habdroid.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import org.openhab.habdroid.util.forEach
import org.w3c.dom.Node

/**
 * This is a class to hold information about openHAB linked page.
 */

@Parcelize
data class LinkedPage(
    val id: String,
    val title: String,
    val icon: String?,
    val iconPath: String,
    val link: String
) : Parcelable {
    companion object {
        internal fun build(
            id: String,
            title: String?,
            icon: String?,
            iconPath: String,
            link: String
        ): LinkedPage {
            val actualTitle = if (title != null && title.indexOf('[') > 0)
                title.substring(0, title.indexOf('[')) else title
            return LinkedPage(id, actualTitle.orEmpty(), icon, iconPath, link)
        }
    }
}

fun Node.toLinkedPage(): LinkedPage? {
    var id: String? = null
    var title: String? = null
    var icon: String? = null
    var link: String? = null

    childNodes.forEach { node ->
        when (node.nodeName) {
            "id" -> id = node.textContent
            "title" -> title = node.textContent
            "icon" -> icon = node.textContent
            "link" -> link = node.textContent
        }
    }

    val finalId = id ?: return null
    val finalLink = link ?: return null
    return LinkedPage.build(finalId, title, icon, "images/$icon.png", finalLink)
}

fun JSONObject?.toLinkedPage(): LinkedPage? {
    if (this == null) {
        return null
    }
    val icon = optString("icon", null)
    return LinkedPage.build(
        getString("id"),
        optString("title", null),
        icon,
        "icon/$icon",
        getString("link"))
}