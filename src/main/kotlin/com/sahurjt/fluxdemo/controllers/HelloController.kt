package com.sahurjt.fluxdemo.controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.ParallelFlux
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux
import java.time.Instant

@RestController
@RequestMapping("/hello")
class HelloController {
    val log: Logger = LoggerFactory.getLogger(HelloController::class.java)

    @GetMapping("/")
    fun getMessage(@RequestParam("name", required = false) name: String?): Mono<Any> {

        log.info("Invoked hello from $name")

        return Mono.just(BasicMessage("ok", "Hello ${name}."))
    }

    val dataList = (1..500000).toList()


    @GetMapping("/test", produces = ["application/json"])
    fun test(): ParallelFlux<Int> {
        val start = Instant.now().nano
        val res = Flux.from(dataList.stream().toFlux()).parallel(4)
            .runOn(Schedulers.boundedElastic())
            .map { it * 13 }
        val end = Instant.now().nano
        log.info("Time start: $start end: $end")
        log.info("Time spend ${(end - start) / 1000}")
        return res
    }

    @GetMapping("/test1", produces = ["application/json"])
    fun test1(): Flux<Int> {
        val start = Instant.now().nano
        val res = Flux.from(dataList.stream().toFlux()).map { it * 13 }
        val end = Instant.now().nano
        log.info("Time start: $start end: $end")
        log.info("Time spend ${(end - start) / 1000}")
        return res
    }

    fun test0() {
        val data = Mono.just("Data")
        data.flatMap { Mono.just(it.toInt()) }
        ParallelFlux.from(Flux.fromArray(arrayOf(1, 2, 3, 4)))
    }

    @PostMapping("/save",  consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun saveData( body : SaveRequest){
        // all code logic...
        log.info("Data received : ${body.name}  ${body.email}")
        Mono.create<String> { getStr() }.subscribe { println(it) }
    }

    fun getStr(): String{
        return "A string"
    }
}

data class SaveRequest(val name: String, val email: String)

data class BasicMessage(val status: String, val message: String)