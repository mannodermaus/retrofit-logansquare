# retrofit-logansquare

A `Converter` implementation using BlueLine Labs' [LoganSquare][logansquare] JSON serialization for Square's [Retrofit 2][retrofit2] REST library.

## Download

```groovy
dependencies {
    implementation "de.mannodermaus.retrofit2:converter-logansquare:2.0.0"
    
    // Don't forget to include the dependencies on LoganSquare as well:
    annotationProcessor "com.bluelinelabs:logansquare-compiler:<latest-logansquare-version>"
    implementation "com.bluelinelabs:logansquare:<latest-logansquare-version>"
}
```

The latest version of LoganSquare: [![Check the repo](https://img.shields.io/github/tag/bluelinelabs/LoganSquare.svg)][logansquare]

Snapshots of the development version are available through [Sonatype's `snapshots` repository][sonatyperepo].

## Usage

Plug in the `LoganSquareConverterFactory` like any other converter while building your `Retrofit` instance:

```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl("https://your.server.com/api/")
    .addConverterFactory(LoganSquareConverterFactory.create())
    .build()
```

<a name="retro1"></a>
## Retrofit 1.x

This library is solely intended for usage with version **2.0.0** and above of Retrofit. If you're still on an older release,
consider upgrading if you decide to use this converter. For a quick bootstrap solution, please refer to [this gist][v1gist] -
no guarantees that it will work 100% of the time, though!

## License

	Copyright 2017 Marcel Schnelle

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

  [logansquare]: https://github.com/bluelinelabs/LoganSquare
  [retrofit2]: https://github.com/square/retrofit
  [v1gist]: https://gist.github.com/aurae/8427b93b27483763d9cb
  [sonatyperepo]: https://oss.sonatype.org/content/repositories/snapshots
 