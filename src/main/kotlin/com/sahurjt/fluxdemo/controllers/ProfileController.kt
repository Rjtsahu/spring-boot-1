package com.sahurjt.fluxdemo.controllers

import com.sahurjt.fluxdemo.services.CombinedProfileResponse
import com.sahurjt.fluxdemo.services.ProfileResponse
import com.sahurjt.fluxdemo.services.ProfileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/profile")
class ProfileController(val service: ProfileService) {

    @GetMapping("/{id}")
    fun getProfile(@PathVariable("id") id: Int): Mono<ProfileResponse> {
        return service.getProfile(id)
    }


    @GetMapping("/{id}/combined")
    fun getCombinedProfile(@PathVariable("id") id: Int): Mono<CombinedProfileResponse> {
        return service.getWithContact(id)
    }
}

