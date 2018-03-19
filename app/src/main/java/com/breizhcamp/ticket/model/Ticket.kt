package com.breizhcamp.ticket.model

import com.google.gson.annotations.SerializedName

class Ticket {
    @SerializedName("nom")
    var lastName: String? = null
        internal set

    @SerializedName("prenom")
    var firstName: String? = null
        internal set

    var desk: String? = null
        internal set

    var days: Array<String>? = null
        internal set

    var type: String? = null
        internal set

    var mail: String? = null
        internal set

    @SerializedName("societe")
    var company: String? = null
        internal set

}
