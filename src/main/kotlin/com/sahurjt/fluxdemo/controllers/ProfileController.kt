package com.sahurjt.fluxdemo.controllers

import com.sahurjt.fluxdemo.services.CombinedProfileResponse
import com.sahurjt.fluxdemo.services.ProfileResponse
import com.sahurjt.fluxdemo.services.ProfileService
import org.springframework.http.MediaType.*
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.ParallelFlux
import java.time.Duration


@RestController
@RequestMapping("/profile")
class ProfileController(val service: ProfileService) {

    @GetMapping("/{id}")
    fun getProfile(@PathVariable("id") id: Int): Mono<ProfileResponse> {
        return service.getProfile(id)
    }

    @GetMapping("/{id}/combined")
    fun getCombinedProfile1(@PathVariable("id") id: Int): Mono<CombinedProfileResponse> {
        return service.zipWithContact(id)
    }

    @GetMapping("/{id}/combined-optional")
    fun getCombinedProfile2(@PathVariable("id") id: Int): Mono<CombinedProfileResponse> {
        return service.zipOptionalContact1(id)
    }

    @GetMapping("/list/example1")
    fun getUsers(): Flux<ProfileResponse> = service.getAllProfile()

    // curl -v -H "Accept: text/event-stream" http://172.23.160.1:8080/profile/list/example2
    @CrossOrigin
    @GetMapping("/list/example2", produces = [TEXT_EVENT_STREAM_VALUE, APPLICATION_JSON_VALUE])
    fun getUsers2(): Flux<ProfileResponse> = service.getAllWithMultipleSubscriber().delayElements(Duration.ofSeconds(2))

    @GetMapping("/list/combined", produces = [TEXT_EVENT_STREAM_VALUE, APPLICATION_JSON_VALUE])
    fun getCombinedProfiles(): ParallelFlux<CombinedProfileResponse> = service.getCombinedProfiles()

    @GetMapping("/list/combined2", produces = [TEXT_EVENT_STREAM_VALUE, APPLICATION_JSON_VALUE])
    fun getCombinedProfiles2(): ParallelFlux<CombinedProfileResponse> = service.getCombinedProfilesWithFallback()

}

