package com.breizhcamp.ticket.model

import com.google.gson.annotations.SerializedName

data class Ticket (
    @SerializedName("nom")
    val lastName: String?,

    @SerializedName("prenom")
    val firstName: String? = null,

    val desk: String?,

    val days: Array<String>?,

    val type: String?,

    val mail: String?,

    @SerializedName("societe")
    val company: String?,

    val checkin: String?,

    @SerializedName("id")
    val identifier: String? = null
)
