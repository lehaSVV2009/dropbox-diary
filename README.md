# dropbox-diary

Simple UI projects (Desktop and Mobile) to add text notes to a single diary text file of your Dropbox account

## Getting Started

### Prerequisites

1. **Dropbox**

1.1. Create Dropbox developer app [here](https://www.dropbox.com/developers/apps/create) (e.g. `MyDiary`)

1.2. Obtain access token

1.3. Locally create `diary.yml` file with the following content:

```
events:
- text: "Hello World"
  date: "2014-11-02T07:49:21Z"
```

1.4. Upload `diary.yml` to the root folder to your Dropbox apps. (`Apps -> MyDiary`)

2. **AWS**

2.1. Create new [AWS Lambda](https://aws.amazon.com/lambda/)

2.2. Create new [API Gateway](https://aws.amazon.com/api-gateway/) connected with just created Lambda (probably within Lambda creating)

2.3. Create `POST /v1` url in API Gateway.

2.4. Create `API key` in AWS Gateway and set up `Usage Plan` there.

3. **Java**

3.1. Install java (tested on version `8 >`)

4. **Node**

4.1. Install node (tested on version `10 >`)

### Installing API

1. Build zip file

```
./gradlew :dropbox-diary-function:build
```

2. Upload zip file to your AWS Lambda from `./dropbox-diary-function/distributions/dropbox-diary-function.zip`

3. Issue 
```
echo '[{ "text":  "bla bla", "date": "2018-05-21T17:48:16.667Z" }]' | http post https://$MY_API_GATEWAY_ID.execute-api.us-east-1.amazonaws.com/v1 'x-api-key:$MY_API_KEY'
```

### Installing JavaFX desktop app

1. Add the following env variables:

* `API_URL` - url to AWS Gateway like `https://$MY_API_GATEWAY_ID.execute-api.us-east-1.amazonaws.com`
* `API_KEY` - api key for url like `1234567890QWERTYUIOP`

2. Build JAR file

```
./gradlew build
```

3. Run

```
java -jar dropbox-diary-desktop/build/libs/dropbox-diary-desktop.jar
```

TODO [Little demo](little-demo.png)

### Installing React Native mobile app

1. Add env.js file to the root of dropbox-diary-mobile folder:
```
export const API_KEY = "MY_API_KEY"
export const API_URL = "https://MY_API_GATEWAY_ID.execute-api.us-east-1.amazonaws.com"
```

2. Install dependencies
```
yarn
```

3. Run application by [Expo](https://expo.io/) (and issue generated url in browser)

3.1. Install Expo client on your mobile (IOS/Android)

3.2. Run

```
yarn start
```

3.3. Issue generated url like `exp://172.20.10.8:19000` in terminal in your mobile browser

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
* [AWS Lambda Java Function](https://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-stream.html) - Serverless compute
* [Retrofit](http://square.github.io/retrofit/) - Java http client
* [JavaFX](http://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html) - Java Desktop UI
* [Create React Native App](https://github.com/react-community/create-react-native-app) - Boilerplate to create react native mobile apps
* [React Native Gifted Chat](https://github.com/FaridSafi/react-native-gifted-chat) - Chat UI for React Native

## Contributing

Do what you want and have fun :smile:

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/lehaSVV2009/dropbox-diary/tags). 

## Authors

* [**Alex Soroka**](https://github.com/lehaSVV2009/resume)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

Great thanks [Katerina Draenkova](https://github.com/KaterinaDraenkova) and [Pavel Evleev](https://github.com/PavelEvleev) for inspiration!