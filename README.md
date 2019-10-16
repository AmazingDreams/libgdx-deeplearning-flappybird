# Flappy Bird deep learning

This is my first try on implementing a neural network in this format.

It works by spawning several hundred birds with a neural network and randomized weights. The birds do their thing
and when every one of them is dead (touching top, bottom, or one of the 'pipes') a new generation is spawned based on 
the most successful birds (ie, those who stayed alive the longest). The networks are copied and the weights are slightly
randomized.

Depending on the random number generation gods you may be very successful within a few generations.

## Running

Simply run 

```shell script
$ ./gradlew desktop:run
```


Using spacebar you can spawn a new generation. And with R you can reset the whole thing to zero (sometimes the birds 
just can't figure it out).

## Tweaking

You can tweak some of the parameters within the code. Maybe I will put these in some central configuration class some time.
