package com.example.hangmanapp.nasa

data class NasaImagesList(
    val collection: Collection
) {
    data class Collection(
        val href: String,
        val items: List<Item>,
        val metadata: Metadata,
        val version: String
    ) {
        data class Item(
            val `data`: List<Data>?,
            val href: String,
            val links: List<Link>?
        ) {
            data class Data(
                val album: List<String>,
                val center: String,
                val dateCreated: String,
                val description: String,
                val keywords: List<String>,
                val location: String,
                val mediaType: String,
                val nasaId: String,
                val title: String
            )

            data class Link(
                val href: String,
                val rel: String,
                val render: String
            )
        }

        data class Metadata(
            val totalHits: Int
        )
    }
}