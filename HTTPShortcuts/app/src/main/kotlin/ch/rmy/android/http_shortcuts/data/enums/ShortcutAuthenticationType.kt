package ch.rmy.android.http_shortcuts.data.enums

enum class ShortcutAuthenticationType(
    val type: String,
    val usesUsernameAndPassword: Boolean = false,
    val usesToken: Boolean = false,
) {

    NONE("none"),
    BASIC("basic", usesUsernameAndPassword = true),
    DIGEST("digest", usesUsernameAndPassword = true),
    BEARER("bearer", usesToken = true);

    override fun toString() =
        type

    companion object {
        fun parse(type: String?) =
            entries.firstOrNull { it.type == type }
                ?: NONE
    }
}
