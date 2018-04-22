# dropbox-diary

Simple UI projects (Desktop and Mobile) to add text notes to a single diary text file of your Dropbox account

## Getting Started

### Prerequisites

1. Create Dropbox developer app [here](https://www.dropbox.com/developers/apps/create) (e.g. `MyDiary`)

2. Obtain access token

3. Locally create `diary.xml` file with the following content:

```
<diary></diary>
```

4. Upload `diary.xml` to the root folder to your Dropbox apps. (`Apps -> MyDiary`)

### Installing JavaFX app

1. Build JAR file (or download it from [releases](https://github.com/lehaSVV2009/dropbox-diary/releases))

```
./gradlew build
```

2. Run with arguments

```
java \
-Ddropbox-client-id=my-dropbox-app-id \
-Ddropbox-access-token=my-access-token \
-Ddiary-path=/diary.xml \
-jar desktop/build/libs/desktop.jar
```

TODO [Little demo](little-demo.png)

## Deployment

Artifacts are automatically built and published to [Github Releases](https://github.com/lehaSVV2009/dropbox-diary/releases) by Travis when code is pushed.

```
git commit -m "My commit"
git tag v0.3.5
git push origin master
```

## Built With

* [Gradle](https://gradle.org/) - Dependency Management
* [Travis](https://travis-ci.org/) - Simple CI Tool integrated with Github
* [Dropbox Client](https://github.com/dropbox/dropbox-sdk-java) - Java library to access Dropbox's HTTP-based Core API v2

## Contributing

Do what you want and have fun :smile:

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/lehaSVV2009/dropbox-diary/tags). 

## Authors

* [**Alex Soroka**](https://github.com/lehaSVV2009/resume)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
