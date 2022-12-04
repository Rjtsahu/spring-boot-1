package com.sahurjt.fluxdemo.services

import com.sahurjt.fluxdemo.repository.User
import com.sahurjt.fluxdemo.repository.UserContactClient
import com.sahurjt.fluxdemo.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import reactor.core.publisher.ParallelFlux
import reactor.core.scheduler.Schedulers
import java.time.Duration

data class ProfileResponse(val id: Int, val name: String, val age: Int)

data class ContactResponse(val phone: String, val email: String)

data class CombinedProfileResponse(val profileResponse: ProfileResponse?, val contactResponse: ContactResponse?)

@Service
class ProfileService(val userRepo: UserRepository, val contactClient: UserContactClient) {

    private val logger: Logger = LoggerFactory.getLogger(ProfileService::class.java)

    fun getProfile(id: Int): Mono<ProfileResponse> {
        return userRepo.findById(id).flatMap { user: User ->
            Mono.just(ProfileResponse(user.id, user.name, user.age))
        }
    }

    fun zipWithContact(id: Int): Mono<CombinedProfileResponse> {
        return userRepo.findById(id)
            .flatMap { Mono.just(ProfileResponse(it.id, it.name, it.age)) }
            .zipWith(
                contactClient.fetchById(id)
                    .flatMap { contact -> Mono.just(ContactResponse(contact.phone, contact.email)) })
            .flatMap { Mono.just(CombinedProfileResponse(it.t1, it.t2)) }
    }

    fun zipOptionalContact(id: Int): Mono<CombinedProfileResponse> {
        return userRepo.findById(id)
            .flatMap { Mono.just(ProfileResponse(it.id, it.name, it.age)) }
            .zipWith(
                contactClient.fetchById(id)
                    .flatMap { contact -> Mono.just(ContactResponse(contact.phone, contact.email)) }
                    .defaultIfEmpty(ContactResponse("", ""))
            ).flatMap {
                Mono.just(CombinedProfileResponse(it.t1, it.t2))
            }
    }

    fun zipOptionalContact1(id: Int): Mono<CombinedProfileResponse> {
        val profile = userRepo.findById(id).flatMap { Mono.just(ProfileResponse(it.id, it.name, it.age)) }
        val contact = contactClient.fetchById(id).flatMap { Mono.just(ContactResponse(it.phone, it.email)) }
            .defaultIfEmpty(ContactResponse("", ""))

        return Mono.zip(profile, contact).flatMap { Mono.just(CombinedProfileResponse(it.t1, it.t2)) }
    }

    fun getAllProfile(): Flux<ProfileResponse> {
        return Flux.from(userRepo.findAll()).flatMap { user: User ->
            Mono.just(ProfileResponse(user.id, user.name, user.age))
        }
            .delayElements(Duration.ofMillis(100))
            .retry(2)
    }

    fun getCombinedProfiles(): ParallelFlux<CombinedProfileResponse> {
        return userRepo.findAll()
            .flatMap { Mono.just(ProfileResponse(it.id, it.name, it.age)) }
            .flatMap { profile ->
                contactClient.fetchById(profile.id)
                    .flatMap { contact -> Mono.just(ContactResponse(contact.phone, contact.email)) }
                    .defaultIfEmpty(ContactResponse("", ""))
                    .flatMap { contact -> Mono.just(CombinedProfileResponse(profile, contact)) }
            }.parallel(4).runOn(Schedulers.boundedElastic())
    }

    /*
    * Just a mock function which represents another client to fetch contact details
    * */
    private fun fetchContactFromSecondarySource(): Mono<ContactResponse> {
        return Mono.just(ContactResponse("dummy", "dummy"))
    }

    fun getCombinedProfilesWithFallback(): ParallelFlux<CombinedProfileResponse> {
        return userRepo.findAll()
            .flatMap { Mono.just(ProfileResponse(it.id, it.name, it.age)) }
            .flatMap { profile ->
                contactClient.fetchById(profile.id)
                    .flatMap { contact -> Mono.just(ContactResponse(contact.phone, contact.email)) }
                    .switchIfEmpty(fetchContactFromSecondarySource())
                    .onErrorContinue { _, _ -> fetchContactFromSecondarySource() }
                    .flatMap { contact -> Mono.just(CombinedProfileResponse(profile, contact)) }
            }.parallel(4).runOn(Schedulers.boundedElastic())
    }

    fun getAllWithMultipleSubscriber(): Flux<ProfileResponse> {
        val profiles = userRepo.findAll()
            .cache(Duration.ofMinutes(1))

        profiles.subscribe { logConsumer(it) }

        return Flux.create { fluxSink: FluxSink<ProfileResponse> ->
            profiles.subscribe(
                { data -> fluxSink.next(ProfileResponse(data.id, data.name, data.age)) },
                { err -> fluxSink.error(err) },
                { fluxSink.complete() }
            )
        }
    }

    private fun logConsumer(user: User) {
        logger.info("Received data: ${user.id}-${user.name}")
    }
}