# dropbox-diary

Simple UI projects (Desktop and Mobile) to add text notes to a single diary text file of your Dropbox account

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

1. Create Dropbox developer app (e.g. `MyDiary`)

2. Obtain access token

3. Locally create `diary.xml` file with the following content:

```
<diary></diary>
```

4. Upload `diary.xml` to the root folder to your Dropbox apps. (`Apps -> MyDiary`)

### Installing JavaFX app

1. Build the project

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

Do you what you want and have fun :smile:

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Alex Soroka** - [PurpleBooth](https://github.com/lehaSVV2009/resume)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
