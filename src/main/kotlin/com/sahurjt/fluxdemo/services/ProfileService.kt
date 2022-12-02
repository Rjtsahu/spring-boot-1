package com.sahurjt.fluxdemo.services

import com.sahurjt.fluxdemo.repository.User
import com.sahurjt.fluxdemo.repository.UserContact
import com.sahurjt.fluxdemo.repository.UserContactClient
import com.sahurjt.fluxdemo.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

data class ProfileResponse(val id: Int, val name: String, val age: Int);

data class ContactResponse(val phone: String, val email: String)

data class CombinedProfileResponse(val profileResponse: ProfileResponse, val contactResponse: ContactResponse)

@Service
class ProfileService(val userRepo: UserRepository, val contactClient: UserContactClient) {

    fun getProfile(id: Int): Mono<ProfileResponse> {
        return userRepo.findById(id).flatMap { user: User ->
            Mono.just(ProfileResponse(user.id, user.name, user.age))
        }
    }


    fun getAllProfile(id: Int): Mono<ProfileResponse> {
        return userRepo.findById(id).flatMap { user: User ->
            Mono.just(ProfileResponse(user.id, user.name, user.age))
        }
    }

    fun getWithContact(id: Int): Mono<CombinedProfileResponse> {
        return contactClient.fetchById(id)
    }


    fun getAllWithContact(id: Int): Mono<UserContact> {
        return contactClient.fetchById(id)
    }
}