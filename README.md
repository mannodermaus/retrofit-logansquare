# retrofit-logansquare

A `Converter` implementation using BlueLine Labs' [LoganSquare][logansquare] JSON serialization for Square's [Retrofit][retrofit] REST library.  
**Please note** that this library is solely intended for usage with **version 2 of Retrofit** (*2.0.0-beta2* and up)! It won't work with 1.9 or earlier. Please refer [to this section of the README](#retro1) for instructions on how to include LoganSquare serialization on these versions.

## Download

Soon.

## Usage

Plug in the `LoganSquareConverterFactory` like any other converter upon creating your `Retrofit` instance:

```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://your.server.com/api/")
    .addConverterFactory(LoganSquareConverterFactory.create())
    .build();
```

<a name="retro1"></a>
## I'm using Retrofit 1.x, though!

In that case, it might be time to upgrade! If you can't or won't, refer to [this gist][v1gist] for a quick solution (no guarantees that it will work 100% of the time, though).

  [logansquare]: https://github.com/bluelinelabs/LoganSquare
  [retrofit]: https://github.com/square/retrofit
  [v1gist]: https://gist.github.com/aurae/8427b93b27483763d9cb
 