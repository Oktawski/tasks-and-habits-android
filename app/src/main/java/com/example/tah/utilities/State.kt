package com.example.tah.utilities

data class State(
        val status: Status,
        val message: String?
) {
    enum class Status{
        LOADING,
        SUCCESS,
        REMOVED,
        ADDED,
        UPDATED,
        ERROR
    }

    companion object {
        fun loading(): State{
            return State(Status.LOADING, null)
        }

        fun success(message: String? = null): State{
            return State(Status.SUCCESS, message)
        }

        fun removed(message: String? = null): State{
            return State(Status.REMOVED, message)
        }

        fun added(message: String? = null): State{
            return State(Status.ADDED, message)
        }

        fun updated(message: String? = null): State = State(Status.UPDATED, message)

        fun error(message: String?): State{
            return State(Status.ERROR, message)
        }
    }
}
