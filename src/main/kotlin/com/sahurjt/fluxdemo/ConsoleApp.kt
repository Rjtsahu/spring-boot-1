package com.sahurjt.fluxdemo

import reactor.core.publisher.Flux
import java.time.Duration
import java.util.stream.IntStream
import kotlin.random.Random
import kotlin.streams.toList

fun main() {
//    fluxWithCreate()
    fluxDeferred()
//    fluxWithGenerate()
}

fun fluxWithGenerate() {
    val flux = Flux.generate<Int> { syncSink ->
        IntStream.range(1, 50).forEach {
            syncSink.next(Random.nextInt(1, 100))
        }
    }
    flux.delayElements(Duration.ofMillis(10)).subscribe { println("First subscription: $it") }
    flux.delayElements(Duration.ofMillis(20)).subscribe { println("Second subscription: $it") }

    println("blocked ${flux.blockLast()}")
}

fun fluxDeferred() {
    val flux = Flux.defer {
        Flux.fromIterable(IntStream.range(1, 50).map {
            Random.nextInt(1, 100)
        }.toList())
    }.cache() // by using cache the subscriber ll get same data (no new invocation)
    flux.subscribe { println("First subscription: $it") }
    flux.subscribe { println("Second subscription: $it") }

    println("blocked ${flux.blockLast()}")
}

fun fluxWithCreate() {
    // Flux with create
    val flux = Flux.create<Int> { sink ->
        IntStream.range(1, 10).forEach {
            println("Sink #$it")
            sink.next(it)
        }
    }
    val consumer1 = flux.delayElements(Duration.ofMillis(5)).subscribe { println("Subscriber 1 : $it") }
    val consumer2 = flux.delayElements(Duration.ofMillis(10)).subscribe { println("Subscriber 2 : $it") }

    println("End: ${flux.blockLast()}, $consumer1, $consumer2")
}