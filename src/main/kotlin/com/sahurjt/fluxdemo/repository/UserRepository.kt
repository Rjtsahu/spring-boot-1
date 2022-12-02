package com.sahurjt.fluxdemo.repository

import com.sahurjt.fluxdemo.repository.InMemoryRepo.Companion.Users
import org.reactivestreams.Publisher
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

data class User(val id: Int, val name: String, val age: Int)

@Repository
class UserRepository : ReactiveCrudRepository<User, Int> {
    override fun findAll(): Flux<User> {
        return Flux.fromIterable(Users)
    }

    override fun findById(id: Int): Mono<User> {
        return Users.toFlux().filter { user: User -> user.id == id }.toMono()
    }

    /// NOT IMPLEMENTED
    override fun <S : User?> save(entity: S): Mono<S> {
        TODO("Not yet implemented")
    }

    override fun <S : User?> saveAll(entities: MutableIterable<S>): Flux<S> {
        TODO("Not yet implemented")
    }

    override fun <S : User?> saveAll(entityStream: Publisher<S>): Flux<S> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Publisher<Int>): Mono<User> {
        TODO("Not yet implemented")
    }

    override fun existsById(id: Int): Mono<Boolean> {
        TODO("Not yet implemented")
    }

    override fun existsById(id: Publisher<Int>): Mono<Boolean> {
        TODO("Not yet implemented")
    }


    override fun count(): Mono<Long> {
        TODO("Not yet implemented")
    }

    override fun deleteAll(): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entityStream: Publisher<out User>): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entities: MutableIterable<User>): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteAllById(ids: MutableIterable<Int>): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun delete(entity: User): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Publisher<Int>): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Int): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun findAllById(idStream: Publisher<Int>): Flux<User> {
        TODO("Not yet implemented")
    }

    override fun findAllById(ids: MutableIterable<Int>): Flux<User> {
        TODO("Not yet implemented")
    }

}