package com.sahurjt.fluxdemo.repository

import reactor.core.publisher.Flux

class InMemoryRepo {
    companion object {
        val Users: List<User> =
            listOf(
                User(1, "Lula", 24),
                User(2, "Franz", 28),
                User(3, "Aubrey", 30),
                User(4, "Barb", 21),
                User(5, "Andris", 29),
                User(6, "Mario", 24),
                User(7, "Cinda", 31),
                User(8, "Gilberto", 24),
                User(9, "Greg", 25),
                User(9, "Shane", 28),
            )

        val Contacts: Flux<UserContact> = Flux.fromIterable(
            listOf(
                UserContact(1, "+91-123456789", "user1@gmail.com"),
                UserContact(2, "+91-223456789", "user2@gmail.com"),
                UserContact(4, "+91-323456789", "user4@gmail.com"),
                UserContact(5, "+91-423456789", "user5@gmail.com"),
                UserContact(7, "+91-523456789", "user7@gmail.com"),
            )
        )
    }


}