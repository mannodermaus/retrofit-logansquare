# retrofit-logansquare

A `Converter` implementation using BlueLine Labs' [LoganSquare][logansquare] JSON serialization for Square's [Retrofit][retrofit] REST library.  
**Please note** that this library is solely intended for usage with **Retrofit 2.0.0 and above**! It won't work with 1.9 or earlier. Please refer [to this section of the README](#retro1) for instructions on how to include LoganSquare serialization on these versions.

## Download

Get it on `jcenter()`:

`compile "com.github.aurae.retrofit2:converter-logansquare:1.4.1"`

Don't forget to include the dependencies on LoganSquare as well!
Their latest release at the moment:
[![Check the repo](https://img.shields.io/github/tag/bluelinelabs/LoganSquare.svg)][logansquare]

```groovy
annotationProcessor "com.bluelinelabs:logansquare-compiler:<latest-logansquare-version>"
compile "com.bluelinelabs:logansquare:<latest-logansquare-version>"
```

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

## License

	Copyright 2016 Marcel Schnelle

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
  [retrofit]: https://github.com/square/retrofit
  [v1gist]: https://gist.github.com/aurae/8427b93b27483763d9cb
 