# DigitForumBot
A Telegram bot that posts latest posts from the Digit Forum

## Building
Before building, you need to get your own bot token and bot username using [@BotFather](https://telegram.im/BotFather). More details on creating Telegram bots can be found [here](https://core.telegram.org/bots#3-how-do-i-create-a-bot).

Once you have your own bot token and bot username, enter these details in the [app.properties](https://github.com/desmond27/DigitForumBot/blob/master/src/main/resources/app.properties) file against the fields `NEW_POSTS_BOT_TOKEN` and `NEW_POSTS_BOT_USERNAME`.

Set the frequency of posting as needed against `POSTING_FREQUENCY_SECONDS`, the default value is one hour.

After this, run `mvn clean package` in the project root and you will get the built executable JAR in the target directory as `DigitForumBot-1.0-SNAPSHOT-jar-with-dependencies.jar`. This can now be run as `java -jar DigitForumBot-1.0-SNAPSHOT-jar-with-dependencies.jar`.

## Adding the bot to your chat
Search for your bot via the handle and add it to your chat.

Once added, either post `/start` or `/start@<bot username>` if you have multiple bots. You should receive a message like:

```
Welcome! DigitForumBot has been activated for this chat.
```

After this, you will receive a message will Latest posts based on the frequency you have set above.
