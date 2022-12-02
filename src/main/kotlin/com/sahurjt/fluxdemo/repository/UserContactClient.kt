package com.sahurjt.fluxdemo.repository

import com.sahurjt.fluxdemo.repository.InMemoryRepo.Companion.Contacts
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

data class UserContact(val id: Int, val phone: String, val email: String)

@Service
class UserContactClient {

    fun fetchById(id: Int): Mono<UserContact> {
        return Contacts.filter { it.id == id }.toMono()
    }

    fun fetchAll(): Flux<UserContact> {
        return Contacts
    }
}