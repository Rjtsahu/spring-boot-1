package com.sahurjt.fluxdemo.controllers

import com.sahurjt.fluxdemo.services.CombinedProfileResponse
import com.sahurjt.fluxdemo.services.ProfileResponse
import com.sahurjt.fluxdemo.services.ProfileService
import org.springframework.http.MediaType.*
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.ParallelFlux


@RestController
@RequestMapping("/profile")
class ProfileController(val service: ProfileService) {


    /**
     * Fetches Profile of user (very basic example)
     */
    @GetMapping("/{id}")
    fun getProfile(@PathVariable("id") id: Int): Mono<ProfileResponse> {
        return service.getProfile(id)
    }


    /**
     * Fetches Profile combined with user contact info
     */
    @GetMapping("/{id}/combined")
    fun getCombinedProfile1(@PathVariable("id") id: Int): Mono<CombinedProfileResponse> {
        return service.zipWithContact(id)
    }


    /**
     * Fetches Profile combined with user contact info (optional with default values)
     */
    @GetMapping("/{id}/combined-optional")
    fun getCombinedProfile2(@PathVariable("id") id: Int): Mono<CombinedProfileResponse> {
        return service.zipOptionalContact1(id)
    }

    /**
     * Fetches all Profiles
     */
    @GetMapping("/list/example1")
    fun getUsers(): Flux<ProfileResponse> = service.getAllProfile()

    /**
     * Fetches all combined Profiles with contact defaulting
     */

    @GetMapping("/list/combined", produces = [TEXT_EVENT_STREAM_VALUE, APPLICATION_JSON_VALUE])
    fun getCombinedProfiles(): ParallelFlux<CombinedProfileResponse> = service.getCombinedProfiles()

    /**
     * Fetches all combined profile with Parallel flux and delay
     */
    @GetMapping("/list/combined2", produces = [TEXT_EVENT_STREAM_VALUE, APPLICATION_JSON_VALUE])
    fun getCombinedProfiles2(): ParallelFlux<CombinedProfileResponse> =
        service.getCombinedProfilesWithFallback()

    // curl -v -H "Accept: text/event-stream" http://localhost:8080/profile/list/example2
    /**
     * Fetches all Profiles, with multiple subscriber and delay differences, with FluxSink Example
     */
    @GetMapping("/list/example2", produces = [TEXT_EVENT_STREAM_VALUE, APPLICATION_JSON_VALUE])
    fun getUsers2(): Flux<ProfileResponse> = service.getAllWithMultipleSubscriber()

}

